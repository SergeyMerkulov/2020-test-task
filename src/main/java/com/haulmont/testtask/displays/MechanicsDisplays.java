package com.haulmont.testtask.displays;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import com.haulmont.testtask.models.Mechanic;
import com.haulmont.testtask.managers.MechanicsManager;

public class MechanicsDisplays {
	
	private Mechanic mechanic;
	private UI ui;
	private Panel mechanicsPanel;
	
	public MechanicsDisplays() {
		
	}

	public MechanicsDisplays( UI ui, Panel mechanicsPanel ) {
		this.ui = ui;
		this.mechanicsPanel = mechanicsPanel;
	}
	
	private final int EXPAND_RATIO = 1;
	private final int COLLUMNS_NUMBER = 7;
	private final int HEADERS_NUMBER = 2;
	private final int MAIN_HEADER_ROW = 0;
	private final int BUTTON_HEADER_ROW = 1;
	private final int FIRST_NAME_COL = 0;
	private final int SECOND_NAME_COL = 1;
	private final int THIRD_NAME_COL = 2;
	private final int HOURLY_SALARY_COL = 3;
	private final int EDIT_COL = 4;
	private final int DELETE_COL = 5;
	private final int STATISTIC_COL = 6;
	
	public GridLayout createMechanicsPage() {
		
		GridLayout mechanicPage = new GridLayout(COLLUMNS_NUMBER, MechanicsManager.getNumber()+HEADERS_NUMBER);
		
		mechanicPage.addComponent(new Label("First Name"), FIRST_NAME_COL, MAIN_HEADER_ROW);
		mechanicPage.addComponent(new Label("Second Name"), SECOND_NAME_COL, MAIN_HEADER_ROW);
		mechanicPage.addComponent(new Label("Third Name"), THIRD_NAME_COL, MAIN_HEADER_ROW);
		mechanicPage.addComponent(new Label("Hourly Salary"), HOURLY_SALARY_COL, MAIN_HEADER_ROW);
		mechanicPage.addComponent(new Label("Edit"), EDIT_COL, MAIN_HEADER_ROW);
		mechanicPage.addComponent(new Label("Delete"), DELETE_COL, MAIN_HEADER_ROW);
		mechanicPage.addComponent(new Label("Show statistic"), STATISTIC_COL, MAIN_HEADER_ROW);
		
		mechanicPage.addComponent(new Button("add", event -> { ui.addWindow(createAddMechanicWindow()); }	), FIRST_NAME_COL, BUTTON_HEADER_ROW);
		
		int i = 1;
		for(Mechanic mechanic : MechanicsManager.getMechanics()) {
			i++;
			mechanicPage.addComponent(new Label(mechanic.getFirstName()), FIRST_NAME_COL, i);
			mechanicPage.addComponent(new Label(mechanic.getSecondName()), SECOND_NAME_COL, i);
			mechanicPage.addComponent(new Label(mechanic.getThirdName()), THIRD_NAME_COL, i);
			mechanicPage.addComponent(new Label(" "+mechanic.getHourlySalary()), HOURLY_SALARY_COL, i);
			mechanicPage.addComponent(new Button("edit", event -> {
				this.mechanic = mechanic;
				ui.addWindow(createEditMechanicWindow());
			} ), EDIT_COL, i);
			mechanicPage.addComponent(new Button("delete", event -> {
				MechanicsManager.delete(mechanic.getId());
				mechanicsPanel.setContent(createMechanicsPage());
			} ), DELETE_COL, i);
			mechanicPage.addComponent(new Button("show statistic", event -> {
				this.mechanic = mechanic;
				ui.addWindow(showMechanicStatistic());
			} ), STATISTIC_COL, i);
		}
		
		mechanicPage.setWidth("100%");
		mechanicPage.setColumnExpandRatio(FIRST_NAME_COL, EXPAND_RATIO);
		mechanicPage.setColumnExpandRatio(SECOND_NAME_COL, EXPAND_RATIO);
		mechanicPage.setColumnExpandRatio(THIRD_NAME_COL, EXPAND_RATIO);
		mechanicPage.setColumnExpandRatio(HOURLY_SALARY_COL, EXPAND_RATIO);
		mechanicPage.setColumnExpandRatio(EDIT_COL, EXPAND_RATIO);
		mechanicPage.setColumnExpandRatio(DELETE_COL, EXPAND_RATIO);
		mechanicPage.setColumnExpandRatio(STATISTIC_COL, EXPAND_RATIO);
		
		return mechanicPage;
	}
	
	public Window createAddMechanicWindow() {
		
		Window window = new Window("Add mechanic");
		
		FormLayout layout = new FormLayout();
		window.setContent( layout );
		
		TextField firstNameField = new TextField("First name: ");
		TextField secondNameField = new TextField("Second name: ");
		TextField thirdNameField = new TextField("Third name: ");
		TextField hourlySalaryField = new TextField("Hourly salary: ");
		Button button = new Button("Add");
		
		layout.addComponent(firstNameField);
		layout.addComponent(secondNameField);
		layout.addComponent(thirdNameField);
		layout.addComponent(hourlySalaryField);
		layout.addComponent(button);
		
		window.setWidth("40%");
        window.center();
		window.setModal(true);
		
		button.addClickListener(click -> {
			
			if(firstNameField.isEmpty()){Notification.show("First name field is empty");return;}
			if(secondNameField.isEmpty()){Notification.show("Second name field is empty");return;}
			if(thirdNameField.isEmpty()){Notification.show("Third name field is empty");return;}
			if(hourlySalaryField.isEmpty()){Notification.show("Hourly salary field is empty");return;}
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
			if(hourlySalaryField.getValue().matches(".*[^0-9].*")) {
				Notification.show("The hourly salary field has to contain a positive number");
				return;
			}
			
			Mechanic mechanic = new Mechanic();
			mechanic.setFirstName(firstNameField.getValue());
			mechanic.setSecondName(secondNameField.getValue());
			mechanic.setThirdName(thirdNameField.getValue());
			mechanic.setHourlySalary( Integer.parseInt( hourlySalaryField.getValue() ) );
			MechanicsManager.add( mechanic );
			window.close();
			mechanicsPanel.setContent(createMechanicsPage());
		});
		return window;
	}
	
	public Window createEditMechanicWindow() {
		
		Window window = new Window("Edit mechanic");

		FormLayout layout = new FormLayout();
		window.setContent( layout );
		
		TextField firstNameField = new TextField("First Name: ");
		TextField secondNameField = new TextField("Second Name: ");
		TextField thirdNameField = new TextField("Third Name: ");
		TextField hourlySalaryField = new TextField("Hourly salary: ");
		Button okButton = new Button("Ok");
		Button cancelButton = new Button("Cancel");
		
		firstNameField.setValue( this.mechanic.getFirstName() );
		secondNameField.setValue( this.mechanic.getSecondName() );
		thirdNameField.setValue( this.mechanic.getThirdName() );
		hourlySalaryField.setValue( String.valueOf( this.mechanic.getHourlySalary() ) );
		
		layout.addComponent( firstNameField );
		layout.addComponent( secondNameField );
		layout.addComponent( thirdNameField );
		layout.addComponent( hourlySalaryField );
		layout.addComponent( okButton );
		layout.addComponent( cancelButton );
		
		window.setWidth("40%");
        window.center();
		window.setModal(true);
		
		okButton.addClickListener(click -> {
			
			if(firstNameField.isEmpty()){Notification.show("First name field is empty");return;}
			if(secondNameField.isEmpty()){Notification.show("Second name field is empty");return;}
			if(thirdNameField.isEmpty()){Notification.show("Third name field is empty");return;}
			if(hourlySalaryField.isEmpty()){Notification.show("Phone field is empty");return;}
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
			if(hourlySalaryField.getValue().matches(".*[^0-9].*")) {
				Notification.show("The hourly salary field has to contain a positive number");
				return;
			}

			this.mechanic.setFirstName(firstNameField.getValue());
			this.mechanic.setSecondName(secondNameField.getValue());
			this.mechanic.setThirdName(thirdNameField.getValue());
			this.mechanic.setHourlySalary( Integer.parseInt( hourlySalaryField.getValue() ) );
			MechanicsManager.update( this.mechanic );
			window.close();
			mechanicsPanel.setContent(createMechanicsPage());
		});
		
		cancelButton.addClickListener(click -> {
			Notification.show("The mechanic is not updated");
			window.close();
		});
		return window;
	}
	
	public Window showMechanicStatistic() {
		
		Window window = new Window("Statistic of "+ this.mechanic.getShortDescription() );
		
		FormLayout layout = new FormLayout();
		window.setContent( layout );

		layout.addComponent( new Label( MechanicsManager.getStatistic( this.mechanic.getId() ), Label.CONTENT_PREFORMATTED ) );
		layout.addComponent( new Button("close", event -> { window.close(); }) );
		
		window.setWidth("40%");
        window.center();
		window.setModal(true);
	
		return window;
	}
}