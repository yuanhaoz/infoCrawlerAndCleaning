package excel;

public class SpreadFileTest {

	public SpreadFileTest() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

//		XlsTest();
//		XlsxTest();
		XlsTest2();
	}

	public static void XlsxTest() {
		String file="testdata/XlsxTest.xlsx";
		SpreadFile sf=new SpreadFile(file);
		System.out.println(sf.getStringValue(1, 1));
//		sf.setStringValue(2, 1, "www");
		int outRow=2;
		String concept="www";
		sf.setStringValue("src", outRow, concept);
		sf.save();
	}

	public static void XlsTest() {
		String file="testdata/XlsTest.xls";
		SpreadFile sf=new SpreadFile(file);
		System.out.println(sf.getStringValue(1, 0));
		sf.setStringValue(2, 1, "www");
		sf.save();
	}
	
	//测试整形数的读出
	public static void XlsTest2() {
		String file="testdata/XlsTest.xls";
		SpreadFile sf=new SpreadFile(file);
		System.out.println(sf.getStringValue(0,3));
//		sf.setStringValue(2, 1, "www");
//		sf.save();
	}



}
