package botobo.core.infrastructure.file;

import botobo.core.exception.user.s3.ImageExtensionNotAllowedException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class FileNameGenerator {

    public UploadFile generateUploadFile(MultipartFile multipartFile) {
        String fileName = makeNewFileName();
        String extension = extension(multipartFile);
        String contentType = contentType(extension);

        return UploadFile.of(
                multipartFile,
                fileName + insertDot(extension),
                contentType
        );
    }

    private String makeNewFileName() {
        return UUID.randomUUID() + "_"
                + LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);
    }

    private String extension(MultipartFile multipartFile) {
        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        if (!ImageType.isAllowedExtension(extension)) {
            throw new ImageExtensionNotAllowedException();
        }
        return extension;
    }

    private String contentType(String extension) {
        return ImageType.contentType(extension);
    }

    private String insertDot(String extension) {
        return "." + extension;
    }
}
