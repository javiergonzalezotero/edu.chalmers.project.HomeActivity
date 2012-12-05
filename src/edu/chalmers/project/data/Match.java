package edu.chalmers.project.data;

public class Match {
	
	private String name;
	private String date;
	private String time;
	private String location;
	private String field;
	private int cost;
	private int numberPlayers;
	
	public Match(String name, String date, String time, String location, String field, int cost, int numberPlayers){
		this.name=name;
		this.date=date;
		this.location=location;
		this.field=field;
		this.cost=cost;
		this.numberPlayers=numberPlayers;
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
