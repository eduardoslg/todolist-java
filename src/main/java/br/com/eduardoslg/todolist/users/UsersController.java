package br.com.eduardoslg.todolist.users;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/v1/users")
public class UsersController {

    @Autowired
    private IUsersRepository userRepository;

    @PostMapping("")
    public ResponseEntity create(@RequestBody UsersModel user) {
        UsersModel userExists = this.userRepository.findByUsername(user.getUsername());

        if (userExists != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
        }

        String passwordHash = BCrypt.withDefaults()
            .hashToString(12, user.getPassword().toCharArray());
        user.setPassword(passwordHash);

        UsersModel output = this.userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }

    @GetMapping("")
    public List<UsersModel> list() {

        List<UsersModel> output = this.userRepository.findAll();

        return output;
    }
    
}
