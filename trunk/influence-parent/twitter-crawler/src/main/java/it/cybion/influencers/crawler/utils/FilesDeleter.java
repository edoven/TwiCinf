package it.cybion.influencers.crawler.utils;


import java.io.File;



public class FilesDeleter
{

	public static void delete(File file)
	{
		if (file.exists())
		{
			if (file.isDirectory())
			{
				// directory is empty, then delete it
				if (file.list().length == 0)
				{
					file.delete();
					// logger.info("Directory is deleted : "+
					// file.getAbsolutePath());
				} else
				{
					// list all the directory contents
					String files[] = file.list();

					for (String temp : files)
					{
						// construct the file structure
						File fileDelete = new File(file, temp);
						// recursive delete
						delete(fileDelete);
					}
					// check the directory again, if empty then delete it
					if (file.list().length == 0)
					{
						file.delete();
						// logger.info("Directory is deleted : " +
						// file.getAbsolutePath());
					}
				}
			} else
			{
				// if file, then delete it
				file.delete();
				// logger.info("File is deleted : " + file.getAbsolutePath());
			}
		}
	}
}
