package course.springboot.model;

public enum Levels {
	
	JUNIOR("Junior"),
	PREPROFESSIONAL("Preprofessional"),
	SENIOR("Senior");
	
	private String name;
	private String value;
	
	private Levels(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value = this.name();
	}
	
}
