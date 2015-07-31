package test1;

import java.io.*;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class test2 {

	public static void main(String[] args) throws Exception {
//		analysis();
		String keyword = "AVL+tree";
		String a = keyword.replaceAll("\\+", "\\_");
		System.out.println(a);
	}

	/**
	 * 解析问题网页，将其保存到本地的Excel表中
	 */
	public static void analysis() throws Exception {
//		File file = new File("F:/已标数据/Radix+sort/Radix+sort-tag_changed3.xls");
//		File file = new File("F:/已标数据/Randomized+algorithm/Randomized+algorithm-tag_changed3.xls");
//		File file = new File("F:/已标数据/Red-black+tree/Red-black+tree-tag_changed3.xls");
//		File file = new File("F:/已标数据/Search+algorithm/Search+algorithm-tag_changed3.xls");
//		File file = new File("F:/已标数据/Selection+sort/Selection+sort-tag_changed3.xls");
		File file = new File("F:/已标数据/Skip+list/Skip+list-tag_changed3.xls");
		Workbook rwb = Workbook.getWorkbook(new FileInputStream(file));
		Sheet[] sheets = rwb.getSheets(); 
		for(int i = 0; i < sheets.length; i++){
			String sheetname = sheets[i].getName();
			if(sheetname.equals("答案二分类变量")){
				Sheet rs = sheets[i];
				int rsRows = rs.getRows();
				int zero[] = {0,0,0,0,0,0,0,0};
				int one[] = {0,0,0,0,0,0,0,0};
				String num[] = {"000","001","010","011","100","101","110","111"};
				for (int m = 0; m < rsRows - 1; m++) {
					String keyword = rs.getCell(0, m + 1).getContents();  //Exist(1)
					String link = rs.getCell(1, m + 1).getContents();     //Link
					String conmment = rs.getCell(2, m + 1).getContents(); //Comment
					String useful = rs.getCell(3, m + 1).getContents();   //useful
//					System.out.println(keyword + " " + link + " " + conmment + " " + useful);
					String com = keyword + link + conmment;
					switch(com){
					case "000":
						if(useful.equals("0")){
							zero[0] = zero[0] + 1;
						}else{
							one[0] = one[0] + 1;
						}
						break;
					case "001":
						if(useful.equals("0")){
							zero[1] = zero[1] + 1;
						}else{
							one[1] = one[1] + 1;
						}
						break;
					case "010":
						if(useful.equals("0")){
							zero[2] = zero[2] + 1;
						}else{
							one[2] = one[2] + 1;
						}
						break;
					case "011":
						if(useful.equals("0")){
							zero[3] = zero[3] + 1;
						}else{
							one[3] = one[3] + 1;
						}
						break;
					case "100":
						if(useful.equals("0")){
							zero[4] = zero[4] + 1;
						}else{
							one[4] = one[4] + 1;
						}
						break;
					case "101":
						if(useful.equals("0")){
							zero[5] = zero[5] + 1;
						}else{
							one[5] = one[5] + 1;
						}
						break;
					case "110":
						if(useful.equals("0")){
							zero[6] = zero[6] + 1;
						}else{
							one[6] = one[6] + 1;
						}
						break;
					case "111":
						if(useful.equals("0")){
							zero[7] = zero[7] + 1;
						}else{
							one[7] = one[7] + 1;
						}
						break;
					}
					
					
				}  //for
				
				System.out.println("useful = 0 :");
				for(int j = 0; j < zero.length; j++){
					System.out.println(num[j] + ": " + zero[j]);
				}
				
				System.out.println("useful = 1 :");
				for(int j = 0; j < zero.length; j++){
					System.out.println(num[j] + ": " + one[j]);
				}
				
			}
		}
	}
}
