package tracker;

import java.util.Arrays;

public enum Options {
	ADD_STUDENTS("add students"),
	ADD_POINTS("add points"),
	LIST("list"),
	FIND("find"),
	BACK("back"),
	EXIT("exit"),
	STATISTICS("statistics"),
	NOTIFY("notify"),
	INVALID("invalid");

	private final String label;

	Options(String s) {
		label = s;
	}

	@Override
	public String toString() {
		return this.label;
	}

	public static Options valueOfLabel(String label) {
		return Arrays.stream(values())
						.filter(option -> option.label.equals(label))
						.findFirst()
						.orElse(Options.INVALID);
	}
}
