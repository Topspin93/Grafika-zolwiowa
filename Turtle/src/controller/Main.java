package controller;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	private Stage primaryStage;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			this.primaryStage = primaryStage;
			mainWindow();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void mainWindow() {
		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/MainWindowView.fxml"));
			AnchorPane pane = loader.load();
			primaryStage.setWidth(1260);
			primaryStage.setHeight(820);
			
			MainWindowController mainWindowController = loader.getController();
			mainWindowController.setMain(this, primaryStage);
			
			Scene scene = new Scene(pane);
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			scene.getStylesheets().add(Main.class.getResource("/view/application.css").toExternalForm());
			primaryStage.setTitle("Turtle / Maciej Gawlowski gr. B");
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
