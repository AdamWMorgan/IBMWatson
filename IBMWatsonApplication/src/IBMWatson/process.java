package IBMWatson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFileChooser;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.watson.developer_cloud.personality_insights.v2.PersonalityInsights;
import com.ibm.watson.developer_cloud.personality_insights.v2.model.Profile;
import com.ibm.watson.developer_cloud.service.exception.BadRequestException;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class process {
	/**
	 * Array list that is responsible for the category ID data returned from the
	 * Watson personality insight service.
	 */
	ArrayList<String> CatID = new ArrayList<>();
	/**
	 * Array list that is responsible for the category ID data returned from the
	 * Watson personality insight service.
	 */
	ArrayList<Double> percentages = new ArrayList<>();
	/**
	 * Array list that is responsible for the tone category data returned from
	 * the Watson tone analyser service.
	 */
	ArrayList<String> toneCategory = new ArrayList<>();
	/**
	 * Array list that is responsible for the tone score data returned from the
	 * Watson tone analyser service.
	 */
	ArrayList<Double> toneScore = new ArrayList<>();
	/** A counter for the number of times text is submitted. */
	int i = 1;

	@FXML
	TextArea watsonText;
	@FXML
	PieChart piechart;
	@FXML
	BarChart<String, Number> barchart;
	@FXML
	LineChart<String, Number> linechart;
	@FXML
	AreaChart<String, Number> areachart;
	@FXML
	Label errorLabel;

	/**
	 * Clears text that has been entered into the text box.
	 */
	public void clearText() {
		watsonText.clear();
	}

	/**
	 * Removes old data from the array lists.
	 */
	public void clearPrevResults() {
		CatID.clear();
		percentages.clear();
		toneCategory.clear();
		toneScore.clear();
	}

	/**
	 * Connection to the Watson personality insight service.
	 */
	protected void watsonConnect() {

		String text = watsonText.getText();

		PersonalityInsights service = new PersonalityInsights();

		service.setUsernameAndPassword("bc1d4960-7688-4283-bb64-a151223af514", "VssAk2BysnQ4");

		Profile profile = service.getProfile(text).execute();
		String back = profile.toString();
		//Navigation through JSON file to return relevant data.
		JsonElement jelement = new JsonParser().parse(back);

		JsonObject jobject = jelement.getAsJsonObject();

		jobject = jobject.getAsJsonObject("tree");

		JsonArray jarray = jobject.getAsJsonArray("children");
		jobject = jarray.get(0).getAsJsonObject();

		JsonArray jarray1 = jobject.getAsJsonArray("children");
		jobject = jarray1.get(0).getAsJsonObject();

		JsonArray jarray2 = jobject.getAsJsonArray("children");
		jobject = jarray2.get(0).getAsJsonObject();

		JsonArray jarray3 = jobject.getAsJsonArray("children");
		//Loop through JSON array and write data to the necessary array lists
		for (int i = 0; i < jarray3.size(); i++) {
			jobject = jarray3.get(i).getAsJsonObject();

			String id = jobject.get("id").toString().replace('"', ' ');
			CatID.add(id);

			double advPercentage = jobject.get("percentage").getAsDouble();
			percentages.add(advPercentage);

		}

	}

	/**
	 * Connection to the Watson tone analyser service.
	 */
	protected void toneAnalyzer() {
		String text = watsonText.getText();
		ToneAnalyzer service = new ToneAnalyzer(ToneAnalyzer.VERSION_DATE_2016_05_19);

		service.setEndPoint("https://gateway.watsonplatform.net/tone-analyzer/api");

		service.setUsernameAndPassword("57916349-b61d-4fb4-bc45-4f293f163d51", "a8ZAwtomgwOM");

		ToneAnalysis tone = service.getTone(text, null).execute();
		String back = tone.toString();
		//Navigation through JSON file to return relevant data.
		JsonElement jelement = new JsonParser().parse(back);

		JsonObject jobject = jelement.getAsJsonObject();

		jobject = jobject.getAsJsonObject("document_tone");

		JsonArray jarray = jobject.getAsJsonArray("tone_categories");
		jobject = jarray.get(0).getAsJsonObject();

		JsonArray jarray1 = jobject.getAsJsonArray("tones");
		jobject = jarray1.get(0).getAsJsonObject();
		//Loop through JSON array and write data to the necessary array lists.
		for (int i = 0; i < jarray1.size(); i++) {
			jobject = jarray1.get(i).getAsJsonObject();

			String toneID = jobject.get("tone_id").toString().replace('"', ' ');
			toneCategory.add(toneID);

			double toneNum = jobject.get("score").getAsDouble();
			toneScore.add(toneNum);

		}
	}
	/**
	 * Writes data to the bar chart.
	 */
	public void barChart() {
		/**
		 * A bug within JavaFX forces an error in animated graphs during their
		 * first iteration. Use the following line if this chart is set as the
		 * default chart. barchart.setAnimated(false);
		 */

		@SuppressWarnings("unchecked")
		final ObservableList<javafx.scene.chart.XYChart.Data<String, Double>> series = FXCollections
				.observableArrayList(new XYChart.Data<>(CatID.get(0), (percentages.get(0) * 100)),
						new XYChart.Data<>(CatID.get(1), (percentages.get(1) * 100)),
						new XYChart.Data<>(CatID.get(2), (percentages.get(2) * 100)),
						new XYChart.Data<>(CatID.get(3), (percentages.get(3) * 100)),
						new XYChart.Data<>(CatID.get(4), (percentages.get(4) * 100)));

		final XYChart.Series series1 = new XYChart.Series(series);
		//Set the text number in the chart legend.
		series1.setName("Text " + i);
		//Add the data to the chart.
		barchart.getData().addAll(series1);

	}
	/**
	 * Displays only the bar chart.
	 */
	public void showBarChart() {
		barchart.setVisible(true);
		piechart.setVisible(false);
		linechart.setVisible(false);
		areachart.setVisible(false);
	}
	/**
	 * Writes data to the pie chart.
	 */
	public void pieChart() {

		ObservableList<Data> list = FXCollections.observableArrayList(
				new PieChart.Data(CatID.get(0), percentages.get(0)),
				new PieChart.Data(CatID.get(1), percentages.get(1)),
				new PieChart.Data(CatID.get(2), percentages.get(2)),
				new PieChart.Data(CatID.get(3), percentages.get(3)),
				new PieChart.Data(CatID.get(4), percentages.get(4)));
		piechart.setLabelsVisible(false);
		piechart.setLegendVisible(true);
		//Add the data to the chart
		piechart.setData(list);

	}
	/**
	 * Displays only the pie chart.
	 */
	public void showPieChart() {
		piechart.setVisible(true);
		barchart.setVisible(false);
		linechart.setVisible(false);
		areachart.setVisible(false);
	}
	/**
	 * Writes data to the line chart
	 */
	public void lineChart() {
		/**
		 * A bug within JavaFX forces an error in animated graphs during their
		 * first iteration. Use the following line if this chart is set as the
		 * default chart. linechart.setAnimated(false);
		 */

		@SuppressWarnings("unchecked")
		final ObservableList<javafx.scene.chart.XYChart.Data<String, Double>> series = FXCollections
				.observableArrayList(new XYChart.Data<>(toneCategory.get(0), (toneScore.get(0) * 100)),
						new XYChart.Data<>(toneCategory.get(1), (toneScore.get(1) * 100)),
						new XYChart.Data<>(toneCategory.get(2), (toneScore.get(2) * 100)),
						new XYChart.Data<>(toneCategory.get(3), (toneScore.get(3) * 100)),
						new XYChart.Data<>(toneCategory.get(4), (toneScore.get(4) * 100)));

		final XYChart.Series series1 = new XYChart.Series(series);
		//Set the text number in the chart legend
		series1.setName("Text " + i);
		//Add the data to the chart
		linechart.getData().addAll(series1);

	}
	/**
	 * Displays only the line chart.
	 */
	public void showLineChart() {
		linechart.setVisible(true);
		barchart.setVisible(false);
		piechart.setVisible(false);
		areachart.setVisible(false);

	}
	/**
	 * Writes data to the area chart.
	 */
	public void areaChart() {
		/**
		 * A bug within JavaFX forces an error in animated graphs during their
		 * first iteration. Use the following line if this chart is set as the
		 * default chart. areachart.setAnimated(false);
		 */
		@SuppressWarnings("unchecked")
		final ObservableList<javafx.scene.chart.XYChart.Data<String, Double>> series = FXCollections
				.observableArrayList(new XYChart.Data<>(toneCategory.get(0), (toneScore.get(0) * 100)),
						new XYChart.Data<>(toneCategory.get(1), (toneScore.get(1) * 100)),
						new XYChart.Data<>(toneCategory.get(2), (toneScore.get(2) * 100)),
						new XYChart.Data<>(toneCategory.get(3), (toneScore.get(3) * 100)),
						new XYChart.Data<>(toneCategory.get(4), (toneScore.get(4) * 100)));

		final XYChart.Series series1 = new XYChart.Series(series);
		//Set the text number in the chart legend
		series1.setName("Text " + i);
		//Add the data to the chart
		areachart.getData().addAll(series1);

	}
	/**
	 * Displays only the area chart.
	 */
	public void showAreaChart() {
		areachart.setVisible(true);
		linechart.setVisible(false);
		barchart.setVisible(false);
		piechart.setVisible(false);
	}
	/**
	 * Opens a file explorer and writes the selected file to a text box.
	 */
	public void selectFile() {
		final JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(fc);
		String returnString = fc.getSelectedFile().getAbsolutePath();

		try {

			Scanner readFile = new Scanner(new File(returnString));

			while (readFile.hasNext()) {
				String text = readFile.nextLine();
				watsonText.appendText(text);
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

	/**
	 * Ensures that Watson can read the text that is submitted.
	 * 
	 * @return boolean
	 * @throws BadRequestException
	 */
	public boolean checkBadRequestException() {
		try {
			watsonConnect();
			toneAnalyzer();
		} catch (BadRequestException b) {
			errorLabel.setText(b.getMessage());
			return true;
		}
		return false;

	}

	/**
	 * Determines how the visual data should be represented if the data entered
	 * is acceptable to follow the set conditions.
	 * 
	 * @see #watsonConnect();
	 * @see #toneAnalyzer();
	 * @see #barChart();
	 * @see #pieChart();
	 * @see #lineChart();
	 * @see #areaChart();
	 */
	public void displayResults() {
		String textArray[] = watsonText.getText().split("\\s+");
		int wordCount = textArray.length;
		int wordsRemaining = 100 - wordCount;
		//If word count is below required amount of words needed output an error message.
		if (wordCount < 100) {
			errorLabel.setText("You have not entered enough words. Please enter " + wordsRemaining + " more words.");
		}
		//if Watson does not recognise enough words do not proceed.
		else if (checkBadRequestException()) {

		}
		//Connect to Watson services and display the visuals.
		else {
			//Expand interface to reveal visuals.
			IBMWatson.primaryStage.setHeight(600);

			errorLabel.setText("");

			watsonConnect();

			toneAnalyzer();

			barChart();

			pieChart();

			lineChart();

			areaChart();

			if (i == 1) {
				piechart.setVisible(true);
				barchart.setVisible(false);
				linechart.setVisible(false);
				areachart.setVisible(false);
			}
			//Count the number of times the user has successfully submitted text.
			i++;
			//Clear text box, ready for the next input.
			clearText();

		}

	}

}
