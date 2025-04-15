package ru.msu.cs.javaprac.DAO.impl;

import lombok.NonNull;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.msu.cs.javaprac.DAO.ICategoryDAO;
import ru.msu.cs.javaprac.models.Category;

import ru.msu.cs.javaprac.models.Thread;
import ru.msu.cs.javaprac.models.User;

import javax.persistence.criteria.*;
import java.util.List;

@Repository
public class CategoryDAO extends CommonDAO<Category> implements ICategoryDAO
{
    public CategoryDAO()
    {
        super(Category.class);
    }

    @Override
    public List<Category> getCategoriesByTitle(String title, ContentSortOrder order)
    {
        try (Session session = sessionFactory.openSession())
        {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Category> criteria = builder.createQuery(Category.class);
            Root<Category> root = criteria.from(Category.class);

            Predicate titleLike = builder.like(root.get("title"), "%" + title + "%");
            criteria.select(root).where(titleLike);

            Sort(criteria, root, builder, order);
            return session.createQuery(criteria).getResultList();
        }
    }

    public List<Thread> getThreadsInCategory(Category category, ContentSortOrder order)
    {
        try (Session session = sessionFactory.openSession())
        {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Thread> criteria = builder.createQuery(Thread.class);
            Root<Thread> root = criteria.from(Thread.class);

            criteria.select(root).where(builder.equal(root.get("category"), category));

            Sort(criteria, root, builder, order);

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
