package com.cos.book.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.book.domain.Book;
import com.cos.book.domain.BookRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BookController {
	
	private final BookRepository bookRepository;
	
	@CrossOrigin
	@PostMapping("/book")
	public Book save(@RequestBody Book book) {
		return bookRepository.save(book);
	}
	
	@CrossOrigin
	@GetMapping("/book")
	public List<Book> findAll() {
		return bookRepository.findAll();
	}
	
	@CrossOrigin
	@GetMapping("/book/{id}")
	public Book findById(@PathVariable Long id) {
		Book bookEntity = bookRepository.findById(id).orElseThrow(()->new IllegalArgumentException("id 확인 필요"));
		
		return bookEntity;
	}

	@CrossOrigin
	@PutMapping("/book/{id}")
	public Book update(@PathVariable Long id, @RequestBody Book book) {
		// 더티 체킹, update 치기
		Book bookEntity = bookRepository.findById(id)
				.orElseThrow(()-> new IllegalArgumentException("id 확인 필요"));
		bookEntity.setTitle(book.getTitle());
		bookEntity.setPrice(book.getPrice());
		bookEntity.setRating(book.getRating());
		return bookEntity;
	}
	
	@CrossOrigin
	@DeleteMapping("/book/{id}")
	public String delete(@PathVariable Long id) {
		bookRepository.deleteById(id);
		return "ok";
	}
}
