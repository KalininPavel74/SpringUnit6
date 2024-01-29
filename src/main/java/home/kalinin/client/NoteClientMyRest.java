package home.kalinin.client;

import home.kalinin.models.Note;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

//@Data
//@RequiredArgsConstructor // не сработало, только @Autowired на конструктор
//@AllArgsConstructor // не сработало, только @Autowired на конструктор

/**
 * Обертка для клиента REST API
 */
@Slf4j
@Service
public class NoteClientMyRest {
    private RestTemplate restTemplate;
    private static final String API_URL = "http://localhost:8080/api";

    @Autowired
    public NoteClientMyRest(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Note getTaskById(Long id) {
        return restTemplate.getForObject(API_URL +"/{id}", Note.class, id);
    }

    public void updateTask(Note note) {
        restTemplate.put(API_URL +"/{id}", note, note.getId());
    }

    public Note createTask(Note note) {
        return restTemplate.postForObject(API_URL, note, Note.class);
    }

    public void deleteTask(Note note) {
        restTemplate.delete(API_URL +"/{id}", note.getId());
    }

    public List<Note> getAllTasks() {
        return restTemplate.exchange(API_URL,
                        HttpMethod.GET
                        , null
                        , new ParameterizedTypeReference<List<Note>>() {
                        })
                .getBody();
    }

}
