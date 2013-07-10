package utils;

import java.io.File;

import org.apache.commons.fileupload.FileItem;

public class FileItemWriter
{
	public static void writeFileItem(FileItem fileItem, String destinationPath) throws Exception
	{
		 File uploadedFile = new File(destinationPath);
	     System.out.println(uploadedFile.getAbsolutePath());
	     fileItem.write(uploadedFile);

	}
}
