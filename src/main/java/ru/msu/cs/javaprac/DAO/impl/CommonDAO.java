package ru.msu.cs.javaprac.DAO.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import ru.msu.cs.javaprac.DAO.ICommonDAO;
import ru.msu.cs.javaprac.models.CommonEntity;

import javax.persistence.criteria.CriteriaQuery;
import java.util.Collection;

@Repository
public abstract class CommonDAO<T extends CommonEntity> implements ICommonDAO<T>
{
    protected SessionFactory sessionFactory;

    protected Class<T> persistentClass;

    public CommonDAO(Class<T> EntityClass)
    {
        this.persistentClass = EntityClass;
    }

    @Autowired
    public void setSessionFactory(LocalSessionFactoryBean sessionFactory)
    {
        this.sessionFactory = sessionFactory.getObject();
    }

    @Override
    public T getById(Integer id)
    {
        try (Session session = sessionFactory.openSession())
        {
            return session.get(persistentClass, id);
        }
    }

    @Override
    public Collection<T> getAll()
    {
        try (Session session = sessionFactory.openSession())
        {
            CriteriaQuery<T> criteria = session.getCriteriaBuilder().createQuery(persistentClass);
            criteria.from(persistentClass);
            return session.createQuery(criteria).getResultList();
        }
    }

    @Override
    public void save(T entity)
    {
        try (Session session = sessionFactory.openSession()) {
            if (entity.getId() != null) {
                entity.setId(null);
            }
            session.beginTransaction();
            session.saveOrUpdate(entity);
            session.getTransaction().commit();
        }
    }

    @Override
    public void saveCollection(Collection<T> entities) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            for (T entity : entities) {
                this.save(entity);
            }
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(T entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(entity);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(T entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(entity);
            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteById(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            T entity = getById(id);
            session.delete(entity);
            session.getTransaction().commit();
        }
    }

}
