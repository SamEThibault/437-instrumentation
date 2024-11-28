import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

// instrumentation class to record and log execution times
public class Instrumentation
{
    private static Instrumentation instance = new Instrumentation(); // ensure singleton behaviour
    private boolean isActive = false; // keeps track of the state of the instance
    private int depthCounter = 0; // keeps track of current timing nest level
    private Stack<Long> startTimes = new Stack<>(); // stores all nested start times
    private long totalTime = 0; // records the final stop time for logging purposes
    private List<String> logs = new ArrayList<>(); // stores all formatted logs before dumping
    private Instrumentation(){} // ensures singleton behaviour

    // constructor: ensures singleton behaviour
    public static Instrumentation Instance() {
        return instance;
    }

    // formats log line based on start or stop message, depth level, and (possibly empty) comment
    private String formatEntry(String message, String comment) {
        StringBuilder formattedLog = new StringBuilder();

        if (depthCounter > 0) {
            formattedLog.append("|\t".repeat(depthCounter));
        }

        return String.valueOf(formattedLog.append(message).append(comment));
    }

    // pushes start time to stack and updates logging depth
    public void startTiming(String comment) {
        if (isActive) {
            startTimes.push(System.nanoTime());
            logs.add(formatEntry("STARTTIMING: ", comment));
            depthCounter++;
        }
    }

    // calculates duration, updates logging depth, and adds formatted log
    public void stopTiming(String comment) {
        if (isActive) {
            long duration = (System.nanoTime() - startTimes.pop()) / 1000000;
            depthCounter--;
            logs.add(formatEntry("STOPTIMING: ", comment) + " " + duration + "ms");

            // if this is the end of the outer timer, the duration is the total execution time
            if (depthCounter == 0) {
                totalTime += duration;
            }
        }
    }

    // pushes formatted comment log
    public void comment(String comment) {
        if (isActive) {
            logs.add(formatEntry("COMMENT: ", comment));
        }
    }

    // overloaded: used to dump logs using a provided file name
    public void dump(String filename) {
        if (isActive) {
            dumpLogs(filename);
        }
    }

    // overloaded: used to dump logs whose name is automatically generated
    public void dump() {
        if (isActive) {
            String filename = "instrumentation" + new SimpleDateFormat("ddyyMMhhmmss").format(Calendar.getInstance().getTime()) + ".log";
            dumpLogs(filename);
        }
    }

    // generates/overwrites file and dumps logs using buffered writer
    private void dumpLogs(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String entry : logs) {
                writer.write(entry);
                writer.newLine();
            }

            writer.write("TOTAL TIME: " + totalTime + "ms");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // manages the instance's state, clear logs
    public void activate(Boolean isActive)
    {
        if (isActive) {
            logs.clear();
            depthCounter = 0;
            startTimes.clear();
            totalTime = 0;
            this.isActive = true;
        } else {
            this.isActive = false;
        }
    }
}
