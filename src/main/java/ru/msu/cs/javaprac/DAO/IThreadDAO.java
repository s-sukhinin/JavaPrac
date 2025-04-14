package ru.msu.cs.javaprac.DAO;

import ru.msu.cs.javaprac.models.Post;
import ru.msu.cs.javaprac.models.Thread;

import java.util.List;

public interface IThreadDAO extends ICommonDAO<Thread>
{
    List<Thread> getThreadsByTitle(String title, ContentSortOrder order);
    List<Post> getPostsInThread(Thread thread);
}
