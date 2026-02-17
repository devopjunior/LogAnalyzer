import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;
import java.util.Map;

public class LogAnalyzerGUI extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> levelFilter;
    private JTextField searchField;
    private JButton loadButton, exportButton, searchButton;
    private JLabel statsLabel;

    private List<LogEntry> allEntries;

    public LogAnalyzerGUI() {
        setTitle("Log Analyzer");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Top panel for controls
        JPanel topPanel = new JPanel();
        levelFilter = new JComboBox<>(new String[]{"ALL", "INFO", "ERROR", "WARNING", "DEBUG"});
        searchField = new JTextField(20);
        loadButton = new JButton("Load Log");
        searchButton = new JButton("Search");
        exportButton = new JButton("Export CSV");
        topPanel.add(new JLabel("Filter Level:"));
        topPanel.add(levelFilter);
        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(loadButton);
        topPanel.add(searchButton);
        topPanel.add(exportButton);

        // Table to display logs
        tableModel = new DefaultTableModel(new String[]{"Date", "Time", "Level", "Message"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Stats label
        statsLabel = new JLabel(" ");

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(statsLabel, BorderLayout.SOUTH);

        // Button actions
        loadButton.addActionListener(e -> loadLog());
        searchButton.addActionListener(e -> applyFilter());
        exportButton.addActionListener(e -> exportCSV());

        setVisible(true);
    }

    private void loadLog() {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("sample.log");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            allEntries = LogParser.parseFromReader(reader);
            updateTable(allEntries);
            updateStats();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading log: " + e.getMessage());
        }
    }

    private void applyFilter() {
        if (allEntries == null) return;

        String selectedLevel = levelFilter.getSelectedItem().toString();
        String keyword = searchField.getText().trim().toLowerCase();

        List<LogEntry> filtered = allEntries.stream()
                .filter(entry -> (selectedLevel.equals("ALL") || entry.getLevel().equals(selectedLevel)))
                .filter(entry -> entry.getMessage().toLowerCase().contains(keyword))
                .toList();

        updateTable(filtered);
        updateStats(filtered);
    }

    private void updateTable(List<LogEntry> entries) {
        tableModel.setRowCount(0);
        for (LogEntry e : entries) {
            tableModel.addRow(new Object[]{e.getDate(), e.getTime(), e.getLevel(), e.getMessage()});
        }
    }

    private void updateStats() {
        updateStats(allEntries);
    }

    private void updateStats(List<LogEntry> entries) {
        if (entries == null || entries.isEmpty()) {
            statsLabel.setText("No logs loaded or matching filter.");
            return;
        }

        LogAnalyzer analyzer = new LogAnalyzer(entries);
        Map<String, Long> summary = analyzer.countByLevel();

        StringBuilder sb = new StringBuilder("Log Summary: ");
        summary.forEach((level, count) -> sb.append(level).append("=").append(count).append(" "));
        statsLabel.setText(sb.toString());
    }

    private void exportCSV() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No data to export!");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("report.csv"))) {
            writer.write("Date,Time,Level,Message\n");
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                writer.write(
                        tableModel.getValueAt(i, 0) + "," +
                                tableModel.getValueAt(i, 1) + "," +
                                tableModel.getValueAt(i, 2) + "," +
                                tableModel.getValueAt(i, 3) + "\n"
                );
            }
            JOptionPane.showMessageDialog(this, "CSV exported successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error exporting CSV: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LogAnalyzerGUI::new);
    }
}
