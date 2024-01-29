package home.kalinin.controllers;

import home.kalinin.client.NoteClientMyRest;
import home.kalinin.models.Note;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import home.kalinin.repository.NoteRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api", produces = "application/json")
@AllArgsConstructor
@Slf4j
public class NoteRestController {
    /**
     * Репозиторий заметок
     */
    private final NoteRepository noteRepository;
    /**
     * Клиент REST API смотрит на данное API, для тестирования.
     */
    private final NoteClientMyRest noteClientMyRest;

    /**
     * Получение полного перечная заметок.
     * @return Список заметок, упакованный в полноценный http формат.
     */
    @GetMapping()
    public ResponseEntity<List<Note>> getAllTasks() {
        return new ResponseEntity<>(noteRepository.findAll(), HttpStatus.OK);
    }

    /**
     * Получение данных по указанной заметке.
     * @param id - код заметки
     * @return Искомая заметока, упакованная в полноценный http формат.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Note> getTask(@PathVariable Long id) {
        if(id == null || id <= 0)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        Optional<Note> oTask = noteRepository.findById(id);
        if (oTask.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(oTask.get(), HttpStatus.OK);
    }

    /**
     * Удаление указанной заметки.
     * @param id - код заметки
     * @return код удаленной заметки, упакованный в полноценный http формат.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteTask(@PathVariable Long id) {
        if(id == null || id <= 0)
            return new ResponseEntity<>(id, HttpStatus.BAD_REQUEST);
        try {
            noteRepository.deleteById(id);
        } catch (DataAccessException ex) {
            log.error("DataAccessException ");
            log.error(ex.getLocalizedMessage());
            return new ResponseEntity<>(id, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    /**
     * Добавление новой заметки
     * @param note - добавляемая заметка
     * @return Добавленная заметка, упакованная в полноценный http формат.
     */
    @PostMapping
    public ResponseEntity<Note> addTask(@RequestBody Note note) {
        if (note == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        try {
            noteRepository.save(note);
        } catch (DataAccessException ex) {
            log.error("DataAccessException ");
            log.error(ex.getLocalizedMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(note, HttpStatus.CREATED);
    }

    /**
     * Заменить заметку с указанным кодом на новую.
     * @param id - код ЗАМЕНЯЕМОЙ заметки.
     * @param note - заметка, все поля которой, заменят существующую заметку с кодом id
     * @return замененная заметка, упакованная в полноценный http формат.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Note> updateTask(
            @PathVariable Long id, @RequestBody Note note) {
        if(id == null || id <= 0 || note == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        note.setId(id);
        try {
            noteRepository.save(note);
        } catch (DataAccessException ex) {
            log.error("DataAccessException ");
            log.error(ex.getLocalizedMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(note, HttpStatus.CREATED);
    }

    /**
     * Обновление (замена) данных полей указанной заметки.
     * @param id - код заметки, в которая будет частично обновлена
     * @param note - заметка содержащая обновленные поля для замены
     * @return обновленная заметка с кодом id, упакованная в полноценный http формат.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Note> updateSelectiveFieldsTask(
            @PathVariable Long id, @RequestBody Note note) {
        if(id == null || id <= 0 || note == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        Optional<Note> oTask = noteRepository.findById(id);
        if (oTask.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        Note existNote = oTask.get();
        if (note.getName() != null && !note.getName().isBlank())
            existNote.setName(note.getName());
        if (note.getBody() != null && !note.getBody().isBlank())
            existNote.setBody(note.getBody());
        try {
            noteRepository.save(existNote);
        } catch (DataAccessException ex) {
            log.error("DataAccessException ");
            log.error(ex.getLocalizedMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(existNote, HttpStatus.OK);
    }

    /**
     * Тестирование REST API клиента.
     * @return Код http ответа с пустыми данными.
     */
    @GetMapping("test_client")
    public ResponseEntity<Object> test_client() {
        try {
            // Клиент через REST API сделанный в ручнею - работает.
            noteClientMyRest.createTask(new Note("Доп №9", "Содержимое Доп №9"));
            noteClientMyRest.createTask(new Note("Доп №10", "Содержимое Доп №10"));
            noteClientMyRest.createTask(new Note("Доп №11", "Содержимое Доп №11"));
            noteClientMyRest.createTask(new Note("Доп №12", "Содержимое Доп №12"));
            log.info("" + noteClientMyRest.getAllTasks());
            log.info("" + noteClientMyRest.getTaskById(Long.valueOf(9)));
            noteClientMyRest.deleteTask(noteClientMyRest.getTaskById(Long.valueOf(9)));
            log.info("" + noteClientMyRest.getAllTasks());
            Note note10 = noteClientMyRest.getTaskById(Long.valueOf(10));
            note10.setBody("Изменение содержания десятой заметки");
            noteClientMyRest.updateTask(note10);
            // Клиент через REST API DEFAULT не заработал.    Может быть в этот момент еще не существует bean-объекта ...
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}