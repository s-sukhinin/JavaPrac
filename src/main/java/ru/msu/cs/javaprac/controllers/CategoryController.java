package ru.msu.cs.javaprac.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.msu.cs.javaprac.DAO.ICommonDAO;
import ru.msu.cs.javaprac.DAO.impl.CategoryDAO;
import ru.msu.cs.javaprac.DAO.impl.ThreadDAO;
import ru.msu.cs.javaprac.DAO.impl.UserDAO;
import ru.msu.cs.javaprac.models.Category;
import ru.msu.cs.javaprac.models.User;
import ru.msu.cs.javaprac.models.Thread;
import ru.msu.cs.javaprac.support.SessionStore;

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

        return "category";
    }

    private ICommonDAO.ContentSortOrder parseSort(String sort)
    {
        return switch (sort) {
            case "titleAsc" -> ICommonDAO.ContentSortOrder.TITLE_ASC;
            case "titleDesc" -> ICommonDAO.ContentSortOrder.TITLE_DESC;
            default -> ICommonDAO.ContentSortOrder.DEFAULT;
        };
    }

}
