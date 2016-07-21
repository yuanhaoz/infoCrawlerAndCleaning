package excel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/*
 * 目标：在内存中操作MS excel文件
 * 1)如果想保存结果，必须调用save()方法
 * 
 * 问题：
 * 1）数字的存入有问题.?
 * 2)数字读出有问题，比如将整形数1，读出后变成1.0，转换为字符串后是”1.0“
 */
public class SpreadFile {
	InputStream inp;
	Workbook wb;
	Sheet defaultSheet;
	String saveFile;

	Map<String, Integer> firstRow = null;
	public int startLine = 1;

	// ------------------------app
	// template-----------------------------------------------
	public static void main(String[] args) {
		
		
		String file = "F:/test.xlsx";
		SpreadFile sf = new SpreadFile(file);
		//行和列都是从0开始的。
		for (int row = 1; row <= sf.getLastRowNum(); row++) {
			String src = sf.getStringValue("sourceURLName", row);
			String des = sf.getStringValue("toURLName", row);

			System.out.println("info:" + src + ":" + des);

			sf.setStringValue("categoryValue", row, "" + 555);
			sf.setStringValue("detail", row, "" + true);
		}
		sf.save(); // 如果修改内容，必须保
	}

	// ------------------------------------------------------------------------------------------------
	public SpreadFile(String rwFileName) {
		// TODO Auto-generated constructor stub
		openFile(rwFileName);
	}
	
	//不推荐使用，为了兼容sheetbase
	public SpreadFile() {
		
	}

	protected void openFile(String rwFileName) {
		// int pos=rwFileName.lastIndexOf(".");
		// String extName=rwFileName.substring(pos+1, rwFileName.length());
		// extName=extName.toLowerCase();
		// System.out.println(rwFileName);

		try {
			inp = new FileInputStream(rwFileName);
			wb = WorkbookFactory.create(inp);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (inp != null) {
				try {
					inp.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		setDefaultSheet(0);
		setSaveFileName(rwFileName);
		printConfigInfo();
	}

	protected void printConfigInfo() {
		System.out.println("startLine=" + startLine);
		System.out.println("openFile=" + saveFile);
		System.out.println(firstRow);

	}
    //0表示第1个sheet
	public void setDefaultSheet(int sheetNum) {
		defaultSheet = wb.getSheetAt(sheetNum);
		initTitle();
	}

	public void setSaveFileName(String out) {
		saveFile = out;
	}

	// ----------------------------------------------------------------------------
	public void initTitle() {
		firstRow = new HashMap<String, Integer>();
		int colNum = getLastColNum();
		for (int i = 0; i < colNum; i++) {
			String title = getStringValue(i, 0);
			firstRow.put(title, i);
		}

	}
	
	public void addColByTitle(String title){
		int num=firstRow.size();
		firstRow.put(title, num);
		setStringValue(title,0,title);
	}

	public int getLastRowNum() {
		return defaultSheet.getLastRowNum();
	}
	
	public int getRows(){
		return getLastRowNum()+1;
	}

	public int getLastColNum() {
		Row firstRow = defaultSheet.getRow(0);
		return firstRow.getLastCellNum();
	}

	// ----------------------get of Cell------------------------------------------------------
	public String getStringValue(String colStr, int row) {
		Integer col = firstRow.get(colStr);
		assert (col != null);
		return getStringValue(col, row);
	}

	//num都是从0开始
	public String getStringValue(int colNum, int rowNum) {
		Row row = defaultSheet.getRow(rowNum);
		if(row==null)
			return "";
		Cell cell = row.getCell(colNum);
		if (cell == null)
			return "";
		//如果是1，会返回1.0		
		if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
			return String.valueOf(cell.getNumericCellValue());
		}
		return cell.getStringCellValue();
	}
	
	//临时方案
	public int getIntValue(String colStr, int row) {
		String value = getStringValue(colStr, row);
		double d=Double.parseDouble(value);
		return (int)(d);
	}
	//临时方案
	public Double getDoubleValue(String colStr, int row) {
		String value = getStringValue(colStr, row);
		return Double.parseDouble(value);
	}

	// ---------------------set of Cell-------------------------------------------------------
	public void setStringValue(String colStr, int row, String str) {
		Integer col = firstRow.get(colStr);
//		assert (col != null);
		if(col==null){
			addColByTitle(colStr);
			 col = firstRow.get(colStr);
		}
		setStringValue(col, row, str);
	}

	public void setStringValue(int colNum, int rowNum, String value) {
		Row row = defaultSheet.getRow(rowNum);
		if(row==null){
			defaultSheet.createRow(rowNum);
			row = defaultSheet.getRow(rowNum);
		}
		Cell cell = row.getCell(colNum);
		if (cell == null)
			cell = row.createCell(colNum);
		cell.setCellType(Cell.CELL_TYPE_STRING);
		cell.setCellValue(value);
	}
	
	//---------------------------------------
	public void setNumberValue(String colStr, int row, Integer value) {
		Integer col = firstRow.get(colStr);
//		assert (col != null);
		if(col==null){
			addColByTitle(colStr);
			 col = firstRow.get(colStr);
		}
		setNumberValue(col, row, value);
	}

	public void setNumberValue(int colNum, int rowNum, Integer value) {
		Row row = defaultSheet.getRow(rowNum);
		Cell cell = row.getCell(colNum);
		if (cell == null)
			cell = row.createCell(colNum);
		cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		cell.setCellValue(value);
	}
	
	public void setNumberValue(String colStr, int row, Double value) {
		Integer col = firstRow.get(colStr);
//		assert (col != null);
		if(col==null){
			addColByTitle(colStr);
			 col = firstRow.get(colStr);
		}
		setNumberValue(col, row, value);
	}

	public void setNumberValue(int colNum, int rowNum, Double value) {
		Row row = defaultSheet.getRow(rowNum);
		Cell cell = row.getCell(colNum);
		if (cell == null)
			cell = row.createCell(colNum);
		cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		cell.setCellValue(value);
	}

	public void setValue(String colStr, int row, Double value) {
		setStringValue(colStr, row, value.toString());
	}
	public void setValue(String colStr, int row, Integer value) {
		setStringValue(colStr, row, value.toString());
	}
	
	
	
	// ----------------------------------------------------------------------------
	public void save() {
		System.out.println("saveFile=" + saveFile);
		if (saveFile == null)
			return;
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(saveFile);
			wb.write(fileOut);
			fileOut.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void close(){
		if (saveFile == null)
			return;
		
	}
	
	// ----------------------------------------------------------------------------
	static public Vector<String> readCol(String file) {
		// 行和列都是从0开始的。
		System.out.println("read file: " + file);
		Vector<String> colStr = new Vector<String>();
		int col = 0;
		SpreadFile sf = new SpreadFile(file);
		for (int row = 1; row <= sf.getLastRowNum(); row++) {
			String src = sf.getStringValue(col, row);
			// System.out.println("info:" + src + ":" + src);
			colStr.add(src);
		}
		System.out.println("read line: " + colStr.size());
		return colStr;
	}

}
