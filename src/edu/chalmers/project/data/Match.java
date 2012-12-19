package edu.chalmers.project.data;

/**
 * Class that is going to represent the matches.
 * It is used for modeling each row of the Match table.
 *
 */
public class Match {

	private int id;
	private String name;
	private String date;
	private String time;
	private String location;
	private String field;
	private int cost;
	private int numberPlayers;
	private int id_organizer;

	public Match(int id, String name, String date, String time, String location, String field, 
			int cost, int numberPlayers, int id_organizer) {
		this.id = id;
		this.name=name;
		this.date=date;
		this.time=time;
		this.location=location;
		this.field=field;
		this.cost=cost;
		this.numberPlayers=numberPlayers;
		this.id_organizer=id_organizer;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId_organizer() {
		return id_organizer;
	}

	public void setId_organizer(int id_organizer) {
		this.id_organizer = id_organizer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getNumberPlayers() {
		return numberPlayers;
	}

	public void setNumberPlayers(int numberPlayers) {
		this.numberPlayers = numberPlayers;
	}

	public String toString(){
		return this.name;
	}

}



