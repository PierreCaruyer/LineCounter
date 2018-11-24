package com.start;

import com.model.ArgsParser;
import com.model.RecursiveFileLineCounter;

import java.io.File;

public class MainLaunch {

	public static boolean timer = false, verbose = false;

	private static void usage() {
		System.out.println("java -jar LineCounter.jar [options] [path]"
				+ "\noptions : "
				+ "\n\t[ -v | --verbose ]\n\t[ --timer ]");
	}
	
	public static void main(final String[] args) {
		final ArgsParser parser = new ArgsParser(args);
		parser.parse();
		if(parser.isHelpActive() || parser.isNullOrEmpty()) {
			usage();
			return;
		}
		final String basePath = args[args.length - 1];
		final File workingDirectory = new File(new File(basePath).getAbsolutePath());
		final String workingDirPath = workingDirectory.getAbsolutePath();
		final String formattedDirPath = workingDirPath.endsWith("/") ? workingDirPath : workingDirPath + '/';

		System.out.println("Counting lines from " + formattedDirPath);
		final RecursiveFileLineCounter lineCounter = new RecursiveFileLineCounter(formattedDirPath, verbose);
		final long start_time = System.currentTimeMillis();
		final int counter = lineCounter.count();
		final long end_time = System.currentTimeMillis();
		if(!timer) {
			System.out.println("Total lines " + counter);
		} else {
			System.out.println("LineCounter has gone through " + counter + " lines in " + ((end_time - start_time) / 1000) + "." + ((end_time - start_time) % 1000) + " seconds");
			System.out.println("Counted " + (counter / ((end_time - start_time) / 1000)) + " lines / sec");
		}
	}
}
