package test1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class test1 {

	@Test
	public void testlastindexof() {
		String str = "This is yiibai";

		// returns the index of last occurrence of character i
		System.out.println("last index of letter 'i' =  "
				+ str.lastIndexOf('i'));

		// returns -1 as character e is not in the string
		System.out.println("last index of letter 'e' =  "
				+ str.lastIndexOf('e'));
	}

	@Test
	public void testtimes() {
		// 在字符串中找ab出现的次数
		Pattern p = Pattern.compile("(?i)and");
		String u = "What AND Andss are the and 10 must-know algorithms and data";
		Matcher m = p.matcher(u);
		int i = 0;
		while (m.find()) {
			i++;
		}
		System.out.println("出现次数： " + i);
	}

}
