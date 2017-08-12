import java.util.*;
import java.io.*;

public class App 
{
	public static void main()
	{
		try
		{
			Properties prop = new Properties();
			InputStream input = new FileInputStream("config.properties");
			prop.load(input);
			String workDir = prop.getProperty("workDir");

			Transcoder transcoder = new Transcoder(workDir);
			AwsStorageHandler storage = new AwsStorageHandler(workDir);
			
			while (true)
			{
				String taskId = UUID.randomUUID().toString();
//				transcoder.transcode(taskId, filename);
			}
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
	}
}