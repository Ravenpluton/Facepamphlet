/*
 * File: FacePamphletCanvas.java
 * -----------------------------
 * This class represents the canvas on which the profiles in the social
 * network are displayed.  NOTE: This class does NOT need to update the
 * display when the window is resized.
 */

import acm.graphics.*;
import acm.util.ErrorException;

import java.awt.*;
import java.util.*;
import java.awt.event.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;

public class FacePamphletCanvas2 extends GCanvas implements FacePamphletConstants2, ComponentListener {

	private GLabel message = new GLabel("");
	private double nameY;
	private double imageY;
	private double genderY;
	private double occupationY;
	private double leftMargin = LEFT_MARGIN;
	private FacePamphletProfile2 lastProfile;
	private String lastMsg;
	private GImage background;
	private GLabel genderLabel, occupationLabel;
	private static final Color darkBlue = new Color(0, 0, 153);
	private boolean b = false;
	private ArrayList<String> pictures;
	private ArrayList<String> names;

	/**
	 * Constructor This method takes care of any initialization needed for the
	 * display
	 */
	public FacePamphletCanvas2() {
		addBackground();
		addComponentListener(this);
		
		lastProfile = null;
		lastMsg = "";

	}

	// adds welcome label at top of the canvas
	private void addWelcomeLabel() {
		GLabel welcomeLabel = new GLabel("Welcome to GotCon! connect to everyone in Gotham city");
		welcomeLabel.setFont(WELCOME_FONT);
		welcomeLabel.setColor(darkBlue);
		;
		double x = (getWidth() - welcomeLabel.getWidth()) / 2;
		double y = welcomeLabel.getHeight();
		add(welcomeLabel, x, y);
	}

	// adds background picture
	public void addBackground() {
		background = new GImage("background.jpg");
		background.setSize(getWidth(), getHeight());
		add(background);
		addWelcomeLabel();
	}

	/**
	 * This method displays a message string near the bottom of the canvas. Every
	 * time this method is called, the previously displayed message (if any) is
	 * replaced by the new message text passed in.
	 */
	public void showMessage(String msg) {
		lastMsg = msg;
		message.setLabel(msg);
		double x = (getWidth() - message.getWidth()) / 2;
		double y = getHeight() - BOTTOM_MESSAGE_MARGIN;
		message.setFont(MESSAGE_FONT);
		add(message, x, y);

	}

	/**
	 * This method displays the given profile on the canvas. The canvas is first
	 * cleared of all existing items (including messages displayed near the bottom
	 * of the screen) and then the given profile is displayed. The profile display
	 * includes the name of the user from the profile, the corresponding image (or
	 * an indication that an image does not exist), the status of the user, and a
	 * list of the user's friends in the social network.
	 */
	public void displayProfile(FacePamphletProfile2 profile) {
		removeAll();
		addBackground();
		lastProfile = profile;
		addName(profile.getName(), profile.getAge());
		addImage(profile.getImage());
		addGender(profile.getGender());
		addOccupation(profile.getOccupation());
		addStatus(profile.getStatus(), profile.getName());
		addFriends(profile.getFriends());
	}

	// adds label for gender
	private void addGender(String gender) {
		genderY = imageY + IMAGE_HEIGHT + GENDER_MARGIN;
		genderLabel = new GLabel("Gender: " + gender);
		// genderLabel.setLabel("Gender: " + gender);
		add(genderLabel, leftMargin, genderY);
	}

	// adds occupation label
	private void addOccupation(String occupation) {
		occupationY = genderY + genderLabel.getHeight() + OCCUPATION_MARGIN;
		occupationLabel = new GLabel("Occupation: ");
		if (occupation != null) {
			occupationLabel.setLabel("Occupation: " + occupation);
		}
		add(occupationLabel, leftMargin, occupationY);
	}

	// add friend lists on the canvas
	private void addFriends(Iterator<String> friends) {
		GLabel friendLabel = new GLabel("Friends");
		double x = getWidth() / 2;
		double y = imageY + friendLabel.getHeight();
		friendLabel.setFont(PROFILE_FRIEND_LABEL_FONT);
		add(friendLabel, x, y);

		int n = 1;
		while (friends.hasNext()) {
			GLabel friendName = new GLabel(friends.next());
			double friendNameY = y + n * friendName.getHeight();
			n++;
			if (friendNameY > getHeight() - BOTTOM_MESSAGE_MARGIN - message.getAscent()) {
				break;
			}
			add(friendName, x, friendNameY);
		}

	}

	// adds status label on the canvas
	private void addStatus(String status, String name) {
		double statusY = occupationY + occupationLabel.getHeight() + STATUS_MARGIN;
		if (status.equals("")) {
			GLabel noStatus = new GLabel("No current status");
			noStatus.setFont(PROFILE_STATUS_FONT);
			double labelY = statusY;
			add(noStatus, leftMargin, labelY);
		} else {
			GLabel newStatus = new GLabel(name + " is " + status);
			newStatus.setFont(PROFILE_STATUS_FONT);
			double labelY = statusY;
			add(newStatus, leftMargin, labelY);
		}
	}

	// adds image on the canvas or rect and no image label if image does not exist
	private void addImage(GImage image) {
		imageY = nameY + IMAGE_MARGIN;
		if (image == null) {
			GRect rect = new GRect(IMAGE_WIDTH, IMAGE_HEIGHT);
			add(rect, leftMargin, imageY);

			GLabel noImage = new GLabel("No Image");
			noImage.setFont(PROFILE_IMAGE_FONT);
			double labelX = leftMargin + (rect.getWidth() - noImage.getWidth()) / 2;
			double labelY = imageY + (rect.getHeight() + noImage.getAscent()) / 2;

			add(noImage, labelX, labelY);
		} else {
			image.setSize(IMAGE_WIDTH, IMAGE_HEIGHT);
			add(image, leftMargin, imageY);
		}
	}

	// adds name label on the canvas
	private void addName(String name, int age) {
		GLabel label = new GLabel(name);
		label.setColor(Color.BLUE);
		nameY = TOP_MARGIN + label.getHeight() + 5;
		label.setFont(PROFILE_NAME_FONT);
		add(label, leftMargin, nameY);

		if (age != 0) {
			label.setLabel(name + " (" + age + ")");
		}

	}

	// updates canvas after it is resized
	private void update() {
		if (b) {
			displayCriminals();
		} else {
			addBackground();
			displayProfile(lastProfile);
			showMessage(lastMsg);
		}
	}

	// displays wanted criminals of gothem
	public void displayCriminals() {
		b = true;
		removeAll();
		addBackground();
		
		pictures = new ArrayList<String>();
		names = new ArrayList<String>();
		readFile();
		
		double criminalImigeWidth = getWidth() / 5.5;
		double criminalImigeHeight = getHeight() / 3;
		
		drawPictures(criminalImigeWidth, criminalImigeHeight);
		addLabels(criminalImigeWidth, criminalImigeHeight);
	}

	// adds criminals' names
	private void addLabels(double criminalImigeWidth, double criminalImigeHeight) {
		for (int i = 0; i < names.size(); i++) {
			GLabel label = new GLabel(names.get(i));
			double x = leftMargin + i * (criminalImigeWidth + 10);
			double y = PICTURES_TOP_MARGIN + criminalImigeHeight + label.getHeight();
			label.setFont("Dialog-18");
			add(label, x, y);
		}
	}

	// adds criminals' pictures
	private void drawPictures(double criminalImigeWidth, double criminalImigeHeight) {
			for (int i = 0; i < pictures.size(); i++) {
				GImage image = new GImage(pictures.get(i));
				double x = leftMargin + i * ((criminalImigeWidth + 10));
				double y = PICTURES_TOP_MARGIN;
				image.setSize(criminalImigeWidth, criminalImigeHeight);
				add(image, x, y);
		}
		
	}

	// reads file where criminals names and pictures are written
	private void readFile() {
		try {
			BufferedReader rd = new BufferedReader(new FileReader(CRIMINALS_FILE));
			while (true) {
				String line = rd.readLine();
				if (line == null)
					break;
				getLists(line);
			}
			rd.close();
		} catch (IOException ex) {
			throw new ErrorException(ex);
		}
	}
	
	// creates lists for criminals names and pictures separately
	private void getLists(String line) {
		int indexOfSpace = line.indexOf(' ');
		String picture = line.substring(0, indexOfSpace);
		String name = line.substring(indexOfSpace + 1);
		pictures.add(picture);
		names.add(name);
	}

	/* Implementation of the ComponentListener interface */
	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentResized(ComponentEvent e) {
		update();
	}

	public void componentShown(ComponentEvent e) {
	}
}
