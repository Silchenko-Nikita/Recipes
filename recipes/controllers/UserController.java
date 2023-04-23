package recipes.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import recipes.models.User;
import recipes.services.UserService;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/api/register")
    public void createUser(@RequestBody @Valid User user) throws Exception {
        boolean added = userService.addUser(user);

        if (!added) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
