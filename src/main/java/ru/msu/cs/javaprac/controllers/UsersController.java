package ru.msu.cs.javaprac.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.msu.cs.javaprac.DAO.ICommonDAO;
import ru.msu.cs.javaprac.DAO.IThreadDAO;
import ru.msu.cs.javaprac.DAO.impl.CategoryDAO;
import ru.msu.cs.javaprac.DAO.impl.ThreadDAO;
import ru.msu.cs.javaprac.DAO.impl.UserDAO;
import ru.msu.cs.javaprac.models.Category;
import ru.msu.cs.javaprac.models.User;
import ru.msu.cs.javaprac.models.Thread;
import ru.msu.cs.javaprac.support.SessionStore;
import ru.msu.cs.javaprac.support.UserFilterForm;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class UsersController
{
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private ThreadDAO threadDAO;

    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private SessionStore sessionStore;

    @GetMapping("/users")
    public String showUsers(@ModelAttribute UserFilterForm filter,
                            Model model,
                            @CookieValue(value = "sessionId", required = false) String sessionId)
    {
        User user = sessionStore.getUserBySessionId(sessionId);
        model.addAttribute("user", user);

        List<User> users = getFilteredUsers(filter);

        model.addAttribute("users", users);
        model.addAttribute("filter", filter);
        List<Category> categories = new ArrayList<>();
        Map<Integer, List<Thread>> threadsByCategory = new HashMap<>();

        for (Category category : categoryDAO.getAll()) {
            List<Thread> threads = categoryDAO.getThreadsInCategory(category, ICommonDAO.ContentSortOrder.DEFAULT);
            if (!threads.isEmpty())
            {
                threadsByCategory.put(category.getId(), threads);
                categories.add(category);
            }
        }

        model.addAttribute("categories", categories);
        model.addAttribute("threadsByCategory", threadsByCategory);

        return "users";
    }

    private List<User> getFilteredUsers(UserFilterForm filter) {
        ICommonDAO.UsersSortOrder order = parseSortOrder(filter.getSort());

        List<User> result = (List<User>) userDAO.getAllUsers(order);
        if (filter.getName() != null && !filter.getName().isBlank()) {
            result.retainAll(userDAO.getUsersByName(filter.getName(), order));
        }

        if (filter.getStartDate() != null && filter.getEndDate() != null) {
            result.retainAll(userDAO.getAllUsersByDateOfRegistration(filter.getStartDate(), filter.getEndDate(), order));
        }

        if ("MODERATOR".equals(filter.getRole())) {
            result.retainAll(userDAO.getModerators(order));
        }

        if ("USER".equals(filter.getRole())) {
            result.retainAll(userDAO.getCommonUsers(order));
        }
        if (filter.getThreads() != null) {
            HashSet<Thread> threads = filter.getThreads().stream()
                                        .map(threadDAO::getById)
                                        .collect(Collectors.toCollection(HashSet::new));
            result.retainAll(userDAO.getByParticipation(threads, ICommonDAO.UsersSortOrder.DEFAULT));
        }

        return result;
    }

    private ICommonDAO.UsersSortOrder parseSortOrder(String sort) {
        return switch (sort) {
            case "nameDesc" -> ICommonDAO.UsersSortOrder.NAME_DESC;
            case "regAsc" -> ICommonDAO.UsersSortOrder.REGISTRATION_ASC;
            case "regDesc" -> ICommonDAO.UsersSortOrder.REGISTRATION_DESC;
            case "nameAsc" -> ICommonDAO.UsersSortOrder.NAME_ASC;
            default -> ICommonDAO.UsersSortOrder.DEFAULT;
        };
    }

}
