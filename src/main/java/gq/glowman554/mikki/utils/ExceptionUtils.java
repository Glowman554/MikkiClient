package gq.glowman554.mikki.utils;

import java.lang.Thread.UncaughtExceptionHandler;
import javax.swing.JOptionPane;

public class ExceptionUtils
{
	public static void intercept()
	{
		Log.log("Installing uncaught exception interceptor...");

		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler()
		{

			@Override
			public void uncaughtException(Thread t, Throwable e)
			{
				JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + ": " + e.getMessage(), "Uncaught Exception", JOptionPane.ERROR_MESSAGE);
			}
		});
	}
}
