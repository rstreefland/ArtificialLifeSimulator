package uk.ac.rdg.rhys.artificiallifesimulator;

import java.io.IOException;
import java.io.ObjectInputStream;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * <h1>Fox.java</h1>
 * <p>
 * This class is used to represent a life form of type fox. It inherits all
 * properties and methods from the ALifeForm and AEntity classes.
 * 
 * @author Rhys Streefland
 * @version 1.0
 * @since 2014-01-20
 * @see ALifeForm, AEntity
 */
public class Fox extends ALifeForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8059759394890495009L;
	private transient Image image = new Image(getClass().getResourceAsStream(
			"images/fox.png"));

	/**
	 * This constructor sets the properties and adds the entities that are
	 * consumed by the life form
	 * 
	 * @param initialUniqueID Sets the unique ID property of the life form
	 * @param initialName Sets the name property of the life form
	 * @param initialEnergy Sets the energy property of the life form
	 * @param initialX Sets the x coordinate of the life form
	 * @param initialY Sets the y coordinate of the life form
	 */
	public Fox(int initialUniqueID, String initialName, int initialEnergy,
			int initialX, int initialY) {

		// Set life form properties
		uniqueID = initialUniqueID;
		name = initialName;
		species = "Fox";
		type = "carnivore";
		energy = initialEnergy;
		x = initialX;
		y = initialY;
		isAlive = true;

		// Add entities that are consumed by the life form
		consumes.add("Rabbit");

		// Set ImageView properties
		imageView.setImage(image);
		imageView.setPreserveRatio(true);
		imageView.setCache(true);
	}

	/**
	 * This method is called when configuration data is loaded from a file.
	 * Image and ImageView objects are not serialized into the configuration
	 * file. This method exists to recreate the Image and ImageView objects when
	 * a configuration file is loaded to prevent the total loss of the image
	 */
	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();

		imageView = new ImageView();
		image = new Image(getClass().getResourceAsStream("images/fox.png"));
		imageView.setImage(image);
		imageView.setPreserveRatio(true);
		imageView.setCache(true);
	}

	/**
	 * Empty method that is only implemented here because it is required by AEntity
	 */
	@Override
	public boolean getDrawn() {
		return false;
	}

	/**
	 * Empty method that is only implemented here because it is required by AEntity
	 */
	@Override
	public int getNutrition() {
		return 0;
	}

}
