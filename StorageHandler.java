/**
 *
 * SimpleTranscoder Project - StorageHandler
 * 
 * The StorageHandler deals with downloading input files from central storage to local storage, and 
 * uploading output files from local storage to central storage.
 *
 */
 

public abstract class StorageHandler
{			
	// Downloads the input file
	public abstract void download(String[] args);
	
	// Uploads the output files
	// Delete all temp files in workDir
	public abstract void upload(String[] args);
	
}