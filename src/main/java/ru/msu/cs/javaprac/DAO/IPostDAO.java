package ru.msu.cs.javaprac.DAO;

import ru.msu.cs.javaprac.models.Attachment;
import ru.msu.cs.javaprac.models.Post;

import java.util.List;

public interface IPostDAO extends ICommonDAO<Post>
{
    List<Attachment> getAttachments(Post post);
}
