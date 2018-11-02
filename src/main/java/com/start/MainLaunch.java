package com.start;

import com.utils.FilesUtils;
import com.utils.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class MainLaunch {

	private static boolean verbose = false;
	private static boolean timer = false;

	private static void usage() {
		System.out.println("java -jar LineCounter.jar [options] [path]"
				+ "\noptions : "
				+ "\n\t[ -v | --verbose ]\n\t[ --timer ]");
	}
	
	public static void main(String[] args) {
		int counter = 0;
		long start_time = 0, end_time = 0;
		
		if(args == null || args.length == 0) {
			usage();
		} else {
			parseArgs(Arrays.asList(args));
            final String basePath = args[args.length - 1];
            final File workingDirectory = new File(new File(basePath).getAbsolutePath());
            final String workingDirPath = workingDirectory.getAbsolutePath();
            final String formattedDirPath = workingDirPath.endsWith(".") ?
                    workingDirPath.substring(0, workingDirPath.length() - 1) :
                    workingDirPath;
			final String dirPathWithTrailingSlash = formattedDirPath.endsWith("/") ?
					formattedDirPath :
					formattedDirPath + '/';

			
			System.out.println("Counting lines from " + dirPathWithTrailingSlash);
			start_time = System.currentTimeMillis();
			counter = recursiveCount(dirPathWithTrailingSlash);
			end_time = System.currentTimeMillis();
			if(!timer) {
				System.out.println("Total lines " + counter);
			} else {
				System.out.println("LineCounter has gone through " + counter + " lines in " + ((end_time - start_time) / 1000) + "." + ((end_time - start_time) % 1000) + " seconds");
				System.out.println("Counted " + (counter / ((end_time - start_time) / 1000)) + " lines / sec");
			}
		}
	}

	private static void parseArgs(final Collection<String> args) {
		if(args == null) {
			return;
		}
		if(args.contains("-h") || args.contains("--help")) {
			usage();
		} else {
			if(args.contains("--verbose") || args.contains("-v"))
				verbose = true;
			if(args.contains("-t") || args.contains("--timer"))
				timer = true;
		}
	}

	private static int recursiveCount(final File file) {
		if(!FilesUtils.fileExists(file)) {
			return 0;
		}
		if(file.isFile()) {
			return FilesUtils.getFileLines(file);
		}
		final Collection<File> dirCollection = FilesUtils.subDirs(file);
		if(dirCollection == null) {
			return 0;
		}
		int count = 0;
		final Iterator<File> dir = dirCollection.iterator();
		while(dir.hasNext()) {
			final String name = dir.next().getName();
			if(verbose) {
				System.out.println("Entering dir : " + file.getAbsolutePath() + "/" + name + "/ ...");
			}
			count += recursiveCount(file.getAbsolutePath() + "/" + name + "/");
			System.out.println("Counted " + count + " lines.");
		}
		return count + directoryLines(FilesUtils.dirFiles(file));
	}

	private static int recursiveCount(final String path) {
		if(StringUtils.isNullOrEmpty(path)) {
			return 0;
		}
		return recursiveCount(new File(path));
	}

    private static int directoryLines(final Collection<File> files) {
        return directoryLinesHelper(files, 0);
    }

    private static int directoryLinesHelper(final Collection<File> files, final int currentCount) {
        if(files == null) {
            return 0;
        }
        return directoryLinesHelper(files.iterator(), currentCount);
    }

	private static int directoryLinesHelper(final Iterator<File> files, final int currentCount) {
	    if(files == null) {
	        return 0;
        }
		return (files.hasNext()) ? directoryLinesHelper(files, currentCount + FilesUtils.getFileLines(files.next())) : currentCount;
	}
}
