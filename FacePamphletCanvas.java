/*
 * File: FacePamphletCanvas.java
 * -----------------------------
 * This class represents the canvas on which the profiles in the social
 * network are displayed.  NOTE: This class does NOT need to update the
 * display when the window is resized.
 */

import acm.graphics.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.util.*;

public class FacePamphletCanvas extends GCanvas implements FacePamphletConstants {

	private GLabel message = new GLabel("");
	private double nameY;
	private double imageY;
	private double leftMargin = LEFT_MARGIN;

	/**
	 * Constructor This method takes care of any initialization needed for the
	 * display
	 */
	public FacePamphletCanvas() {
	}

	/**
	 * This method displays a message string near the bottom of the canvas. Every
	 * time this method is called, the previously displayed message (if any) is
	 * replaced by the new message text passed in.
	 */
	public void showMessage(String msg) {
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
	public void displayProfile(FacePamphletProfile profile) {
		removeAll();
		addName(profile.getName());
		addImage(profile.getImage());
		addStatus(profile.getStatus(), profile.getName());
		addFriends(profile.getFriends());
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
			add(friendName, x, friendNameY);
		}

	}

	// adds status label on the canvas
	private void addStatus(String status, String name) {
		double statusY = imageY + IMAGE_HEIGHT + STATUS_MARGIN;
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
	private void addName(String name) {
		GLabel label = new GLabel(name);
		label.setColor(Color.BLUE);
		nameY = TOP_MARGIN + label.getHeight();
		label.setFont(PROFILE_NAME_FONT);
		add(label, leftMargin, nameY);

	}

}
