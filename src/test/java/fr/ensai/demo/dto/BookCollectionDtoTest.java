package fr.ensai.demo.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

class BookCollectionDtoTest {

    @Test
    void testDefaultConstructorAndSetters() {
        BookCollectionDto dto = new BookCollectionDto();
        dto.setCollectionId(1L);
        dto.setName("My Collection");
        dto.setDistanceJaro(0.75);
        dto.setDistanceJaccard(0.85);

        // BookDto book1 = new BookDto(100L, "Book One", 2000, "Author One", "Genre One", "Country One");
        // BookDto book2 = new BookDto(101L, "Book Two", 2001, "Author Two", "Genre Two", "Country Two");
        List<Long> bookIds = Arrays.asList(100L, 101L);
        dto.setBookIds(bookIds);

        assertEquals(1L, dto.getCollectionId(), "CollectionId should be 1L");
        assertEquals("My Collection", dto.getName(), "Name should be 'My Collection'");
        assertEquals(0.75, dto.getDistanceJaro(), "DistanceJaro should be 0.75");
        assertEquals(0.85, dto.getDistanceJaccard(), "DistanceJaccard should be 0.85");
        assertEquals(bookIds, dto.getBookIds(), "Books list should match the provided list");
    }

    @Test
    void testConstructorWithId() {
        // BookDto book1 = new BookDto(102L, "Book Three", 2002, "Author Three", "Genre Three", "Country Three");
        List<Long> bookIds = Arrays.asList(102L);
        BookCollectionDto dto = new BookCollectionDto(2L, "Another Collection", 0.65, 0.70, bookIds);

        assertEquals(2L, dto.getCollectionId(), "CollectionId should be 2L");
        assertEquals("Another Collection", dto.getName(), "Name should be 'Another Collection'");
        assertEquals(0.65, dto.getDistanceJaro(), "DistanceJaro should be 0.65");
        assertEquals(0.70, dto.getDistanceJaccard(), "DistanceJaccard should be 0.70");
        assertEquals(bookIds, dto.getBookIds(), "Books list should match the provided list");
    }

    @Test
    void testConstructorWithoutId() {
        // BookDto book1 = new BookDto(103L, "Book Four", 2003, "Author Four", "Genre Four", "Country Four");
        List<Long> bookIds = Arrays.asList(103L);
        BookCollectionDto dto = new BookCollectionDto("Third Collection", 0.55, 0.60, bookIds);

        assertNull(dto.getCollectionId(), "CollectionId should be null when not provided");
        assertEquals("Third Collection", dto.getName(), "Name should be 'Third Collection'");
        assertEquals(0.55, dto.getDistanceJaro(), "DistanceJaro should be 0.55");
        assertEquals(0.60, dto.getDistanceJaccard(), "DistanceJaccard should be 0.60");
        assertEquals(bookIds, dto.getBookIds(), "Books list should match the provided list");
    }
}
