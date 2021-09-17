package botobo.core.infrastructure.s3;

import botobo.core.exception.user.s3.FileConvertFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Profile("local")
@Slf4j
@RequiredArgsConstructor
@Component
public class FakeS3Uploader implements FileUploader {
    @Override
    public String upload(MultipartFile multipartFile, String userId) throws IOException {
        return null;
    }

    private File makeUploadImageFile(MultipartFile multipartFile) throws IOException {
        return convert(multipartFile)
                .orElseThrow(FileConvertFailedException::new);
    }

    private Optional<File> convert(MultipartFile multipartFile) throws IOException {
        File convertFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(multipartFile.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    private void deleteTemporaryFile(File uploadImage) {
        if (!uploadImage.delete()) {
            log.info("업로드용 파일이 제대로 삭제되지 않았습니다.");
        }
    }

    @Override
    public void deleteFromS3(String oldImageUrl) {

    }
}
