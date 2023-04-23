package recipes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import recipes.models.Recipe;
import recipes.models.User;
import recipes.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public boolean addUser(User user) {

        if (userRepository.findUserByEmail(user.getEmail()) != null)
            return false;

        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);
        return true;
    }

    public User getUser(String email) {
        return userRepository.findUserByEmail(email);
    }
}
