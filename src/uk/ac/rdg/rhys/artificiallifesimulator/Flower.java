package uk.ac.rdg.rhys.artificiallifesimulator;

import java.util.ArrayList;

import javafx.scene.image.Image;

/**
 * <h1>Flower.java</h1>
 * <p>
 * This class is used to represent a food item of type flower. It inherits all
 * properties and methods from the AFoodItem and AEntity classes.
 * 
 * @author Rhys Streefland
 * @version 1.0
 * @since 2014-01-20
 * @see AFoodItem, AEntity
 */
public class Flower extends AFoodItem {

	private Image image = new Image(getClass().getResourceAsStream(
			"images/flower.png"));

	/**
	 * This constructor sets the coordinates of the food object based on the
	 * provided parameters. The properties of the food item set to be relevant
	 * to a flower
	 * 
	 * @param newX
	 *            This is the x coordinate
	 * @param newY
	 *            This is the y coordinate
	 */
	public Flower(int initialX, int initialY) {

		// Set food item parameters
		type = "Flower";
		nutrition = 5;
		x = initialX;
		y = initialY;
		drawn = false;

		// Set ImageView parameters
		imageView.setImage(image);
		imageView.setPreserveRatio(true);
		imageView.setCache(true);
	}

	/**
	 * Empty method that is only implemented here because it is required by AEntity
	 */
	@Override
	public String getName() {
		return null;
	}

	/**
	 * Empty method that is only implemented here because it is required by AEntity
	 */
	@Override
	public int getEnergy() {
		return 0;
	}

	/**
	 * Empty method that is only implemented here because it is required by AEntity
	 */
	@Override
	public int getLastFoodX() {
		return 0;
	}

	/**
	 * Empty method that is only implemented here because it is required by AEntity
	 */
	@Override
	public int getLastFoodY() {
		return 0;
	}

	/**
	 * Empty method that is only implemented here because it is required by AEntity
	 */
	@Override
	public boolean getIsAlive() {
		return false;
	}

	/**
	 * Empty method that is only implemented here because it is required by AEntity
	 */
	@Override
	public Object getSpecies() {
		return null;
	}

	/**
	 * Empty method that is only implemented here because it is required by AEntity
	 */
	@Override
	public ArrayList<String> getConsumes() {
		return null;
	}

	/**
	 * Empty method that is only implemented here because it is required by AEntity
	 */
	@Override
	public void setIsAlive(boolean b) {
	}

	/**
	 * Empty method that is only implemented here because it is required by AEntity
	 */
	@Override
	public void setEnergy(int parseInt) {
	}

	/**
	 * Empty method that is only implemented here because it is required by AEntity
	 */
	@Override
	public void setName(String text) {
	}

	/**
	 * Empty method that is only implemented here because it is required by AEntity
	 */
	@Override
	public void setLastFoodX(int newX) {
	}

	/**
	 * Empty method that is only implemented here because it is required by AEntity
	 */
	@Override
	public void setLastFoodY(int newY) {
	}

}
