import java.io.*;
import java.nio.file.*;
import com.amazonaws.services.s3.*;
import com.amazonaws.services.s3.model.*;

public class AwsStorageHandler
{	
	public String workDir;
	public AmazonS3Client s3Client = new AmazonS3Client();
	
	public AwsStorageHandler(String workDir)
	{
		this.workDir = workDir;
	}
	// Downloads the input file
	// args[0] - bucket name
	// args[1] - object key
	// args[2] - local filename
	public void download(String[] args)
	{
		try
		{
			String bucket = args[0];
			String key = args[1];
			String localFile = workDir + "/" + args[2];
			S3Object object = s3Client.getObject(new GetObjectRequest(bucket, key));
			InputStream in = object.getObjectContent();
			OutputStream out = new FileOutputStream(localFile);
	
			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = in.read(bytes)) != -1) 
			{
				out.write(bytes, 0, read);
			}
			in.close();
			out.close();
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	// Uploads the output files
	// Delete all temp files in workDir
	// args[0] - bucket name
	// args[1] - object prefix
	// args[2] - local filename
	public void upload(String[] args)
	{
		String[] formats = {"avi", "wmv", "mp4", "mov"};

		try
		{
			String bucket = args[0];
			String prefix = args[1];
			String uuid   = args[2];
			
			for (String format : formats)
			{
				String key = uuid + "." + format;
				if (prefix != null)
				{
					key    = prefix + "/" + key;					
				}
				String localFile = workDir + "/" + uuid + "." + format;
				s3Client.putObject(new PutObjectRequest(bucket, key, new File(localFile)));
			}
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	// Deletes a local file
	public void deleteLocalFile(String filename)
	{
		try
		{
			Files.delete(Paths.get(workDir + "/" + filename));
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}		
	}
}
