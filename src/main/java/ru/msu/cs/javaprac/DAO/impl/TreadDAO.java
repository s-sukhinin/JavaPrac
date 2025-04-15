package ru.msu.cs.javaprac.DAO.impl;

import lombok.NonNull;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.msu.cs.javaprac.DAO.IThreadDAO;
import ru.msu.cs.javaprac.models.Post;
import ru.msu.cs.javaprac.models.Thread;

import javax.persistence.criteria.*;
import java.util.List;

@Repository
public class TreadDAO extends CommonDAO<Thread> implements IThreadDAO
{
    public TreadDAO()
    {
        super(Thread.class);
    }

    public List<Thread> getThreadsByTitle(String title, ContentSortOrder order)
    {
        try(Session session = sessionFactory.openSession())
        {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Thread> criteria = builder.createQuery(Thread.class);
            Root<Thread> root = criteria.from(Thread.class);

            Predicate titleLike = builder.like(root.get("title"), "%" + title + "%");
            criteria.select(root).where(titleLike);

            Sort(criteria, root, builder, order);
            return session.createQuery(criteria).getResultList();
        }
    }

    public List<Post> getPostsInThread(Thread thread)
    {
        try(Session session = sessionFactory.openSession())
        {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Post> criteria = builder.createQuery(Post.class);
            Root<Post> root = criteria.from(Post.class);

            criteria.select(root).where(builder.equal(root.get("thread"), thread));

            return session.createQuery(criteria).getResultList();
        }
    }

    private static void Sort(CriteriaQuery<?> criteria,
                             From<?, ?> root,
                             CriteriaBuilder builder,
                             @NonNull ContentSortOrder order)
    {
        switch (order)
        {
            case TITLE_ASC -> criteria.orderBy(builder.asc(root.get("title")));
            case TITLE_DESC -> criteria.orderBy(builder.desc(root.get("title")));
        }
    }
}
