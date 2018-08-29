package IBMWatson;

import java.io.FileNotFoundException;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import javafx.event.ActionEvent;

public class Controller extends process{
	/**
	 * 
	 * @throws JsonIOException
	 * @throws JsonSyntaxException
	 * @throws FileNotFoundException
	 */
	public Controller() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		super();

	}
	/**
	 * Button to open file explorer.
	 * @param event On click event.
	 */
	public void chooseFile(ActionEvent event) {
		selectFile();
	}

	
	/**
	 * Button to clear text from text box.
	 * @param clear On click event.
	 */
	public void clearButton(ActionEvent clear) {
		clearText();
		errorLabel.setText("");
	}
	/**
	 * Button to submit the entered text.
	 * @param event On click event.
	 */
	public void submitButton(ActionEvent event) {
		
		displayResults();

		clearPrevResults();

	}
	/**
	 * Button to show the pie chart.
	 * @param event On click event.
	 */
	public void showPchartbutton(ActionEvent event){
		showPieChart();
	}
	/**
	 * Button to show the bar chart
	 * @param event On click event.
	 */
	public void showBchartbutton(ActionEvent event){
		showBarChart();
	}
	/**
	 * Button to show the line chart.
	 * @param event On click event.
	 */
	public void showLchartbutton(ActionEvent event){
		showLineChart();
	}
	/**
	 * Button to show the area chart.
	 * @param event On click event.
	 */
	public void showAchartbutton(ActionEvent event){
		showAreaChart();
	}
	
}
