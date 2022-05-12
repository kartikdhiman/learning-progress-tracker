package tracker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class StudentService {
	Scanner scanner = new Scanner(System.in);
	Set<String> emails = new HashSet<>();
	public Map<String, List<Integer>> scores = new HashMap<>();
	int id = 1000;

	public void addStudents() {
		String value;
		System.out.println("Enter student credentials or 'back' to return:");
		do {
			value = scanner.nextLine();
			if (value.equals("back")) {
				var message = String.format("Total %d students have been added.", emails.size());
				System.out.println(message);
			} else {
				var inputs = value.split(" ");
				var message = processStudentCredentials(inputs);
				System.out.println(message);
			}
		} while (!value.equals("back"));
	}

	public String processStudentCredentials(String[] inputs) {
		var msg = "Incorrect credentials.";
		if (inputs.length < 3) {
			return msg;
		} else {
			var firstName = inputs[0];
			var lastNames = Arrays.copyOfRange(inputs, 1, inputs.length - 1);
			var email = inputs[inputs.length - 1];
			if (!isNameCorrect(firstName)) {
				msg = "Incorrect first name.";
			} else if (!isLastNameCorrect(lastNames)) {
				msg = "Incorrect last name.";
			} else if (!isEmailCorrect(email)) {
				msg = "Incorrect email.";
			} else if (emails.contains(email)) {
				msg = "This email is already taken.";
			} else {
				emails.add(email);
				scores.put(String.valueOf(id), new ArrayList<>(List.of(0, 0, 0, 0)));
				id++;
				msg = "The student has been added.";
			}
		}
		return msg;
	}

	public void addPoints() {
		String userValue;
		System.out.println("Enter an id and points or 'back' to return:");
		do {
			userValue = scanner.nextLine();
			if (!userValue.equals("back")) {
				var input = userValue.split(" ");
				System.out.println(scoreResult(input));
			}
		} while (!userValue.equals("back"));
	}

	public String scoreResult(String[] input) {
		var msg = "Incorrect points format.";

		if (input.length != 5) return msg;

		var key = input[0];
		var marks = Arrays.copyOfRange(input, 1, input.length);
		var nonNumeric = Arrays.stream(marks)
						.filter(i -> !i.matches("\\d+"))
						.collect(Collectors.toList());

		if (!nonNumeric.isEmpty()) return msg;

		var integerInput = Arrays.stream(marks)
						.map(Integer::parseInt)
						.collect(Collectors.toList());
		var incorrectScore = integerInput.stream()
						.filter(i -> i < 0)
						.collect(Collectors.toList());

		if (!incorrectScore.isEmpty()) return msg;

		if (!scores.containsKey(key)) {
			msg = "No student is found for id=" + input[0];
		} else {
			var subjects = scores.get(key);
			subjects.add(0, subjects.get(0) + integerInput.get(0));
			subjects.add(1, subjects.get(1) + integerInput.get(1));
			subjects.add(2, subjects.get(2) + integerInput.get(2));
			subjects.add(3, subjects.get(3) + integerInput.get(3));
			msg = "Points updated.";
		}

		return msg;
	}

	public static boolean isEmailCorrect(String email) {
		var test = "[a-zA-Z\\d.-]+@[a-zA-Z\\d.-]+[.][a-zA-Z\\d]+";
		return email.matches(test);
	}

	public static boolean isNameCorrect(String input) {
		var test = "[a-zA-Z]+[-|']?(?!-)(?!')[a-zA-Z]*['|-]?[a-zA-Z]+";
		return input.matches(test);
	}

	public static boolean isLastNameCorrect(String[] input) {
		for (String s : input) {
			if (!isNameCorrect(s)) {
				return false;
			}
		}
		return true;
	}

	public void find() {
		String userValue;
		System.out.println("Enter an id or 'back' to return:");
		do {
			userValue = scanner.nextLine();
			if (!userValue.equals("back")) {
				if (!scores.containsKey(userValue)) {
					System.out.println("No student is found for id=" + userValue);
				} else {
					var subjects = scores.get(userValue);
					var msg = String.format("%s points: Java=%d; DSA=%d; Databases=%d; Spring=%d", userValue,
									subjects.get(0),
									subjects.get(1),
									subjects.get(2),
									subjects.get(3));
					System.out.println(msg);
				}
			}
		} while (!userValue.equals("back"));
	}
}