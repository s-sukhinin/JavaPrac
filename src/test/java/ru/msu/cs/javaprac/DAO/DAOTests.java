package ru.msu.cs.javaprac.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cs.javaprac.models.*;
import ru.msu.cs.javaprac.models.Thread;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations="classpath:application-test.properties")
public class DAOTests
{
    @Autowired
    private IUserDAO userDAO;

    @Autowired
    private IPostDAO postDAO;

    @Autowired
    private IThreadDAO threadDAO;

    @Autowired
    private ICategoryDAO categoryDAO;

    @Autowired
    private IBanDAO banDAO;

    @Autowired
    private IAttachmentDAO attachmentDAO;


    @Autowired
    private SessionFactory sessionFactory;

    @Test
    void userManipulations()
    {
        List<User> userListAll = (List<User>) userDAO.getAll();
        assertEquals(6, userListAll.size());

        List<User> userListName = userDAO.getUsersByName("John", ICommonDAO.UsersSortOrder.DEFAULT);
        assertEquals(2, userListName.size());

        List<User> moderList = userDAO.getModerators(ICommonDAO.UsersSortOrder.NAME_ASC);
        assertEquals(2, moderList.size());
        assertEquals(User.RoleType.MODERATOR, moderList.get(0).getRole());

        List<User>commonUserList = userDAO.getCommonUsers(ICommonDAO.UsersSortOrder.REGISTRATION_DESC);
        assertEquals(4, commonUserList.size());
        assertEquals("SuperUser", commonUserList.get(0).getLogin());

        List<User> usersInThread = userDAO.getByParticipation(new HashSet<>(Set.of(threadDAO.getById(1))), ICommonDAO.UsersSortOrder.NAME_DESC);
        assertEquals(2, usersInThread.size());
        assertEquals("qwerty", usersInThread.get(0).getPassword());
        assertEquals(4, usersInThread.get(0).getId().intValue());
        assertEquals("Julia", usersInThread.get(0).getName());

        List<User> usersByReg = userDAO.getAllUsersByDateOfRegistration(LocalDate.parse("2020-01-01"), LocalDate.parse("2022-12-31"), ICommonDAO.UsersSortOrder.REGISTRATION_ASC);
        assertEquals(3, usersByReg.size());
        assertEquals(Timestamp.valueOf("2020-01-01 10:00:00"), usersByReg.get(0).getDateOfRegistration());

        assertThrows(NullPointerException.class, () -> userDAO.getModerators(null));
        assertThrows(NullPointerException.class, () -> userDAO.getAllUsersByDateOfRegistration(null, LocalDate.parse("2022-12-31"), ICommonDAO.UsersSortOrder.REGISTRATION_ASC));
        assertThrows(NullPointerException.class, () -> userDAO.getAllUsersByDateOfRegistration(LocalDate.parse("2020-01-01"), null, ICommonDAO.UsersSortOrder.REGISTRATION_ASC));
    }

    @Test
    void userUpdate()
    {
        User updateUser = userDAO.getById(1);
        updateUser.setPassword("nlerjnergnjer");
        updateUser.setName("JOHNA");
        userDAO.update(updateUser);

        User user = userDAO.getById(1);
        assertEquals("JOHNA", user.getName());
        assertEquals("nlerjnergnjer", user.getPassword());
    }

    @Test
    void userDelete()
    {
        User deleteUser = userDAO.getUsersByName("Johna", ICommonDAO.UsersSortOrder.DEFAULT).get(0);
        userDAO.delete(deleteUser);

        List<User> userJOHNA = userDAO.getUsersByName("Johna", ICommonDAO.UsersSortOrder.DEFAULT);
        assertEquals(0, userJOHNA.size());

        userDAO.deleteById(2);
        assertNull(userDAO.getById(2));
    }

    @Test
    void banManipulation()
    {
        List<Ban> banListAll = (List<Ban>) banDAO.getAll();
        assertEquals(4, banListAll.size());

        List<Ban> banToUser = banDAO.getActiveBansOfUser(userDAO.getById(3));
        assertEquals(2, banToUser.size());
    }

    @Test
    void postManipulation()
    {
        List<Attachment> attachInPost = postDAO.getAttachments(postDAO.getById(1));
        assertEquals(2, attachInPost.size());
    }

    @Test
    void threadManipulation()
    {
        List<Thread> all = (List<Thread>) threadDAO.getAll();

        List<Thread> threadByTitle = threadDAO.getThreadsByTitle("th1", ICommonDAO.ContentSortOrder.DEFAULT);
        assertEquals(1, threadByTitle.size());

        List<Thread> threadByTitle1 = threadDAO.getThreadsByTitle("th", ICommonDAO.ContentSortOrder.TITLE_DESC);
        assertEquals(3, threadByTitle1.size());
        assertEquals("th3", threadByTitle1.get(0).getTitle());

        List<Thread> threadByTitle2 = threadDAO.getThreadsByTitle("th", ICommonDAO.ContentSortOrder.TITLE_ASC);
        assertEquals(3, threadByTitle2.size());
        assertEquals("th1", threadByTitle2.get(0).getTitle());

        List<Post> postsInThread = threadDAO.getPostsInThread(threadDAO.getById(1));
        assertEquals(2, postsInThread.size());

        assertThrows(NullPointerException.class, () -> threadDAO.getThreadsByTitle("th", null));
    }

    @Test
    void categoryManipulation()
    {
        List<Category> categoryTitle = categoryDAO.getCategoriesByTitle("Cat", ICommonDAO.ContentSortOrder.DEFAULT);
        assertEquals(2, categoryTitle.size());

        List<Category> categoryTitle1 = categoryDAO.getCategoriesByTitle("Cat", ICommonDAO.ContentSortOrder.TITLE_DESC);
        assertEquals(2, categoryTitle1.size());
        assertEquals("Cat2", categoryTitle1.get(0).getTitle());

        List<Thread> threadsInCategory = categoryDAO.getThreadsInCategory(categoryDAO.getById(1), ICommonDAO.ContentSortOrder.TITLE_ASC);
        assertEquals(3, threadsInCategory.size());
        assertEquals("th1", threadsInCategory.get(0).getTitle());
        assertEquals("th3", threadsInCategory.get(2).getTitle());

        assertThrows(NullPointerException.class, () -> categoryDAO.getCategoriesByTitle("Cat", null));
    }

    @BeforeEach
    void beforeEach()
    {
        List<User> users = new ArrayList<>();
        User admin = new User(123, "Admin", "pswrd", "Johna", Timestamp.valueOf("2019-01-01 10:00:00") , User.RoleType.MODERATOR );
        users.add(admin);
        users.add(new User(null, "CoolerAdmin", "strongPassword", "Petr", Timestamp.valueOf("2024-01-01 10:00:00"), User.RoleType.MODERATOR ));

        User user1 = new User(null, "John123", "pswrd123", "John", Timestamp.valueOf("2020-01-01 10:00:00"), User.RoleType.USER );
        User user2 = new User(345, "Julia_", "qwerty", "Julia", Timestamp.valueOf("2021-01-01 10:00:00"), User.RoleType.USER );
        users.add(user1);
        users.add(user2);
        users.add(new User(567, "Oleg007", "qwerty1", "Dave", Timestamp.valueOf("2022-01-01 10:00:00"), User.RoleType.USER ));
        users.add(new User(null, "SuperUser", "qwerty2", "Winston", Timestamp.valueOf("2023-01-01 10:00:00"), User.RoleType.USER ));

        List<Category> categories = new ArrayList<>();
        Category cat1 = new Category(null, "Cat1", "description", admin, Timestamp.valueOf("2020-01-02 10:00:00") );
        categories.add(cat1);
        categories.add(new Category(null, "Cat2", "description", admin, Timestamp.valueOf("2020-01-02 10:00:00") ));
        categories.add(new Category(null, "2taC", "description", admin, Timestamp.valueOf("2020-01-02 10:00:00") ));


        List<Thread> threads = new ArrayList<>();
        Thread th1 = new Thread(100, "th1", cat1, user1, Timestamp.valueOf("2020-10-01 10:00:00"));
        threads.add(th1);
        Thread th2 = new Thread("th2", cat1);
        threads.add(th2);
        threads.add(new Thread("th3", cat1));

        List<Post> posts = new ArrayList<>();
        Post p1 = new Post(null, th1, user1, "hello", Timestamp.valueOf("2024-01-01 10:00:00"));
        Post p2 = new Post(null, th1, user2, "hello1", Timestamp.valueOf("2024-01-02 10:00:00"));
        posts.add(p1);
        posts.add(p2);
        posts.add(new Post(null, th2, user2, "hello2", Timestamp.valueOf("2024-01-02 10:00:00")));

        List<Ban> bans = new ArrayList<>();
        bans.add(new Ban(null, user1, th1, null, Timestamp.valueOf("2024-01-01 10:00:00"), Timestamp.valueOf("2025-01-01 10:00:00"), "spam", admin));
        bans.add(new Ban(null, user1, th2, null, Timestamp.valueOf("2025-01-01 10:00:00"), Timestamp.valueOf("2026-01-01 10:00:00"), "spam", admin));
        bans.add(new Ban(null, user1, null, cat1, Timestamp.valueOf("2024-01-01 10:00:00"), null, "spam", admin));
        bans.add(new Ban(null, user2, th1, null, Timestamp.valueOf("2024-01-01 10:00:00"), Timestamp.valueOf("2025-01-01 10:00:00"), "spam", admin));

        List<Attachment> attachments = new ArrayList<>();
        attachments.add(new Attachment(null, p1, "123", "456", Timestamp.valueOf("2024-01-01 10:00:00")));
        attachments.add(new Attachment(null, p1, "qwer", "ty", Timestamp.valueOf("2024-01-02 10:00:00")));
        attachments.add(new Attachment(null, p2, "123", "456", Timestamp.valueOf("2024-01-01 10:00:00")));

        userDAO.saveCollection(users);
        categoryDAO.saveCollection(categories);
        threadDAO.saveCollection(threads);
        postDAO.saveCollection(posts);
        banDAO.saveCollection(bans);
        attachmentDAO.saveCollection(attachments);
    }

    @BeforeAll
    @AfterEach
    void annihilation()
    {
        try (Session session = sessionFactory.openSession())
        {
            session.beginTransaction();
            session.createSQLQuery("TRUNCATE users RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("TRUNCATE categories RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("TRUNCATE threads RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("TRUNCATE posts RESTART IDENTITY CASCADE;").executeUpdate();

            session.createSQLQuery("ALTER SEQUENCE users_user_id_seq RESTART WITH 1;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE categories_category_id_seq RESTART WITH 1;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE threads_thread_id_seq RESTART WITH 1;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE posts_post_id_seq RESTART WITH 1;").executeUpdate();

            session.getTransaction().commit();
        }
    }
}
