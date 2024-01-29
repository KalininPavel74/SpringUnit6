package home.kalinin.repository;

import home.kalinin.models.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {
    /**
     * Не понятно зачем этот метод добавлять. Он уже существует в наследуемом интерфейсе.
      * @param id
     * @return
     */
    Optional<Note> findById(Long id);
}
