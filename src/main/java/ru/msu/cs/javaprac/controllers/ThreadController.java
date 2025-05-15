package ru.msu.cs.javaprac.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.msu.cs.javaprac.DAO.impl.BanDAO;
import ru.msu.cs.javaprac.DAO.impl.CategoryDAO;
import ru.msu.cs.javaprac.DAO.impl.PostDAO;
import ru.msu.cs.javaprac.DAO.impl.ThreadDAO;
import ru.msu.cs.javaprac.models.*;
import ru.msu.cs.javaprac.models.Thread;
import ru.msu.cs.javaprac.support.SessionStore;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ThreadController
{
    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private ThreadDAO threadDAO;

    @Autowired
    private PostDAO postDAO;

    @Autowired
    private BanDAO banDAO;

    @Autowired
    private SessionStore sessionStore;

    @GetMapping("/category/{id}/create-thread")
    public String showCreateThreadPage(@PathVariable Integer id,
                                       Model model,
                                       @CookieValue(value = "sessionId", required = false) String sessionId) {
        User user = sessionStore.getUserBySessionId(sessionId);
        model.addAttribute("user", user);
        if (user == null) {
            return "redirect:/login";
        }

        Category category = categoryDAO.getById(id);
        model.addAttribute("category", category);

        return "create-thread";
    }

    @PostMapping("/category/{id}/create-thread")
    public String createThread(@PathVariable Integer id,
                               @RequestParam String title,
                               Model model,
                               @CookieValue(value = "sessionId", required = false) String sessionId) {

        User user = sessionStore.getUserBySessionId(sessionId);
        model.addAttribute("user", user);
        if (user == null) {
            return "redirect:/login";
        }

        Category category = categoryDAO.getById(id);


        Thread thread = new Thread();
        thread.setTitle(title);
        thread.setCategory(category);
        thread.setAuthor(user);
        thread.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        threadDAO.save(thread);

        return "redirect:/thread/" + thread.getId();
    }

    @RequestMapping("/thread/{id}")
    public String viewThread(@PathVariable Integer id,
                             Model model,
                             @CookieValue(value = "sessionId", required = false) String sessionId) {

        Thread thread = threadDAO.getById(id);
        List<Post> messages = threadDAO.getPostsInThread(thread);

        model.addAttribute("thread", thread);
        model.addAttribute("messages", messages);

        User user = sessionStore.getUserBySessionId(sessionId);
        model.addAttribute("user", user);
        model.addAttribute("roleType", User.RoleType.class);
        if (isBannedFromCategory(user, thread.getCategory()) || isBannedFromThread(user, thread))
        {
            model.addAttribute("isBanned", true);
        }
        else
        {
            model.addAttribute("isBanned", false);
        }
        return "thread";
    }

    @PostMapping("/thread/{id}/add-message")
    public String addMessage(@PathVariable Integer id,
                             @RequestParam String content,
                             @CookieValue(value = "sessionId", required = false) String sessionId) {

        User user = sessionStore.getUserBySessionId(sessionId);
        if (user == null) return "redirect:/login";

        Thread thread = threadDAO.getById(id);



        Post post = new Post();
        post.setAuthor(user);
        post.setThread(thread);
        post.setContent(content);
        post.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        postDAO.save(post);

        return "redirect:/thread/" + id;
    }

    @PostMapping("/thread/{id}/delete")
    public String deleteThread(@PathVariable("id") Integer id,
                               @CookieValue(value = "sessionId") String sessionId)
    {
        User user = sessionStore.getUserBySessionId(sessionId);
        if (user.getRole() != User.RoleType.MODERATOR)
        {
            return "redirect:/thread/" + id;
        }
        Category category = threadDAO.getById(id).getCategory();
        threadDAO.deleteById(id);
        return "redirect:/category/" + category.getId() + "?sort=default";

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
