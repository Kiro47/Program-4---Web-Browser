
// IMPORTS
// These are some classes that may be useful for completing the project.
// You may have to add others.
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebHistory.Entry;
import javafx.stage.Stage;
import javafx.concurrent.Worker.State;
import javafx.concurrent.Worker;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 * The main class for Program5. Program5 constructs the JavaFX window and
 * handles interactions with the dynamic components contained therein.
 */
public class Program4 extends Application {
	// INSTANCE VARIABLES
	// These variables are included to get you started.
	private Stage stage = null;
	private WebView view = null;
	private WebEngine webEngine = null;
	private TextField statusbar = null;

	// My variables.
	String defaultAddress = "http://www.bing.com";
	TextField address = new TextField(defaultAddress);
	String title = "";
	private Pane window = null;

	private int height = 600;
	private int width = 800;

	/**
	 * 
	 */
	private void bindChain(HBox statusBar, HBox view, HBox commandBar, VBox verticalLayout) {
		ReadOnlyDoubleProperty heighto = stage.getScene().heightProperty();
		ReadOnlyDoubleProperty widtho = stage.getScene().widthProperty();

		// Main window
		window.prefHeightProperty().bind(heighto.divide(1));
		window.prefWidthProperty().bind(widtho);

		ReadOnlyDoubleProperty height = window.heightProperty();
		ReadOnlyDoubleProperty width = window.widthProperty();

		// Each component
		verticalLayout.prefHeightProperty().bind(height.divide(1));
		verticalLayout.prefWidthProperty().bind(width);

		statusBar.prefHeightProperty().bind(height.multiply(0.05));
		statusBar.prefWidthProperty().bind(width);

		view.prefHeightProperty().bind(height.multiply(0.90));
		view.prefWidthProperty().bind(width);

		this.view.prefHeightProperty().bind(height.multiply(0.9));
		;
		this.view.prefWidthProperty().bind(width);

		commandBar.prefHeightProperty().bind(height.multiply(0.05));
		commandBar.prefWidthProperty().bind(width);

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
	 * Generates the status bar layout and text field.
	 * 
	 * @return statusbarPane - the HBox layout that contains the statusbar.
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
	 * 
	 * @return
	 */
	private HBox setupCommandBar() {
		// Layout box
		HBox commandBar = new HBox();
		// Fill it up, and size it properly.
		commandBar.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, new CornerRadii(0), new Insets(0))));
		commandBar.setPrefSize(width, height * 0.05);

		// Buttons for controls.
		Button back = new Button();
		back.setGraphic(new ImageView(new Image("https://i.gyazo.com/d72ae41fc374cf0ee62a3ef739e7c95d.jpg", 40,40,true,true)));
		Button forward = new Button();
		forward.setGraphic(new ImageView(new Image("https://i.gyazo.com/802bef2d69de6f917d4a0b66985f099b.png", 40,40,true,true)));
		Button go = new Button();
		go.setGraphic(new ImageView(new Image("https://i.gyazo.com/747841ba38d0ab903f1a6939322acb76.png", 40,40,true,true)));
		Button home = new Button();
		home.setGraphic(new ImageView(new Image("https://i.gyazo.com/1c069a908c0f512b91278616f7554751.png", 40,40,true,true)));

		// Add Events for buttons.
		buttonEvents(back, forward, go, home);
		
		// Make the address bar fairly normally sized.
		address.prefWidthProperty().bind(commandBar.prefWidthProperty().multiply(0.65));
		address.prefHeightProperty().bind(commandBar.prefHeightProperty());

		// Add spacing to make it look decent.
		commandBar.setSpacing(3.0);
		// so you can actually tell where it is.
		address.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, new CornerRadii(0), new Insets(0))));

		// add in event to remove default text when clicked on.

		commandBar.getChildren().addAll(back, forward, address, go, home);
		return commandBar;
	}

	/**
	 * 
	 */
	private void buttonEvents(Button back, Button forward, Button go, Button home) {
		go.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				webEngine.load(address.getText());
			}
		});
	}
	/**
	 * 
	 * @return
	 */
	private HBox setupView() {
		HBox view = new HBox();
		view.setBackground(new Background(new BackgroundFill(Color.PINK, new CornerRadii(0), new Insets(0))));
		view.setPrefSize(width, height * 0.85);
		view.getChildren().add(makeHtmlView());
		return view;
	}

	/**
	 * 
	 * @param nodes
	 * @return
	 */
	private VBox setupVerticalAlignment(Node... nodes) {
		// Make the box.
		VBox yLayout = new VBox();
		// Force the kids into it... It happens.
		yLayout.getChildren().addAll(nodes);
		return yLayout;

	}

	/**
	 * The main entry point for all JavaFX applications. The start method is
	 * called after the init method has returned, and after the system is ready
	 * for the application to begin running.
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
		stage.setHeight(height);
		stage.setWidth(width);

		// Start with a basic pane
		window = new Pane();
		// basically a test variable.
		window.setBackground(new Background(new BackgroundFill(Color.AQUAMARINE, new CornerRadii(0), new Insets(0))));

		// Setup the general window layout.
		HBox commandBar = setupCommandBar();
		HBox view = setupView();
		HBox statusBar = makeStatusBar();

		// setup the layout.
		VBox verticalLayout = setupVerticalAlignment(commandBar, view, statusBar);

		// Actually put everything into the main pane.
		window.getChildren().add(verticalLayout);

		// Make the magic happen.
		Scene scene = new Scene(window);
		stage.setScene(scene);
		stage.show();

		// Setup up resizing binds.
		bindChain(statusBar, view, commandBar, verticalLayout);

		// EventMegaHandler

		// Homepage stuff.
		if (getParameter(0) == null || getParameter(0).isEmpty()) {
			webEngine.load(defaultAddress);
		}
		else {
			webEngine.load(getParameter(0));
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
		launch(args);
	}
}
