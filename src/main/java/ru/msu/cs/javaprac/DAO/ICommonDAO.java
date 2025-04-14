package ru.msu.cs.javaprac.DAO;


import ru.msu.cs.javaprac.models.CommonEntity;

import java.util.Collection;

public interface ICommonDAO<T extends CommonEntity>
{
    public enum UsersSortOrder
    {
        DEFAULT,
        NAME_DESC,
        NAME_ASC,
        REGISTRATION_DESC,
        REGISTRATION_ASC
    }

    public enum ContentSortOrder
    {
        DEFAULT,
        TITLE_DESC,
        TITLE_ASC
    }

    T getById(Integer id);

    Collection<T> getAll();

    void save(T entity);

    void saveCollection(Collection<T> entities);

    void delete(T entity);

    void deleteById(Integer id);

    void update(T entity);
}
