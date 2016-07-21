package weka;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import java.io.File;

public class CSV2Arff {
	/**
	 * takes 2 arguments: 
	 * - CSV input file 
	 * - ARFF output file
	 */
	public static void main(String[] args) throws Exception {

	}
	
	public void convert(File csvInput, File arffOutput) throws Exception {
		
		System.out.println("Usage: CSV2Arff <input.csv> <output.arff>\n");

		// load CSV
		CSVLoader loader = new CSVLoader();
		loader.setSource(csvInput);
		Instances data = loader.getDataSet();

		// save ARFF
		ArffSaver saver = new ArffSaver();
		saver.setInstances(data);
		saver.setFile(arffOutput);
		saver.setDestination(arffOutput);
		saver.writeBatch();
	}
}