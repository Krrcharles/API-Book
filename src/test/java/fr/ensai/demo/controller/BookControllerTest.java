package fr.ensai.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ensai.demo.dto.BookDto;
import fr.ensai.demo.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private BookService bookService;

	@Autowired
	private ObjectMapper objectMapper;

	// Test POST /books (createBook)
	@Test
	void testCreateBook_Success() throws Exception {
		BookDto inputDto = new BookDto(null, "New Book", 2025, "New Author", "New Genre", "New Country");
		BookDto createdDto = new BookDto(1L, "New Book", 2025, "New Author", "New Genre", "New Country");

		Mockito.when(bookService.createBook(any(BookDto.class))).thenReturn(createdDto);

		mockMvc.perform(post("/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(inputDto)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.bookId").value(1L))
				.andExpect(jsonPath("$.title").value("New Book"))
				.andExpect(jsonPath("$.authorName").value("New Author"));
	}

	@Test
	void testCreateBook_Failure() throws Exception {
		BookDto inputDto = new BookDto(null, "New Book", 2025, "New Author", "New Genre", "New Country");
		Mockito.when(bookService.createBook(any(BookDto.class))).thenReturn(null);

		mockMvc.perform(post("/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(inputDto)))
				.andExpect(status().isBadRequest());
	}

	// Test PUT /books (updateBook)
	@Test
	void testUpdateBook_Success() throws Exception {
		BookDto inputDto = new BookDto(1L, "Updated Book", 2025, "Existing Author", "Existing Genre",
				"Existing Country");
		BookDto updatedDto = new BookDto(1L, "Updated Book", 2025, "Existing Author", "Existing Genre",
				"Existing Country");

		Mockito.when(bookService.updateBook(any(BookDto.class))).thenReturn(updatedDto);

		mockMvc.perform(put("/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(inputDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.bookId").value(1L))
				.andExpect(jsonPath("$.title").value("Updated Book"))
				.andExpect(jsonPath("$.authorName").value("Existing Author"));
	}

	@Test
	void testUpdateBook_Failure() throws Exception {
		BookDto inputDto = new BookDto(1L, "Updated Book", 2025, "Existing Author", "Existing Genre",
				"Existing Country");
		Mockito.when(bookService.updateBook(any(BookDto.class))).thenReturn(null);

		mockMvc.perform(put("/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(inputDto)))
				.andExpect(status().isNotFound());
	}

	// Test DELETE /books/{bookId} (deleteBook)
	@Test
	void testDeleteBook_Success() throws Exception {
		Mockito.when(bookService.deleteBook(eq(1L))).thenReturn(true);

		mockMvc.perform(delete("/books/1"))
				.andExpect(status().isOk())
				.andExpect(content().string("Book 1 deleted"));
	}

	@Test
	void testDeleteBook_Failure() throws Exception {
		Mockito.when(bookService.deleteBook(eq(1L))).thenReturn(false);

		mockMvc.perform(delete("/books/1"))
				.andExpect(status().isNotFound());
	}

	// Test GET /books (getBooks)
	@Test
	void testGetBooks_Success() throws Exception {
		BookDto book1 = new BookDto(1L, "Book1", 2020, "Author1", "Genre1", "Country1");
		BookDto book2 = new BookDto(2L, "Book2", 2021, "Author2", "Genre2", "Country2");
		List<BookDto> books = List.of(book1, book2);

		Mockito.when(bookService.getBooks()).thenReturn(books);

		mockMvc.perform(get("/books"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].bookId").value(1L))
				.andExpect(jsonPath("$[1].bookId").value(2L));
	}

	@Test
	void testGetBooks_NotFound() throws Exception {
		Mockito.when(bookService.getBooks()).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/books"))
				.andExpect(status().isNotFound());
	}

	// Test GET /books/{bookId} (getBookById)
	@Test
	void testGetBookById_Success() throws Exception {
		BookDto book = new BookDto(1L, "Book Title", 2020, "Author", "Genre", "Country");
		Mockito.when(bookService.getBookById(eq(1L))).thenReturn(book);

		mockMvc.perform(get("/books/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.bookId").value(1L))
				.andExpect(jsonPath("$.title").value("Book Title"));
	}

	@Test
	void testGetBookById_Failure() throws Exception {
		Mockito.when(bookService.getBookById(eq(1L))).thenReturn(null);

		mockMvc.perform(get("/books/1"))
				.andExpect(status().isNotFound());
	}
}
