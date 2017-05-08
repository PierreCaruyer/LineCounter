import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {
	
	private static String basePath = "";
	
	private static void CloseInputStream(FileInputStream fis, InputStreamReader isr, BufferedReader reader) throws IOException
	{
		if(reader != null)
			reader.close();
		if(isr != null)
			isr.close();
		if(fis != null)
			fis.close();
	}
	
	public static void main(String[] args) throws IOException, FileNotFoundException{
		int counter = 0;
		basePath = args[0];
		System.out.println(args[0]);
		
		counter = recursiveCount(basePath);
		System.out.println(counter);
	}
	
	private static int recursiveCount(String path) throws IOException {
		int count = 0;
		File file = new File(path);
		if(file.isDirectory()) {
			Iterator<File> dir = subDirs(path), f = dirFiles(path);
			while(dir.hasNext()) {
				String name = dir.next().getName();
				System.out.println(path + name + "/");
				count += recursiveCount(path + name + "/");
			}
			while(f.hasNext()) {
				String name = f.next().getName();
				System.out.println(path + name + "/");
				count += getFileLines(new File(path + name));
			}
		}
		
		return count;
	}
	
	private static int getFileLines(File file) throws IOException {
		int count = 0;
		String newLine;
		System.out.println(file.getAbsolutePath());
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader reader = new BufferedReader(isr);
		while((newLine = reader.readLine()) != null)
		{
			count++;
			System.out.println(newLine);
		}
		CloseInputStream(fis, isr, reader);
		System.out.println(count);
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