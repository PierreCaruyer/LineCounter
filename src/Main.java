import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Main {

	private static String basePath = "";
	private static boolean recursive = false;
	private static boolean verbose = false;
	private static boolean ultimate_verbose = false;
	private static boolean timer = false;

	private static void usage() {
		System.out.println("java -jar LineCounter.jar [options] [path]"
				+ "options : "
				+ "\n\t[ -r | --recursive ]\n\t[ -v | --verbose ]\n\t[ --timer ]");
	}
	
	public static void main(String[] args) {
		int counter = 0;
		long start_time = 0, end_time = 0;
		File workingDirectory = null;
		String workingDirPath = null;
		
		if(args == null || args.length == 0) {
			usage();
		} else {
			parseArgs(Arrays.asList(args));
			
			workingDirectory = new File(new File(basePath).getAbsolutePath());
			workingDirPath = workingDirectory.getAbsolutePath();
			
			if(workingDirPath.charAt(workingDirPath.length() - 1) == '.') {
				workingDirPath = workingDirPath.substring(0, workingDirPath.length() - 1);
			}
			if(workingDirPath.charAt(workingDirPath.length() - 1) != '/') {
				workingDirPath = workingDirPath + '/';
			}
			
			System.out.println("Counting lines from " + workingDirPath);
			start_time = System.currentTimeMillis();
			if(recursive)
				counter = recursiveCount(workingDirPath);
			else
				counter = non_recursiveCount(workingDirPath);
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
		basePath = args.get(args.size() - 1);
		if(args.contains("-h") || args.contains("--help")) {
			usage();
		} else {
			if(args.contains("--verbose") || args.contains("-v"))
				verbose = true;
			if(args.contains("-V"))
				verbose = ultimate_verbose = true;
			if(args.contains("--recursive") || args.contains("-r") || args.contains("-R") || args.contains("--rec"))
				recursive = true;
			if(args.contains("-t") || args.contains("--timer"))
				timer = true;
		}
	}

	private static int recursiveCount(final String path) {
		int count = 0;

		if(new File(path).isDirectory()) {
			if(recursive) {
				Iterator<File> dir = subDirs(path);
				while(dir.hasNext()) {
					String name = dir.next().getName();
					if(verbose)
						System.out.println("Entering dir : " + path + name + "/ ...");
					count += recursiveCount(path + name + "/");
				}
			}
			count += non_recursiveCount(path);
		}
		else
			count += getFileLines(new File(path));

		return count;
	}

	private static int non_recursiveCount(final String path) {
		return directoryLines(dirFiles(path), 0);
	}
	
	private static int directoryLines(final Iterator<File> files, int currentCount) {
		return (files.hasNext()) ? directoryLines(files, currentCount + getFileLines(files.next())) : currentCount;
	}
	
	private static int getFileLines(final File file) {
		int count = 0;
		try {
			String newLine;
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader reader = new BufferedReader(isr);
			while((newLine = reader.readLine()) != null)
			{
				count++;
				if(ultimate_verbose)
					System.out.println(newLine);
			}
			if(reader != null)
				reader.close();
			if(isr != null)
				isr.close();
			if(fis != null)
				fis.close();
			if(verbose)
				System.out.println(file.getAbsolutePath() + " : " + count + " lines");
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
		return count;
	}

	private static Iterator<File> subDirs(final String path) {
		File dir = new File(path);
		if(!dir.exists()) {
			System.out.println("Dir does not exits");
			return null;
		}
		List<File> dirs = new ArrayList<>();

		File[] files = dir.listFiles();
		for(File f : files)
			if(f.isDirectory())
				dirs.add(f);

		return dirs.iterator();
	}

	private static Iterator<File> dirFiles(final String path) {
		final File dir = new File(path);
		
		if(!dir.exists())
			System.err.println(path + " not found.");;

		List<File> dirs = new ArrayList<>();

		File[] files = dir.listFiles();
		for(File f : files)
			if(f.isFile())
				dirs.add(f);

		return dirs.iterator();
	}
}
