package np.pdf;

import java.io.IOException;

import np.pdf.gui.MainWindow;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @author Nikola Peric
 *
 */
public class Main extends Application{
	/**
	 * Launches the PDFMerger program.
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		MainWindow gui = new MainWindow();
		gui.start(stage);
	}
}
