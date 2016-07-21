package informationextraction;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// 在字符串中找关键词出现的次数
		int count = 0;
		String content = "Collision+(computer+science)+dasdaasd";
		
		String keyword = "Collision+(computer+science)";
		String[] keywordArray = keyword.split("\\+");      //将关键词以+分开存到数组里面，用于匹配文本
		System.out.println("关键词的组成有： ");
		for(int i = 0; i < keywordArray.length; i++){
			System.out.println(keywordArray[i]);
		}
		
		for(int i = 0; i < keywordArray.length; i++){
			if(keywordArray[i].contains("(")){
				keywordArray[i] = keywordArray[i].substring(1, keywordArray[i].length());
			}
			if(keywordArray[i].contains(")")){
				keywordArray[i] = keywordArray[i].substring(0, keywordArray[i].length()-1);
			}
			Pattern p = Pattern.compile("(?i)" + keywordArray[i]);    //忽略大小写
			Matcher m = p.matcher(content);
			while (m.find()) {
				count++;
			}
			System.out.println(count);
		}
		
	}

}
