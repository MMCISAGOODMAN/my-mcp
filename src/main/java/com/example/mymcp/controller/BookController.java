package com.example.mymcp.controller;

import com.example.mymcp.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "HTTP: get book by id", description = "Same logic as BookService#findById, also exposed as MCP tool")
    public BookService.Book getBook(@Parameter(description = "Book id") @PathVariable Long id) {
        return bookService.findById(id);
    }
}
