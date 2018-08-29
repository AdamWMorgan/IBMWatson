package IBMWatson;

import java.io.FileNotFoundException;
import java.io.IOException;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
/**
 * 
 * @author Adam Morgan
 *
 */
public class IBMWatson extends Application {

	protected static Stage primaryStage;
	private AnchorPane mainUI;
	
	/**
	 * Displays the user interface.
	 * @param primaryStage sets the user interface
	 */
	@Override
	public void start(Stage primaryStage) throws IOException{
		IBMWatson.primaryStage = primaryStage;
		IBMWatson.primaryStage.setTitle("IBM Watson");
		showMainView();
	}
	/**
	 * Loads the user interface.
	 * @throws IOException
	 */
	protected void showMainView() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(IBMWatson.class.getResource("view/MainView.fxml"));
		mainUI = loader.load();
		Scene scene = new Scene(mainUI);
		// Set initial interface properties
		primaryStage.setX(300);
		primaryStage.setY(30);
		primaryStage.setHeight(290);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();	
	}
	/**
	 * 
	 * @param args launches program.
	 * @throws JsonIOException
	 * @throws JsonSyntaxException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		launch(args);
	}
}
