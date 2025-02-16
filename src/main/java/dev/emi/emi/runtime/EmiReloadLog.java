package dev.emi.emi.runtime;

import com.google.common.collect.Lists;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

public class EmiReloadLog {
	private static List<String> pendingWarnings = Lists.newArrayList();
	public static List<String> warnings = com.rewindmc.retroemi.shim.java.List.of();
	private static int pendingWarningCount;
	public static int warningCount;

	public static void bake() {
		warnings = pendingWarnings;
		warningCount = pendingWarningCount;
		pendingWarnings = Lists.newArrayList();
		pendingWarningCount = 0;
	}

	public static void warn(String warning) {
		pendingWarnings.add(warning);
		EmiLog.error(warning);
		pendingWarningCount++;
	}

	public static void info(String info) {
		pendingWarnings.add(info);
		EmiLog.info(info);
	}

	public static void error(Throwable e) {
		e.printStackTrace();
		StringWriter writer = new StringWriter();
		e.printStackTrace(new PrintWriter(writer, true));
		String[] strings = writer.getBuffer().toString().split("/");
		for (String s : strings) {
			EmiReloadLog.warn(s);
		}
	}
}
