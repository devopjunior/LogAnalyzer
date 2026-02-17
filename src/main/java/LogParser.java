import java.io.*;
import java.util.*;
import java.util.regex.*;

public class LogParser {

    private static final String LOG_PATTERN =
            "(\\d{4}-\\d{2}-\\d{2})\\s" +
                    "(\\d{2}:\\d{2}:\\d{2})\\s" +
                    "(INFO|ERROR|WARNING|DEBUG)\\s" +
                    "(.*)";

    public static List<LogEntry> parseFromReader(BufferedReader reader) throws IOException {
        List<LogEntry> entries = new ArrayList<>();
        Pattern pattern = Pattern.compile(LOG_PATTERN);
        String line;

        while ((line = reader.readLine()) != null) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                LogEntry entry = new LogEntry(
                        matcher.group(1),
                        matcher.group(2),
                        matcher.group(3),
                        matcher.group(4)
                );
                entries.add(entry);
            }
        }
        reader.close();
        return entries;
    }
}

