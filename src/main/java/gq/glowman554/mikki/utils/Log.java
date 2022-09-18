package gq.glowman554.mikki.utils;

import java.io.File;
import java.io.IOException;

public class Log
{
	private static String logs_dir = "./logs";
	private static String current_log_file = "./logs/" + System.currentTimeMillis() + ".log";

	private static boolean save = false;

	public static void log(String message)
	{
		synchronized (Log.class)
		{
			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			String new_message = "";
			for (String line : message.split("\n"))
			{
				new_message += String.format("[%s::%s at %s:%s] %s\n", stackTraceElements[2].getClassName(), stackTraceElements[2].getMethodName(), stackTraceElements[2].getFileName(), stackTraceElements[2].getLineNumber(), line);
			}

			new_message = new_message.trim();

			if (save)
			{
				if (!new File(logs_dir).isDirectory())
				{
					new File(logs_dir).mkdir();
				}

				if (!new File(current_log_file).exists())
				{
					try
					{
						FileUtils.writeFile(current_log_file, "---log start---");
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}

				try
				{
					FileUtils.appendFile(current_log_file, "\n" + new_message);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}

			System.out.println(new_message);
		}
	}

	public static String getCurrent_log_file()
	{
		return current_log_file;
	}
	
	public static void setLogDir(String dir)
	{
		Log.logs_dir = dir;
		Log.current_log_file = dir + "/" + System.currentTimeMillis() + ".log";;
	}
	
	public static void setSave(boolean save)
	{
		Log.save = save;
	}
}
