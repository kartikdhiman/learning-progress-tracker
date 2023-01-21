package tracker;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Course {
	JAVA("Java", 600),
	DSA("DSA", 400),
	DATABASES("Databases", 480),
	SPRING("Spring", 550);

	final String name;
	final int passingScore;
	int totalSubmissions;
	double totalScore;
	Set<Integer> students = new HashSet<>();

	Course(String name,int passingScore) {
		this.name = name;
		this.passingScore = passingScore;
	}

	public int totalSubmissions() {
		return totalSubmissions;
	}

	public void addStudent(Integer id) {
		this.students.add(id);
	}

	public static List<String> mostPopular() {
		var na = "n/a";
		if (courses().noneMatch(course -> course.totalSubmissions > 0))
			return List.of(na);
		var maxStudents = courses().max(Comparator.comparingInt(c -> c.students.size())).get();
		return courses()
						.filter(course -> course.students.size() == maxStudents.students.size())
						.map(course -> course.name)
						.collect(Collectors.toList());
	}

	public static List<String> leastPopular() {
		var na = "n/a";
		if (courses().noneMatch(course -> course.totalSubmissions > 0))
			return List.of(na);
		var minStudents = courses().min(Comparator.comparingInt(c -> c.students.size())).get();
		return courses()
						.filter(course -> course.students.size() == minStudents.students.size())
						.map(course -> course.name)
						.collect(Collectors.toList());
	}

	public static List<String> highestActivity() {
		var na = "n/a";
		if (courses().noneMatch(course -> course.totalSubmissions > 0))
			return List.of(na);
		var maxSubmission = courses().max(Comparator.comparingInt(Course::totalSubmissions)).get();
		return courses()
						.filter(course -> course.totalSubmissions == maxSubmission.totalSubmissions)
						.map(course -> course.name)
						.collect(Collectors.toList());
	}

	public static List<String> lowestActivity() {
		var na = "n/a";
		if (courses().noneMatch(course -> course.totalSubmissions > 0))
			return List.of(na);
		var minSubmission = courses().min(Comparator.comparingInt(Course::totalSubmissions)).get();
		return courses()
						.filter(course -> course.totalSubmissions == minSubmission.totalSubmissions)
						.map(course -> course.name)
						.collect(Collectors.toList());
	}

	public static List<String> easiestCourse() {
		var na = "n/a";
		if (courses().noneMatch(course -> course.totalSubmissions > 0))
			return List.of(na);
		var maxAvg = courses().max(Comparator.comparingDouble(c -> c.totalScore / c.totalSubmissions)).get();
		return courses()
						.filter(c -> c.totalScore / c.totalSubmissions == maxAvg.totalScore / maxAvg.totalSubmissions)
						.map(c -> c.name)
						.collect(Collectors.toList());
	}

	public static List<String> hardestCourse() {
		var na = "n/a";
		if (courses().noneMatch(course -> course.totalSubmissions > 0))
			return List.of(na);
		var minAvg = courses().min(Comparator.comparingDouble(c -> c.totalScore / c.totalSubmissions)).get();
		return courses()
						.filter(c -> c.totalScore / c.totalSubmissions == minAvg.totalScore / minAvg.totalSubmissions)
						.map(c -> c.name)
						.collect(Collectors.toList());
	}

	public static List<String> courseList() {
		return courses().map(course -> course.name.toLowerCase()).toList();
	}

	private static Stream<Course> courses() {
		return Arrays.stream(values());
	}
}
