package edu.chalmers.project.data;

/**
 * Class that is going to represent players.
 *  It is used for modeling each row of the Player table.
 *
 */
public class Player {

	private String username;
	private String password;
	private String firstName;
	private String familyName;
	private String mail;
	private int reliability;
	private String position;
	private String city;
	private String birthdate;
	private String imgPath;



	public Player(String username, String password) {
		this.username = username;
		this.password = password;
	}



	public Player(String username, String password, String firstName,
			String familyName, String mail, int reliability, String position,
			String city, String birthdate, String imgPath) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.familyName = familyName;
		this.mail = mail;
		this.reliability = reliability;
		this.position = position;
		this.city = city;
		this.birthdate = birthdate;
		this.imgPath = imgPath;
	}


	public String getImgPath() {
		return imgPath;
	}



	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public int getReliability() {
		return reliability;
	}

	public void setReliability(int reliability) {
		this.reliability = reliability;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	@Override
	public String toString() {
		return this.getUsername();
	}

	@Override
	public boolean equals(Object o) {
		Player player = (Player) o;
		return this.getUsername().equals(player.getUsername());
	}






}
