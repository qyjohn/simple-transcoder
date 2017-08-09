/**
 *
 * SimpleTranscoder Project - PostProcessor
 * 
 * The PreProcessor uploads the output files to the central storage. 
 *
 */
 

public abstract class PostProcessor
{	
	public String workDir;
	
	public PostProcessor(String workDir)
	{
		this.workDir = workDir;
	}
		
	// Uploads the output files
	// Delete all temp files in workDir
	public abstract String uploadOutput(String jobInfo);
}