package com.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

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
			File workingDirectory = null;
			String workingDirPath = null, basePath = args[args.length - 1];
			parseArgs(Arrays.asList(args));
			
			workingDirectory = new File(new File(basePath).getAbsolutePath());
			workingDirPath = workingDirectory.getAbsolutePath();
			
			if(workingDirPath.charAt(workingDirPath.length() - 1) == '.')
				workingDirPath = workingDirPath.substring(0, workingDirPath.length() - 1);
			if(workingDirPath.charAt(workingDirPath.length() - 1) != '/')
				workingDirPath = workingDirPath + '/';
			
			System.out.println("Counting lines from " + workingDirPath);
			start_time = System.currentTimeMillis();
			counter = recursiveCount(workingDirPath); 
			end_time = System.currentTimeMillis();
			if(!timer) {
				System.out.println("Total lines " + counter);
			} else {
				System.out.println("LineCounter has gone through " + counter + " lines in " + ((end_time - start_time) / 1000) + "." + ((end_time - start_time) % 1000) + " seconds");
				System.out.println("Counted " + (counter / ((end_time - start_time) / 1000)) + " lines / sec");
			}
		}
	}

	private static void parseArgs(final List<String> args) {
		if(args.contains("-h") || args.contains("--help")) {
			usage();
		} else {
			if(args.contains("--verbose") || args.contains("-v"))
				verbose = true;
			if(args.contains("-t") || args.contains("--timer"))
				timer = true;
		}
	}

	private static int recursiveCount(final String path) {
		if(new File(path).isFile())
			return getFileLines(new File(path));
		
		int count = 0;
		Iterator<File> dir = subDirs(path);
		while(dir.hasNext()) {
			String name = dir.next().getName();
			if(verbose)
				System.out.println("Entering dir : " + path + name + "/ ...");
			count += recursiveCount(path + name + "/");
			System.out.println("Counted " + count + " lines.");
		}
		count += directoryLines(dirFiles(path), 0);
		return count;
	}

	private static int directoryLines(final Iterator<File> files, int currentCount) {
		return (files.hasNext()) ? directoryLines(files, currentCount + getFileLines(files.next())) : currentCount;
	}
	
	private static int getFileLines(final File file) {
		int count = 0;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			for(;reader.readLine() != null; ++count);
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
		return count;
	}

	private static Iterator<File> subDirs(final String path) {
		return (!new File(path).exists())? null :
			Arrays.asList(new File(path).listFiles()).stream()
					.filter(file -> file.isDirectory())
					.collect(Collectors.toList())
					.iterator();
	}

	private static Iterator<File> dirFiles(final String path) {
		return (!new File(path).exists())? null :
			Arrays.asList(new File(path).listFiles()).stream()
					.filter(file -> file.isFile())
					.collect(Collectors.toList())
					.iterator();
	}
}
