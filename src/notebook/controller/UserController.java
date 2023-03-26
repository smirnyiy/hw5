package notebook.controller;

import notebook.model.User;
import notebook.model.repository.Repository;

import java.util.List;
import java.util.Optional;

public class UserController {
    private final Repository repository;

    public UserController(Repository repository) {
        this.repository = repository;
    }

    public void saveUser(User user) {
        repository.create(user);
    }

    public User findUser(Long userId) throws Exception {
        Optional<User> user = repository.findById(userId);

        if (user.isPresent())
            return user.get();

        throw new RuntimeException("User not found");

    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public void updateUser(String userId, User update) {
        update.setId(Long.parseLong(userId));
        repository.update(Long.parseLong(userId), update);
    }

    public void deleteUser(String userId) {
        repository.delete(Long.parseLong(userId));
    }
}