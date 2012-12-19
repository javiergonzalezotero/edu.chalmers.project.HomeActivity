package edu.chalmers.project.data;

/**
 * Class that is going to model the goals. It is used to modeling each row of the Goal table
 *
 */
public class Goal {

	private int id;
	private String username;
	private int idMatch;
	private int minute;

	public Goal(int id, String username, int idMatch, int minute){
		this.id=id;
		this.username=username;
		this.idMatch=idMatch;
		this.minute=minute;
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

	public int getIdMatch() {
		return idMatch;
	}

	public void setIdMatch(int idMatch) {
		this.idMatch = idMatch;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	@Override
	public String toString() {
		return this.getMinute() + "'  " + this.getUsername();
	}

}
