package notebook.model.repository.impl;

import notebook.util.DBConnector;
import notebook.util.mapper.impl.UserMapper;
import notebook.model.User;
import notebook.model.repository.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserRepository implements Repository {
    private final UserMapper mapper;
    private final DBConnector connector;

    public UserRepository(DBConnector connector, String sep) {
        this.mapper = new UserMapper(sep);
        this.connector = connector;
    }

    public UserRepository(DBConnector connector) {
        this(connector, ",");
    }

    public List<String> readAll() {
        List<String> lines = new ArrayList<>();
        try {
            File file = new File(connector.dbPath);
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            if (line != null) {
                lines.add(line);
            }
            while (line != null) {
                line = reader.readLine();
                if (line != null) {
                    lines.add(line);
                }
            }
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    @Override
    public List<User> findAll() {
        List<String> lines = readAll();
        List<User> users = new ArrayList<>();
        for (String line : lines) {
            users.add(mapper.toOutput(line));
        }
        return users;
    }

    @Override
    public User create(User user) {
        List<User> users = findAll();
        long max = 0L;
        for (User u : users) {
            long id = u.getId();
            if (max < id) {
                max = id;
            }
        }
        long next = max + 1;
        user.setId(next);
        users.add(user);
        write(users);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        List<User> users = findAll();
        for (User user : users) {
            if (Objects.equals(user.getId(), id)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> update(Long userId, User update) {
        List<User> users = findAll();
        User editUser = users.stream()
                .filter(u -> u.getId()
                        .equals(userId))
                .findFirst().orElseThrow(() -> new RuntimeException("User not found"));
        editUser.setFirstName(update.getFirstName());
        editUser.setLastName(update.getLastName());
        editUser.setPhone(update.getPhone());
        write(users);
        return Optional.of(update);
    }

    @Override
    public boolean delete(Long id) {
        List<User> users = findAll();

        for (User user : users) {
            if (user.getId().equals(id)) {
                users.remove(user);
                write(users);
                return true;
            }
        }

        return false;
    }

    private void write(List<User> users) {
        List<String> lines = new ArrayList<>();
        for (User u : users) {
            lines.add(mapper.toInput(u));
        }

        try (FileWriter writer = new FileWriter(connector.dbPath, false)) {
            for (String line : lines) {
                writer.write(line);
                writer.append('\n');
            }
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}