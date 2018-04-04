package net.amygdalum.comtemplate.engine;

import java.io.PrintStream;

public class Messages {

	private static Messages INFO = new Messages(System.out);
	private static Messages WARN = new Messages(System.out);
	private static Messages ERROR = new Messages(System.err);

	private PrintStream out;

	public Messages(PrintStream out) {
		this.out = out;
	}

	public static void setINFO(Messages info) {
		INFO = info;
	}

	public static void resetINFO() {
		INFO = new Messages(System.out);
	}

	public static void info(Object... msgs) {
		for (Object msg : msgs) {
			INFO.log(msg);
		}
	}

	public static void setWARN(Messages warn) {
		WARN = warn;
	}

	public static void resetWARN() {
		WARN = new Messages(System.out);
	}

	public static void warn(Object... msgs) {
		for (Object msg : msgs) {
			WARN.log(msg);
		}
	}

	public static void setERROR(Messages error) {
		ERROR = error;
	}

	public static void resetERROR() {
		ERROR = new Messages(System.err);
	}

	public static void error(Object... msgs) {
		for (Object msg : msgs) {
			ERROR.log(msg);
		}
	}

	public void log(Object msg) {
		out.println(msg);
		if (msg instanceof Exception) {
			((Exception) msg).printStackTrace(out);
		}
	}

}
