package utils;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import java.io.File;

public class FileItemWriter {

    private static final Logger LOGGER = Logger.getLogger(FileItemWriter.class);

    public static void writeFileItem(FileItem fileItem, String destinationPath) throws Exception {

        File uploadedFile = new File(destinationPath);
        LOGGER.info(uploadedFile.getAbsolutePath());
        fileItem.write(uploadedFile);

    }
}
