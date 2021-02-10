package com.cos.book.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.cos.book.domain.Book;
import com.cos.book.domain.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class BookControllerIntegrateTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private EntityManager entityManager;
	
	@BeforeEach
	public void init() {
		entityManager.createNativeQuery("ALTER TABLE book AUTO_INCREMENT = 1").executeUpdate();
	}
	
	@Test
	public void save_test() throws Exception {
		// given
		Book book = new Book(null, "저장 테스트", 12.3, 4.4);
		String content = new ObjectMapper().writeValueAsString(book);

		// when
		ResultActions resultActions = mockMvc.perform(post("/book").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content).accept(MediaType.APPLICATION_JSON_UTF8));

		// then
		resultActions.andExpect(status().isOk()).andExpect(jsonPath("$.title").value("저장 테스트"))
				.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void findAllTest() throws Exception {
		// given
		List<Book> books = new ArrayList<>();
		books.add(new Book(null, "모두찾기 1 테스트", 12.3, 3.2));
		books.add(new Book(null, "모두찾기 2 테스트", 12.3, 3.2));
		books.add(new Book(null, "모두찾기 3 테스트", 12.3, 3.2));

		bookRepository.saveAll(books);

		// when
		ResultActions resultAction = mockMvc.perform(get("/book").accept(MediaType.APPLICATION_JSON_UTF8));

		// then
		resultAction.andExpect(status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(3)))
				.andExpect(jsonPath("$.[0].title").value("모두찾기 1 테스트")).andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void findById_test() throws Exception {
		
		List<Book> books = new ArrayList<>();
		books.add(new Book(null, "한건찾기 1 테스트", 12.3, 3.2));
		books.add(new Book(null, "한건찾기 2 테스트", 12.3, 3.2));
		books.add(new Book(null, "한건찾기 3 테스트", 12.3, 3.2));

		bookRepository.saveAll(books);
		
		Long id = 1L;
		
		ResultActions resultActions = mockMvc.perform(get("/book/{id}", id).accept(MediaType.APPLICATION_JSON_UTF8));

		resultActions.andExpect(status().isOk()).andExpect(jsonPath("$.title").value("한건찾기 1 테스트"))
				.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void update_test() throws Exception {
		List<Book> books = new ArrayList<>();
		books.add(new Book(null, "업데이트 1 테스트", 12.3, 3.2));
		books.add(new Book(null, "업데이트 2 테스트", 12.3, 3.2));
		books.add(new Book(null, "업데이트 3 테스트", 12.3, 3.2));

		bookRepository.saveAll(books);
		
		Long id = 2L;
		Book book = new Book(null, "업데이트 테스트", 12.3, 4.5);
		String content = new ObjectMapper().writeValueAsString(book);
		
		ResultActions resultActions = mockMvc.perform(put("/book/{id}", id)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		resultActions
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.title").value("업데이트 테스트"))
		.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void delete_test() throws Exception {
		List<Book> books = new ArrayList<>();
		books.add(new Book(null, "삭제 1 테스트", 12.3, 3.2));
		books.add(new Book(null, "삭제 2 테스트", 12.3, 3.2));
		books.add(new Book(null, "삭제 3 테스트", 12.3, 3.2));

		bookRepository.saveAll(books);
		
		Long id = 3L;
		
		ResultActions resultActions = mockMvc.perform(delete("/book/{id}", id)
				.accept(MediaType.TEXT_PLAIN));
		
		resultActions.andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
		
		MvcResult requestResult = resultActions.andReturn();
		String result = requestResult.getResponse().getContentAsString();

		assertEquals("ok", result);
		
	}

}
