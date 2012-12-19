package edu.chalmers.project.data;

/**
 * Class that is going to represent one interval of availability in one day of the week
 * for one user. It is used for modeling each row of the Availability table.
 *
 */
public class Availability {

	private int id;
	private String username;
	private String day;
	private String interval;

	public Availability(int id, String username, String day, String interval){
		this.id = id;
		this.username = username;
		this.day = day;
		this.interval = interval;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	@Override
	public String toString() {
		return this.getInterval();
	}

}
