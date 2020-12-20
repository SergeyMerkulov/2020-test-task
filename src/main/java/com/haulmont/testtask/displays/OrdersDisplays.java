package com.haulmont.testtask.displays;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;  
import java.lang.String;

import com.haulmont.testtask.models.Client;
import com.haulmont.testtask.models.Mechanic;
import com.haulmont.testtask.models.Order;

import com.haulmont.testtask.managers.ClientsManager;
import com.haulmont.testtask.managers.MechanicsManager;
import com.haulmont.testtask.managers.OrdersManager;

public class OrdersDisplays {
	
	private Order order;
	private UI ui;
	
	public OrdersDisplays() {
		
	}

	public OrdersDisplays( UI ui ) {
		this.ui = ui;
	}

	private final int HEADER_ROW = 0;
	private final int COLLUMNS_NUMBER = 7;
	private final int DESCRIPTION_COL = 0;
	private final int CLIENT_COL = 1;
	private final int MECHANIC_COL = 2;
	private final int DATES_COL = 3;
	private final int COST_COL = 4;
	private final int STATUS_COL = 5;
	private final int EDIT_DELETE_COL = 6;
	private final int EXPAND_RATIO = 1;
	private final int HEADERS_NUMBER = 1;
	
	public GridLayout createOrdersPage( String description, String statuses, String idOfClient ) {
		GridLayout orderPage = new GridLayout(COLLUMNS_NUMBER, OrdersManager.getNumber()+HEADERS_NUMBER);
		
		orderPage.addComponent(new Label("<h4>description</h4>", Label.CONTENT_XHTML), DESCRIPTION_COL, HEADER_ROW);
		orderPage.addComponent(new Label("<h4>client</h4>", Label.CONTENT_XHTML), CLIENT_COL, HEADER_ROW);
		orderPage.addComponent(new Label("<h4>mechanic</h4>", Label.CONTENT_XHTML), MECHANIC_COL, HEADER_ROW);
		orderPage.addComponent(new Label("<h4>dates</h4>", Label.CONTENT_XHTML), DATES_COL, HEADER_ROW);
		orderPage.addComponent(new Label("<h4>cost</h4>", Label.CONTENT_XHTML), COST_COL, HEADER_ROW);
		orderPage.addComponent(new Label("<h4>status</h4>", Label.CONTENT_XHTML), STATUS_COL, HEADER_ROW);
		orderPage.addComponent(new Label("<h4>edit/delete</h4>", Label.CONTENT_XHTML), EDIT_DELETE_COL, HEADER_ROW);
		
		String dates;
		int i = 0;
		for( Order order : OrdersManager.getOrdersFast( description, statuses, idOfClient ) ) {
			i++;
			
			orderPage.addComponent(new Label(order.getHtmlDescription(), Label.CONTENT_XHTML), DESCRIPTION_COL, i);
			orderPage.addComponent(new Label(order.getClient().getHtmlShortDescription(), Label.CONTENT_XHTML ), CLIENT_COL, i);
			orderPage.addComponent(new Label(order.getMechanic().getHtmlShortDescription(), Label.CONTENT_XHTML ), MECHANIC_COL, i);
			orderPage.addComponent(new Label(order.getHtmlDates(), Label.CONTENT_XHTML ), DATES_COL, i);
			orderPage.addComponent(new Label(order.getHtmlCost(), Label.CONTENT_XHTML), COST_COL, i);
			orderPage.addComponent(new Label(order.getHtmlStatus(), Label.CONTENT_XHTML), STATUS_COL, i);
			
			VerticalLayout buttonsLayout = new VerticalLayout();
			buttonsLayout.addComponent(new Button("edit", event -> {
				this.order = order;
				ui.addWindow(createEditOrderWindow());
			} ) );
			buttonsLayout.addComponent(new Button("delete", event -> {
				OrdersManager.delete(order.getId());
				Notification.show("The order is deleted. Apply show to see the change.");
			} ) );
			
			orderPage.addComponent(buttonsLayout, EDIT_DELETE_COL , i );
		}
		
		orderPage.setWidth("100%");
		orderPage.setColumnExpandRatio(DESCRIPTION_COL, EXPAND_RATIO);
		orderPage.setColumnExpandRatio(CLIENT_COL, EXPAND_RATIO);
		orderPage.setColumnExpandRatio(MECHANIC_COL, EXPAND_RATIO);
		orderPage.setColumnExpandRatio(DATES_COL, EXPAND_RATIO);
		orderPage.setColumnExpandRatio(COST_COL, EXPAND_RATIO);
		orderPage.setColumnExpandRatio(STATUS_COL, EXPAND_RATIO);
		orderPage.setColumnExpandRatio(EDIT_DELETE_COL, EXPAND_RATIO);
		
		return orderPage;
	}
	
	private final int DESCRIPTION_FIELD_ROWS_NUMBER = 2;
	
	public Window createAddOrderWindow() {
		
		Window window = new Window("Add order");
		FormLayout layout = new FormLayout();
		window.setContent( layout );
		
		NativeSelect clientsSelect = new NativeSelect("Client");
		boolean isFirst = true;
		for(Client client : ClientsManager.getClients()) {
			String stringClient = String.valueOf( client.getId() );
			clientsSelect.addItem( stringClient );
			clientsSelect.setItemCaption( stringClient, client.getShortDescription() );
			if( isFirst ) clientsSelect.setValue( stringClient );
			isFirst = false;
		}
		clientsSelect.setNullSelectionAllowed(false);
		layout.addComponent( clientsSelect );
		
		NativeSelect mechanicsSelect = new NativeSelect("Mechanic");
		isFirst = true;
		for(Mechanic mechanic : MechanicsManager.getMechanics()) {
			String stringMechanic = String.valueOf( mechanic.getId() );
			mechanicsSelect.addItem( stringMechanic );
			mechanicsSelect.setItemCaption( stringMechanic, mechanic.getShortDescription() );
			if( isFirst ) mechanicsSelect.setValue( stringMechanic );
			isFirst = false;
		}
		mechanicsSelect.setNullSelectionAllowed(false);
		layout.addComponent( mechanicsSelect );
		
		TextArea orderDescription = new TextArea("Description");
		orderDescription.setRows( DESCRIPTION_FIELD_ROWS_NUMBER );
		orderDescription.setWidth("20em");
		layout.addComponent( orderDescription );
		
		OptionGroup statusOptionGroup = new OptionGroup("Status of the order");
		statusOptionGroup.addItems("planned", "completed", "accepted");
		statusOptionGroup.setValue("planned");
		layout.addComponent( statusOptionGroup );
		
		DateField beginDateField = new DateField("Begin date");
		beginDateField.setDateFormat("dd/MM/yyyy");
		beginDateField.setValue(new Date());
		layout.addComponent( beginDateField );
		
		DateField endDateField = new DateField("End date");
		endDateField.setDateFormat("dd/MM/yyyy");
		endDateField.setValue(new Date());
		layout.addComponent( endDateField );
		
		TextField costField = new TextField("Cost");
		layout.addComponent( costField );
		
		Button addOrderButton = new Button("Add");
		layout.addComponent( addOrderButton );
		
		window.setWidth("60%");
        window.center();
		window.setModal(true);
		
		addOrderButton.addClickListener(click -> {
			
			if(orderDescription.isEmpty()){Notification.show("The description field is empty");return;}
			if(costField.isEmpty()){Notification.show("The cost field is empty");return;}
			if(costField.getValue().matches(".*[^0-9].*")){Notification.show("The cost field has to contain a positive number");return;}
			Order order = new Order();
			order.setDescription ( String.valueOf(orderDescription.getValue()) );
			order.setClient( ClientsManager.getClientById( Long.parseLong( String.valueOf(clientsSelect.getValue()) ) ) );
			order.setMechanic( MechanicsManager.getMechanicById( Long.parseLong( String.valueOf(mechanicsSelect.getValue()) ) ) );
			order.setBeginAndEndDates ( beginDateField.getValue(), endDateField.getValue() );
			order.setCost( Integer.parseInt( String.valueOf(costField.getValue()) ) );
			order.setStatus( String.valueOf(statusOptionGroup.getValue()) );
			OrdersManager.add( order );
			Notification.show("The order is added!");
			window.close();
		});
		
		return window;
	}	
	
	public Window createEditOrderWindow() {
		
		Window window = new Window("Edit order");
		FormLayout orderForm = new FormLayout();
		window.setContent( orderForm );
		
		NativeSelect clientsSelect = new NativeSelect("Client");

		for(Client client : ClientsManager.getClients()) {
			String stringClient = String.valueOf( client.getId() );
			clientsSelect.addItem( stringClient );
			clientsSelect.setItemCaption( stringClient, client.getShortDescription() );

			if( client.getId() == this.order.getIdOfClient() ) {
				clientsSelect.setValue( stringClient );
			}
		}
		clientsSelect.setNullSelectionAllowed(false);
		orderForm.addComponent( clientsSelect );
		
		NativeSelect mechanicsSelect = new NativeSelect("Mechanic");

		for(Mechanic mechanic : MechanicsManager.getMechanics()) {
			String stringMechanic = String.valueOf( mechanic.getId() );
			mechanicsSelect.addItem( stringMechanic );
			mechanicsSelect.setItemCaption( stringMechanic, mechanic.getShortDescription() );

			if( mechanic.getId() == this.order.getIdOfMechanic() ) {
				mechanicsSelect.setValue( stringMechanic );
			}
		}
		mechanicsSelect.setNullSelectionAllowed(false);
		orderForm.addComponent( mechanicsSelect );
		
		TextArea orderDescription = new TextArea("Description");
		orderDescription.setRows( DESCRIPTION_FIELD_ROWS_NUMBER );
		orderDescription.setWidth("20em");
		orderForm.addComponent( orderDescription );
		orderDescription.setValue( this.order.getDescription() );
		
		OptionGroup statusOptionGroup = new OptionGroup("Status of the order");
		statusOptionGroup.addItems("planned", "completed", "accepted");
		statusOptionGroup.setValue( this.order.getStatus() );
		orderForm.addComponent( statusOptionGroup );
		
		DateField beginDateField = new DateField("Begin date");
		beginDateField.setDateFormat("dd/MM/yyyy");
		beginDateField.setValue( this.order.getBeginDateAsDate() );
		orderForm.addComponent( beginDateField );
		
		DateField endDateField = new DateField("End date");
		endDateField.setDateFormat("dd/MM/yyyy");
		endDateField.setValue( this.order.getEndDateAsDate() );
		orderForm.addComponent( endDateField );
		
		TextField costField = new TextField("Cost");
		orderForm.addComponent( costField );
		costField.setValue(this.order.getCostAsString());
		
		Button okEditOrderButton = new Button("Ok");
		orderForm.addComponent( okEditOrderButton );
		
		Button cancelEditOrderButton = new Button("Cancel");
		orderForm.addComponent( cancelEditOrderButton );
		
		window.setWidth("60%");
        window.center();
		window.setModal(true);
		
		okEditOrderButton.addClickListener(click -> {
			
			if(orderDescription.isEmpty()){Notification.show("The description field is empty");return;}
			if(costField.isEmpty()){Notification.show("The cost field is empty");return;}
			if(costField.getValue().matches(".*[^0-9].*")){Notification.show("The cost field has to contain a positive number");return;}
			this.order.setDescription ( String.valueOf(orderDescription.getValue()) );
			this.order.setClient( ClientsManager.getClientById( Long.parseLong( String.valueOf(clientsSelect.getValue()) ) ) );
			this.order.setMechanic( MechanicsManager.getMechanicById( Long.parseLong( String.valueOf(mechanicsSelect.getValue()) ) ) );
			this.order.setBeginAndEndDates ( beginDateField.getValue(), endDateField.getValue() );
			this.order.setCost( Integer.parseInt( String.valueOf(costField.getValue()) ) );
			this.order.setStatus( String.valueOf(statusOptionGroup.getValue()) );
			OrdersManager.update( this.order );
			Notification.show("The order is updated");
			window.close();
		});
		
		cancelEditOrderButton.addClickListener(click -> {
			window.close();
		});
		
		return window;
	}
}