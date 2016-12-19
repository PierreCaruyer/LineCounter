import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {
	
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
		String inputStr = "Input/";
		
		if(args.length < 1)
			inputStr= args[0];
		
		File input = new File(inputStr);
		String[] list = input.list();
		
		for(int i = 0; i < list.length; i++){
			List<String> allLines = new ArrayList<>();
			BufferedReader reader = null;
			FileInputStream fis = null;
			InputStreamReader isr = null;
			String newLine = new String();
			File readableFile = new File(inputStr + list[i]);
			fis = new FileInputStream(readableFile);
			isr = new InputStreamReader(fis);
			reader = new BufferedReader(isr);
			newLine = reader.readLine();
			while(newLine != null)
			{
				allLines.add(newLine);
				System.out.println(newLine);
				newLine = reader.readLine();
			}
			CloseInputStream(fis, isr, reader);
			if(allLines.size() > 0)
				if(allLines.get(0).isEmpty())
					allLines.remove(allLines.get(allLines.size() - 1));
			counter += allLines.size();
		}
		System.out.println(counter);
	}
}
