package botobo.core.infrastructure;

import botobo.core.domain.user.s3.ImageExtension;
import botobo.core.exception.user.s3.ImageExtensionNotAllowedException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class FileNameGenerator {
    private static final String BASE_DIR = "users/";
    private static final String SLASH = "/";

    public String generateFileName(MultipartFile multipartFile, String userName) {
        return makeNewFileName(replaceWhiteSpace(userName)) + extension(multipartFile);
    }

    private String replaceWhiteSpace(String userName) {
        return userName.replaceAll("\\s+", "_");
    }

    private String makeNewFileName(String userName) {
        String newlyCreatedFileName = UUID.randomUUID() + "_"
                + LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        return insertDirectory(newlyCreatedFileName, userName);
    }

    private String insertDirectory(String fileName, String userName) {
        return BASE_DIR + userName + SLASH + fileName;
    }

    private String extension(MultipartFile multipartFile) {
        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        if (!ImageExtension.isAllowedExtension(extension)) {
            throw new ImageExtensionNotAllowedException();
        }
        return insertDot(extension);
    }

    private String insertDot(String extension) {
        return "." + extension;
    }
}