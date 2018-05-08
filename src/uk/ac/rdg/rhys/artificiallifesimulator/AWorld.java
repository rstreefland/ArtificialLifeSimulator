package uk.ac.rdg.rhys.artificiallifesimulator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * <h1>AWorld.java</h1>
 * <p>
 * This class is responsible for creating the artificial world in which the
 * entities reside. Methods in this class perform logical operations on the
 * world and change the attributes of entities.
 * 
 * @author Rhys Streefland
 * @version 1.0
 * @since 2014-01-20
 * @see Entity
 */
public class AWorld implements Serializable {

	// Define world parameters
	private static final long serialVersionUID = -8210010531362137814L;
	private int simulationCycles;
	private int worldSize;
	private int foodDensity;
	private int objectDensity;
	private int maxSensingDist = 2;
	private int currentCycle;

	/**
	 * Enumerated type for the direction of the life form
	 * 
	 * @author Rhys
	 */
	private enum Direction {
		NORTH, EAST, SOUTH, WEST, NONE
	}

	private Direction currentDirection; // define the currentDirection
	private static String[][] world; // define the world as a 2D array

	/**
	 * This constructor initializes the default values of the world if no
	 * parameters are provided
	 */
	public AWorld() {

		// Initialize default values
		currentDirection = Direction.NONE;
		simulationCycles = 100;
		worldSize = 10;
		foodDensity = 10;
		objectDensity = 10;
		world = new String[10][10];
	}

	/**
	 * This method sets the values of the world based on the parameters provided
	 * 
	 * @param userSimulationCycles
	 *            The number of simulation cycles
	 * @param userWorldSize
	 *            The size of the world (dimensions)
	 * @param userFoodDensity
	 *            The number of food items to be generated in the world
	 * @param userObjectDensity
	 *            The number of object items to be generated in the world
	 */
	public AWorld(int userSimulationCycles, int userWorldSize,
			int userFoodDensity, int userObjectDensity) {

		// Initialize values based on method parameters
		currentDirection = Direction.NONE;
		simulationCycles = userSimulationCycles;
		worldSize = userWorldSize;
		foodDensity = userFoodDensity;
		objectDensity = userObjectDensity;
		world = new String[worldSize][worldSize];

	}

	/**
	 * This method generates the food item, obstacle and life form objects and
	 * stores them in the relevant ArrayList. The food types and life form
	 * species are also stored in the world array
	 * 
	 * @param lifeForm
	 *            The ArrayList of life form objects
	 * @param foodItems
	 *            The ArrayList of food item objects
	 * @param obstacles
	 *            The ArrayList of obstacle objects
	 */
	protected void initWorld(ArrayList<AEntity> lifeForm,
			ArrayList<AEntity> foodItems, ArrayList<AEntity> obstacles) {

		System.out.println("Initialising world...");

		currentCycle = 0;

		// Define temporary variables
		int x;
		int y;

		// Blank the world array and clear the food item ArrayList
		for (String[] row : world)
			Arrays.fill(row, " ");
		foodItems.clear();
		obstacles.clear();

		// Generate random food items
		for (int i = 0; i < foodDensity; i++) {
			createRandomFoodItem(foodItems);
		}

		// Generate random obstacles
		for (int i = 0; i < objectDensity; i++) {

			x = generateRandomNumber(worldSize - 1, 0);
			y = generateRandomNumber(worldSize - 1, 0);

			/*
			 * While current world coordinate is not empty generate new
			 * coordinates
			 */
			while (!world[x][y].equals(" ")) {
				x = generateRandomNumber(worldSize - 1, 0);
				y = generateRandomNumber(worldSize - 1, 0);
			}

			// add a new obstacle to the ArrayList and store in the world array
			obstacles.add(new AObstacle(x, y));
			world[x][y] = "X";
		}

		// Generate life forms
		for (int i = 0; i < lifeForm.size(); i++) {

			x = generateRandomNumber(worldSize - 1, 0);
			y = generateRandomNumber(worldSize - 1, 0);

			/*
			 * While current world coordinate is not empty generate new
			 * coordinates
			 */
			while (!world[x][y].equals(" ")) {
				x = generateRandomNumber(worldSize - 1, 0);
				y = generateRandomNumber(worldSize - 1, 0);
			}

			// Set life form coordinates
			lifeForm.get(i).setX(x);
			lifeForm.get(i).setY(y);
		}
		return;
	}

	/**
	 * This method chooses the sense to use to look for food depending on the
	 * current life form or species. The currentDirection value is then set for
	 * the current bug based on the return value of the sense() method called
	 * 
	 * If the life form is a bug, the 'feel' sense will be used. If the life
	 * form is a herbivore, the 'sight' sense will be used. If the life form is
	 * a carnivore, the 'smell' sense will be used.
	 * 
	 * @param currentLifeForm
	 *            The life form that needs the direction of food
	 * @param foodItems
	 *            The ArrayList of food item objects
	 */
	protected void getDirectionOfFood(AEntity currentLifeForm,
			ArrayList<AEntity> lifeForms) {

		// reset the current direction
		currentDirection = Direction.NONE;

		// Use a different sense depending on the life form type or species
		if (currentLifeForm.getSpecies().equals("Bug")) {
			currentDirection = sense(currentLifeForm, lifeForms, "feel");
		} else if (currentLifeForm.getType().equals("herbivore")) {
			currentDirection = sense(currentLifeForm, lifeForms, "sight");
		} else {
			currentDirection = sense(currentLifeForm, lifeForms, "smell");
		}

		if (currentDirection == Direction.NONE) {
			currentDirection = getRandomDirectionToMove();
		}

		return;
	}

	/**
	 * This method generates a random direction by calling the
	 * generateRandomNumber function then maps the number to the relevant
	 * direction
	 * 
	 * @return Returns the random direction chosen
	 */
	private Direction getRandomDirectionToMove() {

		// Generate a random number using the Random class
		Random rand = new Random();
		int randomNumber = rand.nextInt((4 - 1) + 1) + 1;

		// Return a direction based on the random integer
		switch (randomNumber) {
		case 1:
			return Direction.NORTH;
		case 2:
			return Direction.EAST;
		case 3:
			return Direction.SOUTH;
		case 4:
			return Direction.WEST;
		}
		return Direction.NONE;
	}

	/**
	 * This method detects if the life form is about to cross the world boundary
	 * with it's next movement and changes the direction of the movement
	 * accordingly to prevent an array index out of bounds error
	 * 
	 * @param currentLifeForm
	 *            The bug that is about to move
	 */
	protected void protectBoundaries(AEntity currentLifeForm) {

		// Define temporary variables
		int x = currentLifeForm.getX();
		int y = currentLifeForm.getY();

		/*
		 * While a bug is about to cross a boundary with it's next movement,
		 * select a different direction to move randomly
		 */
		while (((x < 1) && (currentDirection == Direction.WEST))
				|| ((x > worldSize - 2) && (currentDirection == Direction.EAST))
				|| ((y < 1) && (currentDirection == Direction.NORTH))
				|| ((y > worldSize - 2) && (currentDirection == Direction.SOUTH))) {
			currentDirection = getRandomDirectionToMove();
			System.out.println(currentLifeForm.getName()
					+ "reached the world boundary. Changing direction");
		}
		return;
	}

	/**
	 * This method senses for food in all directions. The strength of the
	 * sensing is determined by the senseType parameter.
	 * 
	 * Feel is the weakest strength and Smell is the strongest sense, with sight
	 * in the middle
	 * 
	 * @param currentLifeForm
	 *            The life form that is currently smelling for food
	 * @param lifeForms
	 *            The ArrayList of life form objects
	 * @param senseType
	 *            The type of sensing to be performed
	 * @return Returns the relevant direction if food is found in that
	 *         direction. Returns NONE if no food is found in any direction
	 */
	private Direction sense(AEntity currentLifeForm,
			ArrayList<AEntity> lifeForms, String senseType) {

		// Define temporary variables
		int x = currentLifeForm.getX();
		int y = currentLifeForm.getY();
		int newX;
		int newY;

		switch (senseType) {
		case "feel":
			maxSensingDist = 1;
			break;
		case "sight":
			maxSensingDist = 2;
			break;
		case "smell":
			maxSensingDist = 4;
			break;
		}

		// Check for food in every direction up to the maxSensingDist
		for (int i = 1; i <= maxSensingDist; i++) {

			// Check to the North
			if (y - i >= 0) {

				// Set new coordinates to North of current Position
				newX = x;
				newY = y - i;

				for (int j = 0; j < currentLifeForm.getConsumes().size(); j++) {
					/*
					 * If the current life form is a carnivore - search for
					 * other life forms
					 */
					if (currentLifeForm.getType() == "carnivore") {
						for (int k = 0; k < lifeForms.size(); k++) {
							if (lifeForms.get(k).getSpecies() == currentLifeForm
									.getConsumes().get(j)
									&& lifeForms.get(k).getX() == newX
									&& lifeForms.get(k).getY() == newY) {
								System.out.println(lifeForms.get(k)
										.getSpecies() + " found to the north");
								return Direction.NORTH;
							}
						}
					}
					/*
					 * If the current life form is a herbivore - search for food
					 * items
					 */
					if (world[newX][newY].equals(currentLifeForm.getConsumes()
							.get(j))) {
						System.out.println(world[newX][newY]
								+ " found to the north");
						return Direction.NORTH;
					}
				}
				/*
				 * If the new location contains an obstacle - Don't return this
				 * direction
				 */
				if (world[newX][newY] == "X") {
					return Direction.NONE;
				}
			}

			// Check to the East
			if (x + i < worldSize) {

				// Set new coordinates to East of current position
				newX = x + i;
				newY = y;

				for (int j = 0; j < currentLifeForm.getConsumes().size(); j++) {
					/*
					 * If the current life form is a carnivore - search for
					 * other life forms
					 */
					if (currentLifeForm.getType() == "carnivore") {
						for (int k = 0; k < lifeForms.size(); k++) {
							if (lifeForms.get(k).getSpecies() == currentLifeForm
									.getConsumes().get(j)
									&& lifeForms.get(k).getX() == newX
									&& lifeForms.get(k).getY() == newY) {
								System.out.println(lifeForms.get(k)
										.getSpecies() + " found to the east");
								return Direction.EAST;
							}
						}
					}
					/*
					 * If the current life form is a herbivore - search for food
					 * items
					 */
					if (world[newX][newY].equals(currentLifeForm.getConsumes()
							.get(j))) {
						System.out.println(world[newX][newY]
								+ " found to the east");
						return Direction.EAST;
					}
				}
				/*
				 * If the new location contains an obstacle - Don't return this
				 * direction
				 */
				if (world[newX][newY] == "X") {
					return Direction.NONE;
				}
			}

			// Check to the South
			if (y + i < worldSize) {

				// Set new coordinates to South of current position
				newX = x;
				newY = y + i;

				for (int j = 0; j < currentLifeForm.getConsumes().size(); j++) {
					/*
					 * If the current life form is a carnivore - search for
					 * other life forms
					 */
					if (currentLifeForm.getType() == "carnivore") {
						for (int k = 0; k < lifeForms.size(); k++) {
							if (lifeForms.get(k).getSpecies() == currentLifeForm
									.getConsumes().get(j)
									&& lifeForms.get(k).getX() == newX
									&& lifeForms.get(k).getY() == newY) {
								System.out.println(lifeForms.get(k)
										.getSpecies() + " found to the south");
								return Direction.SOUTH;
							}
						}
					}
					/*
					 * If the current life form is a herbivore - search for food
					 * items
					 */
					if (world[newX][newY].equals(currentLifeForm.getConsumes()
							.get(j))) {
						System.out.println(world[newX][newY]
								+ " found to the south");
						return Direction.SOUTH;
					}
				}
				/*
				 * If the new location contains an obstacle - Don't return this
				 * direction
				 */
				if (world[newX][newY] == "X") {
					return Direction.NONE;
				}
			}

			// Check to the West
			if (x - i >= 0) {

				// Set new coordinates to West of current position
				newX = x - i;
				newY = y;

				for (int j = 0; j < currentLifeForm.getConsumes().size(); j++) {
					/*
					 * If the current life form is a carnivore - search for
					 * other life forms
					 */
					if (currentLifeForm.getType() == "carnivore") {
						for (int k = 0; k < lifeForms.size(); k++) {
							if (lifeForms.get(k).getSpecies() == currentLifeForm
									.getConsumes().get(j)
									&& lifeForms.get(k).getX() == newX
									&& lifeForms.get(k).getY() == newY) {
								System.out.println(lifeForms.get(k)
										.getSpecies() + " found to the west");
								return Direction.WEST;
							}
						}
					}
					/*
					 * If the current life form is a herbivore - search for food
					 * items
					 */
					if (world[newX][newY].equals(currentLifeForm.getConsumes()
							.get(j))) {
						System.out.println(world[newX][newY]
								+ " found to the west");
						return Direction.WEST;
					}
				}
				/*
				 * If the new location contains an obstacle - Don't return this
				 * direction
				 */
				if (world[newX][newY] == "X") {
					return Direction.NONE;
				}
			}
		}
		return Direction.NONE;
	}

	/**
	 * This method moves the bug in the specified direction if possible and
	 * returns the value that the energy of the bug should be changed by. The
	 * current life form's movement is calculated by the moveLogic() helper
	 * method
	 * 
	 * @param currentLifeForm
	 *            The life form that is currently being moved
	 * @param lifeForms
	 *            The ArrayList of life form objects
	 * @param foodItems
	 *            The ArrayList of food item objects
	 * @return Returns the change in the life form's energy level
	 */
	protected int move(AEntity currentLifeForm, ArrayList<AEntity> lifeForms,
			ArrayList<AEntity> foodItems) {

		// Define temporary variables
		int x = currentLifeForm.getX();
		int y = currentLifeForm.getY();
		int newX;
		int newY;

		switch (currentDirection) {

		case NORTH:
			// Set new coordinates to North of current position
			newX = x;
			newY = y - 1;
			return moveLogic(currentLifeForm, lifeForms, foodItems, newX, newY);

		case EAST:
			// Set new coordinates to East of current position
			newX = x + 1;
			newY = y;
			return moveLogic(currentLifeForm, lifeForms, foodItems, newX, newY);

		case SOUTH:
			// Set new coordinates to East of current position
			newX = x;
			newY = y + 1;
			return moveLogic(currentLifeForm, lifeForms, foodItems, newX, newY);

		case WEST:
			// Set new coordinates to West of current position
			newX = x - 1;
			newY = y;
			return moveLogic(currentLifeForm, lifeForms, foodItems, newX, newY);

		default:
			return 0;
		}
	}

	/**
	 * This method is a helper method for the move() method. This method
	 * calculates if and how the life form should move based on the presence of
	 * food items, other bugs, and obstacles.
	 * 
	 * This order of precedence is used in the logic to move into the next
	 * position: 
	 * <p>If the life form is a carnivore it will try and eat a life form
	 * <p>If the life form is a carnivore it will try and eat a food item 
	 * <p>If the life form collides with another life form then it will lose energy 
	 * <p>If the life form collides with an obstacle then it will lose energy 
	 * <p>If the there is nothing in the position then the life form will not lose or gain
	 * energy
	 * 
	 * @param currentLifeForm
	 *            The life form that is currently being moved
	 * @param lifeForms
	 *            The ArrayList of life form objects
	 * @param foodItems
	 *            The ArrayList of food item objects
	 * @param newX
	 *            The x coordinate of the life form once it has moved
	 * @param newY
	 *            The y coordinate of the life form once it has moved
	 * @return Returns the change in the life form's energy level
	 */
	private int moveLogic(AEntity currentLifeForm,
			ArrayList<AEntity> lifeForms, ArrayList<AEntity> foodItems,
			int newX, int newY) {

		int energy; // temporary energy variable

		// If the current life form is a carnivore
		if (currentLifeForm.getType().equals("carnivore")) {
			for (int i = 0; i < currentLifeForm.getConsumes().size(); i++) {
				for (int j = 0; j < lifeForms.size(); j++) {

					// If the current life form can eat the other life form
					if (lifeForms.get(j).getSpecies() == currentLifeForm
							.getConsumes().get(i)
							&& lifeForms.get(j).getX() == newX
							&& lifeForms.get(j).getY() == newY) {

						// Store the other life form's energy in the variable
						energy = lifeForms.get(j).getEnergy();

						// Remove (kill) the other life form
						lifeForms.get(j).setIsAlive(false);
						System.out.println(lifeForms.get(j).getName()
								+ " eaten by " + currentLifeForm.getName());

						// Update current life form position
						currentLifeForm.setX(newX);
						currentLifeForm.setY(newY);
						currentLifeForm.setLastFoodX(newX);
						currentLifeForm.setLastFoodY(newY);
						return energy;
					}
				}
			}
		}

		// If the current life form is a herbivore
		if (currentLifeForm.getType().equals("herbivore")) {
			for (int i = 0; i < currentLifeForm.getConsumes().size(); i++) {

				// If the current life form can eat the food item
				if (world[newX][newY].equals(currentLifeForm.getConsumes().get(
						i))) {
					for (int j = 0; j < foodItems.size(); j++) {

						// Match the food item with it's object
						if (foodItems.get(j).getType()
								.equals(world[newX][newY])) {

							// Store the food's nutrition in the energy variable
							energy = foodItems.get(j).getNutrition();
							System.out.println(foodItems.get(j).getType()
									+ " eaten by " + currentLifeForm.getName());

							// Update current life form position
							currentLifeForm.setX(newX);
							currentLifeForm.setY(newY);
							currentLifeForm.setLastFoodX(newX);
							currentLifeForm.setLastFoodY(newY);
							world[newX][newY] = " ";
							return energy;
						}
					}
				}
			}
		}

		// If the current life form collides with another life form
		for (int i = 0; i < lifeForms.size(); i++) {
			if (newX == lifeForms.get(i).getX()
					&& newY == lifeForms.get(i).getY()) {
				System.out.println(currentLifeForm.getName()
						+ " hit another life form");
				return -1;
			}
		}

		// If the current life form hits an obstacle
		if (world[newX][newY] == "X") {
			System.out.println(currentLifeForm.getName() + " hit an obstacle");
			return -1;
		} else {

			// Move into free space
			System.out.println(currentLifeForm.getName()
					+ " moved into free space");
			currentLifeForm.setX(newX);
			currentLifeForm.setY(newY);
			return 0;
		}
	}

	/**
	 * This method generates a new food item based on a defined probability of
	 * generating each food item. The probabilities are detailed below:
	 * 
	 * Grass - 25%
	 * Flower - 15%
	 * Leaf - 25%
	 * Plankton - 15%
	 * Mushroom - 10%
	 * Berry - 10%
	 * 
	 * @param foodItems
	 *            The ArrayList of food item objects
	 */
	protected void createRandomFoodItem(ArrayList<AEntity> foodItems) {

		// Define temporary variables
		int x;
		int y;
		int randomFood;

		x = generateRandomNumber(worldSize - 1, 0);
		y = generateRandomNumber(worldSize - 1, 0);

		/*
		 * While current world coordinate is not empty generate new coordinates
		 */
		while (!world[x][y].equals(" ")) {
			x = generateRandomNumber(worldSize - 1, 0);
			y = generateRandomNumber(worldSize - 1, 0);
		}

		// Generate random number between 1 and 100
		randomFood = generateRandomNumber(100, 1);

		// Add a new food number based on the result
		if (randomFood > 0 && randomFood < 25) {
			foodItems.add(new Grass(x, y));
			System.out.println("Added new food item of type: "
					+ foodItems.get(foodItems.size() - 1).getType());
			world[x][y] = foodItems.get(foodItems.size() - 1).getType();
		}
		if (randomFood > 24 && randomFood < 40) {
			foodItems.add(new Flower(x, y));
			System.out.println("Added new food item of type: "
					+ foodItems.get(foodItems.size() - 1).getType());
			world[x][y] = foodItems.get(foodItems.size() - 1).getType();
		}
		if (randomFood > 39 && randomFood < 65) {
			foodItems.add(new Leaf(x, y));
			System.out.println("Added new food item of type: "
					+ foodItems.get(foodItems.size() - 1).getType());
			world[x][y] = foodItems.get(foodItems.size() - 1).getType();
		}
		if (randomFood > 64 && randomFood < 80) {
			foodItems.add(new Plankton(x, y));
			System.out.println("Added new food item of type: "
					+ foodItems.get(foodItems.size() - 1).getType());
			world[x][y] = foodItems.get(foodItems.size() - 1).getType();
		}
		if (randomFood > 79 && randomFood < 90) {
			foodItems.add(new Mushroom(x, y));
			System.out.println("Added new food item of type: "
					+ foodItems.get(foodItems.size() - 1).getType());
			world[x][y] = foodItems.get(foodItems.size() - 1).getType();
		}
		if (randomFood > 89 && randomFood < 100) {
			foodItems.add(new Berry(x, y));
			System.out.println("Added new food item of type: "
					+ foodItems.get(foodItems.size() - 1).getType());
			world[x][y] = foodItems.get(foodItems.size() - 1).getType();
		}
		return;
	}

	/**
	 * Generate a random number between the lower boundary and the upper
	 * boundary
	 * 
	 * @param max
	 *            The upper boundary
	 * @param min
	 *            The lower boundary
	 * @return Return the generated random number
	 */
	protected int generateRandomNumber(int max, int min) {
		Random rand = new Random();
		int randomNumber = rand.nextInt((max - min) + 1) + min;
		return randomNumber;
	}

	/**
	 * Resets the current simulation cycle counter to 0
	 */
	protected void resetCurrentCycle() {
		currentCycle = 0;
	}
	
	/**
	 * Increments the current simulation cycle counter
	 */
	protected void incrementCurrentCycle() {
		currentCycle++;
	}

	/**
	 * Getter for the number of simulation cycles
	 * @return Returns the number of simulation cycles
	 */
	protected int getSimulationCycles() {
		return simulationCycles;
	}

	/**
	 * Getter for the world size
	 * @return Returns the world size
	 */
	protected int getWorldSize() {
		return worldSize;
	}

	/**
	 * Getter for the food density
	 * @return Returns the food density
	 */
	protected int getFoodDensity() {
		return foodDensity;
	}

	/**
	 * Getter for the object density
	 * @return Returns the object density
	 */
	protected int getObjectDensity() {
		return objectDensity;
	}

	/**
	 * Getter for the current simulation cycle
	 * @return Returns the current simulation cycle
	 */
	protected int getCurrentCycle() {
		return currentCycle;
	}

	/**
	 * Setter for the number of simulation cycles
	 * @param newSimulationCycles The new number of simulation cycles
	 */
	protected void setSimulationCycles(int newSimulationCycles) {
		simulationCycles = newSimulationCycles;
	}

	/** 
	 * Setter for the world size
	 * @param newWorldSize The new world size
	 */
	protected void setWorldSize(int newWorldSize) {
		worldSize = newWorldSize;
		world = new String[newWorldSize][newWorldSize];
	}

	/**
	 * Setter for the food density
	 * @param newFoodDensity The new food density
	 */
	protected void setFoodDensity(int newFoodDensity) {
		foodDensity = newFoodDensity;
	}

	/**
	 * Setter for the object density
	 * @param newObjectDensity The new object density
	 */
	protected void setObjectDensity(int newObjectDensity) {
		objectDensity = newObjectDensity;
	}
}