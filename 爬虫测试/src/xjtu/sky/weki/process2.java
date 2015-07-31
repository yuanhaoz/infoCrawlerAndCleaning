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
 * ����F�̵�tag_headword.xls��񣬵õ����в��ظ��ĵ��ʣ������䰴����ĸ˳����� Arraylist��̬����
 */
public class process2 {

	public static void main(String[] args) throws Exception {
		 analysis();
	}

	/**
	 * ����ȥ��
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
				if (!text.equals("")) { // ��Ԫ��Ϊ��ʱ�������浽list��̬������
					list.add(text);
				}
			}
		}

		// ��̬���齫��������
		Collections.sort(list);
		ArrayList<Integer> count = new ArrayList<Integer>();
		count = WordCount(list);
		// ��дExcel
		WritableWorkbook workbook = Workbook.createWorkbook(new File(
				"F:/tag_headword(RemoveRepeated1).xls"));
		WritableSheet sheet = workbook.createSheet("ȥ�غ�������", 0);
		for (int i = 0; i < list.size(); i++) {
			sheet.addCell(new Label(0, i, list.get(i)));
			sheet.addCell(new Label(1, i, count.get(i) + ""));
		}
		workbook.write();
		workbook.close();
		System.out
				.println("���������д��Excel�У�·��Ϊ��F:/tag_headword(RemoveRepeated1).xls");
		// ȥ��
		ArrayList<String> deallist1 = new ArrayList<String>();
		deallist1 = RemoveRepeated(list); // ȥ��1
		Collections.sort(deallist1); // ����

		// ����̨������
		for (int i = 0; i < count.size(); i++) {
			System.out.println(count.get(i));
		}
		for (int i = 0; i < deallist1.size(); i++) {
			System.out.println(deallist1.get(i)); // ʹ��ȥ�ط���1
		}
		System.out.println("��������������Ϊ��" + deallist1.size());
		// ����浽Excel��
		WritableWorkbook workbook1 = Workbook.createWorkbook(new File(
				"F:/tag_headword(RemoveRepeated2).xls"));
		WritableSheet sheet1 = workbook1.createSheet("ȥ�غ�������", 0);
		for (int i = 0; i < deallist1.size(); i++) {
			sheet1.addCell(new Label(0, i, deallist1.get(i)));
		}
		workbook1.write();
		workbook1.close();
		System.out
				.println("��������д��Excel�У�·��Ϊ��F:/tag_headword(RemoveRepeated2).xls");
	}

	/** ȥ���ظ��ĵ��ʣ�ֻ����һ�� **/
	public static ArrayList<String> RemoveRepeated(ArrayList<String> arr) {
		ArrayList<String> tmpArr = new ArrayList<String>();
		for (int i = 0; i < arr.size(); i++) {
			if (!tmpArr.contains(arr.get(i))) {
				tmpArr.add(arr.get(i));
			}
		}
		return tmpArr;
	}

	/** ȥ���ظ��ĵ��ʣ�ֻ����һ�� **/
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
			// System.out.println(arr.get(i) + "��ͬ����" + count);
		}
		return count;
	}
	
	public void compare(){
		String str1 = "HELLO"; // �����ַ���
		String str2 = "hello"; // �����ַ���
		System.out.println("\"HELLO\" equals \"hello\" " + str1.equals(str2));
		System.out.println("\"HELLO\" equalsIgnoreCase \"hello\" "
				+ str1.equalsIgnoreCase(str2)); // �����ִ�Сд�ıȽ�
	}

}
