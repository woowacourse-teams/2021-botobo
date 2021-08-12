package botobo.core.utils;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FilenameUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.Objects;

public class FileFactory {

    private static final ClassLoader classLoader = FileFactory.class.getClassLoader();
    private static final String PREFIX = "images/";

    public static MockMultipartFile testFile(String extension) {
        return createMockMultipartFile(PREFIX + "botobo." + extension);
    }

    public static MockMultipartFile emptyFile() {
        return createMockMultipartFile(PREFIX + "empty.jpeg");
    }

    private static MockMultipartFile createMockMultipartFile(String fileName) {
        URL resource = classLoader.getResource(fileName);
        Objects.requireNonNull(resource);

        File file = new File(resource.getFile());
        try {
            // name, originalFileName, contentType, content
            return new MockMultipartFile(
                    "mockFiles",
                    fileName,
                    FilenameUtils.getExtension(file.getName()),
                    Files.readAllBytes(file.toPath())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static MultipartFile uploadFile(File file, String fileName) throws IOException {
        return createMultipartFile(file, fileName);
    }

    private static MultipartFile createMultipartFile(File file, String fileName) throws IOException {
        FileItem fileItem = new DiskFileItem(
                fileName,
                Files.probeContentType(file.toPath()),
                false,
                file.getName(),
                (int) file.length(),
                file.getParentFile()
        );

        InputStream input = new FileInputStream(file);
        OutputStream os = fileItem.getOutputStream();
        IOUtils.copy(input, os);
        return new CommonsMultipartFile(fileItem);
    }
}
