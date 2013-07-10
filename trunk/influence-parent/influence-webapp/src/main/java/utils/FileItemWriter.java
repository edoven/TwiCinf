package utils;

import org.apache.commons.fileupload.FileItem;

import java.io.File;

public class FileItemWriter
{
	public static void writeFileItem(FileItem fileItem, String destinationPath) throws Exception
	{
		 File uploadedFile = new File(destinationPath);
	     System.out.println(uploadedFile.getAbsolutePath());
	     fileItem.write(uploadedFile);

	}
}
