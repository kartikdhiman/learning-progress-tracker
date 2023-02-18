package tracker;

public class LearningProgressTracker {
	private final StudentService studentService;

	public LearningProgressTracker(StudentService studentService) {
		this.studentService = studentService;
	}

	public void start() {
		System.out.println("Learning Progress Tracker");
		String label;

		do {
			label = studentService.getUserInput();
			if (label.isEmpty() || label.isBlank()) {
				System.out.println("No input.");
				continue;
			}
			switch (Options.valueOfLabel(label)) {
				case ADD_STUDENTS -> studentService.addStudents();
				case ADD_POINTS -> studentService.addPoints();
				case LIST -> {
					var ids = studentService.fetchIds();
					if (ids.isEmpty()) {
						System.out.println("No students found.");
						break;
					}
					System.out.println("Students:");
					ids.forEach(System.out::println);
				}
				case FIND -> studentService.find();
				case STATISTICS -> studentService.showStatistics();
				case NOTIFY -> studentService.notifyStudent();
				case BACK -> System.out.println("Enter 'exit' to exit the program.");
				case EXIT -> System.out.println("Bye!");
				default -> System.out.println("Unknown command!");
			}
		} while (!label.equals("exit"));
	}
}
