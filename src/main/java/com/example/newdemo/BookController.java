package com.example.newdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;  // Внедряем репозиторий

    // Метод для добавления книги
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addBook(@RequestBody Book book) {
        // Сохраняем книгу в базе данных
        bookRepository.save(book);

        // Ответ, который возвращается пользователю
        Map<String, Object> response = new HashMap<>();
        response.put("title", book.getTitle());
        response.put("author", book.getAuthor());
        response.put("status", "received");

        return ResponseEntity.ok(response);
    }

    // Метод для получения всех книг
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookRepository.findAll();  // Получаем все книги из базы данных
        return ResponseEntity.ok(books);
    }

    // Метод для получения книги по ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookRepository.findById(id);  // Ищем книгу по ID
        if (book.isPresent()) {
            return ResponseEntity.ok(book.get());
        } else {
            return ResponseEntity.status(404).body(null);  // Книга не найдена
        }
    }

    // Метод для обновления информации о книге
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateBook(@PathVariable Long id, @RequestBody Book book) {
        if (!bookRepository.existsById(id)) {
            return ResponseEntity.status(404).body(null);  // Если книга не найдена
        }

        // Устанавливаем ID книги и сохраняем ее с обновленными данными
        book.setId(id);
        bookRepository.save(book);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "updated");
        response.put("title", book.getTitle());
        response.put("author", book.getAuthor());

        return ResponseEntity.ok(response);
    }

    // Метод для удаления книги по ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteBook(@PathVariable Long id) {
        if (!bookRepository.existsById(id)) {
            return ResponseEntity.status(404).body(Map.of("status", "not found"));
        }

        bookRepository.deleteById(id);  // Удаляем книгу из базы данных

        return ResponseEntity.ok(Map.of("status", "deleted", "id", id.toString()));
    }
}
