
/**
 * @author Ali Ghanem
 *
 */
public class Course {
	private String name;
	private int credit;
	private String grade;

	public Course() {
	}

	public Course(String name, int credit, String grade) {
		this.name = name;
		this.credit = credit;
		this.grade = grade;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}
}
