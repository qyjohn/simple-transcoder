import java.util.*;
import java.io.*;
// AWS Related
import com.amazonaws.*;
import com.amazonaws.auth.*;
import com.amazonaws.auth.profile.*;
import com.amazonaws.regions.*;
import com.amazonaws.services.sqs.*;
import com.amazonaws.services.sqs.model.*;
// Simple JSON
import org.json.simple.*;
import org.json.simple.parser.*;

public class AwsApp 
{
	public static void main(String[] args)
	{
		String[] formats = {"avi", "wmv", "mp4", "mov"};
		try
		{
			Properties prop = new Properties();
			InputStream config = new FileInputStream("config.properties");
			prop.load(config);
			String workDir = prop.getProperty("workDir");
			String sqsUrl = prop.getProperty("sqsUrl");
			String s3OutputBucket = prop.getProperty("s3OutputBucket");
			String s3OutputPrefix = prop.getProperty("s3OutputPrefix");

			Transcoder transcoder = new Transcoder(workDir);
			AwsStorageHandler storage = new AwsStorageHandler(workDir);
			AmazonSQSClient client = new AmazonSQSClient();
			client.configureRegion(Regions.AP_SOUTHEAST_2);
			
			while (true)
			{
				try
				{
					// Checks out messages from SQS, then the jobs are not visible to other workers
					// unless there is a visibility timeout
					ReceiveMessageResult result = client.receiveMessage(sqsUrl);
					
					for (Message message : result.getMessages())
					{
						System.out.println(message.getMessageId() + "\t" + message.getBody());
						
						// Job handling here
						JSONParser parser = new JSONParser();
						Object body = parser.parse(message.getBody());
						JSONObject jsonObj = (JSONObject) body;
						JSONArray records = (JSONArray) jsonObj.get("Records");
						if (records != null)
						{
							Iterator i = records.iterator();
							while (i.hasNext())
							{
								JSONObject record = (JSONObject) i.next();
								JSONObject s3 = (JSONObject) record.get("s3");
								JSONObject s3Bucket = (JSONObject) s3.get("bucket");
								JSONObject s3Object = (JSONObject) s3.get("object");
								String bucket = (String) s3Bucket.get("name");
								String key = (String) s3Object.get("key");
								String filename = key;
								System.out.println(bucket + "\t" + key);
								int pos = key.lastIndexOf("/");
								if (pos != -1)
								{
									filename = key.substring(pos+1);
								}
		
								// Transcoding
								pos = filename.lastIndexOf(".");
								if (pos != -1)
								{
									String ext = filename.substring(pos+1).toLowerCase();
									if (Arrays.asList(formats).contains(ext))
									{
										// Valid video file, download
										String[] inputs = {bucket, key, filename};
										storage.download(inputs);
										
										// Transcode
										String uuid = UUID.randomUUID().toString();
										transcoder.transcode(uuid, filename);
										
										// Upload outputs
										String[] outputs = {s3OutputBucket, s3OutputPrefix, uuid};										
										storage.upload(outputs);
										
										// Delete local files
										storage.deleteLocalFile(filename);
										for (String format : formats)
										{
											storage.deleteLocalFile(uuid + "." + format);											
										}
									}
									
								}
							}							
						}

						// Delete the message from SQS, the job is considered as completed.
						client.deleteMessage(sqsUrl, message.getReceiptHandle());
					}
				} catch (Exception e2)
				{
					System.out.println(e2.getMessage());
					e2.printStackTrace();					
				}
			}
		} catch (Exception e1)	
		{
			System.out.println(e1.getMessage());
			e1.printStackTrace();
		}
		
	}
}