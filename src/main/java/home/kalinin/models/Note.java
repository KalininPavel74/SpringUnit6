package home.kalinin.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@Entity
public class Note {
    /**
     * Идентификатор заметки.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * Название заметки.
     */
    @Column(nullable = false)
    @NotNull( message = "Error. Note name is required.")
    @NotBlank(message = "Error. Note name is required.")
    private String name;
    /**
     * Содержание заметки.
     */
    @Column(nullable = false)
    @NotNull(message = "Error. Note body is required.")
    @NotBlank(message = "Error. Note body is required.")
    private String body;
    /**
     * Дата создания (и обновления заметки).
     */
    private LocalDateTime createdAt = LocalDateTime.now();
    public Note(String name, String body) {
        this.name = name;
        this.body = body;
    }
}
