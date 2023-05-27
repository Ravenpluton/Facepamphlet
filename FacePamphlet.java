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

public class FacePamphlet extends Program implements FacePamphletConstants {

	private JTextField nameTextField, textFieldOne, textFieldTwo, textFieldThree;
	private FacePamphletDatabase profileDatabase;
	private FacePamphletProfile profile;
	private FacePamphletProfile currentProfile;
	private FacePamphletCanvas canvas;

	/**
	 * This method has the responsibility for initializing the interactors in the
	 * application, and taking care of any other initialization that needs to be
	 * performed.
	 */
	public void init() {
		northinitialization();
		westInitialization();

		addActionListeners();

		profileDatabase = new FacePamphletDatabase();

		canvas = new FacePamphletCanvas();
		add(canvas);
	}

	/*
	 * Initializes west side of the window. adds text fields and buttons for change
	 * status, change picture and add firend
	 */
	private void westInitialization() {
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

		profile = new FacePamphletProfile(name);

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
			canvas.showMessage("Profile of " + name + " deleted");
		} else {
			currentProfile = null;
			canvas.removeAll();
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
