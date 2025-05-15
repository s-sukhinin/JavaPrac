package ru.msu.cs.javaprac.models;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "bans")
public class Ban implements CommonEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "ban_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    @NonNull
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "thread")
    private Thread thread;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category")
    private Category category;

    @Column(nullable = false, name = "banned_at")
    @NonNull
    private Timestamp bannedAt;

    @Column(name = "banned_until")
    private Timestamp bannedUntil;

    @Column(name = "ban_reason")
    private String reason;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "moderator")
    private User moderator;

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ban ban = (Ban) o;
        return Objects.equals(id, ban.id) &&
                Objects.equals(user, ban.user) &&
                Objects.equals(thread, ban.thread) &&
                Objects.equals(category, ban.category) &&
                Objects.equals(moderator, ban.moderator) &&
                Objects.equals(reason, ban.reason) &&
                Objects.equals(bannedAt, ban.bannedAt) &&
                Objects.equals(bannedUntil, ban.bannedUntil);
    }
}
