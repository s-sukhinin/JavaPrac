package ru.msu.cs.javaprac.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.msu.cs.javaprac.DAO.impl.UserDAO;
import ru.msu.cs.javaprac.models.User;

@Service
public class UserService
{
    @Autowired
    private final UserDAO userDAO = new UserDAO();

    public User authenticate(String login, String password)
    {
        User user = userDAO.getUserByLogin(login);
        if (user != null)
            if (user.getPassword().equals(password))
                return user;
        return null;
    }

    public void updateProfile(Integer userId, String newName, String newPassword) {
        User user = userDAO.getById(userId);
        if (newName != null && !newName.isBlank())
        {
            user.setName(newName);
        }
        if (newPassword != null && !newPassword.isBlank())
        {
            user.setPassword(newPassword);
        }
        userDAO.update(user);
    }

    public void deleteUser(Integer userId) {
        userDAO.deleteById(userId);
    }

    public User getById(Integer userId) {
       return userDAO.getById(userId);
    }
}
