package com.start;

import com.model.AFileLineCounter;
import com.model.ArgsParser;
import com.model.FileLineCounterManager;

import java.io.File;

public class MainLaunch {

	private static final void usage() {
		System.out.println("java -jar LineCounter.jar [options] [path]"
				+ "\noptions : "
				+ "\n\t[ -v | --verbose ]\n\t[ -t | --timer ]\n\t");
	}

	public static final void main(final String[] args) {
		final ArgsParser parser = new ArgsParser(args).parse();
		if(parser.isHelpActive() || parser.isNullOrEmpty()) {
			usage();
			return;
		}
		final String basePath = parser.getPath();
		final File file = new File(basePath);

		System.out.println("Counting lines from " + basePath);
		final AFileLineCounter lineCounter = FileLineCounterManager.getInstance(file, parser.isRecursive(), parser.isVerboseActive());
		final long start_time = System.currentTimeMillis();
		final int counter = lineCounter.count();
		final long end_time = System.currentTimeMillis();
		if(!parser.isTimerActive()) {
			System.out.println("Total lines " + counter);
		} else {
			final long seconds = (end_time - start_time) / 1000;
			final long commaSeconds = (end_time - start_time) % 1000;
			final long linesPerSec = counter / seconds;
			System.out.println(String.format("LineCounter has gone through %s lines in %s.%s seconds", counter, seconds, commaSeconds));
			System.out.println(String.format("Counted %s lines / sec", linesPerSec));
		}
	}
}
