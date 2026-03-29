package MicGenAI.MicExtractor.dashboard;

//import static java.lang.System.out;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.fasterxml.jackson.databind.ObjectMapper;

import httpClients.AwsAPIGateway;
import httpClients.SourceCodeRequest;
import httpClients.SysResponse;
import httpClients.UmlEditors;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;


public class DataLoader {

	private final AwsAPIGateway awsAPIGateway;

	private FileChooser openFileChooser;


	@FXML
	private TextField path;

	@FXML
	private TextField token;

	@FXML
	private TextField accessKey;

	@FXML
	private TextField secretKey;

	@FXML
	private TextField micExtractorApiKey;

	@FXML
	private Label icsLabel;

	private String icsLabelOriginal;

	@FXML
	private Slider ics;

	@FXML
	private Label ecsLabel;

	private String ecsLabelOriginal;

	@FXML
	private Slider ecs;

	@FXML
	private Button btnInvoke;

	@FXML
	private Button btnSave;

	@FXML
	private Button btnVisualize;

	@FXML
	private ComboBox<String> umlEditor;

	@FXML
	private Button userDefined;


	@FXML
	private HBox kpis;

	@FXML
    private Label kpiTotalMics;

	@FXML
    private Label totalMics;

	@FXML
	private Label kpiTotalClasses;

	@FXML
	private Label totalClasses;

	@FXML
	private Label kpiTotalUrels;

	@FXML
	private Label totalUrels;

	@FXML
	private NumberAxis yAxis;

	@FXML
	private ToggleGroup chartToggleGroup;

	@FXML
	private StackedBarChart<String, Number> chart;

	@FXML
	private CategoryAxis xAxis;

    @FXML
    private RadioButton classRadio;

    @FXML
    private RadioButton umlRelRadio;

    @FXML
    private SplitPane splitPane;

    @FXML
	private Label logo;

    @FXML
    private Label copyrightSymbol;


    private String body, bodyValue, originalBodyValue, totalMicsOriginal, totalClassesOriginal, totalUrelsOriginal, classRadioOriginal, umlRelRadioOriginal, yAxisLabelOriginal, userDefinedOriginal, umlEditorOriginalText;

    private Sys sys;

    private Feedback feedback;


    public DataLoader(AwsAPIGateway awsAPIGateway) { this.awsAPIGateway = awsAPIGateway; }

    public DataLoader() { this(new AwsAPIGateway()); }


    @FXML
    public void initialize() throws Exception {

    	Image image1 = new Image(new File(MicExtractorDashboardApplication.class.getResource("/Logo.png").toURI()).toURI().toURL().toExternalForm());

		ImageView view1 = new ImageView(image1);

		logo.setGraphic(view1);


		Image image2 = new Image(new File(MicExtractorDashboardApplication.class.getResource("/Copyright.png").toURI()).toURI().toURL().toExternalForm());

		ImageView view2 = new ImageView(image2);

		copyrightSymbol.setGraphic(view2);


		userDefinedOriginal = userDefined.getText();

		umlEditorOriginalText = umlEditor.getPromptText();

    	splitPane.setVisible(false);

    	icsLabelOriginal = icsLabel.getText();

    	ecsLabelOriginal = ecsLabel.getText();

    	setupSliders();


    	umlEditor.getItems().addAll(UmlEditors.getKeys());

    	totalMicsOriginal = totalMics.getText();

    	totalClassesOriginal = totalClasses.getText();

    	totalUrelsOriginal = totalUrels.getText();

    	classRadioOriginal = classRadio.getText();

    	umlRelRadioOriginal = umlRelRadio.getText();

    	yAxisLabelOriginal = yAxis.getLabel();


    	yAxis.setAutoRanging(false);

		yAxis.setLowerBound(0);

    	yAxis.setUpperBound(100);

    	yAxis.setTickUnit(1);


    	chartToggleGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> updateChart());

    	awsAPIGateway.setOnSucceeded(e -> processData(awsAPIGateway.getValue()));

    	awsAPIGateway.setOnCancelled(e -> processData(awsAPIGateway.getValue()));

    	awsAPIGateway.setOnFailed(e -> processData(awsAPIGateway.getValue()));

    	btnInvoke.setOnAction(e -> loadData());

    	userDefined.setOnAction(e -> chooseFile());

    	btnSave.setVisible(false);

    	btnSave.setOnAction(e -> export());

    	btnVisualize.setVisible(false);

    	btnVisualize.setOnAction(e -> visualize());
    }

    private void setupSliders() {

    	icsLabel.setText(icsLabelOriginal + " " + String.format("%1.2f", 0.0));

    	ics.valueProperty().addListener((obs, old, val) -> { icsLabel.setText(icsLabelOriginal + " " + String.format("%1.2f", val.doubleValue())); });

    	ecsLabel.setText(ecsLabelOriginal + " " + String.format("%1.2f", 1.0));

    	ecs.valueProperty().setValue(1.0);

    	ecs.valueProperty().addListener((obs, old, val) -> { ecsLabel.setText(ecsLabelOriginal + " " + String.format("%1.2f", val.doubleValue())); });
    }

    private void chooseFile(){

    	if(userDefined.getText().equals(userDefinedOriginal)) {

    		openFileChooser = new FileChooser();

    		if(openFileChooser.getFilePath() != null) {

    			userDefined.setText("Forget File");

    			umlEditor.getItems().clear();

    			umlEditor.getItems().add(umlEditorOriginalText);

    			umlEditor.getItems().addAll(UmlEditors.getKeys());

    			umlEditor.setValue(umlEditorOriginalText); //out.println("umlEditorOriginalText = " + umlEditorOriginalText);

    			umlEditor.setDisable(true);

    			btnSave.setVisible(false);

    	    	btnVisualize.setVisible(false);
    		}
    	}

    	else {

    		userDefined.setText(userDefinedOriginal);

    		umlEditor.setDisable(false);

			umlEditor.getItems().clear();

			umlEditor.getItems().add(umlEditorOriginalText);

			umlEditor.getItems().addAll(UmlEditors.getKeys());

    		umlEditor.setValue(umlEditorOriginalText); //out.println("umlEditorOriginalText = " + umlEditorOriginalText);

    		openFileChooser = null;
    	}
    }

    private void loadData(){

    	if(path.getText() == null || (path.getText() != null && path.getText().isEmpty())) JOptionPane.showMessageDialog(null, "You must type the URL of (the ZIP file) a GitHub/GitLab repository.", MicExtractorDashboardApplication.DASHBOARD, JOptionPane.ERROR_MESSAGE);

    	else if(token.getText() == null || (token.getText() != null && token.getText().isEmpty())) JOptionPane.showMessageDialog(null, "You must type the access token to your GitHub/GitLab account.", MicExtractorDashboardApplication.DASHBOARD, JOptionPane.ERROR_MESSAGE);

    	else if(accessKey.getText() == null || (accessKey.getText() != null && accessKey.getText().isEmpty())) JOptionPane.showMessageDialog(null, "You must type your personal access key to AWS API Gateway.", MicExtractorDashboardApplication.DASHBOARD, JOptionPane.ERROR_MESSAGE);

    	else if(secretKey.getText() == null || (secretKey.getText() != null && secretKey.getText().isEmpty())) JOptionPane.showMessageDialog(null, "You must type your personal secret access key to AWS API Gateway.", MicExtractorDashboardApplication.DASHBOARD, JOptionPane.ERROR_MESSAGE);

    	else if(micExtractorApiKey.getText() == null || (micExtractorApiKey.getText() != null && micExtractorApiKey.getText().isEmpty())) JOptionPane.showMessageDialog(null, "You must type the API ley that you acquired from your subscription to the AWS marketplace for the " + MicExtractorDashboardApplication.TARGET_API + ".", MicExtractorDashboardApplication.DASHBOARD, JOptionPane.ERROR_MESSAGE);

    	else if(awsAPIGateway.isRunning()) JOptionPane.showMessageDialog(null, "You have laready made an invocation to '" + MicExtractorDashboardApplication.TARGET_API + "'. You have to wait until the previous invocation finishes.", MicExtractorDashboardApplication.DASHBOARD, JOptionPane.ERROR_MESSAGE);

    	else{

	    	awsAPIGateway.reset();

	    	btnSave.setVisible(false);

	    	btnVisualize.setVisible(false);

	    	btnInvoke.setText("AWS API Gateway...");

	    	String fileContent = openFileChooser == null ? null : openFileChooser.getFileContent();

	    	SourceCodeRequest sourceCodeRequest = new SourceCodeRequest(path.getText(), UmlEditors.getKey(umlEditor.getValue()), ics.getValue(), ecs.getValue(), fileContent, token.getText());

	    	String sourceCodeRequestStr = null;

	    	try{ sourceCodeRequestStr = new ObjectMapper().writeValueAsString(sourceCodeRequest); } catch(Exception ex) { ex.printStackTrace(); }

	    	awsAPIGateway.setSourceCodeRequest(sourceCodeRequestStr, micExtractorApiKey.getText(), accessKey.getText(), secretKey.getText());
	
	    	awsAPIGateway.start();
    	}
    }

    private void processData(String json) {

    	btnInvoke.setText(MicExtractorDashboardApplication.TARGET_API);

    	if(chart.getData() != null) chart.getData().clear();

    	if(chartToggleGroup.getSelectedToggle() != null) chartToggleGroup.getSelectedToggle().setSelected(false);


    	double kpi1 = 0, kpi2 = 0, kpi3 = 0;

    	body = null;

		bodyValue = null;

		originalBodyValue = null;

		sys = null;

		feedback = null;

		boolean error = false;

		String errorMessage = "One or more of the input values you typed in the text fields (or the syntax of the file you loaded) are not correct\n(or your machine is not connected to the Internet). Please try again.";

    	try {

    		int i = 10;
 
    		String key = "";

    		while(json.charAt(i) != '\"') {

    			key += json.charAt(i);

    			++i;
    		}

    		//out.println("json = " + json);

    		if(key.equals(SysResponse.SYS) || key.equals(SysResponse.FEEDBACK) || key.equals(SysResponse.ERROR)) body = key;

    		String outputMessage = null;

    		if(body != null) {

    			if(body.equals(SysResponse.FEEDBACK) && json.charAt(i+3) == '\"') bodyValue = json.substring(i+4, json.length()-4);

    			else bodyValue = json.substring(i+3, json.length()-3);


    			originalBodyValue = bodyValue;

    			if(body.equals(SysResponse.SYS) || body.equals(SysResponse.FEEDBACK)) {

    	    		bodyValue = bodyValue.replaceAll("&gt;", ">").replaceAll("&lt;", "<");

    	    		bodyValue = bodyValue.replace("<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"yes\\\"?>", "");

    	    		originalBodyValue = bodyValue;


    	    		String open = null, close = null;

    	    		if(body.equals(SysResponse.SYS)){

    	    			open = "<outputMessage>";

    	    			close = "</outputMessage>";
    	    		}

    	    		else if(body.equals(SysResponse.FEEDBACK)){

    	    			open = "<sys><name>";

    	    			close = "</ser></sys>";
    	    		}


    	    		if(open != null && bodyValue.contains(open) && close != null && bodyValue.contains(close)) {

    	    			outputMessage = bodyValue.substring(bodyValue.indexOf(open)+open.length(), bodyValue.indexOf(close));

    	    			outputMessage = outputMessage.replace("\\\"", "\"");

    	    			String prefix = bodyValue.substring(0, bodyValue.indexOf(open));

    	    			String suffix = bodyValue.substring(bodyValue.indexOf(close)+close.length());

    	    			bodyValue = prefix + suffix;
    	    		}
    			}
    		}

    		//out.println("bodyValue = " + bodyValue);


	    	if(body != null && body.equals(SysResponse.SYS)) {

	    		sys = ((Sys) deserializeFromString(bodyValue, new Sys()));

	    		sys.setOutputMessage(outputMessage);

	    		sys.print();

	    		if(sys.getSer() != null) {

	    			kpi1 = sys.getSer().size();

	    			kpi2 = sys.calculateClassNum();

	    			kpi3 = sys.calculateAllUmlRelNum();
	    		}

		    	classRadio.setText(classRadioOriginal);

		    	umlRelRadio.setText(umlRelRadioOriginal);

		    	yAxis.setLabel(yAxisLabelOriginal);
	    	}

	    	else if(body != null && body.equals(SysResponse.FEEDBACK)) {

	    		feedback = ((Feedback) deserializeFromString(bodyValue, new Feedback()));

	    		feedback.print();

	    		classRadio.setText("Minimality");
	    		
	    		umlRelRadio.setText("Completeness");

	    		yAxis.setLabel("Percentage");

	    		if(feedback.getSerializationError()) {

	    			error = true;

	    			JOptionPane.showMessageDialog(null, errorMessage, MicExtractorDashboardApplication.DASHBOARD, JOptionPane.ERROR_MESSAGE);
	    		}

	    		else {

		    		kpi1 = feedback.getCorrectTagPercentage();
	
		    		kpi2 = feedback.getCorrectDeclarationPercentage();
	
		    		kpi3 = feedback.getCorrectSerPercentage();
	    		}
	    	}

	    	else if(body != null && body.equals(SysResponse.ERROR)) {

	    		//out.println(bodyValue);

	    		error = true;

				JOptionPane.showMessageDialog(null, bodyValue, MicExtractorDashboardApplication.DASHBOARD, JOptionPane.ERROR_MESSAGE);
			}

			else {

				error = true;

				JOptionPane.showMessageDialog(null, errorMessage, MicExtractorDashboardApplication.DASHBOARD, JOptionPane.ERROR_MESSAGE);
			}
    	}

    	catch(Exception ex) { ex.printStackTrace();

    		error = true;

			JOptionPane.showMessageDialog(null, errorMessage, MicExtractorDashboardApplication.DASHBOARD, JOptionPane.ERROR_MESSAGE);
    	}

    	if(! error){

    		calculateKPIs(kpi1, kpi2, kpi3);

    		updateChart();

    		btnSave.setVisible(true);

    		splitPane.setVisible(true);

			umlEditor.getItems().clear();

			umlEditor.getItems().add(umlEditorOriginalText);

			umlEditor.getItems().addAll(UmlEditors.getKeys());

    		umlEditor.setValue(umlEditorOriginalText); //out.println("umlEditorOriginalText = " + umlEditorOriginalText);

			umlEditor.setDisable(false);

    		if(sys != null && sys.getOutputMessage() != null) btnVisualize.setVisible(true);
    	}
    }

    private void calculateKPIs(double kpi1, double kpi2, double kpi3) {

    	if(feedback != null) {

    		totalMics.setText("Correct Tag Percentage");

    		kpiTotalMics.setText(String.format("%3.2f", kpi1));

    		totalClasses.setText("Correct Declaration Percentage");

    		kpiTotalClasses.setText(String.format("%3.2f", kpi2));

    		totalUrels.setText("Correct Service Percentage");

    		kpiTotalUrels.setText(String.format("%3.2f", kpi3));
    	}

    	else if(sys != null) {

    		totalMics.setText(totalMicsOriginal);

    		kpiTotalMics.setText(String.format("%1.0f", kpi1));

    		totalClasses.setText(totalClassesOriginal);

    		kpiTotalClasses.setText(String.format("%1.0f", kpi2));

    		totalUrels.setText(totalUrelsOriginal);

    		kpiTotalUrels.setText(String.format("%1.0f", kpi3));
    	}
    }

    private void updateChart() {

    	RadioButton selected = (RadioButton) chartToggleGroup.getSelectedToggle();

    	if(selected == null) return;

    	chart.getData().clear();

    	String mode = selected.getText();

    	xAxis.getCategories().clear();

    	if(body.equals(SysResponse.SYS)) {

	    	sys.print();

	    	int microserviceNumber = sys.getSer() != null ? sys.getSer().size() : 0;

	   		ArrayList<String> labels = new ArrayList<String>();

	    	for(int i = 0 ; i < microserviceNumber; ++i) labels.add(String.valueOf(i+1));

	    	xAxis.setCategories(FXCollections.observableArrayList(labels));

	    	classRadio.setText(classRadioOriginal);

	    	umlRelRadio.setText(umlRelRadioOriginal);

	    	yAxis.setLabel(yAxisLabelOriginal);


		    if(mode.contains(classRadio.getText())) buildChartByClass(sys.getSer());
		
		    else if(mode.contains(umlRelRadio.getText())) buildChartByUmlRel(sys.getSer());
	    }

	    else if(body.equals(SysResponse.FEEDBACK)) {

	    	feedback.print();

	    	classRadio.setText("Minimality");

    		umlRelRadio.setText("Completeness");

    		yAxis.setLabel("Percentage");


		    if(mode.contains(classRadio.getText())) buildChart(feedback.getClassMinimalityErrorMessages(), "(minimality)", classRadio.getText());
		
		    else if(mode.contains(umlRelRadio.getText())) buildChart(feedback.getClassCompletenessErrorMessages(), "(completeness)", umlRelRadio.getText());
	    }
    }

    public static Object deserializeFromString(String objStr, Object obj) throws Exception{

		JAXBContext jAXBContext = null;

		if(obj instanceof Feedback) jAXBContext = JAXBContext.newInstance(Feedback.class);

		else if(obj instanceof Sys) jAXBContext = JAXBContext.newInstance(Sys.class);


		Unmarshaller unmarshaller = jAXBContext.createUnmarshaller();

		StringReader reader = new StringReader(objStr);

		Object values = null;

		if(obj instanceof Feedback) values = (Feedback) unmarshaller.unmarshal(reader);

		else if(obj instanceof Sys) values = (Sys) unmarshaller.unmarshal(reader);

		return values;
	}

    private void buildChartByClass(ArrayList<Ser> ser) {

    	Series<String, Number> series = new Series<String, Number>();

    	int sum = 0;

    	for(int q = 1; q <= ser.size(); q++) {

    		int classNum = ser.get(q-1).calculateClassNum();

    		Data<String, Number> data = new Data<String, Number>(String.valueOf(q), classNum);

    		series.getData().add(data);

    		sum += classNum;
        }

    	series.setName(classRadio.getText());

        chart.getData().add(series);

        yAxis.setUpperBound(sum);
    }

    private void buildChartByUmlRel(ArrayList<Ser> ser) {

    	Series<String, Number> series = new Series<String, Number>();

    	int sum = 0;

    	for(int q = 1; q <= ser.size(); q++) {

    		int uRelNum = ser.get(q-1).calculateAllUmlRelNum();

    		Data<String, Number> data = new Data<String, Number>(String.valueOf(q), uRelNum);

    		series.getData().add(data);

    		sum += uRelNum;
        }

    	series.setName(umlRelRadio.getText());

        chart.getData().add(series);

        yAxis.setUpperBound(sum);
    }

    private void buildChart(ArrayList<String> list, String token, String radioText) {

    	Series<String, Number> series = new Series<String, Number>();

    	int size = 0;

   		for(int i = 0; list != null && i < list.size(); ++i)

   			if(list.get(i).contains(token)) {

   				size++;

   				String percentage = list.get(i).substring(list.get(i).indexOf("=")+1, list.get(i).length()-1);

   				Data<String, Number> data = new Data<String, Number>(String.valueOf(size), Double.parseDouble(percentage.trim()));

   				series.getData().add(data);
   			}

   		ArrayList<String> labels = new ArrayList<String>();

   		for(int i = 0 ; i < size; ++i) labels.add(String.valueOf(i+1));

   		xAxis.setCategories(FXCollections.observableArrayList(labels));

    	series.setName(radioText);

        chart.getData().add(series);

        yAxis.setUpperBound(101);
    }

    private void export() { new FileChooser(originalBodyValue, ".xml"); }

    private void visualize() {

    	if(umlEditor != null && umlEditor.getValue() != null && UmlEditors.getKey(umlEditor.getValue()) != null) new FileChooser(sys.getOutputMessage(), UmlEditors.getKey(umlEditor.getValue()).label);

    	else JOptionPane.showMessageDialog(null, "You had not selected UML editor (drop-down menu). Please try again.", MicExtractorDashboardApplication.DASHBOARD, JOptionPane.ERROR_MESSAGE);
	}
}