package ru.msu.cs.javaprac.models;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@RequiredArgsConstructor
@Table(name = "attachments")
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "attach_id")
    private Integer id;

    @JoinColumn(nullable = false, name = "post")
    @ManyToOne(fetch = FetchType.LAZY)
    @NonNull
    private Post post;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "path_to_file")
    private String pathToFile;

    @Column(name = "uploaded_at")
    private Timestamp uploadedAt;

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attachment attach = (Attachment) o;
        return Objects.equals(id, attach.id) &&
                Objects.equals(post, attach.post) &&
                Objects.equals(fileName, attach.fileName) &&
                Objects.equals(pathToFile, attach.pathToFile) &&
                Objects.equals(uploadedAt, attach.uploadedAt);
    }
}
