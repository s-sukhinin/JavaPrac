package ru.msu.cs.javaprac.DAO.impl;

import org.hibernate.Session;
import ru.msu.cs.javaprac.DAO.IBanDAO;
import ru.msu.cs.javaprac.DAO.ICommonDAO;
import ru.msu.cs.javaprac.models.Ban;
import ru.msu.cs.javaprac.models.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

public class BanDAO extends CommonDAO<Ban> implements IBanDAO
{
    public BanDAO()
    {
        super(Ban.class);
    }

    public List<Ban> getActiveBansOfUser(User user)
    {
        try(Session session = sessionFactory.openSession())
        {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Ban> criteria = builder.createQuery(Ban.class);
            Root<Ban> root = criteria.from(Ban.class);

            criteria.select(root).where(builder.equal(root.get("user"), user));

            Timestamp now = Timestamp.from(Instant.now());

            Predicate userMatch = builder.equal(root.get("user"), user);
            Predicate isPermanent = builder.isNull(root.get("bannedUntil"));
            Predicate isStillActive = builder.greaterThan(root.get("bannedUntil"), now);
            Predicate activeBan = builder.or(isPermanent, isStillActive);

            criteria.where(builder.and(userMatch, activeBan));

            return session.createQuery(criteria).getResultList();
        }
    }


}
