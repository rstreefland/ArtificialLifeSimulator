package uk.ac.rdg.rhys.artificiallifesimulator;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.image.Image;

/**
 * <h1>AObstacle.java</h1>
 * <p>
 * This class extends the AEntity class and thus inherits its properties and
 * methods. Every time an object of this class is created, it has a 50% chance
 * of becoming either a rock or a tree
 * 
 * @author Rhys Streefland
 * @version 1.0
 * @since 2014-01-20
 */
public class AObstacle extends AEntity {

	// Create obstacle image objects
	private Image rock = new Image(getClass().getResourceAsStream(
			"images/rock.png"));
	private Image tree = new Image(getClass().getResourceAsStream(
			"images/tree.png"));

	private String type;

	/**
	 * This constructor sets the coordinates of the obstacle based on the provided
	 * parameters. The type and ImageView properties are then set to represent
	 * either a rock or a tree by chance (50/50)
	 * 
	 * @param newX
	 *            This is the x coordinate
	 * @param newY
	 *            This is the y coordinate
	 */
	protected AObstacle(int newX, int newY) {

		// Set coordinates based on method input
		x = newX;
		y = newY;

		// 50% chance of creating a rock or a tree
		Random random = new Random();
		if (random.nextBoolean()) {
			type = "Rock";
			imageView.setImage(rock);
		} else {
			type = "Tree";
			imageView.setImage(tree);
		}

		// Set ImageView parameters
		imageView.setPreserveRatio(true);
		imageView.setCache(true);
	}

	/**
	 * Getter for the type of the obstacle
	 * @return Returns the type of the obstacle
	 */
	protected String getType() {
		return type;
	}

	/**
	 * Empty method that is only implemented here because it is required by AEntity
	 */
	@Override
	protected String getName() {
		return null;
	}

	/**
	 * Empty method that is only implemented here because it is required by AEntity
	 */
	@Override
	protected int getEnergy() {
		return 0;
	}

	/**
	 * Empty method that is only implemented here because it is required by AEntity
	 */
	@Override
	protected int getLastFoodX() {
		return 0;
	}

	/**
	 * Empty method that is only implemented here because it is required by AEntity
	 */
	@Override
	protected int getLastFoodY() {
		return 0;
	}

	/**
	 * Empty method that is only implemented here because it is required by AEntity
	 */
	@Override
	protected boolean getIsAlive() {
		return false;
	}
	
	/**
	 * Empty method that is only implemented here because it is required by AEntity
	 */
	@Override
	protected boolean getDrawn() {
		return false;
	}

	/**
	 * Empty method that is only implemented here because it is required by AEntity
	 */
	@Override
	protected Object getSpecies() {
		return null;
	}

	/**
	 * Empty method that is only implemented here because it is required by AEntity
	 */
	@Override
	protected void setIsAlive(boolean b) {
	}

	/**
	 * Empty method that is only implemented here because it is required by AEntity
	 */
	@Override
	protected void setEnergy(int parseInt) {		
	}
	
	/**
	 * Empty method that is only implemented here because it is required by AEntity
	 */
	@Override
	protected void setName(String text) {		 
	}

	/**
	 * Empty method that is only implemented here because it is required by AEntity
	 */
	@Override
	protected void setLastFoodX(int newX) {		
	}

	/**
	 * Empty method that is only implemented here because it is required by AEntity
	 */
	@Override
	protected void setLastFoodY(int newY) {		
	}

	/**
	 * Empty method that is only implemented here because it is required by AEntity
	 */
	@Override
	protected ArrayList<String> getConsumes() {
		return null;
	}

	/**
	 * Empty method that is only implemented here because it is required by AEntity
	 */
	@Override
	protected int getNutrition() {
		return 0;
	}

}
