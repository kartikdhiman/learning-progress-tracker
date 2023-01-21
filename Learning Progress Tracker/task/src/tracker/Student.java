package tracker;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class Student {
	static int id = 10000;
	private final String studentId;
	private final String email;
	private final String name;
	double java;
	double dsa;
	double databases;
	double spring;

	public Student(String studentId, String email, String name) {
		this.studentId = studentId;
		this.email = email;
		this.name = name;
	}

	public Student(String email, String name) {
		this(String.valueOf(id), email, name);
		id++;
	}

	public static Optional<Student> findById(List<Student> list, String id) {
		return list.stream().filter(student -> student.studentId.equals(id)).findFirst();
	}

	public Integer id() {
		return Integer.parseInt(studentId);
	}

	public String email() {
		return email;
	}

	public String name() {
		return name;
	}
}

class StudentScoreCard{
	int id;
	double points;
	String percentage;

	public int getId() {
		return id;
	}

	public double getPoints() {
		return points;
	}

	public StudentScoreCard(int id, double points, String percentage) {
		this.id = id;
		this.points = points;
		this.percentage = percentage;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, points, percentage);
	}

	@Override
	public String toString() {
		return "%d\t%.0f\t\t%s%%".formatted(id, points, percentage);
	}

	public static Comparator<StudentScoreCard> scoreCardComparator() {
		return Comparator.comparingDouble(StudentScoreCard::getPoints)
						.reversed()
						.thenComparingInt(StudentScoreCard::getId);
	}
}
