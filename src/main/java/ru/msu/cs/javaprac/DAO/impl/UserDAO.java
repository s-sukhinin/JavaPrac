package ru.msu.cs.javaprac.DAO.impl;

import lombok.NonNull;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.msu.cs.javaprac.DAO.IUserDAO;
import ru.msu.cs.javaprac.models.Post;
import ru.msu.cs.javaprac.models.Thread;
import ru.msu.cs.javaprac.models.User;

import javax.persistence.criteria.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Repository
public class UserDAO extends  CommonDAO<User> implements IUserDAO
{
    public UserDAO()
    {
        super(User.class);
    }

    @Override
    public List<User> getUsersByName(String name, UsersSortOrder order)
    {
        try (Session session = sessionFactory.openSession())
        {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteria = builder.createQuery(User.class);
            Root<User> root = criteria.from(User.class);

            Predicate nameLike = builder.like(root.get("name"), "%" + name + "%");
            criteria.select(root).where(nameLike);

            Sort(criteria, root, builder, order);

            return session.createQuery(criteria).getResultList();
        }
    }

    @Override
    public User getUserByLogin(String login)
    {
        try (Session session = sessionFactory.openSession())
        {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteria = builder.createQuery(User.class);
            Root<User> root = criteria.from(User.class);

            criteria.select(root).where(builder.equal(root.get("login"), login));

            return session.createQuery(criteria).uniqueResult();
        }
    }

    @Override
    public List<User> getModerators(UsersSortOrder order)
    {
        try (Session session = sessionFactory.openSession())
        {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteria = builder.createQuery(User.class);
            Root<User> root = criteria.from(User.class);

            criteria.where(builder.equal(root.get("role"), User.RoleType.MODERATOR)).select(root);

            Sort(criteria, root, builder, order);

            return session.createQuery(criteria).getResultList();
        }
    }

    @Override
    public List<User> getCommonUsers(UsersSortOrder order)
    {
        try (Session session = sessionFactory.openSession())
        {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteria = builder.createQuery(User.class);
            Root<User> root = criteria.from(User.class);

            criteria.where(builder.equal(root.get("role"), User.RoleType.USER)).select(root);

            Sort(criteria, root, builder, order);

            return session.createQuery(criteria).getResultList();
        }
    }

    @Override
    public List<User> getAllUsersByDateOfRegistration(@NonNull LocalDate startDate, @NonNull LocalDate endDate, UsersSortOrder order)
    {
        try (Session session = sessionFactory.openSession())
        {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteria = builder.createQuery(User.class);
            Root<User> root = criteria.from(User.class);

            Timestamp startTimestamp = Timestamp.valueOf(startDate.atStartOfDay());
            Timestamp endTimestamp = Timestamp.valueOf(endDate.plusDays(1).atStartOfDay().minusNanos(1));
            criteria.where(builder.between(root.get("dateOfRegistration"), startTimestamp, endTimestamp));

            Sort(criteria, root, builder, order);

            return session.createQuery(criteria).getResultList();
        }
    }

    @Override
    public List<User> getByParticipation(HashSet<Thread> threads, UsersSortOrder order)
    {
        try (Session session = sessionFactory.openSession())
        {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteria = builder.createQuery(User.class);
            Root<Post> root = criteria.from(Post.class);

            Predicate threadIn = root.get("thread").in(threads);

            Join<Post, User> userJoin = root.join("author");
            criteria.select(userJoin).distinct(true).where(threadIn);

            Sort(criteria, userJoin, builder, order);

            return session.createQuery(criteria).getResultList();
        }
    }


    private static void Sort(CriteriaQuery<User> criteria,
                             From<?, User> root,
                             CriteriaBuilder builder,
                             @NonNull UsersSortOrder order)
    {
        switch (order)
        {
            case NAME_ASC -> criteria.orderBy(builder.asc(root.get("name")));
            case NAME_DESC -> criteria.orderBy(builder.desc(root.get("name")));
            case REGISTRATION_ASC -> criteria.orderBy(builder.asc(root.get("dateOfRegistration")));
            case REGISTRATION_DESC -> criteria.orderBy(builder.desc(root.get("dateOfRegistration")));
        }
    }


}
