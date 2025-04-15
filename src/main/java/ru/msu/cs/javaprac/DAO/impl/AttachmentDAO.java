package ru.msu.cs.javaprac.DAO.impl;

import org.springframework.stereotype.Repository;
import ru.msu.cs.javaprac.DAO.IAttachmentDAO;
import ru.msu.cs.javaprac.models.Attachment;

@Repository
public class AttachmentDAO extends CommonDAO<Attachment> implements IAttachmentDAO
{
    public AttachmentDAO()
    {
        super(Attachment.class);
    }
}
