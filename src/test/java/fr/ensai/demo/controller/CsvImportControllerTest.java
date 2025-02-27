package fr.ensai.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ensai.demo.service.CsvImportService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.io.File;
import java.io.IOException;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CsvImportController.class)
public class CsvImportControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CsvImportService csvImportService;

	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * Test for a successful CSV import.
	 * It simulates a valid CSV file upload and expects a 200 OK response.
	 */
	@Test
	void testImportBooks_Success() throws Exception {
		// Prepare CSV content and file
		String csvContent = "id,title,author\n1,Test Book,Test Author";
		MockMultipartFile file = new MockMultipartFile("file", "books.csv", "text/csv", csvContent.getBytes());

		// Stub the service method to simulate a successful import
		doNothing().when(csvImportService).importBooksFromCsv(any(File.class), any(String.class));

		mockMvc.perform(multipart("/csv/importBooks")
				.file(file)
				.param("charsetName", "windows-1252"))
				.andExpect(status().isOk())
				.andExpect(content().string("Books imported successfully"));
	}

	/**
	 * Test for a CSV import failure due to an IOException.
	 * It uses a custom MockMultipartFile that throws an IOException on transferTo.
	 */
	@Test
	void testImportBooks_Failure_OnIOException() throws Exception {
		// Create a custom MultipartFile that throws IOException during file.transferTo(...)
		MockMultipartFile failingFile = new MockMultipartFile("file", "books.csv", "text/csv",
				"dummy content".getBytes()) {
			@Override
			public void transferTo(File dest) throws IOException {
				throw new IOException("Forced failure");
			}
		};

		mockMvc.perform(multipart("/csv/importBooks")
				.file(failingFile)
				.param("charsetName", "windows-1252"))
				.andExpect(status().isInternalServerError())
				.andExpect(content().string(Matchers.containsString("Failed to import books: Forced failure")));
	}
}
