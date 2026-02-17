import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class LogAnalyzer {

    private List<LogEntry> entries;

    public LogAnalyzer(List<LogEntry> entries) {
        this.entries = entries;
    }

    public Map<String, Long> countByLevel() {
        return entries.stream()
                .collect(Collectors.groupingBy(
                        LogEntry::getLevel,
                        Collectors.counting()
                ));
    }

    public Map<String, Long> mostCommonErrors() {
        return entries.stream()
                .filter(e -> e.getLevel().equals("ERROR"))
                .collect(Collectors.groupingBy(
                        LogEntry::getMessage,
                        Collectors.counting()
                ));
    }

    public List<LogEntry> searchKeyword(String keyword) {
        return entries.stream()
                .filter(e -> e.getMessage().toLowerCase()
                        .contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    public void generateCSVReport(String fileName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write("Date,Time,Level,Message\n");
        for (LogEntry entry : entries) {
            writer.write(entry.getDate() + "," +
                    entry.getTime() + "," +
                    entry.getLevel() + "," +
                    entry.getMessage() + "\n");
        }
        writer.close();
    }
}

