package test1;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Termprocess {
	public static void main(String[] args) throws FileNotFoundException, EncryptedDocumentException, InvalidFormatException {
		// 读入术语文件term_192
	            
		InputStream excel = new FileInputStream(new File("F:\\termV2_192.xlsx"));
		System.out.println(excel);
		XSSFWorkbook xssfWorkbook = null;
		List<String> termSet = new ArrayList<String>();
		try {
			xssfWorkbook = new XSSFWorkbook(excel);
			for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
				XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
				if(xssfSheet == null){
					continue;
				}
				for(int rowNum = 0;numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++){
					XSSFRow xssfRow = xssfSheet.getRow(rowNum);
					if(xssfRow != null){
						String term = xssfRow.getCell(0).getStringCellValue();
						termSet.add(term);
;					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=0;i<termSet.size();i++){
			System.out.println(i+termSet.get(i));
		}
		
		
	}

}
