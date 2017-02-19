package controller;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Turtle;

public class MainWindowController {
	private Main main;
	private Stage primaryStage;
	private int allCommandsLength; // do okreœlenia d³ugoœci wszystkich
									// wpisanych komend, aby mo¿na by³o braæ
									// tylko ostatni¹ komendê
	private ArrayList<String> loadedCommands = new ArrayList<String>(); // lista
																		// do
																		// za³adowania
																		// pliku
	private int licznik = 0; // licznik linii do wypisania
	private Turtle turtle = new Turtle();

	@FXML
	private ImageView imageTurtle;
	@FXML
	private Pane root;
	@FXML
	private TextField textSetX, textSetY, textForward, textRotate;
	@FXML
	private ColorPicker colorPicker;
	@FXML
	private ToggleButton buttonLift;
	@FXML
	private TextArea textArea;
	@FXML
	private HBox loadHBox;

	public void setMain(Main main, Stage primaryStage) {
		this.main = main;
		this.primaryStage = primaryStage;

	}

	public void initialize() {
		textArea.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				int commandLength = textArea.getText().length() - allCommandsLength;
				String command = textArea.getText().substring(allCommandsLength, allCommandsLength + commandLength);
				allCommandsLength = allCommandsLength + commandLength + 1;
				if (command.matches("ustaw \\d+,\\d+")) {
					char przecinek = ','; // szukany przecinek
					int i = 7; // zmienna przeszukuj¹ca indeksy
					while (command.charAt(i) != ',') {
						i++;
					}
					int x = Integer.parseInt(command.substring(6, i));
					int y = Integer.parseInt(command.substring(i + 1, commandLength));
					turtle.set(x, y);
				} else if (command.matches("naprzod \\d+")) {
					int forward = Integer.parseInt(command.substring(8, commandLength));
					Path path = turtle.forward(forward, colorPicker.getValue());
					root.getChildren().add(path);
				} else if (command.matches("obrot \\d+")) {
					int angle = Integer.parseInt(command.substring(6, commandLength));
					turtle.rotate(angle);
				} else if (command.matches("podnies")) {
					buttonLift.setSelected(true);
					turtle.setLifted(true);
				} else if (command.matches("opusc")) {
					buttonLift.setSelected(false);
					turtle.setLifted(false);
				} else if (command.matches("kolor \\D+")) {
					try {
						Color color = Color.valueOf(command.substring(6, command.length()));
						colorPicker.setValue(color);
					} catch (IllegalArgumentException exception) {
					}
				}
			}
		});
		colorPicker.setValue(Color.BLACK);
		turtle.setImageView(imageTurtle);
	}

	@FXML
	public void set() {
		try {
			int x = Integer.parseInt(textSetX.getText());
			int y = Integer.parseInt(textSetY.getText());
			czyscPolaTekstowe();
			turtle.set(x, y);
			String command = "ustaw " + x + "," + y + "\n";
			textArea.setText(textArea.getText() + command);
			allCommandsLength = textArea.getText().length();
		} catch (Exception e) {
			czyscPolaTekstowe();
		}

	}

	@FXML
	public void forward() {
		try {
			int forward = Integer.parseInt(textForward.getText());
			czyscPolaTekstowe();
			Path path = turtle.forward(forward, colorPicker.getValue());
			root.getChildren().add(path);
			String command = "naprzod " + forward + "\n";
			textArea.setText(textArea.getText() + command);
			allCommandsLength = textArea.getText().length();
		} catch (Exception e) {
			czyscPolaTekstowe();
		}
	}

	@FXML
	public void rotate() {
		try {
			int angle = Integer.parseInt(textRotate.getText());
			czyscPolaTekstowe();
			turtle.rotate(angle);
			String command = "obrot " + angle + "\n";
			textArea.setText(textArea.getText() + command);
			allCommandsLength = textArea.getText().length();
		} catch (Exception e) {
			czyscPolaTekstowe();
		}
	}

	@FXML
	public void lift() {
		if (buttonLift.isSelected()) {
			turtle.setLifted(true);
			String command = "podnies" + "\n";
			textArea.setText(textArea.getText() + command);
			allCommandsLength = textArea.getText().length();
		} else {
			turtle.setLifted(false);
			String command = "opusc" + "\n";
			textArea.setText(textArea.getText() + command);
			allCommandsLength = textArea.getText().length();
		}
	}

	public void czyscPolaTekstowe() {
		textSetX.clear();
		textSetY.clear();
		textForward.clear();
		textRotate.clear();
	}

	@FXML
	public void showCommandsWindow() {
		Stage stage = new Stage();
		stage.setTitle("Dostêpne komendy");
		stage.setWidth(300);
		VBox rootdialog = new VBox();
		rootdialog.setSpacing(10);
		rootdialog.setPadding(new Insets(20, 20, 20, 20));
		rootdialog.setAlignment(Pos.CENTER);
		Label label1 = new Label(
				"Dostêpne komendy: \n-ustaw x,y \n-naprzod x \n-obrot x \n-kolor x \n-opusc \n-podnies");
		label1.setFont(new Font(16));
		Button button1 = new Button("Zamknij");
		button1.setOnAction(e -> {
			stage.close();
		});
		rootdialog.getChildren().addAll(label1, button1);
		stage.setScene(new Scene(rootdialog));
		stage.initOwner(primaryStage);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.show();
	}

	@FXML
	public void close() {
		System.exit(0);
	}

	@FXML
	public void executeCommands() {
		try {
			for (int i = 0; i < loadedCommands.size(); i++) {
				String command = loadedCommands.get(i);
				textArea.setText(textArea.getText() + command);
				if (command.matches("ustaw \\d+,\\d+")) {
					char przecinek = ','; // szukany przecinek
					int ii = 7; // zmienna przeszukuj¹ca indeksy
					while (command.charAt(ii) != ',') {
						ii++;
					}
					int x = Integer.parseInt(command.substring(6, ii));
					int y = Integer.parseInt(command.substring(ii + 1, command.length()));
					turtle.set(x, y);
				} else if (command.matches("naprzod \\d+")) {
					int forward = Integer.parseInt(command.substring(8, command.length()));
					Path path = turtle.forward(forward, colorPicker.getValue());
					root.getChildren().add(path);
				} else if (command.matches("obrot \\d+")) {
					int angle = Integer.parseInt(command.substring(6, command.length()));
					turtle.rotate(angle);
				} else if (command.matches("podnies")) {
					buttonLift.setSelected(true);
					turtle.setLifted(true);
				} else if (command.matches("opusc")) {
					buttonLift.setSelected(false);
					turtle.setLifted(false);
				} else if (command.matches("kolor \\D+")) {
					try {
						Color color = Color.valueOf(command.substring(6, command.length()));
						colorPicker.setValue(color);
					} catch (IllegalArgumentException exception) {
					}
				}
				textArea.setText(textArea.getText() + "\n");
				licznik++;
				if (licznik==loadedCommands.size())
					loadHBox.setDisable(true);
			}
		} catch (IndexOutOfBoundsException e) {
		}
	}

	@FXML
	public void nextCommand() {
		try {
			String command = loadedCommands.get(licznik);
			textArea.setText(textArea.getText() + command);
			if (command.matches("ustaw \\d+,\\d+")) {
				char przecinek = ','; // szukany przecinek
				int ii = 7; // zmienna przeszukuj¹ca indeksy
				while (command.charAt(ii) != ',') {
					ii++;
				}
				int x = Integer.parseInt(command.substring(6, ii));
				int y = Integer.parseInt(command.substring(ii + 1, command.length()));
				turtle.set(x, y);
			} else if (command.matches("naprzod \\d+")) {
				int forward = Integer.parseInt(command.substring(8, command.length()));
				Path path = turtle.forward(forward, colorPicker.getValue());
				root.getChildren().add(path);
			} else if (command.matches("obrot \\d+")) {
				int angle = Integer.parseInt(command.substring(6, command.length()));
				turtle.rotate(angle);
			} else if (command.matches("podnies")) {
				buttonLift.setSelected(true);
				turtle.setLifted(true);
			} else if (command.matches("opusc")) {
				buttonLift.setSelected(false);
				turtle.setLifted(false);
			} else if (command.matches("kolor \\D+")) {
				try {
					Color color = Color.valueOf(command.substring(6, command.length()));
					colorPicker.setValue(color);
				} catch (IllegalArgumentException exception) {
				}
			}
			textArea.setText(textArea.getText() + "\n");
			licznik++;
		} catch (IndexOutOfBoundsException e) {
			loadHBox.setDisable(true);
		}
	}

	@FXML
	public void load() {
		Scanner in = null;
		try {
			loadedCommands.clear();
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Wczytaj plik");
			fileChooser.getExtensionFilters().add(new ExtensionFilter("Pliki tekstowe", "*.txt"));
			File selectedFile = fileChooser.showOpenDialog(primaryStage);
			if (selectedFile != null) {
				in = new Scanner(Paths.get(selectedFile.getAbsolutePath()));
				while (in.hasNextLine()) {
					loadedCommands.add(in.nextLine());
				}
				licznik = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null)
				in.close();
		}
		loadHBox.setDisable(false);
	}
	
	@FXML
	public void clearPane(){
		root.getChildren().clear();
		root.getChildren().add(imageTurtle);
		imageTurtle.setRotate(0);
		turtle.set(0, 0);
		turtle.setRotation(0);
		turtle.setLifted(false);
		buttonLift.setSelected(false);
		textArea.clear();
	}

}
