package ru.msu.cs.javaprac.controllers;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.msu.cs.javaprac.DAO.impl.UserDAO;
import ru.msu.cs.javaprac.authentication.SessionStore;
import ru.msu.cs.javaprac.authentication.UserService;
import ru.msu.cs.javaprac.models.User;

@Controller
public class ProfileController
{
    @Autowired
    private SessionStore sessionStore;

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String profilePage(@CookieValue(value = "sessionId", required = false) String sessionId,
                              Model model) {
        if (sessionId == null || sessionStore.getUserBySessionId(sessionId) == null) {
            return "redirect:/login";
        }

        User user = sessionStore.getUserBySessionId(sessionId);
        model.addAttribute("user", user);
        model.addAttribute("roleType", User.RoleType.class);
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@CookieValue("sessionId") String sessionId,
                                @RequestParam String username,
                                @RequestParam String password) {
        User user = sessionStore.getUserBySessionId(sessionId);
        if (user != null) {
            userService.updateProfile(user.getId(), username, password);
            User newUser = userService.getById(user.getId());
            sessionStore.updateSession(sessionId, newUser);
        }
        return "redirect:/profile";
    }

    @PostMapping("/profile/delete")
    public String deleteProfile(@CookieValue("sessionId") String sessionId,
                                HttpServletResponse response) {
        User user = sessionStore.getUserBySessionId(sessionId);
        if (user != null) {
            userService.deleteUser(user.getId());
            sessionStore.removeSession(sessionId);
            Cookie cookie = new Cookie("sessionId", null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        return "redirect:/";
    }
}
