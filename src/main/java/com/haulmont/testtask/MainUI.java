package com.haulmont.testtask;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;  
import java.lang.String;

import com.haulmont.testtask.Create;

import com.haulmont.testtask.models.Client;
import com.haulmont.testtask.models.Mechanic;
import com.haulmont.testtask.models.Order;

import com.haulmont.testtask.managers.ClientsManager;
import com.haulmont.testtask.managers.MechanicsManager;
import com.haulmont.testtask.managers.OrdersManager;

import com.haulmont.testtask.displays.ClientsDisplays;
import com.haulmont.testtask.displays.MechanicsDisplays;
import com.haulmont.testtask.displays.OrdersDisplays;


@Theme(ValoTheme.THEME_NAME)
public class MainUI extends UI {
	
	private final int DESCRIPTION_ROWS_NUMBER = 3;
	
	public Panel clientsPanel;
	public Panel mechanicsPanel;
	public Panel ordersPanel;
	
    @Override
    protected void init(VaadinRequest request) {

		Create.tables();
		
		VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
		
		TabSheet tabsheet = new TabSheet();
		layout.addComponent(tabsheet);
		
		NativeSelect clientsSelect = new NativeSelect("Client");
		
		//Client
		clientsPanel = new Panel();
		clientsPanel.setContent( new ClientsDisplays( UI.getCurrent(), clientsPanel, clientsSelect ).createClientsPage() );
		tabsheet.addTab(clientsPanel).setCaption("Clients");
		
		//Mechanic
		mechanicsPanel = new Panel();
		mechanicsPanel.setContent( new MechanicsDisplays( UI.getCurrent(), mechanicsPanel ).createMechanicsPage() );
		tabsheet.addTab(mechanicsPanel).setCaption("Mechanics");
		
		//Order
		VerticalLayout orderPage = new VerticalLayout();
		HorizontalLayout controlPanel = new HorizontalLayout();
		//order description	
		TextArea orderDescription = new TextArea("Description");
		orderDescription.setRows( DESCRIPTION_ROWS_NUMBER );
		orderDescription.setWidth("10em");
		controlPanel.addComponent( orderDescription );
		//border
		controlPanel.addComponent( new Label("<div style='padding: 8px'></div>",Label.CONTENT_XHTML) );
		//select client 
		FormLayout statusAndClientLayout = new FormLayout();
		for(Client client : ClientsManager.getClients()) {
			String clientId = String.valueOf( client.getId() );
			clientsSelect.addItem( clientId );
			clientsSelect.setItemCaption( clientId, client.getShortDescription() );
		}
		clientsSelect.addItem( "everybody" );
		clientsSelect.setItemCaption( "everybody", "everybody" );
		clientsSelect.setValue( "everybody" );
		clientsSelect.setNullSelectionAllowed(false);		
		statusAndClientLayout.addComponent( clientsSelect );
		//select status
		NativeSelect statusSelect = new NativeSelect("Status");
		statusSelect.addItems("all","planned","completed","accepted");
		statusSelect.addItems("planned, completed","planned, accepted","completed, accepted");
		statusSelect.setValue("all");
		statusSelect.setNullSelectionAllowed(false);
		statusAndClientLayout.addComponent( statusSelect );
		controlPanel.addComponent( statusAndClientLayout );
		//buttons of order panel
		FormLayout buttonsOfControlPanel = new FormLayout();
		buttonsOfControlPanel.addComponent( new Button("apply and show", event -> { 
			ordersPanel.setContent( new OrdersDisplays( UI.getCurrent() ).createOrdersPage( String.valueOf(orderDescription.getValue()), 
			String.valueOf(statusSelect.getValue()), String.valueOf(clientsSelect.getValue()) ) );
		}	) );
		buttonsOfControlPanel.addComponent( new Button("add order", event -> { 
			addWindow(new OrdersDisplays().createAddOrderWindow()); 
		} ) );
		controlPanel.addComponent( buttonsOfControlPanel );
			
		orderPage.addComponent(controlPanel);
		ordersPanel = new Panel();
		orderPage.addComponent(ordersPanel);
		tabsheet.addTab(orderPage).setCaption("Orders");

        setContent(layout);
    }
}