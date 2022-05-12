package tracker;

public class Main {
	public static void main(String[] args) {
		StudentService studentService = new StudentService();
		var tracker = new LearningProgressTracker(studentService);
		tracker.start();
	}
}
