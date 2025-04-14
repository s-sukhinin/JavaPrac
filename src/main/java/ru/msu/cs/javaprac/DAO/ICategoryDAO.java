package ru.msu.cs.javaprac.DAO;

import ru.msu.cs.javaprac.models.Category;
import ru.msu.cs.javaprac.models.Thread;
import ru.msu.cs.javaprac.models.User;

import java.time.LocalDate;
import java.util.List;

public interface ICategoryDAO extends ICommonDAO<Category>
{
    List<Category> getCategoriesByTitle(String title, ContentSortOrder order);
    List<Thread> getThreadsInCategory(Category category, ContentSortOrder order);
}
