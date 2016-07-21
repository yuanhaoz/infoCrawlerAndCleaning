package word2vec.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Test3 {
	public static void test(){
		String path = "F:\\word2vec\\similarityMatrix\\Array1algorithm.txt";
		File inputFilepath = new File(path);
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inputFilepath)));
			String temp = null;
			while ((temp = br.readLine()) != null) {
				String[] split = temp.split(" ");
				int trainWordsCount = split.length;
				System.out.println("����Ϊ��"+trainWordsCount);
			}
		}catch(Exception e){
			System.out.println("+++++++++++");
		}
		
	}
	
	// substring
	public static void test1(){
		String labelName = "aaa.txt";
		String str = labelName.substring(0,labelName.lastIndexOf("."));
		System.out.println(str);
	}
	
	// StringBuffer
	public static void test2(){
		StringBuffer labelName = new StringBuffer("aaa.txt");
		labelName = labelName.append("a" + "\n");
		labelName = labelName.append("a" + "\n");
		System.out.println(labelName);
	}
	public static void main(String[] args){
//		test();
//		test1();
		test2();
	}
	
}
