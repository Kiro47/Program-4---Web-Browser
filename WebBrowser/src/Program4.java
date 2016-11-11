import javafx.application.Application;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class Program4 extends Application{

	TextField address = new TextField("http://www.bing.com");
	
	int height = 600;
	int width = 800;
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		primaryStage.setHeight(height);
		primaryStage.setWidth(width);
		Pane window= new Pane();
		window.setBackground(new Background(new BackgroundFill(Color.AQUAMARINE, new CornerRadii(0), new Insets(0))));
		
		HBox commandBar = setupCommandBar();
		HBox view = setupView();
		
		HBox statusBar = setupStatusBar();
		WebEngine engine = new WebEngine(address.getText());
		
		VBox verticalLayout = setupVerticalAlignment(commandBar,view,statusBar);

		window.getChildren().add(verticalLayout);
		
		
		
		Scene scene = new Scene(window);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		// add selectall text on click.
		//address.setOnMouseClicked();
	
	}
	
	private void bindHeight(Stage stage) {
		// Bind height to stage
	}
	
	private void bindWidth(Stage stage) {
		// Bind width to stage
	}
	
	private VBox setupVerticalAlignment(Node... nodes){
		VBox yLayout = new VBox();
		yLayout.getChildren().addAll(nodes);
		
		
		return yLayout;
		
	}
	
	private HBox setupStatusBar() {
		HBox statusBar = new HBox();
		statusBar.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, new CornerRadii(0), new Insets(0))));
		statusBar.setPrefSize(width, height * 0.1);
		return statusBar;
	}
	
	private HBox setupView() {
		HBox view = new HBox();
		view.setBackground(new Background(new BackgroundFill(Color.PINK, new CornerRadii(0), new Insets(0))));
		view.setPrefSize(width, height * 0.85 );
		return view;
	}

	private HBox setupCommandBar(){
		HBox commandBar = new HBox();
		commandBar.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, new CornerRadii(0), new Insets(0))));
		commandBar.setPrefSize(width, height * 0.05);
		
		Button back = new Button("b");
		Button forward = new Button("f");
				
		address.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, new CornerRadii(0), new Insets(0))));
		// add in event to remove default text when clicked on.
		commandBar.getChildren().addAll(back,forward,address);
		return commandBar;
	}
	public static void main(String[] args){
		launch(args);
	}
}
