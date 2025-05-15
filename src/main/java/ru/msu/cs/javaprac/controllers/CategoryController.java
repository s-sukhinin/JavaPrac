package ru.msu.cs.javaprac.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.msu.cs.javaprac.DAO.ICommonDAO;
import ru.msu.cs.javaprac.DAO.impl.BanDAO;
import ru.msu.cs.javaprac.DAO.impl.CategoryDAO;
import ru.msu.cs.javaprac.DAO.impl.ThreadDAO;
import ru.msu.cs.javaprac.DAO.impl.UserDAO;
import ru.msu.cs.javaprac.models.Ban;
import ru.msu.cs.javaprac.models.Category;
import ru.msu.cs.javaprac.models.User;
import ru.msu.cs.javaprac.models.Thread;
import ru.msu.cs.javaprac.support.SessionStore;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class CategoryController
{
    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private ThreadDAO threadDAO;

    @Autowired
    private SessionStore sessionStore;

    @Autowired
    private BanDAO banDAO;

    @GetMapping("/category/{id}")
    public String viewCategory(@PathVariable("id") Integer id,
                               @RequestParam String sort,
                               Model model,
                               @CookieValue(value = "sessionId", required = false) String sessionId) {

        Category category = categoryDAO.getById(id);
        List<Thread> threads = categoryDAO.getThreadsInCategory(category, parseSort(sort));

        model.addAttribute("category", category);
        model.addAttribute("threads", threads);
        model.addAttribute("sort", sort);

        User user = sessionStore.getUserBySessionId(sessionId);
        model.addAttribute("user", user);
        model.addAttribute("roleType", User.RoleType.class);

        if (isBannedFromCategory(user, category))
        {
            model.addAttribute("isBanned", true);
        }
        else
        {
            model.addAttribute("isBanned", false);
        }

        return "category";
    }

    @GetMapping("/create_category")
    public String showCreateCategoryPage(Model model,
                                 @CookieValue(value = "sessionId", required = false) String sessionId)
    {
        model.addAttribute("user", sessionStore.getUserBySessionId(sessionId));
        model.addAttribute("roleType", User.RoleType.class);
        return "create_category";
    }

    @PostMapping("/create_category")
    public String createCategory(Model model,
                                 @CookieValue(value = "sessionId", required = false) String sessionId,
                                 @RequestParam String title,
                                 @RequestParam String description)
    {
        User author = sessionStore.getUserBySessionId(sessionId);

        Category category = new Category();
        category.setTitle(title);
        category.setDescription(description);
        category.setAuthor(author);
        category.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        categoryDAO.save(category);

        return "redirect:/category/" + category.getId() + "?sort=default";
    }

    @PostMapping("/category/{id}/delete")
    public String deleteCategory(@PathVariable("id") Integer id,
                                 @CookieValue(value = "sessionId") String sessionId)
    {
        User user = sessionStore.getUserBySessionId(sessionId);
        if (user.getRole() != User.RoleType.MODERATOR)
        {
            return "redirect:/category/" + id + "?sort=default";
        }

        categoryDAO.deleteById(id);
        return "redirect:/";
    }

    private ICommonDAO.ContentSortOrder parseSort(String sort)
    {
        return switch (sort) {
            case "titleAsc" -> ICommonDAO.ContentSortOrder.TITLE_ASC;
            case "titleDesc" -> ICommonDAO.ContentSortOrder.TITLE_DESC;
            default -> ICommonDAO.ContentSortOrder.DEFAULT;
        };
    }

    private boolean isBannedFromCategory(User user, Category category) {
        List<Ban> bans = banDAO.getActiveBansOfUser(user);
        return bans.stream()
                .anyMatch(b -> b.getCategory() != null && b.getCategory().equals(category));
    }

    private boolean isBannedFromThread(User user, Thread thread) {
        List<Ban> bans = banDAO.getActiveBansOfUser(user);
        return bans.stream()
                .anyMatch(b -> b.getThread() != null && b.getThread().equals(thread) ||
                        (b.getCategory() != null && b.getCategory().equals(thread.getCategory())));
    }

}
