package xjtu.sky.testprogram;

public class test1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s = "0 1 Let's go for lunch!";
		String[] as = s.split(" ");
		System.out.println(as[0]);
		int a = Integer.parseInt(as[0]);
		int b = a + 2;
		System.out.println(b);
	}

}
