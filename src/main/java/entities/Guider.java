package entities;

import lombok.Data;

@Data
public class Guider {
	private long guider_id;
	private String first_name;
	private String last_name;
	private int age;
	private String about_me;
	private long contribution_point;
	private String city;
	private boolean active;
	private String[] language;

	public Guider() {
	}

	public Guider(long guider_id, String first_name, String last_name, int age, String about_me, long contribution_point, String city, boolean active, String[] language) {
		this.guider_id = guider_id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.age = age;
		this.about_me = about_me;
		this.contribution_point = contribution_point;
		this.city = city;
		this.active = active;
		this.language = language;
	}

	public long getGuider_id() {
		return guider_id;
	}

	public boolean isActive() {
		return active;
	}

	public String getFirst_name() {
		return first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public int getAge() {
		return age;
	}

	public String getAbout_me() {
		return about_me;
	}

	public long getContribution_point() {
		return contribution_point;
	}

	public String getCity() {
		return city;
	}

	public void setGuider_id(long guider_id) {
		this.guider_id = guider_id;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setAbout_me(String about_me) {
		this.about_me = about_me;
	}

	public void setContribution_point(long contribution_point) {
		this.contribution_point = contribution_point;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String[] getLanguage() {
		return language;
	}

	public void setLanguage(String[] language) {
		this.language = language;
	}
}
