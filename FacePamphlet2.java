/* 
 * File: FacePamphlet.java
 * -----------------------
 * When it is finished, this program will implement a basic social network
 * management system.
 */

import acm.program.*;
import acm.graphics.*;
import acm.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class FacePamphlet2 extends Program implements FacePamphletConstants2 {

	private JTextField nameTextField, textFieldOne, textFieldTwo, textFieldThree, textFieldOccupation;
	private FacePamphletDatabase2 profileDatabase;
	private FacePamphletProfile2 profile;
	private FacePamphletProfile2 currentProfile;
	private FacePamphletCanvas2 canvas;
	private JComboBox<String> gender;
	private JComboBox<Integer> age;
	private JButton batSignal, policeSignal;

	/**
	 * This method has the responsibility for initializing the interactors in the
	 * application, and taking care of any other initialization that needs to be
	 * performed.
	 */
	public void init() {
		northinitialization();
		westInitialization();
		eastInitialization();
		addActionListeners();
		  
		profileDatabase = new FacePamphletDatabase2();

		canvas = new FacePamphletCanvas2();
		add(canvas);
	}

	// initializes east side
	private void eastInitialization() {
		addBatSignal();
		
		addPoliceButton();
	}

	// adds batman logo button on east side
	private void addBatSignal() {
		Icon icon = new ImageIcon("batmanlogo.png");
		batSignal = new JButton(icon);
		add(batSignal, EAST);
	}

	// adds gotham city police logo button on east side
	private void addPoliceButton() {
		Icon icon = new ImageIcon("police.png");
		policeSignal = new JButton(icon);
		add(policeSignal, EAST);
	}
	

	/*
	 * Initializes west side of the window. adds text fields and buttons for change
	 * status, change picture and add friend
	 */
	private void westInitialization() {
		addGender();
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);

		addAge();
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);

		addOcupation();

		textFieldOne = new JTextField(TEXT_FIELD_SIZE);
		add(textFieldOne, WEST);
		textFieldOne.addActionListener(this);
		add(new JButton("Change Status"), WEST);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);

		textFieldTwo = new JTextField(TEXT_FIELD_SIZE);
		add(textFieldTwo, WEST);
		textFieldTwo.addActionListener(this);
		add(new JButton("Change Picture"), WEST);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);

		textFieldThree = new JTextField(TEXT_FIELD_SIZE);
		add(textFieldThree, WEST);
		textFieldThree.addActionListener(this);
		add(new JButton("Add Friend"), WEST);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);

	}

	// adds occupation text field and button
	private void addOcupation() {
		textFieldOccupation = new JTextField(TEXT_FIELD_SIZE);
		add(textFieldOccupation, WEST);
		textFieldOccupation.addActionListener(this);
		add(new JButton("Add Occupation"), WEST);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);
	}

	// adds choose age button and combo box
	private void addAge() {
		age = new JComboBox<Integer>();
		for (int i = 13; i <= 100; i++) {
			age.addItem(i);
		}
		add(age, WEST);
		age.addActionListener(this);
	}

	// adds choose gender button and combo box
	private void addGender() {

		gender = new JComboBox<String>();
		gender.addItem("Choose gender");
		gender.addItem("Male");
		gender.addItem("Female");
		gender.addItem("Non binary");
		gender.setEditable(true);
		add(gender, WEST);
		gender.addActionListener(this);
	}

	/*
	 * Initializes north side of the window. add Name label, text field for name and
	 * buttons
	 */
	private void northinitialization() {
		JLabel Name = new JLabel("Name");
		add(Name, NORTH);

		nameTextField = new JTextField(TEXT_FIELD_SIZE);
		add(nameTextField, NORTH);

		addButtons();
	}

	// Adds buttons: add, delete and lookup for the north side initialization
	private void addButtons() {
		add(new JButton("Add"), NORTH);
		add(new JButton("Delete"), NORTH);
		add(new JButton("Lookup"), NORTH);
	}

	/**
	 * This class is responsible for detecting when the buttons are clicked or
	 * interactors are used, so you will have to add code to respond to these
	 * actions.
	 */
	public void actionPerformed(ActionEvent e) {
		if (!nameTextField.getText().equals("")) {
			nameFieldCase(e);

		}

		if (!textFieldOne.getText().equals("")) {
			if (e.getSource().equals(textFieldOne) || e.getActionCommand().equals("Change Status")) {
				changeStatusCase();
			}
		}

		if (!textFieldTwo.getText().equals("")) {
			if (e.getSource().equals(textFieldTwo) || e.getActionCommand().equals("Change Picture")) {
				changePictureCase();
			}
		}

		if (!textFieldThree.getText().equals("")) {
			if (e.getSource().equals(textFieldThree) || e.getActionCommand().equals("Add Friend")) {
				addFriendCase();
			}
		}

		if (!textFieldOccupation.getText().equals("")) {
			if (e.getSource().equals(textFieldOccupation) || e.getActionCommand().equals("Add Occupation")) {
				addOccupationCase();
			}
		}

		if (e.getSource().equals(gender)) {
			chooseGenderCase();
		}

		if (e.getSource().equals(age)) {
			chooseAgeCase();
		}
		
		if (e.getSource().equals(batSignal)) {
			batSignalCase();
		}
		
		if (e.getSource().equals(policeSignal)) {
			canvas.displayCriminals();
			canvas.showMessage("Displaying most wanted criminals of gothem");
		}
	}

	// this method happens if bat signal button is clicked
	private void batSignalCase() {
		if (currentProfile != null) {
			currentProfile.setStatus("in danger");
			canvas.displayProfile(currentProfile);
			canvas.showMessage(currentProfile.getName() + " has called bat signal. They need help");
		} else {
			canvas.showMessage("Please select a profile to call bat signal");
		}
	}
	
	// this method happens if occupation button is clicked
	private void addOccupationCase() {
		if (currentProfile != null) {
			currentProfile.setOccupation(textFieldOccupation.getText());
			canvas.displayProfile(currentProfile);
			canvas.showMessage("Occupation updated to " + currentProfile.getOccupation());
		} else {
			canvas.showMessage("Please select a profile to add occupation");
		}
		textFieldOccupation.setText("");
	}

	/*
	 *
	 * This method is what happens when user chooses age. It checks if current
	 * profile is selected and changes age.
	 *
	 */
	private void chooseAgeCase() {
		if (currentProfile != null) {
			int ageInt = (int) age.getSelectedItem();
			currentProfile.setAge(ageInt);
			canvas.displayProfile(currentProfile);
			canvas.showMessage("Age updated");
		} else {
			canvas.showMessage("Please select a profile to change age");
		}
	}

	/*
	 *
	 * This method is what happens when user chooses gender. It checks if current
	 * profile is selected, if string is actually entered and changes gender after
	 * that.
	 *
	 */
	private void chooseGenderCase() {
		if (currentProfile != null) {
			String genderString = (String) gender.getSelectedItem();
			if (!genderString.equals("Choose gender") && !genderString.equals("")) {
				currentProfile.setGender(genderString);
				canvas.displayProfile(currentProfile);
				canvas.showMessage("Gender updated");
			}
		} else {
			canvas.showMessage("Please select a profile to change gender");
		}
	}

	/*
	 * This method is what happens when add friend button is clicked. It checks if
	 * current profile is selected, if entered name exists in database and if this
	 * name is already a friend or not.
	 */
	private void addFriendCase() {
		if (currentProfile != null) {
			if (profileDatabase.containsProfile(textFieldThree.getText())) {
				if (currentProfile.addFriend(textFieldThree.getText())) {
					profileDatabase.getProfile(textFieldThree.getText()).addFriend(currentProfile.getName());
					canvas.displayProfile(currentProfile);
					canvas.showMessage(textFieldThree.getText() + " added as a friend");
				} else {
					canvas.showMessage(
							currentProfile.getName() + " already has " + textFieldThree.getText() + " as a friend");
				}
			} else {
				canvas.showMessage(textFieldThree.getText() + " does not exist");
			}
		} else {
			canvas.showMessage("Please select a profile to add as a friend");
		}
		textFieldThree.setText("");
	}

	/*
	 * This method is what happens when user wants to change profile picture. At
	 * first it checks if current profile is selected, then if image can be opened
	 * and then if image is null or not.
	 * 
	 */
	private void changePictureCase() {
		if (currentProfile != null) {
			GImage image = null;
			try {
				image = new GImage(textFieldTwo.getText());
			} catch (ErrorException ex) {
				canvas.showMessage("Unable to open image file: " + textFieldTwo.getText());
			}

			if (image == null) {
				canvas.showMessage("Picture can not be changed");
			} else {
				currentProfile.setImage(image);
				canvas.displayProfile(currentProfile);
				canvas.showMessage("Picture updated");
			}
		} else {
			canvas.showMessage("Please select a profile to change picture");
		}
		textFieldTwo.setText("");
	}

	/*
	 * This method is what happens when user wants to change status. It checks if
	 * current profile is selected and updates status after that.
	 * 
	 */
	private void changeStatusCase() {
		if (currentProfile != null) {
			currentProfile.setStatus(textFieldOne.getText());
			canvas.displayProfile(currentProfile);
			canvas.showMessage("Status updated to " + currentProfile.getStatus());
		} else {
			canvas.showMessage("Please select a profile to change status");
		}
		textFieldOne.setText("");
	}

	/*
	 * This method is what happens when name text field is not empty and either add,
	 * delete or lookup button is selected. it creates String name, checks if
	 * profile exists in database and creates new facePamphletProfile object.
	 * 
	 */
	private void nameFieldCase(ActionEvent e) {
		String name = nameTextField.getText();

		boolean b = profileDatabase.containsProfile(name);

		profile = new FacePamphletProfile2(name);

		if (e.getActionCommand().equals("Add")) {
			addCase(name, b);
		} else if (e.getActionCommand().equals("Delete")) {
			deleteCase(name, b);
		} else if (e.getActionCommand().equals("Lookup")) {
			lookupCase(name, b);
		}
	}

	/*
	 * Method which happens when lookup is clicked. This method sets current profile
	 * and updates canvas in both cases when name exists in database and when it
	 * does not.
	 * 
	 */
	private void lookupCase(String name, boolean b) {
		if (b) {
			currentProfile = profileDatabase.getProfile(name);
			canvas.displayProfile(currentProfile);
			canvas.showMessage("Displaying " + name);
		} else {
			currentProfile = null;
			canvas.removeAll();
			canvas.addBackground();
			canvas.showMessage("A profile with the name " + name + " does not exist");
		}
	}

	/*
	 * Method which happens when delete is clicked. This method sets current profile
	 * and updates canvas in both cases when name exists in database and when it
	 * does not.
	 * 
	 */
	private void deleteCase(String name, boolean b) {
		if (b) {
			profileDatabase.deleteProfile(name);
			currentProfile = null;
			canvas.removeAll();
			canvas.addBackground();
			canvas.showMessage("Profile of " + name + " deleted");
		} else {
			currentProfile = null;
			canvas.removeAll();
			canvas.addBackground();
			canvas.showMessage("A profile with the name " + name + " does not exist");
		}
	}

	/*
	 * Method which happens when add is clicked. This method sets current profile
	 * and updates canvas in both cases when name exists in database and when it
	 * does not.
	 * 
	 */
	private void addCase(String name, boolean b) {
		if (b) {
			currentProfile = profileDatabase.getProfile(name);
			canvas.displayProfile(currentProfile);
			canvas.showMessage("A profile with the name " + name + " already exists");
		} else {
			profileDatabase.addProfile(profile);
			currentProfile = profile;
			canvas.displayProfile(currentProfile);
			canvas.showMessage("New profile created");
		}
	}
}
