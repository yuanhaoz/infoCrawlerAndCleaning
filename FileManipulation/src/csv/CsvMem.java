package csv;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class CsvMem {
	Vector<Vector<String>> spread = new Vector<Vector<String>>();
	int rowNum = 0;
	int colNum=0;
	
	Vector<String> files=new Vector<String>();
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String file = "F:/Wiki分类体系/DS/test.csv";
		String file2 = "F:/Wiki分类体系/DS/test2.csv";
		CsvMem cfr = new CsvMem(file);
		System.out.println(cfr.getCol(0));
//		cfr.save();
	}
	
	public CsvMem(String inFile){
		addCsvFile(inFile);
	}
	public CsvMem(){

	}
	
	public Vector<String> getCol(int i){
		return spread.get(i);
	}
	
	public int addCsvFile(String file){
		System.out.println("try reading: "+file);
		
		BufferedReader br;
		FileReader reader;
		try {
			reader = new FileReader(file);
			br = new BufferedReader(reader);
			String str = br.readLine();
			if (str == null) {
				br.close();
				return rowNum;
			}
			
			//文件列数与内存中的是否一致。
			String[] items = str.split(",");			
			if (colNum == 0) {
				colNum = items.length;
				files.add(file);
			}
			if (colNum != items.length) {
				System.err.println("file not compatible: ");
				System.out.println(file);
				System.out.println(files.toString());
				br.close();
				return rowNum;
			}
			
			//添加第一行。
			Vector<String> col;
			for (int i = 0; i < colNum; i++) {
				col = new Vector<String>();
				col.add(items[i]);
				spread.add(col);
			}
			rowNum++;
			
			while ((str = br.readLine()) != null) {
				items = str.split(",");
				for (int i = 0; i < colNum; i++) {
					col = spread.get(i);					
					if (i < items.length) {
						col.add(items[i]);
					} else {
						col.add("");
					}
				}
				rowNum++;
			}

			br.close();
			reader.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rowNum;
	}
	

	public int save(){
		int saveNum = 0;
		if(files.size()>0){
			saveNum=save(files.get(0));
		}
		return saveNum;
	}
	public int save(String outfile) {
		int saveNum = 0;
		if (rowNum == 0|| colNum==0 || outfile == null) {
			return 0;
		}
		try {
			FileWriter writer = new FileWriter(outfile);
			for (int i = 0; i < rowNum; i++) {
				String row = spread.get(0).get(i);
				for (int j = 1; j < spread.size(); j++) {
					row = row + "," + spread.get(j).get(i);
				}
				writer.write(row + "\n");
			}
			writer.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return saveNum;

	}
	
	//----------getter & setter-------------------------------------------------------------------------
		public Vector<String> getFile() {
			return files;
		}


		
		public int getRowNum() {
			return rowNum;
		}

		public void setRowNum(int rowNum) {			
			if(this.rowNum >= rowNum){
				System.err.println("this num is less than mem:"+rowNum);
			}else{
				System.err.println("not immplemented");
			}
		}

		public int getColNum() {
			return colNum;
		}

		public void setColNum(int colNum) {
			System.err.println("not immplemented");
		}
}
