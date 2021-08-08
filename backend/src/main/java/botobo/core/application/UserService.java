package botobo.core.application;

import botobo.core.domain.user.AppUser;
import botobo.core.domain.user.User;
import botobo.core.domain.user.UserRepository;
import botobo.core.dto.user.UserResponse;
import botobo.core.dto.user.UserUpdateRequest;
import botobo.core.exception.user.UserNotFoundException;
import botobo.core.exception.user.UserUpdateNotAllowedException;
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
        return UserResponse.of(findUserById(id));
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    public UserResponse update(Long id, UserUpdateRequest userUpdateRequest, AppUser appUser) {
        validateId(id, appUser.getId());
        User user = findUserById(appUser.getId());
        user.update(userUpdateRequest.toUser());
        return UserResponse.of(user);
    }

    private void validateId(Long id, long appUserId) {
        if (id != appUserId) {
            throw new UserUpdateNotAllowedException();
        }
    }
}
