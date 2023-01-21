package tracker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StudentServiceTest {
	StudentService studentService;

	@BeforeEach
	void setUp() {
		studentService = new StudentService();
	}

	@ParameterizedTest
	@MethodSource(value = "correctCredentials")
	void testCorrectCredentials(String[] input) {
		var actual = studentService.processCredentials(input);
		assertEquals("The student has been added.", actual);
	}

	@ParameterizedTest
	@MethodSource(value = "incorrectCredentials")
	void testIncorrectCredentials(String[] input) {
		var actual = studentService.processCredentials(input);
		assertEquals("Incorrect credentials.", actual);
	}

	@ParameterizedTest
	@MethodSource(value = "incorrectFirstName")
	void testIncorrectFirstName(String[] input) {
		var actual = studentService.processCredentials(input);
		assertEquals("Incorrect first name.", actual);
	}

	@Test
	void testInCorrectEmail() {
		var value = "John Doe abc@anc";
		var actual = studentService.processCredentials(value.split(" "));
		Assertions.assertEquals("Incorrect email.", actual);
	}

	static Stream<Arguments> correctCredentials() {
		var value1 = "John Doe johnd@email.net".split(" ");
		var value2 = "Jane Spark jspark@yahoo.com".split(" ");
		return Stream.of(
						Arguments.of((Object) value1),
						Arguments.of((Object) value2)
		);
	}

	static Stream<Arguments> incorrectCredentials() {
		var value1 = "John Doe".split(" ");
		var value2 = "Jane".split(" ");
		var value3 = " ".split(" ");
		return Stream.of(
						Arguments.of((Object) value1),
						Arguments.of((Object) value2),
						Arguments.of((Object) value3)
		);
	}

	static Stream<Arguments> incorrectFirstName() {
		var value1 = "J. Doe johnd@email.net".split(" ");
		var value2 = "हओय Doe johnd@email.net".split(" ");
		var value3 = "'g Doe johnd@email.net".split(" ");
		var value4 = "a Doe johnd@email.net".split(" ");
		var value5 = "hello- Doe johnd@email.net".split(" ");
		var value6 = "a-' Doe johnd@email.net".split(" ");
		var value7 = "a-'b Doe johnd@email.net".split(" ");
		var value8 = "-f Doe johnd@email.net".split(" ");
		return Stream.of(
						Arguments.of((Object) value1),
						Arguments.of((Object) value2),
						Arguments.of((Object) value3),
						Arguments.of((Object) value4),
						Arguments.of((Object) value5),
						Arguments.of((Object) value6),
						Arguments.of((Object) value7),
						Arguments.of((Object) value8)
		);
	}
}
