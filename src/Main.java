import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
	private static boolean u_verbose = false;
	private static boolean timer = false;

	private static void CloseInputStream(FileInputStream fis, InputStreamReader isr, BufferedReader reader) throws IOException
	{
		if(reader != null)
			reader.close();
		if(isr != null)
			isr.close();
		if(fis != null)
			fis.close();
	}

	private static void usage() {
		System.out.println("java -jar LineCounter.jar [options] [path]"
				+ "options : "
				+ "\n\t[ -r | --recursive ]\n\t[ -v | --verbose ]\n\t");
	}
	
	public static void main(String[] args) throws IOException, FileNotFoundException{
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
			System.out.println("Total lines " + counter);
			if(timer) {
				System.out.println("Duration " + ((end_time - start_time) / 1000) + "." + ((end_time - start_time) % 1000) + " seconds");
				System.out.println("Counted " + (counter / ((end_time - start_time) / 1000)) + " lines / sec");
			}
		}
	}

	private static void parseArgs(List<String> args) {
		basePath = args.get(args.size() - 1);
		if(args.contains("-h") || args.contains("--help")) {
			usage();
		} else {
			if(args.contains("--verbose") || args.contains("-v"))
				verbose = true;
			if(args.contains("-V"))
				verbose = u_verbose = true;
			if(args.contains("--recursive") || args.contains("-r") || args.contains("-R") || args.contains("--rec"))
				recursive = true;
			if(args.contains("-t"))
				timer = true;
		}
	}

	private static int recursiveCount(String path) throws IOException {
		int count = 0;

		File file = new File(path);
		if(file.isDirectory()) {
			if(recursive) {
				Iterator<File> dir = subDirs(path);
				while(dir.hasNext()) {
					String name = dir.next().getName();
					if(verbose)
						System.out.println("Entering dir : " + path + name + "/ ...");
					count += recursiveCount(path + name + "/");
				}
			}
			Iterator<File> f = dirFiles(path);
			while(f.hasNext())
				count += getFileLines(new File(path + f.next().getName()));
		}
		else
			count += getFileLines(new File(path));

		return count;
	}

	private static int non_recursiveCount(String path) throws IOException {
		int count = 0;
		Iterator<File> f = dirFiles(path);
		while(f.hasNext())
			count += getFileLines(new File(path + f.next().getName()));
		return count;
	}

	private static int getFileLines(File file) throws IOException {
		int count = 0;
		String newLine;
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader reader = new BufferedReader(isr);
		while((newLine = reader.readLine()) != null)
		{
			count++;
			if(u_verbose)
				System.out.println(newLine);
		}
		CloseInputStream(fis, isr, reader);
		if(verbose)
			System.out.println(file.getAbsolutePath() + " : " + count + " lines");
		return count;
	}

	private static Iterator<File> subDirs(String path) {
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

	private static Iterator<File> dirFiles(String path) {
		File dir = new File(path);
		if(!dir.exists()) {
			System.out.println("Dir does not exits");
			return null;
		}
		List<File> dirs = new ArrayList<>();

		File[] files = dir.listFiles();
		for(File f : files)
			if(f.isFile())
				dirs.add(f);

		return dirs.iterator();
	}
}