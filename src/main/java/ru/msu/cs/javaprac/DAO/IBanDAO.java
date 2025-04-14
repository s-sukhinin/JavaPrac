package ru.msu.cs.javaprac.DAO;

import ru.msu.cs.javaprac.models.Ban;
import ru.msu.cs.javaprac.models.Thread;
import ru.msu.cs.javaprac.models.User;

import java.util.List;

public interface IBanDAO  extends ICommonDAO<Ban>
{
    List<Ban> getActiveBansOfUser(User user);
}
