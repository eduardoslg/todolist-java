package br.com.eduardoslg.todolist.users;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IUsersRepository extends JpaRepository<UsersModel, UUID> {
    UsersModel findByUsername(String username);
}
