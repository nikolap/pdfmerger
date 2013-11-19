package np.pdf.gui;

import java.io.File;
import java.net.URI;
import java.util.List;

import np.pdf.PDFMerger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialogs;
import javafx.scene.control.Dialogs.DialogOptions;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainWindow extends Application {
	private ListView<File> fileList = new ListView<File>();
	private final ObservableList<File> pdfFiles = FXCollections.observableArrayList();
	private TextField outputPDFField = new TextField();
	private File outputPDFFile;
	private Stage stage;
	
	@Override
	public void start(final Stage stage) {
		this.stage = stage;
		this.stage.setTitle("PDF Merger");
		Scene scene = new Scene(new VBox(), 800, 600);
		scene.setFill(Color.BEIGE);
		scene.getRoot().setStyle("-fx-font-size: 14;");
		
		MenuBar menuBar = new MenuBar();
		Menu menuFile = new Menu("File");
		MenuItem about = new MenuItem("About");
		MenuItem exit = new MenuItem("Exit");
		menuFile.getItems().addAll(about, exit);
		menuBar.getMenus().addAll(menuFile);
		
		about.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				aboutDialog();
			}
		});
		
		exit.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				System.exit(0);
			}
		});
		//TODO: sizing		
		Button addPDFButton = new Button("Add PDFs to merge...");
		Button upButton = new Button("Move Up");
		Button downButton = new Button("Move Down");
		Button removePDFButton = new Button("Remove PDF");
		Button selectSaveButton = new Button("Select...");
		Button startButton = new Button("Start!");
		Text inputLabel = new Text("Input PDFs\nThis is the order in which the files will be merged.");
		Text outputLabel = new Text("Output PDF");
		
		addPDFButton.setMaxWidth(Double.MAX_VALUE);
		upButton.setMaxWidth(Double.MAX_VALUE);
		downButton.setMaxWidth(Double.MAX_VALUE);
		removePDFButton.setMaxWidth(Double.MAX_VALUE);
		selectSaveButton.setMaxWidth(Double.MAX_VALUE);
		startButton.setMaxWidth(Double.MAX_VALUE);
		
		fileList.setMinWidth(650);
		fileList.setItems(pdfFiles);
		fileList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		outputPDFField.setMinWidth(650);
		outputPDFField.setDisable(true);

		addPDFButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				addPDFsDialog();
			}
		});
		upButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				upAction();
			}
		});
		downButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				downAction();
			}
		});
		removePDFButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				removeAction();
			}
		});
		selectSaveButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				savePDFDialog();
			}
		});
		startButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				startAction();
			}
		});
		
		GridPane inputPane = new GridPane();
		inputPane.setAlignment(Pos.CENTER);
		inputPane.setHgap(12);
		inputPane.setVgap(12);
		inputPane.setPadding(new Insets(12));
		inputPane.add(addPDFButton, 0, 0, 2, 1);
		inputPane.add(inputLabel, 0, 1);
		inputPane.add(fileList, 0, 2, 1, 4);
		inputPane.add(upButton, 1, 2);
		inputPane.add(downButton, 1, 3);
		inputPane.add(removePDFButton, 1, 4);
		
		HBox outputHBox = new HBox();
		outputHBox.setPadding(new Insets(12));
		outputHBox.setSpacing(10);
		outputHBox.getChildren().addAll(outputPDFField, selectSaveButton);
		
		VBox outputVBox = new VBox();
		outputVBox.setPadding(new Insets(12));
		outputVBox.setSpacing(8);
		outputVBox.getChildren().addAll(outputLabel, outputHBox, new Separator(), startButton);
		
		VBox root = new VBox();
		root.setAlignment(Pos.CENTER);
		root.setPadding(new Insets(12));
		root.setSpacing(8);
		root.getChildren().addAll(inputPane, new Separator(), outputVBox);
		
		((VBox) scene.getRoot()).getChildren().addAll(menuBar,
				inputPane, new Separator(), outputVBox);
		this.stage.setScene(scene);
		this.stage.show();
	}
	
	private void aboutDialog(){
		
		Text aboutLabel = new Text("PDF Merger\n2013 Nikola Peric");
		Text licenseLabel = new Text("Licensed under The MIT License");
		final Hyperlink homepage = new Hyperlink("http://www.example.com");
		final Hyperlink github = new Hyperlink("https://github.com/nikolap/pdfmerger");
		final Hyperlink license = new Hyperlink("http://opensource.org/license/MIT");
		
		homepage.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent e) {
		    	try {
					openUrl(homepage.getText());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		    }
		});
		github.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent e) {
		    	try {
					openUrl(github.getText());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		    }
		});
		license.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent e) {
		        try {
					openUrl(license.getText());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		    }
		});
		
		VBox aboutBox = new VBox();
		aboutBox.getChildren().addAll(aboutLabel, homepage, github, licenseLabel, license);
		Dialogs.showCustomDialog(stage, aboutBox, "Please log in", "Login", DialogOptions.OK, null);
	}
	
	private void openUrl(String url) throws Exception {
		java.awt.Desktop.getDesktop().browse(new URI(url));
	}
	
	private void addPDFsDialog(){
		FileChooser openFileChooser = new FileChooser();
		FileChooser.ExtensionFilter openExtension = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
		openFileChooser.getExtensionFilters().add(openExtension);
		openFileChooser.setTitle("Add PDFs to merge...");
		openFileChooser.setInitialDirectory(new File(System.getProperty("user.home"))); 
		
		List<File> list = openFileChooser.showOpenMultipleDialog(stage);
		if (list != null) {
            for (File file : list) {
                addPDFs(file);
            }
        }
	}
	
	private void addPDFs(File pdf){
		pdfFiles.add(pdf);
	}
	
	private void upAction(){
		ObservableList<Integer> selectedPDFs = fileList.getSelectionModel().getSelectedIndices();
		if (selectedPDFs.indexOf(0) == -1){
			for (int selectedPDF : selectedPDFs){
				File pdfFileUp = pdfFiles.get(selectedPDF);
				File pdfFileDown = pdfFiles.get(selectedPDF - 1);
				pdfFiles.set(selectedPDF - 1, pdfFileUp);
				pdfFiles.set(selectedPDF, pdfFileDown);
			}
		}
	}
	
	private void downAction(){
		ObservableList<Integer> selectedPDFs = fileList.getSelectionModel().getSelectedIndices();
		if (selectedPDFs.indexOf(pdfFiles.size() - 1) == -1){
			for (int selectedPDF : selectedPDFs){
				File pdfFileUp = pdfFiles.get(selectedPDF + 1);
				File pdfFileDown = pdfFiles.get(selectedPDF);
				pdfFiles.set(selectedPDF + 1, pdfFileDown);
				pdfFiles.set(selectedPDF, pdfFileUp);
			}
		}
	}

	private void removeAction(){
		ObservableList<File> selectedPDFs = fileList.getSelectionModel().getSelectedItems();
		int itemCount = selectedPDFs.size();

		for (int i = itemCount - 1; i > -1; i--){
			File selectedPDF = selectedPDFs.get(i);
			pdfFiles.remove(pdfFiles.indexOf(selectedPDF));
		}
	}
	
	private void savePDFDialog(){
		FileChooser saveFileChooser = new FileChooser();
		FileChooser.ExtensionFilter saveExtension = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
		saveFileChooser.getExtensionFilters().add(saveExtension);
		saveFileChooser.setTitle("Merged PDF save location");
		saveFileChooser.setInitialDirectory(new File(System.getProperty("user.home"))); 

        File file = saveFileChooser.showSaveDialog(stage);
        
        if (file != null) {
        	String path = file.getAbsolutePath();
        	String extension = ".pdf";
        	int offset = path.length() - extension.length();
        	
        	// case insensitive check for .pdf extension in file name
        	if (path.regionMatches(true, offset, extension, 0, extension.length())){
        		outputPDFField.setText(file.getAbsolutePath());
        		outputPDFFile = file;
        	}
        	else{
        		outputPDFField.setText(file.getAbsolutePath() + extension);
        		outputPDFFile = new File(file.getAbsoluteFile() + extension);
        	}
        }
	}
	
	private void startAction(){
		if (pdfFiles.size() < 2) {
			showWarningDialog("You need a minimum of two (2) PDF files to merge!");
		}
		else if (outputPDFFile == null) {
			showWarningDialog("You need a destination for your merged PDF!");
		}
		else {
		PDFMerger merger = new PDFMerger(pdfFiles, outputPDFFile);
		if (merger.run())
			showSuccessDialog(outputPDFFile);
		else
			showErrorDialog();
		}
	}
	
	private void showWarningDialog(String text){
		Dialogs.showWarningDialog(stage, text, "Missing information!", "Warning");
	}
	
	private void showSuccessDialog(File destination){
		Dialogs.showInformationDialog(stage, "Your PDFs have successfully been merged to the file " + destination.getName(),
			    "Merger complete!", "Success");
	}
	private void showErrorDialog(){
		Dialogs.showErrorDialog(stage, "Something went wrong...", "Oops, an error happened!",
			    "Error"); //TODO: exception included in info
	}
}