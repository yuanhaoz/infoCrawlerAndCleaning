package test1;

public class RemovePunctuation {
 
public static void main(String[] args) {
		String str = "!!������!!!!%*��%����KTVȥ���ű�ţ�����Ȼ,����!!..**���";
		System.out.println(str);
		String str1 = str.replaceAll("[\\pP\\p{Punct}]", "");
		System.out.println("str1:" + str1);


		String str2 = str.replaceAll("[//pP]", "");
		System.out.println("str2:" + str2);


		String str3 = str.replaceAll("[//p{P}]", "");
		System.out.println("str3:" + str3);
	}
}
