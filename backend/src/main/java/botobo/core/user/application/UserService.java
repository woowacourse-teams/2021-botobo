package botobo.core.user.application;

import botobo.core.user.domain.User;
import botobo.core.user.domain.UserRepository;
import botobo.core.user.dto.UserResponse;
import botobo.core.user.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        return UserResponse.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .profileUrl(user.getProfileUrl())
                .build();
    }
}
