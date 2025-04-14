package ru.msu.cs.javaprac.DAO;

import ru.msu.cs.javaprac.models.User;
import ru.msu.cs.javaprac.models.Thread;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

public interface IUserDAO extends ICommonDAO<User>
{
    List<User> getUsersByName(String userName, UsersSortOrder order);
    List<User> getModerators(UsersSortOrder order);
    List<User> getCommonUsers(UsersSortOrder order);

    List<User> getAllUsersByDateOfRegistration(LocalDate startDate, LocalDate endDate, UsersSortOrder order);

    List<User> getByParticipation(HashSet<Thread> threads, UsersSortOrder order);
}
