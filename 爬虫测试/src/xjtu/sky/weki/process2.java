package xjtu.sky.weki;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * 处理F盘的tag_headword.xls表格，得到所有不重复的单词，并将其按照字母顺序输出 Arraylist动态数组
 */
public class process2 {

	public static void main(String[] args) throws Exception {
		 analysis();
	}

	/**
	 * 排序及去重
	 */
	public static void analysis() throws Exception {
		File file = new File("F:/tag_headword.xls");
		Workbook rwb = Workbook.getWorkbook(new FileInputStream(file));
		Sheet rs = rwb.getSheet(0);
		int rsRows = rs.getRows();
		int rsColumns = rs.getColumns();
		ArrayList<String> list = new ArrayList<String>();
		for (int n = 0; n < rsColumns - 1; n++) {
			for (int m = 0; m < rsRows - 1; m++) {
				String text = rs.getCell(n, m).getContents().toLowerCase();
				if (!text.equals("")) { // 单元格为空时，不保存到list动态数组中
					list.add(text);
				}
			}
		}

		// 动态数组将单词排序
		Collections.sort(list);
		ArrayList<Integer> count = new ArrayList<Integer>();
		count = WordCount(list);
		// 重写Excel
		WritableWorkbook workbook = Workbook.createWorkbook(new File(
				"F:/tag_headword(RemoveRepeated1).xls"));
		WritableSheet sheet = workbook.createSheet("去重后排序结果", 0);
		for (int i = 0; i < list.size(); i++) {
			sheet.addCell(new Label(0, i, list.get(i)));
			sheet.addCell(new Label(1, i, count.get(i) + ""));
		}
		workbook.write();
		workbook.close();
		System.out
				.println("排序后结果已写入Excel中，路径为：F:/tag_headword(RemoveRepeated1).xls");
		// 去重
		ArrayList<String> deallist1 = new ArrayList<String>();
		deallist1 = RemoveRepeated(list); // 去重1
		Collections.sort(deallist1); // 排序

		// 控制台输出结果
		for (int i = 0; i < count.size(); i++) {
			System.out.println(count.get(i));
		}
		for (int i = 0; i < deallist1.size(); i++) {
			System.out.println(deallist1.get(i)); // 使用去重方法1
		}
		System.out.println("含有特征词数量为：" + deallist1.size());
		// 结果存到Excel中
		WritableWorkbook workbook1 = Workbook.createWorkbook(new File(
				"F:/tag_headword(RemoveRepeated2).xls"));
		WritableSheet sheet1 = workbook1.createSheet("去重后排序结果", 0);
		for (int i = 0; i < deallist1.size(); i++) {
			sheet1.addCell(new Label(0, i, deallist1.get(i)));
		}
		workbook1.write();
		workbook1.close();
		System.out
				.println("处理结果已写入Excel中，路径为：F:/tag_headword(RemoveRepeated2).xls");
	}

	/** 去除重复的单词，只保留一个 **/
	public static ArrayList<String> RemoveRepeated(ArrayList<String> arr) {
		ArrayList<String> tmpArr = new ArrayList<String>();
		for (int i = 0; i < arr.size(); i++) {
			if (!tmpArr.contains(arr.get(i))) {
				tmpArr.add(arr.get(i));
			}
		}
		return tmpArr;
	}

	/** 去除重复的单词，只保留一个 **/
	public static ArrayList<Integer> WordCount(ArrayList<String> arr) {
		ArrayList<Integer> count = new ArrayList<Integer>();
		for (int i = 0; i < arr.size(); i++) {
			arr.get(i);
			count.add(i, 0);
			int a = 0;
			for (int j = 0; j < arr.size(); j++) {
				if (arr.get(i).equals(arr.get(j))) {
					count.set(i, ++a);
				}
			}
			// System.out.println(arr.get(i) + "相同的有" + count);
		}
		return count;
	}
	
	public void compare(){
		String str1 = "HELLO"; // 定义字符串
		String str2 = "hello"; // 定义字符串
		System.out.println("\"HELLO\" equals \"hello\" " + str1.equals(str2));
		System.out.println("\"HELLO\" equalsIgnoreCase \"hello\" "
				+ str1.equalsIgnoreCase(str2)); // 不区分大小写的比较
	}

}
