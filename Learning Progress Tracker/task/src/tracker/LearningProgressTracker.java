package tracker;

import java.util.Scanner;

public class LearningProgressTracker {
	private final StudentService studentService;

	public LearningProgressTracker(StudentService studentService) {
		this.studentService = studentService;
	}

	public void start() {
		System.out.println("Learning Progress Tracker");
		var scanner = new Scanner(System.in);
		String label;

		do {
			label = scanner.nextLine();
			if (label.isEmpty() || label.isBlank()) {
				System.out.println("No input.");
				continue;
			}

			switch (Options.valueOfLabel(label)) {
				case ADD_STUDENTS:
					studentService.addStudents();
					break;
				case ADD_POINTS:
					studentService.addPoints();
					break;
				case LIST:
					if (studentService.scores.isEmpty()) {
						System.out.println("No students found.");
					} else {
						System.out.println("Students:");
						studentService.scores.keySet().forEach(System.out::println);
					}
					break;
				case FIND:
					studentService.find();
					break;
				case BACK:
					System.out.println("Enter 'exit' to exit the program.");
					break;
				case EXIT:
					System.out.println("Bye!");
					break;
				default:
					System.out.println("Unknown command!");
			}
		} while (!label.equals("exit"));
	}
}