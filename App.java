import java.util.UUID;

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

			PreProcessor pre = new AwsPreProcessor(workDir);
			Transcoder transcoder = new Transcoder(workDir);
			PostProcessor post = new AwsPostProcessor(workDir);
			
			while (true)
			{
				String taskId = UUID.randomUUID().toString();
				String filename = pre.fetchJob();
				transcoder.transcode(taskId, filename);
				post.uploadOutput();
			}
		}
		
	}
}