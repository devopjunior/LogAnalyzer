import java.io.*;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        try {
            // Read sample.log from resources
            InputStream is = Main.class.getClassLoader().getResourceAsStream("sample.log");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            List<LogEntry> entries = LogParser.parseFromReader(reader);

            LogAnalyzer analyzer = new LogAnalyzer(entries);

            System.out.println("Log Level Summary:");
            Map<String, Long> summary = analyzer.countByLevel();
            summary.forEach((level, count) -> System.out.println(level + ": " + count));

            System.out.println("\nMost Common Errors:");
            analyzer.mostCommonErrors().forEach((msg, count) ->
                    System.out.println(msg + " - " + count + " times"));

            System.out.println("\nSearch for 'User':");
            analyzer.searchKeyword("User").forEach(System.out::println);

            analyzer.generateCSVReport("report.csv");
            System.out.println("\nCSV report generated successfully!");

        } catch (IOException e) {
            System.out.println("Error reading log file: " + e.getMessage());
        }
    }
}
