package botobo.core.infrastructure.s3;

import botobo.core.domain.user.s3.ImageType;
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

    public String generateUploadFile(MultipartFile multipartFile, String userId) {
        return makeNewFileName(userId) + extension(multipartFile);
    }

    private String makeNewFileName(String userId) {
        String newlyCreatedFileName = UUID.randomUUID() + "_"
                + LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        return insertDirectory(newlyCreatedFileName, userId);
    }

    private String insertDirectory(String fileName, String userId) {
        return BASE_DIR + userId + SLASH + fileName;
    }

    private String extension(MultipartFile multipartFile) {
        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        if (!ImageType.isAllowedExtension(extension)) {
            throw new ImageExtensionNotAllowedException();
        }
        return insertDot(extension);
    }

    private String insertDot(String extension) {
        return "." + extension;
    }
}
