package br.com.eduardoslg.todolist.users;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    private IUserRepository userRepository;

    @PostMapping("")
    public UserModel create(@RequestBody UserModel user) {
        UserModel output = this.userRepository.save(user);

        return output;
    }
}
