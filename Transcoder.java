/**
 *
 * SimpleTranscoder Project - Transcoder
 * 
 * The transcoder performs the transcoding work using ffmpeg
 *
 */
 
import java.io.*;
 
public class Transcoder
{
	public String workDir;
	
	public Transcoder(String workDir)
	{
		this.workDir  = workDir;
	}
	
	// Converts the input files into multiple formats
	// The output files are taskid.flv, taskid.mov, taskid.mp4, taskid.avi, taskid.wmv
	// The actual format conversion is done by calling a bash script with the necessary parameters
	
	public void transcode(String taskId, String inputFile)
	{
		try
		{
			String command = String.format("/bin/bash bin/convert.sh %s %s %s", workDir, inputFile, taskId);
			Process p = Runtime.getRuntime().exec(command);
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) 
			{
				System.out.println(line);
			}
			p.waitFor();
			in.close();
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}