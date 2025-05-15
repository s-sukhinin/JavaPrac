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
import ru.msu.cs.javaprac.support.SessionStore;
import ru.msu.cs.javaprac.support.UserService;
import ru.msu.cs.javaprac.models.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Controller
public class AuthController
{
    @Autowired
    private final UserService userService = new UserService();

    @Autowired
    private final SessionStore sessionStore = new SessionStore();

    @Autowired
    private UserDAO userDAO;

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            Model model) {
        model.addAttribute("error", error != null ? "Неверное имя пользовтеля или пароль" : null);
        model.addAttribute("user", null);
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          HttpServletResponse response) {
        User user = userService.authenticate(username, password);
        if (user == null) {
            return "redirect:/login?error=true";
        }

        String sessionId = sessionStore.createSession(user);

        Cookie cookie = new Cookie("sessionId", sessionId);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(3600);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(@CookieValue(value = "sessionId", required = false) String sessionId,
                         HttpServletResponse response) {

        if (sessionId != null) {
            sessionStore.removeSession(sessionId);
        }

        Cookie cookie = new Cookie("sessionId", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String login,
                           @RequestParam String password,
                           @RequestParam String name,
                           HttpServletResponse response,
                           Model model) {

        if (userDAO.getUserByLogin(login) != null) {
            model.addAttribute("error", "Пользователь с таким логином уже существует");
            return "register";
        }

        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        user.setName(name);
        user.setRole(User.RoleType.USER);
        user.setDateOfRegistration(Timestamp.valueOf(LocalDateTime.now()));

        userDAO.save(user);

        String sessionId = sessionStore.createSession(user);

        Cookie cookie = new Cookie("sessionId", sessionId);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        return "redirect:/profile";
    }
}
