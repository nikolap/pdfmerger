package np.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.util.PDFMergerUtility;

public class PDFMerger {
	private List<InputStream> sourcePDFs = new ArrayList<InputStream>();
	private PDFMergerUtility mergerUtility = new PDFMergerUtility();
	
	public PDFMerger(ObservableList<File> files, File destination){
		for (File file : files){
			try {
				sourcePDFs.add(new FileInputStream(file));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		mergerUtility.addSources(sourcePDFs);
		try {
			mergerUtility.setDestinationStream(new FileOutputStream(destination));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean run(){
		try {
			mergerUtility.mergeDocuments();
			return true;
		} catch (COSVisitorException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
}
