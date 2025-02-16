package dev.emi.emi.runtime;

import net.minecraft.launchwrapper.Launch;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

public class EmiLog {

	public static final Logger logger = Logger.getLogger("EMI");

	public static void info(String str) {
		logger.info("EMI: " + str);
	}

	public static void warn(String str) {
		logger.warning("EMI: " + str);
	}

	public static void error(String str) {
		logger.severe("EMI: " + str);
	}

	public static void debug(String str) {
		if ((Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment")) {
			logger.info("EMI: " + str);
		}
	}

	public static void error(Throwable e) {
		e.printStackTrace();
		StringWriter writer = new StringWriter();
		e.printStackTrace(new PrintWriter(writer, true));
		String[] strings = writer.getBuffer().toString().split("/");
		for (String s : strings) {
			EmiLog.error(s);
		}
	}
}
