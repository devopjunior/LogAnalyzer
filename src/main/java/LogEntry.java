public class LogEntry {
    private String date;
    private String time;
    private String level;
    private String message;

    public LogEntry(String date, String time, String level, String message) {
        this.date = date;
        this.time = time;
        this.level = level;
        this.message = message;
    }

    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getLevel() { return level; }
    public String getMessage() { return message; }

    @Override
    public String toString() {
        return date + " " + time + " " + level + " " + message;
    }
}

