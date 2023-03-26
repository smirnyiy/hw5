package notebook.model.repository;

import notebook.model.User;

import java.util.List;
import java.util.Optional;

public interface Repository {
    List<String> readAll();

    List<User> findAll();

    User create(User user);

    Optional<User> findById(Long id);

    Optional<User> update(Long userId, User update);

    boolean delete(Long id);
}