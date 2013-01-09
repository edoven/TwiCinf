package it.cybion.influencers.utils;

import java.io.File;
import java.io.IOException;

public class FilesDeleter {
	public static void delete(File file) throws IOException{
		if(file.isDirectory()){
			//directory is empty, then delete it
	    	if(file.list().length==0){	 
	    		   file.delete();
//	    		   logger.info("Directory is deleted : "+ file.getAbsolutePath());
	    	}
	    	else{
	    		//list all the directory contents
	    		String files[] = file.list();
	 
	    		for (String temp : files) {
	    			//construct the file structure
	    			File fileDelete = new File(file, temp);
	    			//recursive delete
	    			delete(fileDelete);
	    		}
	    		//check the directory again, if empty then delete it
	        	if(file.list().length==0){
	        		file.delete();
//	        	    logger.info("Directory is deleted : " + file.getAbsolutePath());
	        	}
	    	}	 
	    }
		else{
	    		//if file, then delete it
	    		file.delete();
//	    		logger.info("File is deleted : " + file.getAbsolutePath());
	    	}
	    }
}
