package uk.ac.rdg.rhys.artificiallifesimulator;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * <h1>DrawWorld.java</h1>
 * <p>
 * This class is used as a helper class to draw the dynamic elements of the user
 * interface such as the map and the information displayed on the information
 * pane. It contains methods to add and remove single entities as they need to
 * be updated rather than continually re-drawing all of the elements on to the
 * world
 * 
 * @author Rhys Streefland
 * @version 1.0
 * @since 2014-01-20
 * @see UserInterface
 */
public class DrawWorld extends Application {

	// Define map objects and variables
	protected GridPane map = new GridPane();
	protected ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();
	private int worldSize;

	// Define infoPane objects
	protected BorderPane infoPaneBorder = new BorderPane();
	protected GridPane infoPane = new GridPane();

	// Define infoPane labels
	private Label infoTitle = new Label("Live Information");
	private Label lifeFormTitle = new Label("Life Form Energy");
	private Label infoLabel = new Label("No information to display");
	private Label lifeFormLabel = new Label("No life forms present");

	// Define speed box related objects
	private VBox speedVBox = new VBox();
	private HBox speedHBox = new HBox();
	private Label speedLabel = new Label("Simulation Speed");
	protected Button speedInc = new Button("+");
	protected Button speedDec = new Button("-");

	/**
	 * This constructor handles the layout of the information pane. It adds the
	 * relevant elements to the information pane and handles their layout,
	 * alignment, and CSS styles.
	 */
	public DrawWorld() {
		// Add CSS styles
		infoPane.getStyleClass().add("InfoPane");
		infoTitle.getStyleClass().add("label-header");
		lifeFormTitle.getStyleClass().add("label-header");
		speedLabel.getStyleClass().add("label-bright");
		speedVBox.getStyleClass().add("Speed");

		// Wrap the text
		infoLabel.setWrapText(true);
		lifeFormLabel.setWrapText(true);
		infoTitle.setWrapText(true);
		lifeFormTitle.setWrapText(true);

		// Add labels to GridPane
		infoPane.add(infoTitle, 0, 0);
		infoPane.add(infoLabel, 0, 1);
		infoPane.add(lifeFormTitle, 0, 2);
		infoPane.add(lifeFormLabel, 0, 3);

		// Set alignment
		speedLabel.alignmentProperty().set(Pos.CENTER);
		speedHBox.alignmentProperty().set(Pos.CENTER);
		speedVBox.alignmentProperty().set(Pos.CENTER);

		// Add children
		speedVBox.getChildren().addAll(speedLabel, speedHBox);
		speedHBox.getChildren().addAll(speedDec, speedInc);

		// Set infoPane border nodes
		infoPaneBorder.setCenter(infoPane);
		infoPaneBorder.setBottom(speedVBox);
	}

	/**
	 * This method generates the map grid which is used to display the world. It
	 * initially fills the grid with transparent squares to ensure even space
	 * distribution. It then calls the updateObstacles(), createFood(), and
	 * updatelifeForms() methods which are responsible for populating the blank
	 * world with entities
	 * 
	 * @param lifeForms
	 *            The ArrayList of life form objects
	 * @param world
	 *            The world object
	 * @param foodItems
	 *            The ArrayList of food item objects
	 * @param obstacles
	 *            The ArrayList of obstacle objects
	 */
	protected void createMap(ArrayList<AEntity> lifeForms, AWorld world,
			ArrayList<AEntity> foodItems, ArrayList<AEntity> obstacles) {

		worldSize = world.getWorldSize();

		// Clear Rectangles and GridPane
		rectangles.clear();
		map.getChildren().clear();

		// Clear all contraints
		for (int i = 0; i < world.getWorldSize(); i++) {
			map.getColumnConstraints().clear();
			map.getRowConstraints().clear();
		}

		for (int x = 0; x < worldSize; x++) {
			for (int y = 0; y < worldSize; y++) {

				final Rectangle rect = new Rectangle();
				rect.setX(x);
				rect.setY(y);
				rect.setFill(Color.TRANSPARENT);
				rect.heightProperty().bind(
						map.heightProperty().divide(worldSize));
				rect.widthProperty()
						.bind(map.widthProperty().divide(worldSize));
				rectangles.add(rect);

				// On hover over
				rect.setOnMouseEntered(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {
						rect.setFill(Color.WHITE);
						rect.setOpacity(0.3);
					}
				});

				// On hover off
				rect.setOnMouseExited(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {
						rect.setFill(Color.TRANSPARENT);
					}
				});

				// Add rectangle to GridPane and align to center
				map.add(rect, x, y);
				GridPane.setHalignment(rect, HPos.CENTER);

			}
		}

		// Calculate row and column constraints
		for (int i = 0; i < worldSize; i++) {
			map.getColumnConstraints().add(
					new ColumnConstraints(5, Control.USE_COMPUTED_SIZE,
							Double.POSITIVE_INFINITY, Priority.ALWAYS,
							HPos.CENTER, true));
			map.getRowConstraints().add(
					new RowConstraints(5, Control.USE_COMPUTED_SIZE,
							Double.POSITIVE_INFINITY, Priority.ALWAYS,
							VPos.CENTER, true));
		}

		// Call methods to populate
		updateObstacles(obstacles);
		createFood(world, foodItems);
		updateLifeForms(lifeForms);
	}

	/**
	 * This method adds each food item to the map by calling the getImage()
	 * method for each food item
	 * 
	 * @param world
	 *            The world object
	 * @param foodItems
	 *            The ArrayList of food item objects
	 */
	private void createFood(AWorld world, ArrayList<AEntity> foodItems) {

		// Define temporary variables
		int x = 0;
		int y = 0;

		for (int i = 0; i < foodItems.size(); i++) {

			// Get food item coordinates
			x = foodItems.get(i).getX();
			y = foodItems.get(i).getY();

			// Add food item ImageView to GridPane and align to center
			map.add(foodItems.get(i).getImage(map, worldSize), x, y);
			GridPane.setHalignment(foodItems.get(i).getImage(map, worldSize),
					HPos.CENTER);
		}
	}

	/**
	 * This method removes the food items that are no longer needed on the map.
	 * This method also adds new food items if the food item's getDrawn()
	 * function returns false. This method was implemented because it is much
	 * more efficient to add and remove single food items rather than redrawing
	 * every item every cycle.
	 * 
	 * @param lifeForms
	 *            The ArrayList of life form objects
	 * @param foodItems
	 *            The ArrayList of food item objects
	 */
	protected void updateFood(ArrayList<AEntity> lifeForms,
			ArrayList<AEntity> foodItems) {

		// If a food item is not yet drawn
		for (int j = 0; j < foodItems.size(); j++) {
			if (foodItems.get(j).getDrawn() == false) {

				// Add food items ImageView to GridPane and align to center
				map.add(foodItems.get(j).getImage(map, worldSize), foodItems
						.get(j).getX(), foodItems.get(j).getY());
				GridPane.setHalignment(foodItems.get(j)
						.getImage(map, worldSize), HPos.CENTER);
			}
		}

		// If a food item needs to be removed
		for (int i = 0; i < lifeForms.size(); i++) {
			for (int j = 0; j < foodItems.size(); j++) {

				if (lifeForms.get(i).getLastFoodX() == foodItems.get(j).getX()
						&& lifeForms.get(i).getLastFoodY() == foodItems.get(j)
								.getY()) {

					// Remove food item ImageView from GridPane
					map.getChildren().remove(foodItems.get(j).getImage(map, i));
					foodItems.get(j);
				}
			}
		}
	}

	/**
	 * This method removes and re-creates all life forms every time it is called
	 * 
	 * @param lifeForms
	 *            The ArrayList of life form objects
	 */
	protected void updateLifeForms(ArrayList<AEntity> lifeForms) {

		// Define temporary variables
		int x;
		int y;

		// For every life form
		for (int i = 0; i < lifeForms.size(); i++) {

			// Remove life form
			map.getChildren().remove(lifeForms.get(i).getImage(map, worldSize));

			// If life form is not alive - remove it from the ArrayList
			if (lifeForms.get(i).getIsAlive() == false) {
				lifeForms.remove(i);
				break;
			}

			// Get life form coordinates
			x = lifeForms.get(i).getX();
			y = lifeForms.get(i).getY();

			// Add life form to GridPane and align to center
			map.add(lifeForms.get(i).getImage(map, worldSize), x, y);
			GridPane.setHalignment(lifeForms.get(i).getImage(map, worldSize),
					HPos.CENTER);
		}
	}

	/**
	 * This method adds each obstacle to the map by calling the getImage()
	 * method for each obstacle
	 * 
	 * @param obstacles
	 *            The ArrayList of obstacle objects
	 */
	protected void updateObstacles(ArrayList<AEntity> obstacles) {

		// Define temporary variables
		int x = 0;
		int y = 0;

		// Add every obstacle to the GridPane and align to center
		for (int i = 0; i < obstacles.size(); i++) {
			x = obstacles.get(i).getX();
			y = obstacles.get(i).getY();
			map.add(obstacles.get(i).getImage(map, worldSize), x, y);
			GridPane.setHalignment(obstacles.get(i).getImage(map, worldSize),
					HPos.CENTER);
		}
	}

	/**
	 * This updates the information pane labels with the relevant information
	 * about the simulation and life forms
	 * 
	 * @param world
	 *            The world object
	 * @param lifeForms
	 *            The ArrayList of life forms
	 * @param displayMap
	 *            This property dictates whether the map updates should be
	 *            displayed
	 * @param speedMultiplier
	 *            This property dictates the speed at which the simulation
	 *            should run
	 */
	protected void updateInfoPane(AWorld world, ArrayList<AEntity> lifeForms,
			boolean displayMap, double speedMultiplier) {

		// Force speedMultiplier to 1dp
		String sM = String.format("%.1f", speedMultiplier);

		// Define temporary variables
		String displayMapString = "ON";
		String bugsString = "";

		if (displayMap == false)
			displayMapString = "OFF";

		/*
		 * Create the bug string by concatenating information from all bugs in
		 * the ArrayList
		 */
		for (int i = 0; i < lifeForms.size(); i++) {
			bugsString = bugsString + lifeForms.get(i).toString() + "\n";
		}

		// Set the info label text string with map information
		infoLabel.setText("Map Updates: " + displayMapString + "\n"
				+ "Current cycle: " + world.getCurrentCycle() + "/"
				+ world.getSimulationCycles() + "\nSpeed multiplier: " + sM
				+ "\n\n");

		lifeFormLabel.setText(bugsString);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub

	}
}