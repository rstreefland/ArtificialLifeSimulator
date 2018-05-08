package uk.ac.rdg.rhys.artificiallifesimulator;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <h1>ALifeForm.java</h1>
 * <p>
 * This class extends the AEntity class and thus inherits its properties and
 * methods. The properties and methods in this class are inherited by all life forms
 * 
 * @author Rhys Streefland
 * @version 1.0
 * @since 2014-01-20
 */
public abstract class ALifeForm extends AEntity implements Serializable {

	// Define life form parameters
	private static final long serialVersionUID = 4639205277430806894L;
	protected int uniqueID;
	protected String name;
	protected String species;
	protected String type;
	protected int energy;
	protected int lastFoodX;
	protected int lastFoodY;
	protected boolean isAlive;
	protected ArrayList<String> consumes = new ArrayList<String>();

	/**
	 * @return Returns a string comprised of the name and energy value of the life form
	 */
	public String toString() {
		String output;
		output = name + ": " + energy;
		return output;
	}

	/**
	 * Getter for the uniqueID of the life form
	 * @return Returns the uniqueID of the life
	 */
	protected int getUniqueID() {
		return uniqueID;
	}

	/** 
	 * Getter for the name of the life form
	 * @return Returns the name of the life form
	 */
	protected String getName() {
		return name;
	}

	/**
	 * Getter for the species of the life form
	 * @return Returns the species of the life form
	 */
	protected String getSpecies() {
		return species;
	}

	/**
	 * Getter for the type of the life form
	 * @return Returns the type of the life form
	 */
	protected String getType() {
		return type;
	}

	/**
	 * Getter for the energy of the life form
	 * @return Returns the energy of the life form
	 */
	protected int getEnergy() {
		return energy;
	}

	/**
	 * Getter for the lastFoodX value of the life form
	 * @return Returns the lastFoodX value of the life form
	 */
	protected int getLastFoodX() {
		return lastFoodX;
	}

	/**
	 * Getter for the lastFoodY value of the life form
	 * @return Returns the lastFoodY value of the life form
	 */
	protected int getLastFoodY() {
		return lastFoodY;
	}

	/** 
	 * Getter for the isAlive property of the life form
	 * @return Returns the isAlive property of the life form
	 */
	protected boolean getIsAlive() {
		return isAlive;
	}

	/**
	 * Getter for the ArrayList of consumes for the life form
	 * @return Returns the ArrayList of consumes for the life form
	 */
	protected ArrayList<String> getConsumes() {
		return consumes;
	}

	/**
	 * Setter for the uniqueID of the life form
	 * @param newUniqueID The new uniqueID of the life form
	 */
	protected void setUniqueID(int newUniqueID) {
		uniqueID = newUniqueID;
	}

	/**
	 * Setter for the name of the life form
	 * @return newName The new name of the life form
	 */
	protected void setName(String newName) {
		name = newName;
	}

	/**
	 * Setter for the type of the life form
	 * @param newType The new type of the life form
	 */
	protected void setType(String newType) {
		type = newType;
	}

	/**
	 * Setter for the energy of the life form
	 * @param newEnergy The new energy value of the life form
	 */
	protected void setEnergy(int newEnergy) {
		energy = newEnergy;
	}

	/**
	 * Setter for the lastFoodX value of the life form
	 * @param newLastFoodX The new lastFoodX value of the life form
	 */
	protected void setLastFoodX(int newLastFoodX) {
		lastFoodX = newLastFoodX;
	}

	/**
	 * Setter for the lastFoodY value of the life form
	 * @param newLastFoodY The new lastFoodY value of the life form
	 */
	protected void setLastFoodY(int newLastFoodY) {
		lastFoodY = newLastFoodY;
	}

	/**
	 * Setter for the isAlive parameter of the life form
	 * @param newValue The new isAlive parameter of the life form
	 */
	protected void setIsAlive(boolean newValue) {
		isAlive = newValue;
	}
}
