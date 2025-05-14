package ru.msu.cs.javaprac.authentication;

import org.springframework.stereotype.Component;
import ru.msu.cs.javaprac.models.User;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionStore
{
    private final Map<String, User> sessions = new ConcurrentHashMap<>();


    public String createSession(User user)
    {
        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, user);
        return sessionId;
    }

    public User getUserBySessionId(String sessionId)
    {
        return sessions.get(sessionId);
    }

    public void removeSession(String sessionId)
    {
        sessions.remove(sessionId);
    }

    public void updateSession(String sessionId, User updatedUser) {
        sessions.put(sessionId, updatedUser);
    }
}
