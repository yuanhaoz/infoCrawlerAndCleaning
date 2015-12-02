package test1;

import java.util.HashMap;
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
	public void testTimes() {
		// 在字符串中找ab出现的次数
		Pattern p = Pattern.compile("(?i)and");
		String u = "What AND Andss are the and 10 must-know algorithms and data";
		Matcher m = p.matcher(u);
		int i = 0;
		while (m.find()) {
			i++;
		}
		System.out.println("出现次数： " + i);
		
		 HashMap<String , Double> map = new HashMap<String , Double>(); 
		 map.put("语文" , 80.0); 
		 map.put("数学" , 89.0); 
		 map.put("英语" , 78.2); 
	}
	
	@Test
	public void deleteString() {
		String s1 = "abc";
		String s2 = "1111abcdefe";
		if (s2.contains(s1)) {
			System.out.println("s2包含了s1");
			// 删掉s1
			s2 = s2.replace(s2.substring(s2.indexOf(s1), s2.indexOf(s1) + s1.length()), "");
			System.out.println(s2);
		} else {
			System.out.println("s2不包含s1");
		}
	}
		   

}
