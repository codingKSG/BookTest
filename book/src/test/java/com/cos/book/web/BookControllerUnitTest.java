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

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.cos.book.domain.Book;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
public class BookControllerUnitTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BookController bookController;

	@Test
	public void save_test() throws Exception {
		// given
		Book book = new Book(null, "저장 테스트", 12.3, 4.4);
		String content = new ObjectMapper().writeValueAsString(book);
		when(bookController.save(book)).thenReturn(new Book(1L, "저장 테스트", 12.3, 4.4));

		// when
		ResultActions resultActions = mockMvc.perform(post("/book").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content).accept(MediaType.APPLICATION_JSON_UTF8));

		// then
		resultActions.andExpect(status().isOk()).andExpect(jsonPath("$.title").value("저장 테스트"))
				.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void findAll_test() throws Exception{
		//given
		List<Book> books = new ArrayList<>();
		books.add(new Book(null, "모두찾기 1 테스트", 12.3, 3.2));
		books.add(new Book(null, "모두찾기 2 테스트", 12.3, 3.2));
		when(bookController.findAll()).thenReturn(books);
		
		// when
		ResultActions resultAction = mockMvc.perform(get("/book")
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		// then
		resultAction
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", Matchers.hasSize(2)))
		.andExpect(jsonPath("$.[0].title").value("모두찾기 1 테스트"))
		.andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void findById_test() throws Exception {
		Long id = 1L;
		
		when(bookController.findById(id)).thenReturn(new Book(1L, "한건찾기 테스트", 12.3, 4.1));

		ResultActions resultActions = mockMvc.perform(get("/book/{id}", id).accept(MediaType.APPLICATION_JSON_UTF8));

		resultActions.andExpect(status().isOk()).andExpect(jsonPath("$.title").value("한건찾기 테스트"))
				.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void update_test() throws Exception {
		Long id = 1L;
		Book book = new Book(null, "업데이트 테스트", 12.3, 4.5);
		String content = new ObjectMapper().writeValueAsString(book);
		when(bookController.update(id, book)).thenReturn(new Book(1L, "업데이트 테스트", 12.3, 4.5));
		
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
		Long id = 1L;
		when(bookController.delete(id)).thenReturn("ok");
		
		ResultActions resultActions = mockMvc.perform(delete("/book/{id}", id)
				.accept(MediaType.TEXT_PLAIN));
		
		resultActions.andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
		
		MvcResult requestResult = resultActions.andReturn();
		String result = requestResult.getResponse().getContentAsString();

		assertEquals("ok", result);
		
	}

}
