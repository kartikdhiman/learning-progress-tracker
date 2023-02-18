package tracker;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.join;

public class StudentService {
	Scanner scanner = new Scanner(System.in);
	Set<String> emails = new HashSet<>();
	List<Student> students = new ArrayList<>();

	public void addStudents() {
		String value;
		print("Enter student credentials or 'back' to return:");
		do {
			value = getUserInput();
			if (value.equals("back")) {
				var message = String.format("Total %d students have been added.", emails.size());
				print(message);
			} else {
				var inputs = value.split(" ");
				var message = processCredentials(inputs);
				print(message);
			}
		} while (!value.equals("back"));
	}

	public String processCredentials(String[] inputs) {
		var msg = "Incorrect credentials.";
		if (inputs.length < 3) {
			return msg;
		}
		var firstName = inputs[0];
		var lastNames = Arrays.copyOfRange(inputs, 1, inputs.length - 1);
		var email = inputs[inputs.length - 1];
		if (isNameNotCorrect(firstName)) {
			msg = "Incorrect first name.";
		} else if (!isLastNameCorrect(lastNames)) {
			msg = "Incorrect last name.";
		} else if (!isEmailCorrect(email)) {
			msg = "Incorrect email.";
		} else if (emails.contains(email)) {
			msg = "This email is already taken.";
		} else {
			emails.add(email);
			students.add(new Student(email, firstName + " " + join(" ", lastNames)));
			msg = "The student has been added.";
		}
		return msg;
	}

	public void addPoints() {
		String userValue;
		print("Enter an id and points or 'back' to return:");
		do {
			userValue = getUserInput();
			if (!userValue.equals("back")) {
				var input = userValue.split(" ");
				print(inputMessage(input));
			}
		} while (!userValue.equals("back"));
	}

	private String inputMessage(String[] input) {
		var msg = "Incorrect points format.";

		if (input.length != 5) return msg;

		var id = input[0];
		var marks = Arrays.copyOfRange(input, 1, input.length);
		var nonNumeric = Arrays.stream(marks).filter(i -> !i.matches("\\d+")).toList();
		if (!nonNumeric.isEmpty()) return msg;

		var inputScores = Arrays.stream(marks).map(Integer::parseInt).toList();
		var incorrectScore = inputScores.stream().filter(i -> i < 0).toList();
		if (!incorrectScore.isEmpty()) return msg;

		var record = Student.findById(students, id);
		if (record.isEmpty()) {
			return "No student is found for id=" + id;
		}
		var student = record.get();
		student.java += inputScores.get(0);
		student.dsa += inputScores.get(1);
		student.databases += inputScores.get(2);
		student.spring += inputScores.get(3);

		Course.JAVA.totalScore += inputScores.get(0);
		Course.DSA.totalScore += inputScores.get(1);
		Course.DATABASES.totalScore += inputScores.get(2);
		Course.SPRING.totalScore += inputScores.get(3);

		if (inputScores.get(0) > 0) {
			Course.JAVA.totalSubmissions++;
			Course.JAVA.addStudent(student.id());
		}
		if (inputScores.get(1) > 0) {
			Course.DSA.totalSubmissions++;
			Course.DSA.addStudent(student.id());
		}
		if (inputScores.get(2) > 0) {
			Course.DATABASES.totalSubmissions++;
			Course.DATABASES.addStudent(student.id());
		}
		if (inputScores.get(3) > 0) {
			Course.SPRING.totalSubmissions++;
			Course.SPRING.addStudent(student.id());
		}
		return "Points updated.";
	}

	public void find() {
		String userValue;
		print("Enter an id or 'back' to return:");
		do {
			userValue = getUserInput();
			if (!userValue.equals("back")) {
				var record = Student.findById(students, userValue);
				if (record.isEmpty()) {
					print("No student is found for id=" + userValue);
					continue;
				}
				var student = record.get();
				print("%s points: Java=%.0f; DSA=%.0f; Databases=%.0f; Spring=%.0f".formatted(userValue,
								student.java,
								student.dsa,
								student.databases,
								student.spring
				));
			}
		} while (!userValue.equals("back"));
	}

	public void showStatistics() {
		var na = "n/a";
		String userInput;
		var mostPopular = Course.mostPopular();
		var leastPopular = Course.leastPopular()
						.stream().filter(c -> !mostPopular.contains(c)).collect(Collectors.toList());
		if (leastPopular.isEmpty()) leastPopular.add(na);
		var highestActivity = Course.highestActivity();
		var lowestActivity = Course.lowestActivity()
						.stream().filter(c -> !highestActivity.contains(c)).collect(Collectors.toList());
		if (lowestActivity.isEmpty()) lowestActivity.add(na);
		var easiestCourse = Course.easiestCourse();
		var hardestCourse = Course.hardestCourse()
						.stream().filter(c -> !easiestCourse.contains(c)).collect(Collectors.toList());
		if (hardestCourse.isEmpty()) hardestCourse.add(na);
		print("""
						Type the name of a course to see details or 'back' to quit:
						Most popular: %s
						Least popular: %s
						Highest activity: %s
						Lowest activity: %s
						Easiest course: %s
						Hardest course: %s""".formatted(
						join(", ", mostPopular), join(", ", leastPopular),
						join(", ", highestActivity), join(", ", lowestActivity),
						join(", ", easiestCourse), join(", ", hardestCourse)
		));

		do {
			userInput = getUserInput();
			if (!userInput.equals("back")) {
				if (!Course.courseList().contains(userInput.toLowerCase())) {
					print("Unknown course");
				} else {
					if (userInput.equalsIgnoreCase(Course.JAVA.name)) {
						printStatistics(Course.JAVA);
					} else if (userInput.equalsIgnoreCase(Course.DSA.name)) {
						printStatistics(Course.DSA);
					} else if (userInput.equalsIgnoreCase(Course.DATABASES.name)) {
						printStatistics(Course.DATABASES);
					} else if (userInput.equalsIgnoreCase(Course.SPRING.name)) {
						printStatistics(Course.SPRING);
					}
				}
			}
		} while (!userInput.equals("back"));
	}

	private void printStatistics(Course course) {
	 List<StudentScoreCard> scores = new ArrayList<>();
		print("""
						%s
						id		points	completed""".formatted(course.name));
		students.stream()
						.filter(student -> course.students.contains(student.id()))
						.forEach(student -> {
							switch (course) {
								case JAVA -> {
									var percentage = new BigDecimal(student.java / course.passingScore * 100).setScale(1, RoundingMode.HALF_UP);
									scores.add(new StudentScoreCard(student.id(), student.java, percentage.toString()));
									scores.sort(StudentScoreCard.scoreCardComparator());
								}
								case DSA -> {
									var percentage = new BigDecimal(student.dsa / course.passingScore * 100).setScale(1, RoundingMode.HALF_UP);
									scores.add(new StudentScoreCard(student.id(), student.dsa, percentage.toString()));
									scores.sort(StudentScoreCard.scoreCardComparator());
								}
								case DATABASES -> {
									var percentage = new BigDecimal(student.databases / course.passingScore * 100).setScale(1, RoundingMode.HALF_UP);
									scores.add(new StudentScoreCard(student.id(), student.databases, percentage.toString()));
									scores.sort(StudentScoreCard.scoreCardComparator());
								}
								case SPRING -> {
									var percentage = new BigDecimal(student.spring / course.passingScore * 100).setScale(1, RoundingMode.HALF_UP);
									scores.add(new StudentScoreCard(student.id(), student.spring, percentage.toString()));
									scores.sort(StudentScoreCard.scoreCardComparator());
								}
							}
						});
		scores.forEach(System.out::println);
	}

	public void notifyStudent() {
		var total = new HashSet<Integer>();
		var template = """
						To: %s
						Re: Your Learning Progress
						Hello, %s! You have accomplished our %s course!""";
		for (Student student : students) {
			if (!student.isJavaNotified && Course.JAVA.passingScore == student.java) {
				print(template.formatted(student.email(), student.name(), Course.JAVA.name));
				student.isJavaNotified = true;
				total.add(student.id());
			}
			if (!student.isDSANotified && Course.DSA.passingScore == student.dsa) {
				print(template.formatted(student.email(), student.name(), Course.DSA.name));
				student.isDSANotified = true;
				total.add(student.id());
			}
			if (!student.isDatabasesNotified && Course.DATABASES.passingScore == student.dsa) {
				print(template.formatted(student.email(), student.name(), Course.DATABASES.name));
				student.isDatabasesNotified = true;
				total.add(student.id());
			}
			if (!student.isSpringNotified && Course.SPRING.passingScore == student.dsa) {
				print(template.formatted(student.email(), student.name(), Course.SPRING.name));
				student.isSpringNotified = true;
				total.add(student.id());
			}
		}
		print("Total %d students have been notified.".formatted(total.size()));
	}

	public String getUserInput() {
		return scanner.nextLine();
	}

	public List<Integer> fetchIds() {
		return students.stream().map(Student::id).toList();
	}

	private boolean isEmailCorrect(String email) {
		var test = "[a-zA-Z\\d.-]+@[a-zA-Z\\d.-]+[.][a-zA-Z\\d]+";
		return email.matches(test);
	}

	private boolean isNameNotCorrect(String input) {
		var test = "[a-zA-Z]+[-|']?(?!-)(?!')[a-zA-Z]*['|-]?[a-zA-Z]+";
		return !input.matches(test);
	}

	private boolean isLastNameCorrect(String[] input) {
		for (String s : input) {
			if (isNameNotCorrect(s)) {
				return false;
			}
		}
		return true;
	}

	private void print(String msg) {
		System.out.println(msg);
	}
}