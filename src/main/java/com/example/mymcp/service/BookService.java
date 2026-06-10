package com.example.mymcp.service;

import io.mcpplus.autoconfigure.annotation.McpExpose;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BookService {

    private final Map<Long, Book> books = new ConcurrentHashMap<>();

    public BookService() {
        books.put(1L, new Book(1L, "Spring in Action", "Craig Walls", 59.99));
        books.put(2L, new Book(2L, "Clean Code", "Robert Martin", 45.00));
        books.put(3L, new Book(3L, "Designing Data-Intensive Applications", "Martin Kleppmann", 52.50));
    }

    @Operation(summary = "Find book by id", description = "Returns book details for the given id")
    public Book findById(@Parameter(description = "Book id") Long id) {
        Book book = books.get(id);
        if (book == null) {
            throw new IllegalArgumentException("Book not found: " + id);
        }
        return book;
    }

    @McpExpose(name = "list_all_books", description = "List all books in the catalog")
    public List<Book> listAll() {
        return books.values().stream().sorted(java.util.Comparator.comparing(Book::id)).toList();
    }

    @Operation(summary = "Search books by keyword", description = "Match title or author, case-insensitive")
    public List<Book> search(@Parameter(description = "Keyword in title or author") String keyword) {
        String q = keyword.toLowerCase();
        return books.values().stream()
                .filter(b -> b.title().toLowerCase().contains(q) || b.author().toLowerCase().contains(q))
                .sorted(java.util.Comparator.comparing(Book::id))
                .toList();
    }

    public record Book(Long id, String title, String author, double price) {
    }
}
