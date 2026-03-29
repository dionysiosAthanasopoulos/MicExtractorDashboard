package MicGenAI.MicExtractor.dashboard;

import java.io.File;

import httpClients.AwsAPIGateway;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class MicExtractorDashboardApplication extends Application{

	public static void main(String[] args) { launch(); }

	public static final String TARGET_API = "'MicExtractorAPI'", DASHBOARD = TARGET_API + "'s Dashboard";

	@SuppressWarnings("exports")
	@Override
	public void start(Stage stage) throws Exception {

		FXMLLoader fxmlLoader = new FXMLLoader(MicExtractorDashboardApplication.class.getResource("/main.fxml"));

		fxmlLoader.setControllerFactory(param -> { return new DataLoader(new AwsAPIGateway()); });


		Scene scene = new Scene(fxmlLoader.load(), 1300, 700);

		scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

		stage.setScene(scene);

		stage.setTitle(DASHBOARD);

		stage.getIcons().clear();

		Image image = new Image(new File(MicExtractorDashboardApplication.class.getResource("/LogoIcon.png").toURI()).toURI().toURL().toExternalForm());

		stage.getIcons().add(image);

		stage.show();
   }
}