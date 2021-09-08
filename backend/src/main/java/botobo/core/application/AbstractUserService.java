package botobo.core.application;

import botobo.core.domain.user.AppUser;
import botobo.core.domain.user.User;
import botobo.core.domain.user.UserRepository;
import botobo.core.exception.user.UserNotFoundException;

public abstract class AbstractUserService {
    protected final UserRepository userRepository;

    protected AbstractUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    protected User findUser(AppUser appUser) {
        return userRepository.findById(appUser.getId())
                .orElseThrow(UserNotFoundException::new);
    }
}
