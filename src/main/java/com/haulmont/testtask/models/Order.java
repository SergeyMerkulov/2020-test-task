package com.haulmont.testtask.models;

import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;  

import com.haulmont.testtask.models.Client;
import com.haulmont.testtask.models.Mechanic;

import com.haulmont.testtask.managers.ClientsManager;
import com.haulmont.testtask.managers.MechanicsManager;

public class Order {

	private long id;
	private String description;
	private long idOfClient;
	private long idOfMechanic;
	private Client client;
	private Mechanic mechanic;
	private String beginDate;
	private String endDate;
	private int cost;
	private String status;
	
	public Order () {

	}
	
	public void setId( long id ) {
		this.id = id;
	}
	public long getId() {
		return id;
	}
	
	public void setDescription ( String description ) {
		this.description = description;
	}
	public String getDescription () {
		return description;
	}
	public String getHtmlDescription () {
		return "<div style='padding: 5px'><small>"+description+"</small></div>";
	}
	
	public void setIdOfClient ( long idOfClient ) {
		this.idOfClient = idOfClient;
	}
	public long getIdOfClient () {
		return idOfClient;
	}
	
	public void setIdOfMechanic ( long idOfMechanic ) {
		this.idOfMechanic = idOfMechanic;
	}
	public long getIdOfMechanic () {
		return idOfMechanic;
	}

	public void setClient ( Client client ) {
		this.client = client;
		this.idOfClient = client.getId();
	}
	public void setClientById ( long idOfClient ) {
		this.client = ClientsManager.getClientById( idOfClient );
		this.idOfClient = idOfClient;
	}
	public Client getClient () {
		if( this.client == null ) {
			this.client.setId(0);
			this.client.setFirstName("null");
			this.client.setSecondName("null");
			this.client.setThirdName("null");
			this.client.setPhone("00-00-00");
		}
		return this.client;
	}
	
	public void setMechanic ( Mechanic mechanic ) {
		this.mechanic = mechanic;
		this.idOfMechanic = mechanic.getId();
	}
	public void setMechanicById ( long idOfMechanic ) {
		this.mechanic = MechanicsManager.getMechanicById( idOfMechanic );
		this.idOfMechanic = idOfMechanic;
	}
	public Mechanic getMechanic () {
		return mechanic;
	}
	
	public void setBeginDate ( String beginDate ) {
		this.beginDate = beginDate;
	}
	public String getBeginDate () { 
		return beginDate;
	}
	public Date getBeginDateAsDate () {
		try {		
			Date date = dateFormat.parse(beginDate);
			return date;
		} catch(Exception e) {
			e.printStackTrace(); 
			Date date = new Date();
			return date;
		}	
	}
	public String getHtmlDates () { 
		String dates = "<div style='padding: 5px'>";
		dates += "<small>begin:"+beginDate+"<br>end:"+endDate+"</small>";
		dates += "</div>";
		return dates;
	}
	
	public void setEndDate ( String endDate ) {
		this.endDate = endDate;
	}
	public String getEndDate () { 
		return endDate;
	}
	public Date getEndDateAsDate () { 
		
		try {
			Date date = dateFormat.parse(endDate);
			return date;
		} catch(Exception e) {
			e.printStackTrace(); 
			Date date = new Date();
			return date;
		}
	}
	
	public void setCost ( int cost ) {
		this.cost = cost;
	}
	public int getCost () {
		return cost;
	}
	public String getCostAsString () {
		return String.valueOf(cost);
	}
	public String getHtmlCost () {
		return "<div style='padding: 5px'><small>"+cost+"</small></div>";
	}
	
	public void setStatus ( String status ) {
		this.status = status;
	}
	public String getStatus () {
		return status;
	}
	public String getHtmlStatus () {
		return "<div style='padding: 5px'><small>"+status+"</small></div>";
	}
	
	public void setClientAndMechanic ( Client client, Mechanic mechanic ) {
		this.client = client;
		this.mechanic = mechanic;
		this.idOfClient = client.getId();
		this.idOfMechanic = mechanic.getId();
	}
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	public void setBeginAndEndDates ( Date beginDate, Date endDate ) {
		this.beginDate = dateFormat.format(beginDate);
		this.endDate = dateFormat.format(endDate);
	}
}