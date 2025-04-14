package ru.msu.cs.javaprac.DAO.impl;

import org.hibernate.Session;
import ru.msu.cs.javaprac.DAO.ICommonDAO;
import ru.msu.cs.javaprac.DAO.IPostDAO;
import ru.msu.cs.javaprac.models.Attachment;
import ru.msu.cs.javaprac.models.Post;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class PostDAO extends CommonDAO<Post> implements IPostDAO
{
    public PostDAO()
    {
        super(Post.class);
    }

    public List<Attachment> getAttachments(Post post)
    {
        try(Session session = sessionFactory.openSession())
        {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Attachment> criteria = builder.createQuery(Attachment.class);
            Root<Attachment> root = criteria.from(Attachment.class);

            criteria.select(root).where(builder.equal(root.get("post"), post));

            return session.createQuery(criteria).getResultList();
        }
    }
}
