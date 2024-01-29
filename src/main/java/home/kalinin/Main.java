package home.kalinin;

import home.kalinin.client.NoteClientMyRest;
import home.kalinin.models.Note;
import home.kalinin.repository.NoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 Фреймворк Spring (семинары)
 Урок 6. Проектирование и реализация API для серверного приложения.
 Калинин Павел
 29.01.2024

+ Базовое задание:
+ Условие:
+ Важно! В проекте используем обязательно Spring Data и Lombok!
+ Разработайте небольшое веб-приложение на Spring Boot,
+ которое будет представлять из себя сервис для учета личных заметок.
+ Приложение должно поддерживать следующие функции:
+ Все методы контроллера возвращают ResponseEntity(как на семинаре)
+ 1. Добавление заметки. (Подсказка @PostMapping )
+ 2. Просмотр всех заметок.(Подсказка @GetMapping )
+ 3. Получение заметки по id. (Подсказка @GetMapping("/{id}") )
+ 4. Редактирование заметки.(Подсказка @PutMapping("/{id}") )
+ 5. Удаление заметки.(Подсказка @DeleteMapping("/{id}") )
+ Структура заметки:
+ - ID (автоинкрементное)(тип - Long)
+ - Заголовок (не может быть пустым)(тип - String)
+ - Содержимое (не может быть пустым)(тип - String)
+ - Дата создания (автоматически устанавливается при создании заметки)(тип - LocalDateTime)
+ Подсказка:
+ Репозиторий насладует JpaRepository<Note, Long>. В репозитории добавляем метод Optional<Note> findById(Long id);
+ Подсказка:
+ В проект добавляем зависимости: spring data jpa, h2, lombok, spring web
 */

@Slf4j
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    /**
     * Создание экземпляра простого синхронного клиента REST API, предоставляемое ядром Spring Framework.
     * @return экземпляр REST клиента
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Предварительное заполнение репозиротория заметками.
     * @param noteRepository - репозиторий заметок.
     * @return bean-объект, который будет запущен сразу после создания всех bean-объектов
     */
    @Bean
    public CommandLineRunner dataLoader(NoteRepository noteRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                noteRepository.save(new Note("Изучить Spring", "Содержимое Изучить Spring"));
                noteRepository.save(new Note("Сдать ДЗ №2 по Spring", "Содержимое Сдать ДЗ №2 по Spring"));
                noteRepository.save(new Note("Сдать ДЗ №3 по Spring", "Содержимое Сдать ДЗ №3 по Spring"));
                noteRepository.save(new Note("Сдать ДЗ №4 по Spring", "Содержимое Сдать ДЗ №4 по Spring"));
                noteRepository.save(new Note("Сдать ДЗ №5 по Spring", "Содержимое Сдать ДЗ №5 по Spring"));
                noteRepository.save(new Note("Сдать ДЗ №6 по Spring", "Содержимое Сдать ДЗ №6 по Spring"));
                noteRepository.save(new Note("Изучить Spring Security", "Содержимое Изучить Spring Security"));
                noteRepository.save(new Note("Изучить Spring OAuth2", "Содержимое Изучить Spring OAuth2"));
            }
        };
    }

}