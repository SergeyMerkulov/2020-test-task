package com.haulmont.testtask.models;

public class Mechanic {

	private long id;
	private String firstName;
	private String secondName;
	private String thirdName;
	private int hourlySalary;
	
	public Mechanic ()
	{
		this.id = 0;
		this.firstName = null;
		this.secondName = null;
		this.thirdName = null;
		this.hourlySalary = 0;
	}
	
	public Mechanic ( long id, String firstName, String secondName, String thirdName, int hourlySalary)
	{
		this.id = id;
		this.firstName = firstName;
		this.secondName = secondName;
		this.thirdName = thirdName;
		this.hourlySalary = hourlySalary;
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
	
	public void setHourlySalary( int hourlySalary ) {
		this.hourlySalary = hourlySalary;
	}
	public int getHourlySalary() {
		return hourlySalary;
	}
	
	public String getDescription() {
		return firstName+" "+secondName+" "+thirdName+" "+hourlySalary+" id|"+id+"|";
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