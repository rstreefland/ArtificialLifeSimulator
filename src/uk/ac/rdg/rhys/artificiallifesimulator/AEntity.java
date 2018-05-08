package uk.ac.rdg.rhys.artificiallifesimulator;

import java.util.ArrayList;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

/**
 * <h1>AEntity.java</h1>
 * <p>
 * The properties and methods of this class are inherited by every entity in the
 * simulation (life forms, food items, and obstacles)
 * 
 * @author Rhys Streefland
 * @version 1.0
 * @since 2014-01-20
 */
public abstract class AEntity {

	/**
	 * Stores the entity's image. It is set to transient because it should not
	 * be serialized and stored into a file
	 */
	protected transient ImageView imageView = new ImageView();

	// Store entity coordinates
	protected int x;
	protected int y;

	/**
	 * Getter for the x coordinate of the entity
	 * @return Returns the x coordinate of the entity
	 */
	protected int getX() {
		return x;
	}

	/**
	 * Getter for the y coordinate of the entity
	 * @return Returns the y coordinate of the entity
	 */
	protected int getY() {
		return y;
	}

	/**
	 * Setter for the x coordinate of the entity
	 * @param newX The new x coordinate the entity
	 */
	protected void setX(int newX) {
		x = newX;
	}

	/**
	 * Setter for the y coordinate of the entity
	 * @param newY The new y coordinate of the entity
	 */
	protected void setY(int newY) {
		y = newY;
	}

	protected abstract String getName();

	protected abstract String getType();

	protected abstract int getEnergy();

	protected abstract int getLastFoodX();

	protected abstract int getLastFoodY();

	protected abstract boolean getIsAlive();

	protected abstract boolean getDrawn();

	protected abstract Object getSpecies();

	protected abstract ArrayList<String> getConsumes();

	protected abstract int getNutrition();

	protected abstract void setIsAlive(boolean b);

	protected abstract void setEnergy(int parseInt);

	protected abstract void setName(String text);

	protected abstract void setLastFoodX(int newX);

	protected abstract void setLastFoodY(int newY);

	/**
	 * This method binds the size of the ImageView to the size of the map
	 * divided by the world dimensions. This ensures that the image is the right
	 * size and it is fully responsive
	 * 
	 * @param map
	 *            This is the map GridPane
	 * @param worldSize
	 *            This is the world size (dimensions)
	 * @return imageView Returns the sized ImageView of the entity.
	 */
	protected ImageView getImage(GridPane map, int worldSize) {
		imageView.fitHeightProperty().bind(
				map.heightProperty().divide(worldSize * 1.5));
		imageView.fitWidthProperty().bind(
				map.widthProperty().divide(worldSize * 1.5));
		return imageView;
	}
}
