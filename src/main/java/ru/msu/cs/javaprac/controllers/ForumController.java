package ru.msu.cs.javaprac.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.msu.cs.javaprac.DAO.impl.CategoryDAO;
import ru.msu.cs.javaprac.support.SessionStore;
import ru.msu.cs.javaprac.models.Category;
import ru.msu.cs.javaprac.models.User;

import java.util.List;

@Controller
public class ForumController
{
    @Autowired
    private final CategoryDAO categoryDAO = new CategoryDAO();

    @Autowired
    private final SessionStore sessionStore = new SessionStore();

    @RequestMapping(value = {"/", "/index"})
    public String index(Model model, @CookieValue(value = "sessionId", required = false) String sessionId)
    {

        List<Category> categories = (List<Category>) categoryDAO.getAll();
        model.addAttribute("categories", categories);

        if (sessionId != null)
        {
            User user = sessionStore.getUserBySessionId(sessionId);
            model.addAttribute("user", user);
        }
        else
            model.addAttribute("user", null);

        model.addAttribute("roleType", User.RoleType.class);

        return "index";
    }
}
