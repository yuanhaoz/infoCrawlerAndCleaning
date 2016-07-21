package util;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

//import analyzer.StanfordLemmatizer;

/**
 * 对字符串进行高级处理
 * @author MJ
 * @description
 * @usage 所有方法都是静态方法，所以可以在目标类里直接使用类方法来直接调用
 */
public class StringUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		  String s="ConferenceId";
		  String dir="D:\\Program Files\\MyEclipse\\xjtu_workspace\\graduate\\src";
		  find(dir,s);
	}
	
	/**
	 * 将以separator分隔的s转换成vector
	 * @param s
	 * @param separator
	 * @return
	 */
	public static Vector<String> convertStringToVector(String s,String separator){
		if(s==null)
			return null;
		Vector<String> v=new Vector<String>();
		String temp[]=s.split(separator);
		for(String str:temp)
			v.add(str);
		return v;
	}
	
	/**
	 * 将v转换成以separator分隔的字符串
	 * @param v
	 * @param separator
	 * @return
	 */
	public static String convertVectorToString(Vector<String> v,String separator){
		if(v==null||v.size()==0)
			return null;
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<v.size()-1;i++){
			sb.append(v.get(i)).append(separator);
		}
		sb.append(v.get(v.size()-1));
		return sb.toString();
	}

	/**
	 * 在s中查找pos之前的str位置
	 * 
	 * @param s
	 * @param str
	 * @param pos
	 * @return
	 */
	public static int upIndexOf(String s, String str, int pos) {
		String subS = s.substring(0, pos);
		return subS.lastIndexOf(str, pos);
	}

	/**
	 * 将s在size尺寸中居中
	 * 
	 * @param s
	 * @param size
	 * @return
	 */
	public static String getCenterString(String s, int size) {
		String centerS = s;
		int length = s.length();
		if (length >= size)
			return s;
		else {
			int spaceNum = (size - length) / 2;
			for (int i = 0; i < spaceNum; i++) {
				centerS = " " + centerS + " ";
			}
		}
		return centerS;
	}

	/**
	 * 找出含有subStr的文件
	 * 
	 * @param dir
	 * @param subStr
	 */
	public static void find(String dir, String subStr) {
		File f = new File(dir);
		File childs[] = f.listFiles();
		for (int i = 0; i < childs.length; i++) {
			if (childs[i].isDirectory())
				find(childs[i].getAbsolutePath(), subStr);
			else {
				String filePath = childs[i].getAbsolutePath();
				String s = FileUtil.readFile(filePath);
				if (s.toLowerCase().contains(subStr.toLowerCase()))
					System.out.println(filePath);
			}
		}
	}

	/**
	 * 查找s中包含subStr的个数
	 * 
	 * @param s
	 * @param subStr
	 * @return
	 */
	public static int containStringNumber(String s, String subStr) {
		int number = 0;
		int pos = s.indexOf(subStr);
		while (pos != -1) {
			number++;
			if (pos + subStr.length() < s.length())
				pos = s.indexOf(subStr, pos + subStr.length());
			else
				pos = -1;
		}
		return number;
	}

	/**
	 * 在s中查找第k个subStr的位置
	 * 
	 * @param s
	 * @param subStr
	 * @param k
	 * @return
	 */
	public static int indexOf(String s, String subStr, int k) {
		int pos = -1;
		while (k-- > 0) {
			pos = s.indexOf(subStr, pos + 1);
		}
		return pos;
	}

	/**
	 * 在s中从后往前查找第k个subStr的位置
	 * 
	 * @param s
	 * @param subStr
	 * @param k
	 * @return
	 */
	public static int backIndexOf(String s, String subStr, int k) {
		int pos = s.length();
		while (k-- > 0 && pos != -1) {
			pos = upIndexOf(s, subStr, pos - 1);
		}
		return pos;
	}

	/**
	 * 返回三個值的最小值
	 * @param one
	 * @param two
	 * @param three
	 * @return
	 */
	private static int min(int one, int two, int three) {
		int min = one;
		if (two < min) {
			min = two;
		}
		if (three < min) {
			min = three;
		}
		return min;
	}

	/**
	 * 求出由str1变为str2的编辑距离
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static int ld(String str1, String str2) {
		int d[][]; // 矩阵
		int n = str1.length();
		int m = str2.length();
		int i; // 遍历str1的
		int j; // 遍历str2的
		char ch1; // str1的
		char ch2; // str2的
		int temp; // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
		if (n == 0) {
			return m;
		}
		if (m == 0) {
			return n;
		}
		d = new int[n + 1][m + 1];
		for (i = 0; i <= n; i++) { // 初始化第一列
			d[i][0] = i;
		}
		for (j = 0; j <= m; j++) { // 初始化第一行
			d[0][j] = j;
		}
		for (i = 1; i <= n; i++) { // 遍历str1
			ch1 = str1.charAt(i - 1);
			// 去匹配str2
			for (j = 1; j <= m; j++) {
				ch2 = str2.charAt(j - 1);
				if (ch1 == ch2) {
					temp = 0;
				} else {
					temp = 1;
				}
				// 左边+1,上边+1, 左上角+temp取最小
				d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1]
						+ temp);
			}
		}
		return d[n][m];
	}

	/**
	 * 根据编辑距离求两个字符串的相似度
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static double sim(String str1, String str2) {
		int ld = ld(str1, str2);
		return 1 - (double) ld / Math.max(str1.length(), str2.length());
	}
	
	/**
	 * 去除s左边的空格
	 * @param s
	 * @return
	 */
	public static String trimLeft(String s){
		while(s.startsWith(" "))
			s=s.substring(1, s.length());
		return s;
	}
	
	/**
	 * 去除s右边的空格
	 * @param s
	 * @return
	 */
	public static String trimRight(String s){
		while(s.endsWith(" "))
			s=s.substring(0, s.length()-1);
		return s;
	}
	
	/**
	 * 去除s左边的[
	 * @param s
	 * @return
	 */
	public static String trimLeftBrac(String s){
		if(s.startsWith("[")){
			System.out.println(s.startsWith("["));
			s=s.substring(1, s.length());
		}
		return s;
	}
	
	/**
	 * 去除s右边的]
	 * @param s
	 * @return
	 */
	public static String trimRightBrac(String s){
		if(s.endsWith("]")){
			s=s.substring(0, s.length()-1);
		}
		return s;
	}
	/**
	 * 使用replacement替换掉所有标点符号
	 * @param s
	 * @param replacement
	 * @return
	 */
	public static String removePunct(String s,String replacement){
		s = s.replaceAll("[\\pP\\p{Punct}]", replacement);
		return s;
	}
	
	/**
	 * 替换多个空格或者\t
	 * @param s
	 * @return
	 */
	public static String replaceBlank(String s){
		return s.replaceAll("[' '|\t|' ']+"," "); 
	}
	
	public String rmStopWords(String s){
		Vector<String> vStopWord=new Vector<String>();
		String stopWordPath=getClass().getResource("/resources/en1.txt").getPath().replace("%20", " ");
		vStopWord=SetUtil.readSetFromFile(stopWordPath);
		String words[]=s.split(" ");
		String str="";
		for(String word:words){
			if(!word.matches("^[0-9]+$")&&!vStopWord.contains(word)){
				str+=word+" ";
			}
		}
		return str;
	}
//	public static String lemmatize(String str){
//		StanfordLemmatizer sl=new StanfordLemmatizer();//lemmatize
//		List<String> rst = new LinkedList<String>();
//    	rst=sl.lemmatize(str);
//    	String outString="";
//    	for(int i=0;i<rst.size();i++){
//    		outString+=rst.get(i)+" ";
//    	}
//		return outString;
//	}
	public static String rmDup(String s){
		String outString="";
		ArrayList<String> alStr=new ArrayList<String>();
		String tmp[]=s.split(" ");
		for(int i=0;i<tmp.length;i++){
			if(!alStr.contains(tmp[i])){
				alStr.add(tmp[i]);
			}
		}
		for(String str:alStr){
			outString+=str+" ";
		}
		return outString;
	}
}
