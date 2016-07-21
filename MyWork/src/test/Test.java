package test;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		test();
		test1();
//		String a = "http://finance.chinanews.com/cj/2016/02-23/7769882.shtml".replace("finance", "www");
//		System.out.println(a);
	}
	
	public static void test(){
		String time = "2016年02月23日12:03";
		String[] time1 = time.split("年");
//		System.out.println(time1[0]+"---->"+time1[1]);
		String[] time2 = time1[1].split("月");
		String[] time3 = time2[1].split("日");
		String year = time1[0];
		String month = time2[0];
		String day = time3[0];
		String clock = time3[1];
		String date = year+"-"+month+"-"+day+" "+clock+":00";
		System.out.println(date);
	}
	
	public static void test1(){
		String time = " 2016年02月23日 23:33　来源：中国新闻网参与互动";
		String[] time1 = time.split("年");
//		System.out.println(time1[0]+"---->"+time1[1]);
		String[] time2 = time1[1].split("月");
		String[] time3 = time2[1].split("日");
		String[] time4 = time3[1].split(" ");
		String[] time5 = time4[1].split("来源");
		String year = time1[0].substring(1,time1[0].length());
		String month = time2[0];
		String day = time3[0];
		String clock = time5[0].substring(0,time5[0].length()-1);
		String date = year+"-"+month+"-"+day+" "+clock+":00";
		System.out.println(date);
	}

}
