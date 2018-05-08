package uk.ac.rdg.rhys.artificiallifesimulator;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

/**
 * <h1>AFoodItem.java</h1>
 * <p>
 * This class extends the AEntity class and thus inherits its properties and
 * methods. The properties and methods in this class are inherited by all food
 * items
 * 
 * @author Rhys Streefland
 * @version 1.0
 * @since 2014-01-20
 */
public abstract class AFoodItem extends AEntity {

	protected String type;
	protected int nutrition;
	protected boolean drawn;

	/**
	 * Getter for the type of the food item
	 * @return Returns the type of the food item
	 */
	protected String getType() {
		return type;
	}

	/**
	 * Getter for the nutrition of the food item
	 * @return Returns the nutrition value of the food item
	 */
	protected int getNutrition() {
		return nutrition;
	}

	/**
	 * Getter for the drawn property of the food item
	 * @return Returns the drawn property of the food item
	 */
	protected boolean getDrawn() {
		return drawn;
	}

	/**
	 * Setter for the drawn property of the food item
	 * @param newDrawn The new value of the drawn property of the food item
	 */
	protected void setDrawn(boolean newDrawn) {
		drawn = newDrawn;
	}

	/**
	 * This overriding method binds the size of the ImageView to the size of the
	 * map divided by the world dimensions but also sets the drawn property of
	 * the food item of true.
	 * 
	 * @param map
	 *            This is the map GridPane
	 * @param worldSize
	 *            This is the world size (dimensions)
	 * @return imageView This method returns the sized ImageView of the entity.
	 */
	@Override
	protected ImageView getImage(GridPane map, int worldSize) {
		drawn = true;
		imageView.fitHeightProperty().bind(
				map.heightProperty().divide(worldSize * 1.5));
		imageView.fitWidthProperty().bind(
				map.widthProperty().divide(worldSize * 1.5));
		return imageView;
	}
}
