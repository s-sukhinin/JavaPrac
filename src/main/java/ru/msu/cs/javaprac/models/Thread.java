package ru.msu.cs.javaprac.models;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "threads")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Thread {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "thread_id")
    private Integer id;

    @Column(nullable = false, name = "title")
    @NonNull
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "category")
    @NonNull
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author")
    private User author;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Thread thread = (Thread) o;
        return Objects.equals(id, thread.id) &&
                Objects.equals(title, thread.title) &&
                Objects.equals(category, thread.category) &&
                Objects.equals(author, thread.author) &&
                Objects.equals(createdAt, thread.createdAt);
    }
}
