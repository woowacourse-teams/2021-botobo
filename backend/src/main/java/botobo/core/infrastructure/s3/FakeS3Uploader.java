package botobo.core.infrastructure.s3;

import botobo.core.domain.user.User;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Profile({"local", "test"})
@Component
public class FakeS3Uploader implements FileUploader {
    @Override
    public String upload(MultipartFile multipartFile, User user) {
        return user.getProfileUrl();
    }

    @Override
    public void deleteFromS3(String oldImageUrl) {
        // nothing to do
    }
}
