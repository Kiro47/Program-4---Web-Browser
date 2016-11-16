
// IMPORTS
// These are some classes that may be useful for completing the project.
// You may have to add others.
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * @author James Helm
 * @version 1.0 Date Last Modified : 11 - 16 - 2016
 * 
 * 
 *          The main class for Program5. Program5 constructs the JavaFX window
 *          and handles interactions with the dynamic components contained
 *          therein.
 */
public class Program4 extends Application {
	// INSTANCE VARIABLES
	// These variables are included to get you started.
	private Stage stage = null; // Main stage for the program.
	private WebView view = null; // Main web view to access the Internet
									// through.
	private WebEngine webEngine = null; // Main web engine to handle processing.
	private TextField statusbar = null; // StatusBar Text to be changed way too
										// much.

	// My variables.
	private ArrayList<String> history = new ArrayList<String>(); // Array list
																	// to hold
																	// history.
	private String defaultAddress = "http://www.bing.com"; // Default address, a
															// home page.
	private TextField address = new TextField(defaultAddress); // Text field to
																// hold the
																// address of
																// the page.
	private Pane window = null; // Window that everything hides in.
	private int level = 0; // Level of history of the currently loaded page.

	private int height = 600; // Standard starting height.
	private int width = 800; // Standard starting width.

	private Button backButton = new Button(); // Global back button
	private Button forwardButton = new Button(); // Global forward button.

	private static Image forward = new Image("https://i.gyazo.com/802bef2d69de6f917d4a0b66985f099b.png", 40, 40, true, true); // pretty
																														// button
																														// image
	private static Image back = new Image("https://i.gyazo.com/d72ae41fc374cf0ee62a3ef739e7c95d.jpg", 40, 40, true, true); // pretty
																													// button
																													// image.
	private static Image back_gray = new Image("https://i.gyazo.com/15ee5f494367bb1c3c5291c9c6626e88.png", 40, 40, true, true); // pretty
																															// grayed
																															// out
																															// button
																															// image.
	private static Image forward_gray = new Image("https://i.gyazo.com/2b6d3a99b9a582ee156d2ef143be7e9b.png", 40, 40, true,
			true); // pretty grayed out button image.

	private static Image icon = new Image("https://i.gyazo.com/d47d64119c8c0821abe581e575bf43d5.png"); // Icon
	/**
	 * bindChain true); true);
	 * 
	 * @param statusBar
	 *            the StatusBar Box
	 * @param view
	 *            The Viewing Box
	 * @param commandBar
	 *            The command bar Box
	 * @param verticalLayout
	 *            The layout everything goes in.
	 * 
	 *            Sets up all the binding and limitations for the problems.
	 */
	private void bindChain(HBox statusBar, HBox view, HBox commandBar, VBox verticalLayout) {
		// heights to bind to
		ReadOnlyDoubleProperty heighto = stage.getScene().heightProperty();
		// Width to bind to
		ReadOnlyDoubleProperty widtho = stage.getScene().widthProperty();

		// Main window
		// Bind to stage
		window.prefHeightProperty().bind(heighto);
		// Bind to stage
		window.prefWidthProperty().bind(widtho);

		// Assign variables for the window property.
		// height
		ReadOnlyDoubleProperty height = window.heightProperty();
		// width
		ReadOnlyDoubleProperty width = window.widthProperty();

		// Each component
		// binds vertical box to height.
		verticalLayout.prefHeightProperty().bind(height);
		// Bind vertical box width.
		verticalLayout.prefWidthProperty().bind(width);

		// Bind more things. and make it look pretty.
		statusBar.prefHeightProperty().bind(height.multiply(0.05));
		// All the binding
		statusBar.prefWidthProperty().bind(width);

		// Bind again, sizing properly.
		view.prefHeightProperty().bind(height.multiply(0.90));
		// Every binding.
		view.prefWidthProperty().bind(width);

		// Bind heights sized properly.
		this.view.prefHeightProperty().bind(height.multiply(0.9));
		// More Binding
		this.view.prefWidthProperty().bind(width);

		// another binding for size.
		commandBar.prefHeightProperty().bind(height.multiply(0.05));
		// STAY EXTENDED ACROSS THE SCREEN !
		commandBar.prefWidthProperty().bind(width);

		// Bind web page Title to page.
		stage.titleProperty().bind(webEngine.titleProperty());
	}

	// HELPER METHODS
	/**
	 * Retrieves the value of a command line argument specified by the index.
	 * 
	 * @param index
	 *            - position of the argument in the args list.
	 * @return The value of the command line argument.
	 */
	private String getParameter(int index) {
		Parameters params = getParameters();
		List<String> parameters = params.getRaw();
		return !parameters.isEmpty() ? parameters.get(index) : "";
	}

	/**
	 * makeHtmlView
	 * 
	 * Creates a WebView which handles mouse and some keyboard events, and
	 * manages scrolling automatically, so there's no need to put it into a
	 * ScrollPane. The associated WebEngine is created automatically at
	 * construction time.
	 * 
	 * @return browser - a WebView container for the WebEngine.
	 */
	private WebView makeHtmlView() {
		view = new WebView();
		webEngine = view.getEngine();
		return view;
	}

	/**
	 * makeStatusBar
	 * 
	 * @return statusbarPane - the HBox layout that contains the statusbar.
	 * 
	 *         Generates the status bar layout and text field.
	 */
	private HBox makeStatusBar() {
		HBox statusbarPane = new HBox();
		statusbarPane.setPadding(new Insets(5, 4, 5, 4));
		statusbarPane.setSpacing(10);
		statusbarPane.setStyle("-fx-background-color: #336699;");
		statusbar = new TextField();
		// Status bar is to show. no reason to allow them to edit it.
		statusbar.setEditable(false);

		HBox.setHgrow(statusbar, Priority.ALWAYS);
		statusbarPane.getChildren().addAll(statusbar);
		return statusbarPane;
	}

	/**
	 * setupCommandBar
	 * 
	 * @return Returns an HBox with a GUI layout for command processing.
	 * 
	 *         sets up the commandBar layout.
	 */
	private HBox setupCommandBar() {
		// Layout box
		HBox commandBar = new HBox();
		// Fill it up, and size it properly.
		commandBar.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, new CornerRadii(0), new Insets(0))));
		// yay sizing.
		commandBar.setPrefSize(width, height * 0.05);

		// Buttons for controls.
		// Make the back button
		Button back = this.backButton;
		// re reference for instance.
		this.backButton = back;
		// MAKE IT PRETTY
		back.setGraphic(new ImageView(this.back_gray));
		// Make the forward button.
		Button forward = this.forwardButton;
		// re reference for instance.
		this.forwardButton = forward;
		// MAKE IT PRETTY
		forward.setGraphic(new ImageView(this.forward_gray));
		// Make a go button.
		Button go = new Button();
		// Make it pretty.
		go.setGraphic(new ImageView(
				new Image("https://i.gyazo.com/747841ba38d0ab903f1a6939322acb76.png", 40, 40, true, true)));
		// Home Button for reasons.
		Button home = new Button();
		// Home button should be a house.
		home.setGraphic(new ImageView(
				new Image("https://i.gyazo.com/1c069a908c0f512b91278616f7554751.png", 40, 40, true, true)));
		// Help button, simple and small.
		Button help = new Button("?");

		// Add Events for buttons.
		buttonEvents(back, forward, go, home, help);

		// Make the address bar fairly normally sized.
		address.prefWidthProperty().bind(commandBar.prefWidthProperty().multiply(0.65));
		// and across the screen.
		address.prefHeightProperty().bind(commandBar.prefHeightProperty());

		// Add spacing to make it look decent.
		commandBar.setSpacing(3.0);
		// so you can actually tell where it is.
		address.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, new CornerRadii(0), new Insets(0))));

		// add in event to load page when enter key is hit.
		address.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				// If I hit the enter button.
				if (event.getCode().equals(KeyCode.ENTER)) {
					// Literally, just load the freaking page.
					goToPage(address.getText(), null);
				}

			}

		});
		// Add everything in.
		commandBar.getChildren().addAll(back, forward, address, go, home, help);
		// Return the command bar.
		return commandBar;
	}

	/**
	 * goToPage
	 * 
	 * @param URL
	 *            The URL to go to.
	 * 
	 *            method to send someone to a page.
	 */
	private void goToPage(String URL, String path) {
		// If it doesn't exist.
		if (URL == null || URL.isEmpty() || URL.trim().isEmpty()) {
			// Do nothing.
		} else {
			// If it doesn't contain the http proto,
			if ((!(URL.contains("http://"))) && (!(URL.contains("https://")))) {
				// add it in.
				URL = "http://" + URL;
			}

			// If the path is null
			if (path == null) {
				// add it to history bar.
				history.add(URL);
				// Sets the level to highest in history.
				// Uses this instead of ++ to mind
				// when people go to a new page midway in history.
				level = history.size() - 1;
			}

			// LOAD THE PAGE
			webEngine.load(URL);

			// New page, so back button should be green again.
			
			// If you're on the original page.
			if (level - 1 == 0) {
				// gray out button
				this.backButton.setGraphic( new ImageView(this.back_gray));
			}
			// otherwise
			else {
				// color button
				this.backButton.setGraphic(new ImageView(this.back));
			}
			// Do stuff for the forward button.
			if (level + 1  == (history.size() - 1)) {
				// set the forward button to grayed out. because there's nothing
				// to to.
				this.forwardButton.setGraphic(new ImageView(this.forward_gray));
			} else {
				// else set it green and let it do it's thing.
				this.forwardButton.setGraphic(new ImageView(this.forward));
			}
		}
	}

	/**
	 * buttonEvents
	 * 
	 * @param back
	 *            back button
	 * @param forward
	 *            forward button
	 * @param go
	 *            go button
	 * @param home
	 *            home button
	 * @param help
	 *            help button
	 * 
	 *            Starts the event listeners for the specific buttons in the
	 *            commandBar.
	 */
	private void buttonEvents(Button back, Button forward, Button go, Button home, Button help) {

		// If the go button is clicked.
		go.setOnMouseClicked(new EventHandler<MouseEvent>() {

			// Do your thing
			@Override
			public void handle(MouseEvent event) {
				// If you left click.
				if (event.getButton().equals(MouseButton.PRIMARY)) {
					// Go to page in the address bar.
					goToPage(address.getText(), null);
				}
				// if you right click.
				else if (event.getButton().equals(MouseButton.SECONDARY)) {
					// plan to allow changes of home page.
				}
				// odd clicks, multiple button mice and scroll clicks.
				else {
					// maybe do something on scroll wheel click? maybe not?
				}
			}
		});

		// On home button clicked.
		home.setOnMouseClicked(new EventHandler<MouseEvent>() {

			// do your thing.
			@Override
			public void handle(MouseEvent event) {
				// left click here.
				if (event.getButton().equals(MouseButton.PRIMARY)) {
					// go back to home page.
					goToPage(defaultAddress, null);
				}
				// if you right click.
				else if (event.getButton().equals(MouseButton.SECONDARY)) {
					// right click
				}
				// odd clicks, multiple button mice and scroll clicks.
				else {
					// maybe do something on scroll wheel click? maybe not?
				}

			}
		});

		// on back button clicked.
		back.setOnMouseClicked(new EventHandler<MouseEvent>() {

			// do your thing.
			@Override
			public void handle(MouseEvent event) {
				// If left clicked.
				if (event.getButton().equals(MouseButton.PRIMARY)) {
					// if you're on the initial history.
					if (level == 0) {
						// Make the button gray, you can't use it.
						back.setGraphic(new ImageView(new Program4().back_gray));
					}
					// otherwise.
					else {
						// go to the previous page.
						// No the word tacos has no real usage, it's just not
						// null
						goToPage(history.get(level - 1), "tacos");
						// Lower level history.
						level--;
					}
				}
				// Right click!
				else if (event.getButton().equals(MouseButton.SECONDARY)) {
					// right click

				}
				// odd clicks, multiple button mice and scroll clicks.
				else {
					// maybe do something on scroll wheel click? maybe not?
				}
			}

		});

		// On forward button clicked.
		forward.setOnMouseClicked(new EventHandler<MouseEvent>() {

			// DO YOUR THING.
			@Override
			public void handle(MouseEvent event) {
				// LEFT CLICK.
				if (event.getButton().equals(MouseButton.PRIMARY)) {
					// if the page is the most current page.
					if (level == history.size() - 1) {
						// make it grey.
						forward.setGraphic(new ImageView(new Program4().forward_gray));
					}
					// Otherwise, go to the next page in history.
					else {
						// Go to the next page in history.
						// I like muffins.
						goToPage(history.get(level + 1), "muffins");
						level++;
					}

				}
				// RIGHT CLICK.
				else if (event.getButton().equals(MouseButton.SECONDARY)) {
					// right click
				}
				// odd clicks, multiple button mice and scroll clicks.
				else {
					// maybe do something on scroll wheel click? maybe not?
				}
			}

		});

		// When the help button is clicked.
		help.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// light left click.
				if (event.getButton().equals(MouseButton.PRIMARY)) {

					// add in a help page
				
					
				}
				// Lovely right click.
				else if (event.getButton().equals(MouseButton.SECONDARY)) {
					// right click
				}
				// odd clicks, multiple button mice and scroll clicks.
				else {
					// maybe do something on scroll wheel click? maybe not?
				}
			}

		});
	}

	/**
	 * 
	 */
	/*
	 * private void historyBox() {
	 * 
	 * Eventually want to add in a "history box" on right clicking back history
	 * button.
	 * 
	 * }
	 */

	/**
	 * setupView
	 * 
	 * @return Returns an HBox
	 * 
	 *         Returns a set box to contain the webView and engine components.
	 */
	private HBox setupView() {
		// New holder
		HBox view = new HBox();
		// Set the initial size.
		view.setPrefSize(width, height * 0.85);
		// add the web view
		view.getChildren().add(makeHtmlView());
		// return the layout.
		return view;
	}

	/**
	 * setupVerticalAlignment
	 * 
	 * @param nodes
	 *            Nodes set in formatting order, from top to bottom.
	 * @return Returns a VBox with nodes set into it in order.
	 */
	private VBox setupVerticalAlignment(Node... nodes) {
		// Make the box.
		VBox yLayout = new VBox();
		// Force the kids into it... It happens.
		yLayout.getChildren().addAll(nodes);
		// return the layout.
		return yLayout;

	}

	/**
	 * statusBarEvent
	 * 
	 * Captures the usage of hyper links in the status bar.
	 */
	private void statusBarEvent() {
		// Get the hyper link change event.
		webEngine.setOnStatusChanged(e -> {
			// Set the status bar to that URL and remove all the extra numbers.
			statusbar.setText(e.toString().substring(94, e.toString().length() - 1));
		});

	}

	/**
	 * 
	 */
	private void engineEvents() {
		// make the address bar change on page change.

		webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
			@Override
			public void changed(ObservableValue<? extends State> value, State oldState, State newState) {
				if (newState.equals(Worker.State.SUCCEEDED)) {
					// Add URL to address bar.
					address.setText(webEngine.getLocation());
					// Add URL to history.
					history.add(webEngine.getLocation());
					// Reset Level
					level = history.size() -1;
					// color button
					backButton.setGraphic(new ImageView(back));
					// set the forward button to grayed out. because there's nothing
					// to to.
					forwardButton.setGraphic(new ImageView(forward_gray));
					
					stage.getIcons().add(icon);
				}
			}
		});
	}

	/**
	 * The main entry point for all JavaFX applications. The start method is
	 * called after the initial method has returned, and after the system is
	 * ready for the application to begin running.
	 * 
	 * NOTE: This method is called on the JavaFX Application Thread.
	 * 
	 * @param primaryStage
	 *            - the primary stage for this application, onto which the
	 *            application scene can be set.
	 */
	@Override
	public void start(Stage stage) {
		// Assign stage to instance level
		this.stage = stage;

		// Initial setup of window.
		// Initial height
		stage.setHeight(height);
		// Initial Width
		stage.setWidth(width);

		// Start with a basic pane
		window = new Pane();
		// basically a test variable.
		window.setBackground(new Background(new BackgroundFill(Color.AQUAMARINE, new CornerRadii(0), new Insets(0))));

		// Setup the general window layout.
		// Make commandBar component.
		HBox commandBar = setupCommandBar();
		// Make the view component.
		HBox view = setupView();
		// Make statusBar component.
		HBox statusBar = makeStatusBar();

		// setup the layout.
		VBox verticalLayout = setupVerticalAlignment(commandBar, view, statusBar);

		// Actually put everything into the main pane.
		window.getChildren().add(verticalLayout);

		// Make the magic happen.
		Scene scene = new Scene(window);
		// Set the scene.
		stage.setScene(scene);
		// Very important, like showing up to work.
		stage.show();

		// Setup up resizing binds.
		bindChain(statusBar, view, commandBar, verticalLayout);
		// Active statusBarEvents
		statusBarEvent();
		// Activate engineEvents.
		engineEvents();

		// Home page stuff.

		// If there's no parameter.
		if (getParameter(0) == null || getParameter(0).isEmpty()) {
			// go to default home page.
			goToPage(defaultAddress, null);
		}
		// if there is a parameter.
		else {
			// go to it.
			goToPage(getParameter(0), null);
			// also home button now take you there.
			defaultAddress = getParameter(0);
		}

	}

	/**
	 * The main( ) method is ignored in JavaFX applications. main( ) serves only
	 * as fallback in case the application is launched as a regular Java
	 * application, e.g., in IDEs with limited FX support.
	 *
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		// START YOUR ENGINES.
		launch(args);
	}
}
