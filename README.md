# LogAnalyzer

A **Java command-line tool** to analyze log files.  
It reads a log file, counts occurrences of different log levels (`INFO`, `WARN`, `ERROR`), and provides a detailed line-by-line report.

## Features

- Counts total `ERROR`, `WARN`, and `INFO` messages.  
- Provides a detailed report with line numbers.  
- Works with plain-text log files (.log, .txt).  
- Easy to extend for filtering, exporting, or parsing timestamps.

## How to Run

1. Open the project in **IntelliJ IDEA**.  
2. Run `LogAnalyzer.java`.  
3. Provide the path to your log file as a program argument:
```bash
java LogAnalyzer /path/to/your/logfile.log
