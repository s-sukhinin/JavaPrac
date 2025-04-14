package ru.msu.cs.javaprac.models;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "categories")
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@NoArgsConstructor
public class Category implements CommonEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "category_id")
    private Integer id;

    @Column(nullable = false, name = "title")
    @NonNull
    private String title;

    @Column(name = "description")
    private String description;

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
        Category category = (Category) o;
        return Objects.equals(id, category.id) &&
                Objects.equals(title, category.title) &&
                Objects.equals(description, category.description) &&
                Objects.equals(author, category.author) &&
                Objects.equals(createdAt, category.createdAt);
    }
}
