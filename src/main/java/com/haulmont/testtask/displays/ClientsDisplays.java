package com.haulmont.testtask.displays;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import com.haulmont.testtask.models.Client;
import com.haulmont.testtask.managers.ClientsManager;

public class ClientsDisplays {
	
	private Client client;
	private UI ui;
	private Panel clientsPanel;
	private NativeSelect clientsSelect;
	
	public ClientsDisplays() {
		
	}

	public ClientsDisplays( UI ui, Panel clientsPanel, NativeSelect clientsSelect ) {
		this.ui = ui;
		this.clientsPanel = clientsPanel;
		this.clientsSelect = clientsSelect;
	}
	
	private final int EXPAND_RATIO = 1;
	private final int COLLUMNS_NUMBER = 6;
	private final int HEADERS_NUMBER = 2;
	private final int MAIN_HEADER_ROW = 0;
	private final int BUTTON_HEADER_ROW = 1;
	private final int FIRST_NAME_COL = 0;
	private final int SECOND_NAME_COL = 1;
	private final int THIRD_NAME_COL = 2;
	private final int PHONE_COL = 3;
	private final int EDIT_COL = 4;
	private final int DELETE_COL = 5;
	
	public GridLayout createClientsPage() {
		
		GridLayout clientsPage = new GridLayout(COLLUMNS_NUMBER, ClientsManager.getNumber()+HEADERS_NUMBER);
		
		clientsPage.addComponent(new Label("First Name"), FIRST_NAME_COL, MAIN_HEADER_ROW);
		clientsPage.addComponent(new Label("Second Name"), SECOND_NAME_COL, MAIN_HEADER_ROW);
		clientsPage.addComponent(new Label("Third Name"), THIRD_NAME_COL, MAIN_HEADER_ROW);
		clientsPage.addComponent(new Label("Phone"), PHONE_COL, MAIN_HEADER_ROW);
		clientsPage.addComponent(new Label("Edit"), EDIT_COL, MAIN_HEADER_ROW);
		clientsPage.addComponent(new Label("Delete"), DELETE_COL, MAIN_HEADER_ROW);
		
		clientsPage.addComponent(new Button("add", event -> { ui.addWindow(createAddClientWindow()); }	), FIRST_NAME_COL, BUTTON_HEADER_ROW);
		
		int i = 1;
		for(Client client : ClientsManager.getClients()) {

			i++;
			clientsPage.addComponent(new Label(client.getFirstName()), FIRST_NAME_COL, i);
			clientsPage.addComponent(new Label(client.getSecondName()), SECOND_NAME_COL, i);
			clientsPage.addComponent(new Label(client.getThirdName()), THIRD_NAME_COL, i);
			clientsPage.addComponent(new Label(client.getPhone()), PHONE_COL, i);
			clientsPage.addComponent(new Button("edit", event -> {
				this.client = client;
				ui.addWindow(createEditClientWindow());
			} ), EDIT_COL, i );
			clientsPage.addComponent(new Button("delete", event -> {
				ClientsManager.delete(client.getId());
				clientsPanel.setContent(createClientsPage());
				updateSelectClientForm();
			} ), DELETE_COL, i );
		}
		
		clientsPage.setWidth("100%");
		clientsPage.setColumnExpandRatio(FIRST_NAME_COL, EXPAND_RATIO);
		clientsPage.setColumnExpandRatio(SECOND_NAME_COL, EXPAND_RATIO);
		clientsPage.setColumnExpandRatio(THIRD_NAME_COL, EXPAND_RATIO);
		clientsPage.setColumnExpandRatio(PHONE_COL, EXPAND_RATIO);
		clientsPage.setColumnExpandRatio(EDIT_COL, EXPAND_RATIO);
		clientsPage.setColumnExpandRatio(DELETE_COL, EXPAND_RATIO);
		
		return clientsPage;
	}
	
	private final int TEXT_MAX_LENGTH = 100;
	
	public Window createAddClientWindow() {
		
		Window window = new Window("Add client");
		
		FormLayout layout = new FormLayout();
		window.setContent( layout );
		
		TextField firstNameField = new TextField("First Name: ");
		TextField secondNameField = new TextField("Second Name: ");
		TextField thirdNameField = new TextField("Third Name: ");
		TextField phoneField = new TextField("Phone: ");
		Button button = new Button("Add");
		
		firstNameField.setMaxLength(TEXT_MAX_LENGTH);
		secondNameField.setMaxLength(TEXT_MAX_LENGTH);
		thirdNameField.setMaxLength(TEXT_MAX_LENGTH);
		phoneField.setMaxLength(TEXT_MAX_LENGTH);
		
		layout.addComponent(firstNameField);
		layout.addComponent(secondNameField);
		layout.addComponent(thirdNameField);
		layout.addComponent(phoneField);
		layout.addComponent(button);
		
		window.setWidth("40%");
        window.center();
		window.setModal(true);
		
		button.addClickListener(click -> {
			
			if(firstNameField.isEmpty()){Notification.show("First name field is empty");return;}
			if(secondNameField.isEmpty()){Notification.show("Second name field is empty");return;}
			if(thirdNameField.isEmpty()){Notification.show("Third name field is empty");return;}
			if(phoneField.isEmpty()){Notification.show("Phone field is empty");return;}
			if(firstNameField.getValue().matches(".*[^a-z^A-Z^а-я^А-Я].*")) {
				Notification.show("The first name field has to contain only letters");
				return;
			}
			if(secondNameField.getValue().matches(".*[^a-z^A-Z^а-я^А-Я].*")) {
				Notification.show("The second name field has to contain only letters");
				return;
			}
			if(thirdNameField.getValue().matches(".*[^a-z^A-Z^а-я^А-Я].*")) {
				Notification.show("The third name field has to contain only letters");
				return;
			}
			if(phoneField.getValue().matches(".*[a-zA-Zа-яА-Я].*")) {
				Notification.show("The phone number cannot to contain letters");
				return;
			}
			
			Client client = new Client();
			client.setFirstName(firstNameField.getValue());
			client.setSecondName(secondNameField.getValue());
			client.setThirdName(thirdNameField.getValue());
			client.setPhone(phoneField.getValue());
			ClientsManager.add( client );
			Notification.show("The client is added!");
			window.close();
			clientsPanel.setContent(createClientsPage());
			updateSelectClientForm();
		});
		return window;
	}
	
	public Window createEditClientWindow() {
		
		Window window = new Window("Edit client");

		FormLayout layout = new FormLayout();
		window.setContent( layout );
		
		TextField firstNameField = new TextField("First Name: ");
		TextField secondNameField = new TextField("Second Name: ");
		TextField thirdNameField = new TextField("Third Name: ");
		TextField phoneField = new TextField("Phone: ");
		Button okButton = new Button("Ok");
		Button cancelButton = new Button("Cancel");
		
		firstNameField.setValue( this.client.getFirstName() );
		secondNameField.setValue( this.client.getSecondName() );
		thirdNameField.setValue( this.client.getThirdName() );
		phoneField.setValue( this.client.getPhone() );
		
		firstNameField.setMaxLength(TEXT_MAX_LENGTH);
		secondNameField.setMaxLength(TEXT_MAX_LENGTH);
		thirdNameField.setMaxLength(TEXT_MAX_LENGTH);
		phoneField.setMaxLength(TEXT_MAX_LENGTH);
		
		layout.addComponent( firstNameField );
		layout.addComponent( secondNameField );
		layout.addComponent( thirdNameField );
		layout.addComponent( phoneField );
		layout.addComponent( okButton );
		layout.addComponent( cancelButton );
		
		window.setWidth("40%");
        window.center();
		window.setModal(true);
		
		okButton.addClickListener(click -> {
			
			if(firstNameField.isEmpty()){Notification.show("First name field is empty");return;}
			if(secondNameField.isEmpty()){Notification.show("Second name field is empty");return;}
			if(thirdNameField.isEmpty()){Notification.show("Third name field is empty");return;}
			if(phoneField.isEmpty()){Notification.show("Phone field is empty");return;}
			if(firstNameField.getValue().matches(".*[^a-z^A-Z^а-я^А-Я].*")) {
				Notification.show("The first name field has to contain only letters");
				return;
			}
			if(secondNameField.getValue().matches(".*[^a-z^A-Z^а-я^А-Я].*")) {
				Notification.show("The second name field has to contain only letters");
				return;
			}
			if(thirdNameField.getValue().matches(".*[^a-z^A-Z^а-я^А-Я].*")) {
				Notification.show("The third name field has to contain only letters");
				return;
			}
			if(phoneField.getValue().matches(".*[a-zA-Zа-яА-Я].*")) {
				Notification.show("The phone number cannot to contain letters");
				return;
			}

			this.client.setFirstName(firstNameField.getValue());
			this.client.setSecondName(secondNameField.getValue());
			this.client.setThirdName(thirdNameField.getValue());
			this.client.setPhone(phoneField.getValue());
			ClientsManager.update( this.client );
			Notification.show("The client is updated");
			window.close();
			clientsPanel.setContent(createClientsPage());
			updateSelectClientForm();
		});
		
		cancelButton.addClickListener(click -> {
			Notification.show("The client is not updated");
			window.close();
		});
		return window;
	}
	
	public void updateSelectClientForm() {
		
		clientsSelect.removeAllItems();
		
		for(Client client : ClientsManager.getClients()) {
			String clientId = String.valueOf( client.getId() );
			clientsSelect.addItem( clientId );
			clientsSelect.setItemCaption( clientId, client.getShortDescription() );
		}
		clientsSelect.addItem( "everybody" );
		clientsSelect.setItemCaption( "everybody", "everybody" );
		clientsSelect.setValue( "everybody" );
	}
}