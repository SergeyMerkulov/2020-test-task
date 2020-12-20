package com.haulmont.testtask.models;

public class Client {

	private long id;
	private String firstName;
	private String secondName;
	private String thirdName;
	private String phone;
	
	public Client(  ) {

	}
	
	public Client( long id, String firstName, String secondName, String thirdName, String phone ) {
		this.id = id;
		this.firstName = firstName;
		this.secondName = secondName;
		this.thirdName = thirdName;
		this.phone = phone;
	}
	
	public void setId( long id ) {	
		this.id = id;
	}
	public long getId() {
		return id;
	}
	
	public void setFirstName( String firstName ) {
		this.firstName = firstName;
	}
	public String getFirstName() {
		return firstName;
	}
	
	public void setSecondName( String secondName ) {
		this.secondName = secondName;
	}
	public String getSecondName() {
		return secondName;
	}
	
	public void setThirdName( String thirdName ) {
		this.thirdName = thirdName;
	}
	public String getThirdName() {
		return thirdName;
	}
	
	public void setPhone( String phone ) {
		this.phone = phone;
	}
	public String getPhone() {
		return phone;
	}
	
	public String getDescription() {
		return firstName+" "+secondName+" "+thirdName+" "+phone+" id|"+id+"|";
	}
	
	public String getShortDescription() {
		return firstName+"\n"+secondName+"\n"+thirdName;
	}
	
	public String getHtmlShortDescription() {
		String description = "<div style='padding: 5px'>";
		description += "<small>"+firstName+"<br>"+secondName+"<br>"+thirdName+"</small>";
		description += "</div>";
		return description;
	}
}