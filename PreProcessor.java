/**
 *
 * SimpleTranscoder Project - PreProcessor
 * 
 * The PreProcessor gets a job from the system, and downloads the input file. 
 *
 */
 

public abstract class PreProcessor
{	
	public String workDir;
	
	public PreProcessor(String workDir)
	{
		this.workDir = workDir;
	}
	
	public String fetchJob()
	{
		// Blocking function to obtain a job from the system. If there is no job in the system, wait for ever
		String jobInfo = waitForJob();
		
		// Downloads the input file to workDir
		String fileName = downloadInput(jobInfo);
		
		// Return the input filename to the application
		return fileName;
	}
	
	// Retrieves a job definitions
	public abstract String waitForJob();
	
	// Downloads the input file
	public abstract String downloadInput(String jobInfo);
}