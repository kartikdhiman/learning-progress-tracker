package tracker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class StudentServiceTest {
	@Test
	void incorrectCredentials() {
		String[] input1 = {"help"};
		String[] input2 = {"hello", "there"};
		StudentService studentService = new StudentService();

		var result1 = studentService.processStudentCredentials(input1);
		var result2 = studentService.processStudentCredentials(input2);

		assertEquals("Incorrect credentials.", result1);
		assertEquals("Incorrect credentials.", result2);
	}

	@ParameterizedTest
	@ValueSource(strings = {"J.", "हओय", "-f", "'g", "a", "hello-", "a-'", "a-'b"})
	void incorrectFirstName(String input) {
		assertFalse(StudentService.isNameCorrect(input));
	}

	@ParameterizedTest
	@ValueSource(strings = {"hello", "a-b", "john's", "kd"})
	void correctFirstName(String input) {
		assertTrue(StudentService.isNameCorrect(input));
	}

	@ParameterizedTest
	@ValueSource(strings = {"anny.md@mail.edu"})
	void correctEmail(String input) {
		assertTrue(StudentService.isEmailCorrect(input));
	}

	@ParameterizedTest
	@ValueSource(strings = {"abc@anc"})
	void inCorrectEmail(String input) {
		assertFalse(StudentService.isEmailCorrect(input));
	}
}