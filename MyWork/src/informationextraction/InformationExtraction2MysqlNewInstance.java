package informationextraction;

import java.io.File;

public class InformationExtraction2MysqlNewInstance {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		test();
//		test("Data_structure");
//		test("Data_mining");
//		test("Computer_network");
//		testHttpClient("Data_structure_excel112");
		testSelenium("Data_structure_excel59");
//		System.out.println("aa aa".length());;
	}
	
	/**
	 * 将数据结构下的Array的数据存到数据库中
	 */
	public static void test(){
		String keyword = "Array";
		int pagelength = 37;
		String course = "Data_structure";
		InformationExtraction2MysqlNew m = new InformationExtraction2MysqlNew();
		try {
			m.storeAllFragmentForHttpClient(keyword, pagelength, course);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("数据导入数据库出错...");
			e.printStackTrace();
		}
	}
	
	/**
	 * 将某门课程course下的数据存到数据库中
	 */
	public static void testHttpClient(String course){
		InformationExtraction2MysqlNew m = new InformationExtraction2MysqlNew();
		File file0 = new File("file/datacollection/" + course);
		File[] files = file0.listFiles();
		for (int i = 0; i < files.length; i++) {
			String keyword = files[i].getName();
			if (!keyword.contains(".html")) {
				int pagelength = 0;
				try {
					pagelength = InformationExtraction2ExcelForHttpClient.questionPageNumber(keyword);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("关键词页面长度计算出错...");
					e.printStackTrace();
				}
				try {
					m.storeAllFragmentForHttpClient(keyword, pagelength, course);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("数据导入数据库出错...");
					e.printStackTrace();
				} 
				System.out.println(keyword + "的信息已经保存到数据库...");
				System.out.println("-----------------------------");
			}
		}
	}
	
	/**
	 * 将某门课程course下的数据存到数据库中
	 */
	public static void testSelenium(String course){
		InformationExtraction2MysqlNew m = new InformationExtraction2MysqlNew();
		File file0 = new File("file/datacollection/" + course);
		File[] files = file0.listFiles();
		for (int i = 0; i < files.length; i++) {
			String keyword = files[i].getName();
			if (!keyword.contains(".html")) {
				int pagelength = 0;
				try {
					pagelength = InformationExtraction2ExcelForSelenium.questionPageNumber(keyword);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("关键词页面长度计算出错...");
					e.printStackTrace();
				}
				try {
					m.storeAllFragmentForSelenium(keyword, pagelength, course);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("数据导入数据库出错...");
					e.printStackTrace();
				} 
				System.out.println(keyword + "的信息已经保存到数据库...");
				System.out.println("-----------------------------");
			}
		}
	}

}
