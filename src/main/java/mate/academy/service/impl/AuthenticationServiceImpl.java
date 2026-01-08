package mate.academy.service.impl;

import java.util.Optional;
import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Inject;
import mate.academy.model.User;
import mate.academy.service.AuthenticationService;
import mate.academy.service.UserService;
import mate.academy.util.HashUtil;

public class AuthenticationServiceImpl implements AuthenticationService {
    @Inject
    private UserService userService;

    @Override
    public User login(String email, String password) throws AuthenticationException {
        Optional<User> userFromDbOption = userService.findByEmail(email);
        if (userFromDbOption.isEmpty()) {
            throw new AuthenticationException("Can't authenticate user");
        }
        User user = userFromDbOption.get();
        String hashPassword = HashUtil.hashPassword(password, user.getSalt());
        if (user.getPassword().equals(hashPassword)) {
            return user;
        }
        throw new AuthenticationException("Password does not match");
    }

    @Override
    public User register(String email, String password) throws RegistrationException {
        Optional<User> userFromDbOption = userService.findByEmail(email);
        if (userFromDbOption.isPresent()) {
            throw new RegistrationException("Already exists, try replacing it " + email);
        }
        User user = userFromDbOption.get();
        user.setEmail(email);
        String hashPassword = HashUtil.hashPassword(password, user.getSalt());
        user.setPassword(hashPassword);
        return user;
    }

}
