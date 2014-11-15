package diaryServer;



import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import enums.ELogLevel;

public class Logger {
	public static String FILES_SEPERATOR = System.getProperty("file.separator");
	
	private Logger() {
		lines = new StringBuilder();
	}

	private static Logger instance;

	public static synchronized Logger getInstance() {
		if (instance == null) {
			instance = new Logger();
		}
		return instance;
	}

	private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("HH:mm:ss.SSS");

	private int rowsCounter = 1;

	public void Log(ELogLevel logLevel, String className, String methodName, String message) {
		String line = String.format("%d-%s(%s): %s.%s - %s", rowsCounter, dateTimeFormat.format(Calendar.getInstance().getTime()), logLevel, className,
				methodName, message);

		lines.append(line);
		if (logLevel == ELogLevel.error || logLevel == ELogLevel.critical) {
			System.err.println(line);
		} else {
			System.out.println(line);
		}
		rowsCounter++;

		if (rowsCounter % 1000 == 0) {
			flushToFile();
		}
	}

	public void closeLog() {
		flushToFile();
		System.out.println("Saved to file");
	}

	private StringBuilder lines = null;

	private void flushToFile() {
		String filePath = String.format("%s.txt", dateTimeFormat.format(Calendar.getInstance().getTime()).replace(" ", "").replace(":", "").replace(".", ""));
		String folderPath = String.format("%s%slogFiles%s", System.getProperty("user.dir"), FILES_SEPERATOR, FILES_SEPERATOR);
		new File(folderPath).mkdirs();

		byte[] strToWrite = lines.toString().getBytes();
		java.nio.file.Path path = null;
		path = Paths.get(folderPath, filePath);
		try {
			Files.write(path, strToWrite, StandardOpenOption.CREATE);
		} catch (IOException e) {
			e.printStackTrace();
		}

		lines = new StringBuilder();
	}

	public void addSeperatorLine() {
		String line = String
				.format("=========================================================================================================================");
		lines.append(line);
		System.out.println(line);
		rowsCounter++;

		if (rowsCounter % 1000 == 0) {
			flushToFile();
		}

	}
}
