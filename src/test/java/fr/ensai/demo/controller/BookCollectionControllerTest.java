package fr.ensai.demo.controller;

import fr.ensai.demo.dto.BookCollectionDto;
import fr.ensai.demo.dto.BookDto;
import fr.ensai.demo.service.BookCollectionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.junit.jupiter.api.Assertions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

// @TestPropertySource(locations = "classpath:application.properties")
@TestPropertySource(locations = "classpath:application.properties")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookCollectionControllerTest {
	Logger LOG = LoggerFactory.getLogger(BookCollectionControllerTest.class);

	@Autowired
	private BookCollectionController bookCollectionController;

	@MockitoBean
	private BookCollectionService bookCollectionService;

	// Test for createBookCollection
	@Test
	void testCreateBookCollection_Success() {
		BookCollectionDto dto = new BookCollectionDto();
		dto.setCollectionId(1L);
		dto.setName("Test Collection");

		Mockito.when(bookCollectionService.createBookCollection(any(BookCollectionDto.class)))
				.thenReturn(dto);

		ResponseEntity<BookCollectionDto> response = bookCollectionController.createBookCollection(dto);
		assertNotNull(response);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(1L, response.getBody().getCollectionId());
		assertEquals("Test Collection", response.getBody().getName());
	}

	@Test
	void testCreateBookCollection_Failure() {
		BookCollectionDto dto = new BookCollectionDto();
		dto.setName("Test Collection");

		Mockito.when(bookCollectionService.createBookCollection(any(BookCollectionDto.class)))
				.thenReturn(null);

		assertThrows(ResponseStatusException.class, () -> {
			bookCollectionController.createBookCollection(dto);
		}, "Book Collection creation should fail and throw BAD_REQUEST");
	}

	// Test for copyBookCollection
	@Test
	void testCopyBookCollection_Success() {
		BookCollectionDto copied = new BookCollectionDto();
		copied.setCollectionId(2L);
		copied.setName("Copied Collection");

		Mockito.when(bookCollectionService.copyBookCollection(eq(1L), eq("NewName")))
				.thenReturn(copied);

		ResponseEntity<BookCollectionDto> response = bookCollectionController.copyBookCollection(1L, "NewName");
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2L, response.getBody().getCollectionId());
		assertEquals("Copied Collection", response.getBody().getName());
	}

	@Test
	void testCopyBookCollection_Failure() {
		Mockito.when(bookCollectionService.copyBookCollection(eq(1L), eq("NewName")))
				.thenReturn(null);

		assertThrows(ResponseStatusException.class, () -> {
			bookCollectionController.copyBookCollection(1L, "NewName");
		}, "Copying a non-existing collection should throw NOT_FOUND");
	}

	// Test for getBookCollections
	@Test
	void testGetBookCollections_Success() {
		BookCollectionDto dto = new BookCollectionDto();
		dto.setCollectionId(1L);
		dto.setName("Collection1");
		List<BookCollectionDto> list = Arrays.asList(dto);

		Mockito.when(bookCollectionService.getBookCollections())
				.thenReturn(list);

		ResponseEntity<Iterable<BookCollectionDto>> response = bookCollectionController.getBookCollections();
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		Iterable<BookCollectionDto> collections = response.getBody();
		assertTrue(collections.iterator().hasNext());
	}

	@Test
	void testGetBookCollections_Empty() {
		Mockito.when(bookCollectionService.getBookCollections())
				.thenReturn(Collections.emptyList());

		assertThrows(ResponseStatusException.class, () -> {
			bookCollectionController.getBookCollections();
		}, "No collections should throw NOT_FOUND");
	}

	// Test for getBookCollection by ID
	@Test
	void testGetBookCollection_Success() {
		BookCollectionDto dto = new BookCollectionDto();
		dto.setCollectionId(1L);
		dto.setName("Collection1");

		Mockito.when(bookCollectionService.getBookCollection(1L))
				.thenReturn(Optional.of(dto));

		ResponseEntity<Optional<BookCollectionDto>> response = bookCollectionController.getBookCollection(1L);
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		Optional<BookCollectionDto> opt = response.getBody();
		assertTrue(opt.isPresent());
		assertEquals(1L, opt.get().getCollectionId());
		assertEquals("Collection1", opt.get().getName());
	}

	@Test
	void testGetBookCollection_NotFound() {
		Mockito.when(bookCollectionService.getBookCollection(1L))
				.thenReturn(Optional.empty());

		assertThrows(ResponseStatusException.class, () -> {
			bookCollectionController.getBookCollection(1L);
		}, "Missing collection should throw NOT_FOUND");
	}

	// Test for addBookToCollection (with BookDto in request body)
	@Test
	void testAddBookToCollection_WithBookDto_Success() {
		BookDto bookDto = new BookDto();
		bookDto.setBookId(1L);
		bookDto.setTitle("Book Title");

		BookCollectionDto updated = new BookCollectionDto();
		updated.setCollectionId(1L);
		updated.setName("Updated Collection");

		Mockito.when(bookCollectionService.addBookToCollection(eq(1L), any(BookDto.class)))
				.thenReturn(updated);

		ResponseEntity<BookCollectionDto> response = bookCollectionController.addBookToCollection(1L, bookDto);
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(1L, response.getBody().getCollectionId());
		assertEquals("Updated Collection", response.getBody().getName());
	}

	@Test
	void testAddBookToCollection_WithBookDto_NotFound() {
		BookDto bookDto = new BookDto();
		bookDto.setBookId(1L);
		bookDto.setTitle("Book Title");

		Mockito.when(bookCollectionService.addBookToCollection(eq(1L), any(BookDto.class)))
				.thenReturn(null);

		assertThrows(ResponseStatusException.class, () -> {
			bookCollectionController.addBookToCollection(1L, bookDto);
		}, "Adding a book to a non-existent collection should throw NOT_FOUND");
	}

	// Test for addBookToCollection (with bookId in path)
	@Test
	void testAddBookToCollection_WithBookId_Success() {
		BookCollectionDto updated = new BookCollectionDto();
		updated.setCollectionId(1L);
		updated.setName("Updated Collection");

		Mockito.when(bookCollectionService.addBookToCollection(1L, 2L))
				.thenReturn(updated);

		ResponseEntity<BookCollectionDto> response = bookCollectionController.addBookToCollection(1L, 2L);
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(1L, response.getBody().getCollectionId());
		assertEquals("Updated Collection", response.getBody().getName());
	}

	@Test
	void testAddBookToCollection_WithBookId_NotFound() {
		Mockito.when(bookCollectionService.addBookToCollection(1L, 2L))
				.thenReturn(null);

		assertThrows(ResponseStatusException.class, () -> {
			bookCollectionController.addBookToCollection(1L, 2L);
		}, "Adding a book by id to a non-existent collection should throw NOT_FOUND");
	}

	// Test for removeBookFromCollection
	@Test
	void testRemoveBookFromCollection_Success() {
		BookCollectionDto updated = new BookCollectionDto();
		updated.setCollectionId(1L);
		updated.setName("Updated Collection");

		Mockito.when(bookCollectionService.removeBookFromCollection(1L, 2L))
				.thenReturn(updated);

		ResponseEntity<BookCollectionDto> response = bookCollectionController.removeBookFromCollection(1L, 2L);
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(1L, response.getBody().getCollectionId());
		assertEquals("Updated Collection", response.getBody().getName());
	}

	@Test
	void testRemoveBookFromCollection_NotFound() {
		Mockito.when(bookCollectionService.removeBookFromCollection(1L, 2L))
				.thenReturn(null);

		assertThrows(ResponseStatusException.class, () -> {
			bookCollectionController.removeBookFromCollection(1L, 2L);
		}, "Removing a book from a non-existent collection should throw NOT_FOUND");
	}

	// Test for getBooks in a collection
	@Test
	void testGetBooks_Success() {
		BookDto bookDto = new BookDto();
		bookDto.setBookId(1L);
		bookDto.setTitle("Book Title");
		List<BookDto> books = List.of(bookDto);

		Mockito.when(bookCollectionService.getBooks(1L, "asc"))
				.thenReturn(books);

		ResponseEntity<Iterable<BookDto>> response = bookCollectionController.getBooks(1L, "asc");
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		Iterable<BookDto> result = response.getBody();
		assertTrue(result.iterator().hasNext());
	}

	@Test
	void testGetBooks_NotFound() {
		Mockito.when(bookCollectionService.getBooks(1L, "asc"))
				.thenReturn(Collections.emptyList());

		assertThrows(ResponseStatusException.class, () -> {
			bookCollectionController.getBooks(1L, "asc");
		}, "No books in the collection should throw NOT_FOUND");
	}

	// Test for compareCollections
	@Test
	void testCompareCollections_InvalidMetric() {
		assertThrows(ResponseStatusException.class, () -> {
			bookCollectionController.compareCollections(1L, 2L, "invalid");
		}, "An invalid metric should throw BAD_REQUEST");
	}

	@Test
	void testCompareCollections_NullMetric() {
		ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
			bookCollectionController.compareCollections(1L, 2L, null);
		});
		assertTrue(exception.getReason().contains("Comparison metric must be provided"));
	}

	@Test
	void testCompareCollections_NullResult() {
		Mockito.when(bookCollectionService.compareCollections(1L, 2L, "size"))
				.thenReturn(null);

		assertThrows(ResponseStatusException.class, () -> {
			bookCollectionController.compareCollections(1L, 2L, "size");
		}, "A null comparison result should throw BAD_REQUEST");
	}

	@Test
	void testCompareCollections_Success() {
		Mockito.when(bookCollectionService.compareCollections(1L, 2L, "jaro"))
				.thenReturn(0.75);

		ResponseEntity<Double> response = bookCollectionController.compareCollections(1L, 2L, "jaro");
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(0.75, response.getBody());
	}

	@Test
	void testCompareCollections_Success_Size() {
		Mockito.when(bookCollectionService.compareCollections(1L, 2L, "size"))
				.thenReturn(1.0);

		ResponseEntity<Double> response = bookCollectionController.compareCollections(1L, 2L, "size");

		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(1.0, response.getBody());
	}

	@Test
	void testCompareCollections_Success_Jaccard() {
		Mockito.when(bookCollectionService.compareCollections(1L, 2L, "jaccard"))
				.thenReturn(0.5);

		ResponseEntity<Double> response = bookCollectionController.compareCollections(1L, 2L, "jaccard");

		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(0.5, response.getBody());
	}

}
