package uk.ac.rdg.rhys.artificiallifesimulator;

import java.util.ArrayList;

import javafx.scene.image.Image;

/**
 * <h1>Berry.java</h1>
 * <p>
 * This class is used to represent a food item of type berry. It inherits all
 * properties and methods from the AFoodItem and AEntity classes.
 * 
 * @author Rhys Streefland
 * @version 1.0
 * @since 2014-01-20
 * @see AFoodItem, AEntity
 */
public class Berry extends AFoodItem {

	private Image image = new Image(getClass().getResourceAsStream(
			"images/berry.png"));

	public Berry(int initialX, int initialY) {

		// Set food item parameters
		type = "Berry";
		nutrition = -3;
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

	/**
	 * Empty method that is only implemented here because it is required by AEntity
	 */
	@Override
	public ArrayList<String> getConsumes() {
		return null;
	}

}
