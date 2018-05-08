package uk.ac.rdg.rhys.artificiallifesimulator;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;import uk.ac.rdg.rhys.artificiallifesimulator.*;

/**
 * <h1>Simulation.java</h1>
 * <p>
 * This class is responsible for handling all simulation related functions of
 * the program. Three ArrayLists are defined, one for life forms, one for food
 * items, and one for obstacles. An object of type AWorld is also created in
 * this class. All methods in this class read or manipulate data in the above
 * objects
 * 
 * @author Rhys Streefland
 * @version 1.0
 * @since 2014-01-20
 * @see AWorld
 */
public class Simulation {

	protected ArrayList<AEntity> lifeForms = new ArrayList<AEntity>();
	protected ArrayList<AEntity> foodItems = new ArrayList<AEntity>();
	protected ArrayList<AEntity> obstacles = new ArrayList<AEntity>();
	protected AWorld world = new AWorld();

	protected String filePath;

	private enum Species {
		Bear, Bird, Bug, Cow, Fish, Fox, Lion, Mouse, Pig, Rabbit, Whale
	}

	/**
	 * Empty constructor for Simulation class
	 */
	public Simulation() {
	}

	/**
	 * This method creates the relevant life form object based on the species
	 * parameter. The life form objects are created by calling the relevant
	 * constructor and supplying the parameters
	 * 
	 * @param chosenSpecies
	 *            The chosen species for the new life form
	 * @param intitalUniqueID
	 *            The unique ID for the new life form
	 * @param initialName
	 *            The name for the new life form
	 * @param initialEnergy
	 *            The energy for the new life form
	 * @param initialX
	 *            The x coordinate for the new life form
	 * @param initialY
	 *            The y coordinate for the new life form
	 * 
	 */
	private void createObject(Species chosenSpecies, int initialUniqueID,
			String initialName, int initialEnergy, int initialX, int initialY) {
		switch (chosenSpecies) {
		case Bear:
			lifeForms.add(new Bear(initialUniqueID, initialName, initialEnergy,
					initialX, initialY));
			break;
		case Bird:
			lifeForms.add(new Bird(initialUniqueID, initialName, initialEnergy,
					initialX, initialY));
			break;
		case Bug:
			lifeForms.add(new Bug(initialUniqueID, initialName, initialEnergy,
					initialX, initialY));
			break;
		case Cow:
			lifeForms.add(new Cow(initialUniqueID, initialName, initialEnergy,
					initialX, initialY));
			break;
		case Fish:
			lifeForms.add(new Fish(initialUniqueID, initialName, initialEnergy,
					initialX, initialY));
			break;
		case Fox:
			lifeForms.add(new Fox(initialUniqueID, initialName, initialEnergy,
					initialX, initialY));
			break;
		case Lion:
			lifeForms.add(new Lion(initialUniqueID, initialName, initialEnergy,
					initialX, initialY));
			break;
		case Mouse:
			lifeForms.add(new Mouse(initialUniqueID, initialName,
					initialEnergy, initialX, initialY));
			break;
		case Pig:
			lifeForms.add(new Pig(initialUniqueID, initialName, initialEnergy,
					initialX, initialY));
			break;
		case Rabbit:
			lifeForms.add(new Rabbit(initialUniqueID, initialName,
					initialEnergy, initialX, initialY));
			break;
		case Whale:
			lifeForms.add(new Whale(initialUniqueID, initialName,
					initialEnergy, initialX, initialY));
			break;
		}
	}

	/**
	 * This method is invoked each simulation cycle and calls the relevant
	 * methods in the World class for each life form. If the number of food
	 * items is less than the food density then the a new food item will be
	 * generated based on a 50% chance.
	 * 
	 * This method also updates the energy value of the current life form with
	 * the value returned from the world.move() function. The method also sets
	 * the life form's isAlive property to false if the life form's energy
	 * reaches 0.
	 */
	protected void runSimulation() {

		// Define temporary variables
		int energyIncrease;
		int oldEnergy;
		int newEnergy;

		// If number of food items < food density
		if (foodItems.size() < world.getFoodDensity()) {
			Random random = new Random();

			// 50% chance of creating a new food items
			if (random.nextBoolean()) {
				world.createRandomFoodItem(foodItems);
			}
		}

		// Call the simulation methods for each life form
		for (int i = 0; i < lifeForms.size(); i++) {
			if (lifeForms.get(i).getEnergy() != 0) {
				world.getDirectionOfFood(lifeForms.get(i), lifeForms);
				world.protectBoundaries(lifeForms.get(i));

				// Update the life form's energy
				energyIncrease = world.move(lifeForms.get(i), lifeForms,
						foodItems);
				oldEnergy = lifeForms.get(i).getEnergy();
				newEnergy = oldEnergy + energyIncrease;
				lifeForms.get(i).setEnergy(newEnergy);
			} else {
				lifeForms.get(i).setIsAlive(false);
			}
		}
		world.incrementCurrentCycle();
	}

	/**
	 * This method reads the lifeForms and world objects from the file specified
	 * in the filePath variable using the serializer class and notifies the user
	 * if it is successful.
	 */
	@SuppressWarnings("unchecked")
	protected int readFile() {
		try {
			FileInputStream fin = new FileInputStream(filePath);
			ObjectInputStream ois = new ObjectInputStream(fin);

			// Store into objects
			lifeForms = (ArrayList<AEntity>) ois.readObject();
			world = (AWorld) ois.readObject();

			ois.close();

			System.out.println("Data read from file sucessfully");
			world.setWorldSize(world.getWorldSize());

			// Show information dialog
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information");
			alert.setHeaderText("Information");
			alert.setContentText("Existing configuration loaded successfully!");
			alert.showAndWait();
		} catch (Exception ex) {
			System.out.println("No existing configuration file found");
			return 1;
		}
		return 0;
	}

	/**
	 * This method saves the lifeForms and world objects to the file specified
	 * in the filePath variable using the serializer class and notifies the user
	 * if it is successful.
	 */
	protected void saveFile() {
		try {
			if (filePath == null) {
				filePath = "simulationData.ser";
			}
			FileOutputStream fout = new FileOutputStream(filePath);
			ObjectOutputStream oos = new ObjectOutputStream(fout);

			// Write lifeForms object and world object to file
			oos.writeObject(lifeForms);
			oos.writeObject(world);

			oos.flush();
			oos.close();
			System.out.println("Data written to file sucessfully");

			// Show information dialog
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information");
			alert.setHeaderText("Information");
			alert.setContentText("Configuration saved successfully!");
			alert.showAndWait();
		} catch (Exception ex) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Error");
			alert.setContentText("Failed to save configuration!");
			alert.showAndWait();
			return;
		}
	}

	/**
	 * This method adds a new life form based on the information entered into
	 * the form by the user. Coordinates are randomly generated for the life
	 * form in this method
	 */
	protected void addLifeForm() {

		// Define temporary variables
		int initialUniqueID;
		String initialName;
		int initialEnergy;
		int initialX;
		int initialY;

		// Set up dialog
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("New Life Form");
		dialog.setHeaderText("Adding a new life form");

		// Create and add buttons
		ButtonType okButton = new ButtonType("Ok", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes()
				.addAll(okButton, ButtonType.CANCEL);

		final Node okButtonNode = dialog.getDialogPane().lookupButton(okButton);
		okButtonNode.setDisable(true);

		// Create and set up GridPane
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		ComboBox<Species> species = new ComboBox<>();
		species.getItems().setAll(Species.values());

		TextField name = new TextField("");
		name.setPromptText("Name");
		TextField energy = new TextField("");
		energy.setPromptText("Energy");

		// Disable the ok button if any of the fields are empty
		okButtonNode.disableProperty().bind(
				Bindings.isNull(species.valueProperty())
						.or(Bindings.isEmpty(name.textProperty()))
						.or(Bindings.isEmpty(energy.textProperty())));

		// Only allow user to enter numbers in energy field
		energy.addEventFilter(KeyEvent.KEY_TYPED, restrictInput);

		// Add elements to GridPane
		grid.add(new Label("Species:"), 0, 0);
		grid.add(species, 1, 0);
		grid.add(new Label("Name:"), 0, 1);
		grid.add(name, 1, 1);
		grid.add(new Label("Energy:"), 0, 2);
		grid.add(energy, 1, 2);
		grid.add(new Label("(No blank fields permitted)"), 1, 3);
		dialog.getDialogPane().setContent(grid);

		Optional<ButtonType> result = dialog.showAndWait();
		if (result.get() == okButton) {

			// Set variables and create object
			initialUniqueID = lifeForms.size();
			initialName = name.getText();
			initialEnergy = Integer.parseInt(energy.getText());
			initialX = world.generateRandomNumber(world.getWorldSize() - 1, 1);
			initialY = world.generateRandomNumber(world.getWorldSize() - 1, 1);
			createObject(species.getValue(), initialUniqueID, initialName,
					initialEnergy, initialX, initialY);

			// Information Dialog
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("New Life Form");
			alert.setHeaderText("Life form added successfully!");
			alert.setContentText("Would you like to add another life form?");

			Optional<ButtonType> result2 = alert.showAndWait();
			if (result2.get() == ButtonType.OK) {
				addLifeForm();
			} else {
				return;
			}

		}
	}

	/**
	 * This overloaded method adds a new life form based on the information
	 * entered into the form by the user. Coordinates are provided as input
	 * parameters to this method instead of being randomly generated
	 * 
	 * @param x
	 *            The life forms x coordinate
	 * @param y
	 *            The life forms y coordinate
	 */
	protected void addLifeForm(int x, int y) {

		// Define temporary variables
		int initialUniqueID;
		String initialName;
		int initialEnergy;
		int initialX;
		int initialY;

		// Set up dialog
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("New Life Form");
		dialog.setHeaderText("Adding a new life form");

		// Create and add buttons
		ButtonType okButton = new ButtonType("Ok", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes()
				.addAll(okButton, ButtonType.CANCEL);

		final Node okButtonNode = dialog.getDialogPane().lookupButton(okButton);
		okButtonNode.setDisable(true);

		// Create and set up GridPane
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		ComboBox<Species> species = new ComboBox<>();
		species.getItems().setAll(Species.values());

		TextField name = new TextField("");
		name.setPromptText("Name");
		TextField energy = new TextField("");
		energy.setPromptText("Energy");

		// Disable the ok button if any of the fields are empty
		okButtonNode.disableProperty().bind(
				Bindings.isNull(species.valueProperty())
						.or(Bindings.isEmpty(name.textProperty()))
						.or(Bindings.isEmpty(energy.textProperty())));

		// Only allow user to enter numbers in energy field
		energy.addEventFilter(KeyEvent.KEY_TYPED, restrictInput);

		// Add elements to GridPane
		grid.add(new Label("Species:"), 0, 0);
		grid.add(species, 1, 0);
		grid.add(new Label("Name:"), 0, 1);
		grid.add(name, 1, 1);
		grid.add(new Label("Energy:"), 0, 2);
		grid.add(energy, 1, 2);
		grid.add(new Label("(No blank fields permitted)"), 1, 3);
		dialog.getDialogPane().setContent(grid);

		Optional<ButtonType> result = dialog.showAndWait();
		if (result.get() == okButton) {

			// Set values
			initialUniqueID = lifeForms.size();
			initialName = name.getText();
			initialEnergy = Integer.parseInt(energy.getText());
			initialX = x;
			initialY = y;

			createObject(species.getValue(), initialUniqueID, initialName,
					initialEnergy, initialX, initialY);

		}
	}

	/**
	 * This method modifies a life form based on the information entered into
	 * the form by the user.
	 */
	protected void modifyLifeForm() {

		// Create dialog
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Modify Life Form");
		dialog.setHeaderText("Modify Life Form");

		// Create and add buttons
		ButtonType okButton = new ButtonType("Ok", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes()
				.addAll(okButton, ButtonType.CANCEL);

		final Node okButtonNode = dialog.getDialogPane().lookupButton(okButton);
		okButtonNode.setDisable(true);

		// Create GridPane and set parameters
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		ComboBox<String> comboBox = new ComboBox<String>();
		for (int i = 0; i < lifeForms.size(); i++) {
			comboBox.getItems().add(lifeForms.get(i).getName());
		}

		TextField name = new TextField("");
		name.setPromptText("Name");
		TextField energy = new TextField("");
		energy.setPromptText("Energy");

		// Disable ok button if any fields are empty
		okButtonNode.disableProperty().bind(
				Bindings.isNull(comboBox.valueProperty())
						.or(Bindings.isEmpty(name.textProperty()))
						.or(Bindings.isEmpty(energy.textProperty())));

		// Only allow user to enter integer in energy field
		energy.addEventFilter(KeyEvent.KEY_TYPED, restrictInput);

		// Add elements into GridPane
		grid.add(new Label("Please select a bug to modify:"), 0, 0);
		grid.add(comboBox, 1, 0);
		grid.add(new Label("Name:"), 0, 1);
		grid.add(name, 1, 1);
		grid.add(new Label("Energy:"), 0, 2);
		grid.add(energy, 1, 2);
		dialog.getDialogPane().setContent(grid);

		Optional<ButtonType> result = dialog.showAndWait();
		if (result.get() == okButton) {
			for (int i = 0; i < lifeForms.size(); i++) {
				if (comboBox.getValue() == lifeForms.get(i).getName()) {

					// Update life form information
					lifeForms.get(i).setName(name.getText());
					lifeForms.get(i).setEnergy(
							Integer.parseInt(energy.getText()));
				}
			}
		}

	}

	/**
	 * This method removes a life form based on the name of the life form
	 * selected by the user
	 */
	protected void removeLifeForm() {

		// Make a list of life form names for the ChoiceDialog
		ArrayList<String> choices = new ArrayList<String>();
		for (int i = 0; i < lifeForms.size(); i++) {
			choices.add(lifeForms.get(i).getName());
		}

		// Create dialog
		ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0),
				choices);
		dialog.setTitle("Remove bug");
		dialog.setHeaderText("Remove bug");
		dialog.setContentText("Please choose the desired bug to remove:");

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			for (int i = 0; i < lifeForms.size(); i++) {
				if (result.get() == lifeForms.get(i).getName())

					// Remove the life form from the ArrayList
					lifeForms.remove(i);
			}
		}
	}

	/**
	 * This method creates a new configuration for the simulation by clearing
	 * all three ArrayLists. The simulation cycles, world size, food density,
	 * and object density are all reset to default values;
	 */
	protected void newConfiguration() {
		lifeForms.clear();
		foodItems.clear();
		obstacles.clear();
		world.setSimulationCycles(1000);
		world.setWorldSize(10);
		world.setFoodDensity(20);
		world.setObjectDensity(10);
	}

	/**
	 * This method allows the user to edit the configuration settings by
	 * inputting new values for the number of simulation cycles, world size,
	 * food density, and object density
	 */
	protected void editConfiguration() {

		// Create dialog
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Edit Configuration");
		dialog.setHeaderText("Edit Configuration");

		// Create and add buttons
		ButtonType okButton = new ButtonType("Ok", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes()
				.addAll(okButton, ButtonType.CANCEL);

		final Node okButtonNode = dialog.getDialogPane().lookupButton(okButton);
		okButtonNode.setDisable(true);

		// Create GridPane and set properties
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		// Create TextFields
		final TextField simulationCycles = new TextField("");
		simulationCycles.setPromptText("1000");
		TextField worldSize = new TextField("");
		worldSize.setPromptText("10");
		TextField foodDensity = new TextField("");
		foodDensity.setPromptText("30");
		TextField objectDensity = new TextField("");
		objectDensity.setPromptText("10");

		// Only allow integers to be input into these fields
		simulationCycles.addEventFilter(KeyEvent.KEY_TYPED, restrictInput);
		worldSize.addEventFilter(KeyEvent.KEY_TYPED, restrictInput);
		foodDensity.addEventFilter(KeyEvent.KEY_TYPED, restrictInput);
		objectDensity.addEventFilter(KeyEvent.KEY_TYPED, restrictInput);

		// Disable ok button if any fields are blank
		okButtonNode.disableProperty().bind(
				Bindings.isEmpty(simulationCycles.textProperty())
						.or(Bindings.isEmpty(worldSize.textProperty()))
						.or(Bindings.isEmpty(foodDensity.textProperty()))
						.or(Bindings.isEmpty(objectDensity.textProperty())));

		// Add elements to GridPane
		grid.add(new Label("Number of simulation cycles:"), 0, 0);
		grid.add(simulationCycles, 1, 0);
		grid.add(new Label("World size:"), 0, 1);
		grid.add(worldSize, 1, 1);
		grid.add(new Label("Food density:"), 0, 2);
		grid.add(foodDensity, 1, 2);
		grid.add(new Label("Obstacle density:"), 0, 3);
		grid.add(objectDensity, 1, 3);
		grid.add(new Label("(No blank fields permitted)"), 1, 4);
		dialog.getDialogPane().setContent(grid);

		Optional<ButtonType> result = dialog.showAndWait();
		if (result.get() == okButton) {

			// Set world values based on user input
			world.setSimulationCycles(Integer.parseInt(simulationCycles
					.getText()));
			world.setWorldSize(Integer.parseInt(worldSize.getText()));
			world.setFoodDensity(Integer.parseInt(foodDensity.getText()));
			world.setObjectDensity(Integer.parseInt(objectDensity.getText()));
			world.initWorld(lifeForms, foodItems, obstacles);
		}

	}

	/**
	 * This method displays a dialog with the world information provided by the
	 * getWorldInfo() method
	 */
	protected void displayConfiguration() {

		// Create dialog
		Dialog<ButtonType> dialog = new Dialog<ButtonType>();
		dialog.setTitle("Current Configuration");
		dialog.setHeaderText("Current Configuration");

		// Create and add buttons
		ButtonType okButton = new ButtonType("Ok", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(okButton);

		dialog.getDialogPane().setContent(getWorldInfo());
		dialog.showAndWait();
	}

	/**
	 * This method displays a dialog with a table food item information and a
	 * table of obstacle item information. The tables are created and returned
	 * by the getFoodInfo() and getObstacleInfo() methods
	 */
	protected void displayMapInfo() {

		// Create dialog
		Dialog<ButtonType> dialog = new Dialog<ButtonType>();
		dialog.setTitle("Map Information");
		dialog.setHeaderText("Map Information");

		// Create and add buttons
		ButtonType okButton = new ButtonType("Ok", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(okButton);

		// Create labels
		Label food = new Label("Food Items");
		food.setFont(new Font("Arial", 16));
		Label obstacle = new Label("Obstacles");
		obstacle.setFont(new Font("Arial", 16));

		// Add nodes to VBox
		VBox vBox = new VBox(10);
		vBox.getChildren().addAll(food, getFoodInfo(), obstacle,
				getObstacleInfo());
		vBox.alignmentProperty().set(Pos.CENTER);

		dialog.getDialogPane().setContent(vBox);
		dialog.showAndWait();
	}

	/**
	 * This method displays the life form information table which is created and
	 * returned by the getLifeFormInfo() method
	 */
	protected void displayLifeFormInfo() {

		// Create dialog
		Dialog<ButtonType> dialog = new Dialog<ButtonType>();
		dialog.setTitle("Life Form Information");
		dialog.setHeaderText("Life Form Information");

		// Create and add buttons
		ButtonType okButton = new ButtonType("Ok", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(okButton);

		dialog.getDialogPane().setContent(getLifeFormInfo());
		dialog.showAndWait();
	}

	/**
	 * This method returns all of the world information concatenated into one
	 * label
	 * 
	 * @return A label containing the world information
	 */
	private Label getWorldInfo() {
		Label label = new Label();
		label.setText("Number of simulation cycles: "
				+ world.getSimulationCycles() + "\n" + "World size: "
				+ world.getWorldSize() + "\n" + "Food Density: "
				+ world.getFoodDensity() + "\n" + "Obstacle Density: "
				+ world.getObjectDensity() + "\n" + "Number of life forms "
				+ lifeForms.size());

		return label;
	}

	/**
	 * This method creates and returns a table of life form information
	 * 
	 * @return A TableView of life form information
	 */
	private TableView<AEntity> getLifeFormInfo() {

		// Create observable list of life forms
		final ObservableList<AEntity> bugsObservable = FXCollections
				.observableArrayList(lifeForms);

		// Create table
		TableView<AEntity> table = new TableView<AEntity>();

		// Create table columns and set column values
		TableColumn<AEntity, String> idCol = new TableColumn<AEntity, String>(
				"ID");
		idCol.setCellValueFactory(new PropertyValueFactory<AEntity, String>(
				"uniqueID"));
		table.getColumns().add(idCol);

		TableColumn<AEntity, String> nameCol = new TableColumn<AEntity, String>(
				"Name");
		nameCol.setCellValueFactory(new PropertyValueFactory<AEntity, String>(
				"name"));
		table.getColumns().add(nameCol);

		TableColumn<AEntity, String> speciesCol = new TableColumn<AEntity, String>(
				"Species");
		speciesCol
				.setCellValueFactory(new PropertyValueFactory<AEntity, String>(
						"species"));
		table.getColumns().add(speciesCol);

		TableColumn<AEntity, String> energyCol = new TableColumn<AEntity, String>(
				"Energy");
		energyCol
				.setCellValueFactory(new PropertyValueFactory<AEntity, String>(
						"energy"));
		table.getColumns().add(energyCol);

		TableColumn<AEntity, String> xCol = new TableColumn<AEntity, String>(
				"X");
		xCol.setCellValueFactory(new PropertyValueFactory<AEntity, String>("x"));
		table.getColumns().add(xCol);

		TableColumn<AEntity, String> yCol = new TableColumn<AEntity, String>(
				"Y");
		yCol.setCellValueFactory(new PropertyValueFactory<AEntity, String>("y"));
		table.getColumns().add(yCol);

		// Set table properties
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.setItems(bugsObservable);
		table.setPrefSize(400, 150);

		return table;
	}

	/**
	 * This method creates and returns a table of food item information
	 * 
	 * @return A TableView of food item information
	 */
	private TableView<AEntity> getFoodInfo() {

		// Create observable list of food items
		final ObservableList<AEntity> foodItemsObservable = FXCollections
				.observableArrayList(foodItems);

		// Create table
		TableView<AEntity> table = new TableView<AEntity>();

		// Create table columns and set column values
		TableColumn<AEntity, String> typeCol = new TableColumn<AEntity, String>(
				"Type");
		typeCol.setCellValueFactory(new PropertyValueFactory<AEntity, String>(
				"type"));
		table.getColumns().add(typeCol);

		TableColumn<AEntity, String> nutritionCol = new TableColumn<AEntity, String>(
				"Nutrition");
		nutritionCol
				.setCellValueFactory(new PropertyValueFactory<AEntity, String>(
						"nutrition"));
		table.getColumns().add(nutritionCol);

		TableColumn<AEntity, String> xCol = new TableColumn<AEntity, String>(
				"X");
		xCol.setCellValueFactory(new PropertyValueFactory<AEntity, String>("x"));
		table.getColumns().add(xCol);

		TableColumn<AEntity, String> yCol = new TableColumn<AEntity, String>(
				"Y");
		yCol.setCellValueFactory(new PropertyValueFactory<AEntity, String>("y"));
		table.getColumns().add(yCol);

		// Set table properties
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.setItems(foodItemsObservable);
		table.setPrefSize(400, 150);

		return table;
	}

	/**
	 * This method creates and returns a table of obstacle information
	 * 
	 * @return A TableView of obstacle information
	 */
	private TableView<AEntity> getObstacleInfo() {

		// Create observable list of food items
		final ObservableList<AEntity> obstaclesObservable = FXCollections
				.observableArrayList(obstacles);

		// Create table
		TableView<AEntity> table = new TableView<AEntity>();

		// Create table columns and set column values
		TableColumn<AEntity, String> typeCol = new TableColumn<AEntity, String>(
				"Type");
		typeCol.setCellValueFactory(new PropertyValueFactory<AEntity, String>(
				"type"));
		table.getColumns().add(typeCol);

		TableColumn<AEntity, String> xCol = new TableColumn<AEntity, String>(
				"X");
		xCol.setCellValueFactory(new PropertyValueFactory<AEntity, String>("x"));
		table.getColumns().add(xCol);

		TableColumn<AEntity, String> yCol = new TableColumn<AEntity, String>(
				"Y");
		yCol.setCellValueFactory(new PropertyValueFactory<AEntity, String>("y"));
		table.getColumns().add(yCol);

		// Set table properties
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.setItems(obstaclesObservable);
		table.setPrefSize(400, 150);

		return table;
	}

	/**
	 * This event handler restricts the input of text to numbers
	 */
	private EventHandler<KeyEvent> restrictInput = new EventHandler<KeyEvent>() {
		public void handle(KeyEvent inputevent) {
			if (!inputevent.getCharacter().matches("\\d")) {
				inputevent.consume();
			}
		}
	};

}