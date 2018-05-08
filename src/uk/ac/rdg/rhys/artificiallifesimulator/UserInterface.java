package uk.ac.rdg.rhys.artificiallifesimulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.Scanner;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.JOptionPane;

/**
 * <h1>UserInterface.java</h1>
 * <p>
 * This class is responsible for the majority of the JavaFX UI elements of the
 * program. The menus and tool bar are created in this class. Nearly all UI
 * events are handled in this class. This class handles all of the static UI
 * elements.
 * 
 * The responsibility of drawing the UI representation of the world and updating
 * the information pane (dynamic UI elements) is delegated to the DrawWorld
 * helper class
 * 
 * @author Rhys Streefland
 * @version 1.0
 * @since 2014-01-20
 * @see Simulation, DrawWorld
 */
public class UserInterface extends Application {

	private Simulation simulation = new Simulation();
	private DrawWorld draw = new DrawWorld();
	private BorderPane border = new BorderPane();
	private VBox topContainer = new VBox();
	private VBox bottomContainer = new VBox();

	private Label status = new Label("Ready to run simulation...");
	private boolean displayMap;
	private double speedMultiplier = 1;

	/**
	 * This method displays an information pane using the Dialog class. This
	 * method was written solely to reduce code repetition
	 * 
	 * @param title
	 *            The title text of the dialog
	 * @param header
	 *            The header text of the dialog
	 * @param body
	 *            The body text of the dialog
	 */
	private void informationPane(String title, String header, String body) {
		Dialog<ButtonType> dialog = new Dialog<ButtonType>();
		dialog.setTitle(title);
		dialog.setHeaderText(header);

		ButtonType okButton = new ButtonType("Ok", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(okButton);

		Label label = new Label(body);
		dialog.getDialogPane().setContent(label);
		dialog.showAndWait();
	}

	/**
	 * This method manages all of the map updating methods in order to reduce
	 * code repetition. A summary of each option is given below:
	 * 
	 *<p> 1 - Create the whole map (obstacles, food items, and life forms) 
	 *<p> 2 - Update life forms 
	 *<p> 3 - Update food items 
	 *<p> 4 - Update life forms & food items
	 *<p> 5 - Update the information pane
	 * 
	 * @param option
	 *            This parameter controls which methods are called
	 */
	private void manageMap(int option) {
		switch (option) {
		case 1:
			draw.createMap(simulation.lifeForms, simulation.world,
					simulation.foodItems, simulation.obstacles);
			mouseEvent();
			break;
		case 2:
			draw.updateLifeForms(simulation.lifeForms);
			break;
		case 3:
			draw.updateFood(simulation.lifeForms, simulation.foodItems);
			break;
		case 4:
			draw.updateLifeForms(simulation.lifeForms);
			draw.updateFood(simulation.lifeForms, simulation.foodItems);
			break;
		case 5:
			draw.updateInfoPane(simulation.world, simulation.lifeForms,
					displayMap, speedMultiplier);
			break;
		}
	}

	/**
	 * This method calls the addLifeForm(int,int) method every time the mouse is
	 * clicked somewhere on the map
	 */
	private void mouseEvent() {
		// For each rectangle on the screen
		for (int i = 0; i < draw.rectangles.size(); i++) {
			final Rectangle rect = draw.rectangles.get(i);

			// If the rectangle is clicked
			rect.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {

					// Add a life form, then update the map
					int x = (int) rect.getX();
					int y = (int) rect.getY();
					simulation.addLifeForm(x, y);
					manageMap(2);
					manageMap(5);
				}
			});
		}
	}

	/**
	 * This method allows the user to choose a .ser file to open using the
	 * FileChooser class. The value of the selected file is stored in the
	 * filePath variable and used by the readFile() method
	 */
	private void openFile() {
		// Open the file chooser - only allow users to select .ser files
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Existing Configuration");
		fileChooser.getExtensionFilters()
				.addAll(new ExtensionFilter("Artificial Life Simulator files",
						"*.ser"));

		File selectedFile = fileChooser.showOpenDialog(null);
		if (selectedFile != null) {
			// Call readFile method, then update map
			simulation.filePath = selectedFile.getAbsolutePath();
			simulation.readFile();
			simulation.world.initWorld(simulation.lifeForms,
					simulation.foodItems, simulation.obstacles);
			manageMap(1);
			manageMap(5);
		}
	}

	/**
	 * This method allows the user to choose a file to save to using the
	 * FileChooser class. The value of the selected file is stored in the
	 * filePath variable and used by the saveFile() method
	 */
	private void saveFileAs() {
		// Open the file chooser - only allow users to save .ser files
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Configuration As");
		fileChooser.getExtensionFilters()
				.addAll(new ExtensionFilter("Artificial Life Simulator files",
						"*.ser"));

		File selectedFile = fileChooser.showSaveDialog(null);
		if (selectedFile != null) {
			// Call readFile method, then update map
			simulation.filePath = selectedFile.getAbsolutePath();
			simulation.saveFile();
		}
	}

	/**
	 * This method tests if the DO_NOT_DELETE file is present and sets the
	 * filePath variable based on the contents of the file. It then attempts to
	 * load existing configuration from the file specified in the filePath
	 * variable
	 */
	private void loadExistingConfiguration() {

		try {
			// Read filePath from DO_NOT_DELETE file
			Scanner scan = new Scanner(new File("DO_NOT_DELETE"));
			simulation.filePath = scan.useDelimiter("\\Z").next();
			scan.close();
		} catch (FileNotFoundException e1) {
			System.out
					.println("WARNING: DO_NOT_DELETE file not found \nIgnore this warning if this is the first time the program has been run");
		}

		if (simulation.readFile() == 1) {
			// Create new warning dialog
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Information");
			alert.setHeaderText("No existing configuration detected");
			alert.setContentText("Would you like to load the default configuration? \n(Program will exit if you click cancel)");

			// Add buttons to dialog
			ButtonType buttoncancel = new ButtonType("Cancel",
					ButtonData.CANCEL_CLOSE);
			ButtonType buttonyes = new ButtonType("Yes", ButtonData.YES);
			alert.getButtonTypes().setAll(buttoncancel, buttonyes);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == buttonyes) {
				simulation.newConfiguration();
			} else {
				Platform.exit();
			}
		}
	}

	/**
	 * This is the start method which creates all of the UI elements, sets the
	 * scene, and configures the primaryStage. It also handles all UI related
	 * events
	 * 
	 * @param primaryStage
	 *            The stage on which the application UI is drawn
	 */
	@Override
	public void start(Stage primaryStage) {

		displayMap = true;

		// Try to load existing configuration if possible
		loadExistingConfiguration();

		// Set up MAIN MENU
		MenuBar mainMenu = new MenuBar();
		mainMenu.getStyleClass().add("menu");
		topContainer.getChildren().add(mainMenu);
		border.setTop(topContainer);

		// Create FILE sub-menu.
		Menu fileMenu = new Menu("File");
		MenuItem newConfig = new MenuItem("New configuration");
		MenuItem openConfig = new MenuItem("Open configuration file");
		MenuItem saveConfig = new MenuItem("Save");
		MenuItem saveConfigAs = new MenuItem("Save as");
		MenuItem exit = new MenuItem("Exit");
		fileMenu.getItems().addAll(newConfig, openConfig, saveConfig,
				saveConfigAs, exit);

		// Create VIEW sub-menu
		Menu viewMenu = new Menu("View");
		MenuItem displayConfig = new MenuItem("Display configuration");
		MenuItem editConfig = new MenuItem("Edit configuration");
		MenuItem lifeFormInfo = new MenuItem("Display life form info");
		MenuItem mapInfo = new MenuItem("Display map info");
		viewMenu.getItems().addAll(displayConfig, editConfig, lifeFormInfo,
				mapInfo);

		// Create EDIT sub-menu
		Menu editMenu = new Menu("Edit");
		MenuItem addLifeForm = new MenuItem("Add life form");
		MenuItem removeLifeForm = new MenuItem("Remove life form");
		MenuItem modifyLifeForm = new MenuItem("Modify life form");
		editMenu.getItems().addAll(addLifeForm, removeLifeForm, modifyLifeForm);

		// Create SIMULATION sub-menu
		Menu simulationMenu = new Menu("Simulation");
		MenuItem start = new MenuItem("Start");
		MenuItem stop = new MenuItem("Stop");
		MenuItem pauseRestart = new MenuItem("Pause/Restart");
		MenuItem reset = new MenuItem("Reset");
		MenuItem toggleMapDisplay = new MenuItem("Toggle map display");
		simulationMenu.getItems().addAll(start, stop, pauseRestart, reset,
				toggleMapDisplay);

		// Create HELP sub-menu.
		Menu helpMenu = new Menu("Help");
		MenuItem applicationInfo = new MenuItem("Application info");
		MenuItem authorInfo = new MenuItem("Author info");
		MenuItem aboutInfo = new MenuItem("About");
		helpMenu.getItems().addAll(applicationInfo, authorInfo, aboutInfo);
		mainMenu.getMenus().addAll(fileMenu, viewMenu, editMenu,
				simulationMenu, helpMenu);

		// Set up TOOLBAR
		ToolBar toolBar = new ToolBar();
		toolBar.getStyleClass().add("background");
		bottomContainer.getChildren().add(toolBar);
		border.setBottom(bottomContainer);

		// Create TOOLBAR buttons
		Button startButton = new Button("Start");
		Button pauseButton = new Button("Pause");
		Button stopButton = new Button("Stop");
		Button resetButton = new Button("Reset");
		toolBar.getItems().addAll(startButton, pauseButton, stopButton,
				resetButton, status);

		// Draw Map and Information Pane
		simulation.world.initWorld(simulation.lifeForms, simulation.foodItems,
				simulation.obstacles);
		draw.infoPane.minWidthProperty().bind(border.widthProperty().divide(5));
		draw.infoPane.maxWidthProperty().bind(border.widthProperty().divide(4));
		manageMap(1);
		manageMap(5);
		border.setCenter(draw.map);
		border.setRight(draw.infoPaneBorder);

		// Simulation timeline
		final Timeline simTimeline = new Timeline();
		KeyFrame frame = new KeyFrame(Duration.millis(500),
				new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent ae) {
						if (simulation.lifeForms.size() == 0) {
							simTimeline.stop();
						}
						simulation.runSimulation();
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								// Update information pane only
								manageMap(5);
								if (displayMap == true) {
									// Update map as well
									manageMap(4);
								}
							}
						});
					}
				});
		// Add keyframe to timeline
		simTimeline.getKeyFrames().add(frame);
		// Set timeline rate
		simTimeline.setRate(speedMultiplier);

		// When simulation finishes
		simTimeline.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				status.setText("Simulation finished");
				simulation.world.resetCurrentCycle();
				manageMap(1);
			}
		});

		draw.speedDec.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (speedMultiplier < 0.1) {
					System.out
							.println("Cannot decrease simulation speed further");
				} else {
					// Set simulation rate based on speedMultiplier value
					speedMultiplier = speedMultiplier - 0.5;
					simTimeline.setRate(speedMultiplier);
					if (simTimeline.getStatus() != Animation.Status.RUNNING) {
						manageMap(5);
					}
				}
			}
		});

		draw.speedInc.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (speedMultiplier == 10.0) {
					simTimeline.pause();
					informationPane(
							"Information",
							"Information",
							"Increasing the speed multiplier above 10 may not\nincrease the simulation speed much further, especially\n if your computer is slow");
					simTimeline.play();
				}

				// Set simulation rate based on speedMultiplier value
				speedMultiplier = speedMultiplier + 0.5;
				simTimeline.setRate(speedMultiplier);
				if (simTimeline.getStatus() != Animation.Status.RUNNING) {
					manageMap(5);
				}
			}
		});
		/* END SIMULATION EVENTS */

		/* START TOOLBAR BUTTON EVENTS */
		startButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				simTimeline.setCycleCount(simulation.world
						.getSimulationCycles());
				status.setText("Simulation running");
				simTimeline.play();
			}

		});
		pauseButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				status.setText("Simulation paused");
				simTimeline.pause();
			}

		});
		stopButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				status.setText("Simulation finished");
				simTimeline.stop();
			}

		});
		resetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (simTimeline.getStatus() == Animation.Status.RUNNING) {
					simTimeline.pause();
					informationPane("Information", "Information",
							"Please stop the simulation before attempting to reset the map");
					simTimeline.play();
				} else {
					simulation.world.initWorld(simulation.lifeForms,
							simulation.foodItems, simulation.obstacles);
					manageMap(5);
					manageMap(1);
					status.setText("Ready to run simulation...");
				}
			}

		});
		/* END TOOOLBAR BUTTON EVENTS */

		/* START FILE MENU BUTTON EVENTS */
		newConfig.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				simulation.newConfiguration();
				manageMap(1);
				manageMap(5);
			}

		});

		openConfig.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				openFile();
			}

		});

		saveConfig.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				simulation.saveFile();
			}

		});

		saveConfigAs.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				saveFileAs();
			}

		});

		exit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Platform.exit();
			}

		});
		/* END FILE MENU BUTTON EVENTS */

		/* START VIEW MENU BUTTON EVENTS */
		displayConfig.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				simulation.displayConfiguration();
			}
		});

		editConfig.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				simulation.editConfiguration();
				manageMap(1);
				manageMap(5);
			}
		});

		lifeFormInfo.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				simulation.displayLifeFormInfo();
			}
		});

		mapInfo.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				simulation.displayMapInfo();
			}
		});

		/* END VIEW MENU BUTTON EVENTS */

		/* START EDIT MENU BUTTON EVENTS */
		addLifeForm.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				simulation.addLifeForm();
				manageMap(1);
				manageMap(5);
			}

		});

		removeLifeForm.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				simulation.removeLifeForm();
				manageMap(1);
				manageMap(5);
			}
		});

		modifyLifeForm.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				simulation.modifyLifeForm();
				manageMap(1);
				manageMap(5);
			}
		});
		/* END EDIT MENU BUTTON EVENTS */

		/* START SIMULATION MENU BUTTON EVENTS */
		start.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				simTimeline.setCycleCount(simulation.world
						.getSimulationCycles());
				status.setText("Simulation running");
				simTimeline.play();
			}
		});

		stop.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				status.setText("Simulation finished");
				simTimeline.stop();
			}
		});

		pauseRestart.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (simTimeline.getStatus() == Animation.Status.RUNNING) {
					status.setText("Simulation paused");
					simTimeline.pause();
				} else if (simTimeline.getStatus() == Animation.Status.PAUSED) {
					status.setText("Simulation running");
					simTimeline.play();
				}
			}
		});

		reset.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				simulation.world.initWorld(simulation.lifeForms,
						simulation.foodItems, simulation.obstacles);
				manageMap(5);
				manageMap(1);
				status.setText("Ready to run simulation...");
			}
		});

		toggleMapDisplay.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (displayMap == true) {
					simTimeline.setRate(Integer.MAX_VALUE);
					displayMap = false;
					manageMap(5);
				} else {
					simTimeline.setRate(speedMultiplier);
					displayMap = true;
					manageMap(5);
				}

			}
		});

		/* END SIMULATION MENU BUTTON EVENTS */

		/* START HELP MENU BUTTON EVENTS */

		applicationInfo.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				informationPane(
						"Application Information",
						"Application Information",
						"To start a simulation you must first add one or more\nlife forms using the option in the edit menu."
								+ "\n\nIf you require more help to use this application,\nplease see the documentation");
			}

		});

		authorInfo.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				informationPane("Author Information", "Author Information",
						"Created by Rhys Streefland for SE2JA11");
			}

		});

		aboutInfo.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				informationPane(
						"About",
						"About",
						"Artificial Life Simulator v1.0\n\nAll images used under education copyright exception");
			}

		});

		/* END HELP MENU BUTTON EVENTS */

		// Application shutdown event
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				try {
					// Try and write filePath to DO_NOT_DELETE file
					PrintWriter out = new PrintWriter("DO_NOT_DELETE");
					out.println(simulation.filePath);
					out.flush();
					out.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

			}
		}));

		// Make the scene and apply CSS
		final Scene scene = new Scene(border, 600, 500);
		scene.getStylesheets().add(
				UserInterface.class.getResource("style.css").toExternalForm());
		draw.map.getStyleClass().add("world");

		// Setup the Stage
		primaryStage.setTitle("Artificial Life Simulator");
		primaryStage.setScene(scene);
		primaryStage.setMinHeight(500);
		primaryStage.setMinWidth(600);
		primaryStage.getIcons().add(
				new Image(getClass().getResourceAsStream("logo.png")));
		primaryStage.show();

	}

	/**
	 * The main() method is ignored in correctly deployed JavaFX application.
	 * main() serves only as fallback in case the application can not be
	 * launched through deployment artifacts, e.g., in IDEs with limited FX
	 * support. NetBeans ignores main().
	 * 
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		String java = System.getProperty("java.specification.version");
		double version = Double.valueOf(java);
		if (version < 1.8) {
			JOptionPane
					.showMessageDialog(
							null,
							"Java 8 is required to run this application!\nPlease install JRE 8 and try again...",
							"Error", JOptionPane.ERROR_MESSAGE);
			Platform.exit();
		}
		launch(args);
	}
}