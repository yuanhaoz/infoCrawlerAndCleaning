package data;

import java.io.File;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import base.DirFile;
/*  weibifan 2013年9月29日
 * UTF8编码转为ASCII码。
 * 
 * 
 */
public class UTF8Code {

	static Map<Integer, String> unicodeMap=null;

	//构建替换字典
	static void initUnicodeMap(){
		if(unicodeMap==null)
			unicodeMap=new HashMap<Integer, String>();

		unicodeMap.put(0xfb01, "fi");
		unicodeMap.put(0xfb02, "fl");
		unicodeMap.put(0x2014, "-");
		unicodeMap.put(0x2013 , "-");
		unicodeMap.put(0x2212 , "-");
		unicodeMap.put(0x2019 , "'");
		unicodeMap.put(0xfffd , "'");
		unicodeMap.put(0xb4 , "'");
		unicodeMap.put(0x2018 , "'");

		unicodeMap.put(0x2208 , " _IN_ "); //集合的 属于
		unicodeMap.put(0x2203 , " _EXIST_ "); //集合的 属于
		unicodeMap.put(0x2229 , " _CAP_ ");
		unicodeMap.put(0x222a , " _CUP_ ");
		unicodeMap.put(0x2205 , " _NULL_ ");
		unicodeMap.put(0x2228 , " _OR_ ");
		unicodeMap.put(0x2227 , " _AND_ ");
		unicodeMap.put(0xd7 , " _*_ ");

		unicodeMap.put(0x2190 , "<-"); //<-
		unicodeMap.put(0x2192 , "->"); //<-
		unicodeMap.put(0x21d2 , "==>"); //<-

		unicodeMap.put(0x2264 , "<="); //<=
		unicodeMap.put(0x2265 , ">="); //<=

		unicodeMap.put(0x201c , "\""); //”
		unicodeMap.put(0x201d , "\""); //"
		unicodeMap.put(0x2248 , "~="); //"


		unicodeMap.put(0x25e6 , "^C"); //摄氏度
		unicodeMap.put(0x221a , "_V_"); //
		unicodeMap.put(0x3c0 , "_PI_"); //

		unicodeMap.put(0x3b1 , "_ALPHA_"); //对号
		unicodeMap.put(0x3b2 , "_BETA_"); //对号
		unicodeMap.put(0x3b3 , "_GAMMA_"); //对号
		unicodeMap.put(0x3a9 , "_OMIGA_"); //对号
		unicodeMap.put(0x3c6 , "_PHI_"); //对号

		unicodeMap.put(0x2dc , "~"); //
		unicodeMap.put(0xa9 , "C"); //trade mark
		unicodeMap.put(0x398 , "O"); //
		unicodeMap.put(0x2022 , "."); //
		unicodeMap.put(0xb7 , "."); //
		unicodeMap.put(0x2217 , "*"); //

		unicodeMap.put(0x221e , "_INF_"); //
		unicodeMap.put(0xa2 , "_OU_"); //
		unicodeMap.put(0x2261 , "_=_"); //
	}

	static{
		initUnicodeMap();
	}


	public UTF8Code() {
		// TODO Auto-generated constructor stub
	}


	//方案1：替换方案
	//使用方法：调用，看那些字符没有识别，然后修改initUnicodeMap()函数
	public static StringBuffer unicodeReplace(String input){
		StringBuffer output=new StringBuffer();

		for(int i=0; i<input.length();i++){
//			System.out.printf("0x%x ",(int)input.charAt(i));			
//			System.out.printf(" %c   \n",input.charAt(i));			
			int unicode=(int)input.charAt(i);
			if(unicode > 0x7f){				
				String target = unicodeMap.get(unicode);				
				if(target == null){
//					System.out.printf("0x%x ",(int)input.charAt(i));			
//					System.out.printf(" %c   \n",input.charAt(i));	
//					System.err.printf("can not find unicode: 0x%x \n", unicode);
//					System.err.println(input.substring(i-20, i+20));
					output.append(" ");
				}else{
//					System.out.printf("0x%x ",(int)input.charAt(i));			
//					System.out.printf(" %c   \n",input.charAt(i));	
//					System.out.println("new:"+target);
					output.append(target);
				}

			}else{
				output.append(input.charAt(i));
			}
		}
		return output;
	}



	//方案2：删除方案
	private static void unicodeRemove() {
		String input="Ford 锟?Fulkerson";

		// "Ford 锟?Fulkerson";
		// "Ford ?Fulkerson";
		System.out.println(
				Normalizer
				.normalize(input, Normalizer.Form.NFD)
				.replaceAll("[^\\p{ASCII}]", "")
				);

		//输出： "Ford ?Fulkerson";
		System.out.println(input.replaceAll("[^\\p{ASCII}]", ""));	

		//=================================================
		//可以解决这类问题：
		// input = "Tĥïŝ ĩš â fůňķŷ Šťŕĭńġ";		
		// "Tĥïŝ ĩš â fůňķŷ Šťŕĭńġ";
		//This is a funky String
		System.out.println(
				Normalizer
				.normalize(input, Normalizer.Form.NFD)
				.replaceAll("[^\\p{ASCII}]", "")
				);

		//输出：T   f 
		System.out.println(input.replaceAll("[^\\p{ASCII}]", ""));
	}
	
	public static void unicodeReplaced(){
//		utf8Tester();
		initUnicodeMap();

		String input="Ford 锟?Fulkerson";
		input="classiﬁcation";  //这个里面fi是一个字符
		input="there are n−1 links in any tree 2×(n−1)/n, or approximately 2";  //这个里面fi是一个字符

		for(int i=0; i<input.length();i++){
			System.out.printf("0x%x ",(int)input.charAt(i));			
			System.out.printf(" %c   \n",input.charAt(i));

			int unicode = (int)input.charAt(i);
			if(unicode > 0x7f){
				String target = unicodeMap.get(unicode);
				System.out.println("new:"+target);
			}
		}
		
		StringBuffer in = unicodeReplace(input);
		System.out.println("结果为：" + in.toString());

//		System.out.println(
//				Normalizer
//				.normalize(input, Normalizer.Form.NFD)
//				.replaceAll("[^\\p{ASCII}]", "")
//				);

	}
	
	public static void singleDeal(String inPath, String outPath){
//		String path = "F:\\高质量\\二叉树\\wiki\\AVL tree\\AVL tree_all.txt";
//		String path2 = "F:\\高质量\\二叉树\\wiki\\AVL tree\\AVL tree_all2.txt";
		String text = DirFile.getStringFromPathFile(inPath);
		String textDeal = unicodeReplace(text).toString();
		DirFile.storeString2File(textDeal, outPath);
	}
	
	public static void allDeal(){
		String in = "F:\\高质量\\二叉树\\wiki";
		ArrayList<String> list = DirFile.getFolderFileNamesFromDirectorybyArraylist(in);
		for(int i = 0; i < list.size(); i++){
			String word = list.get(i);
			String path = in + "\\" + word;
			File[] f = new File(path).listFiles();
			for(int j = 0; j < f.length; j++){
				String fileName = f[j].getName();
				if(fileName.contains(".txt")){
					String inPath = path + "\\" + fileName;
					String outPath = path + "\\" + fileName;
					singleDeal(inPath, outPath);
				}
			}
//			String inPath = in + "\\" + word + "\\" + word + "_all.txt";
//			String outPath = in + "\\" + word + "\\" + word + "_all2.txt";
//			singleDeal(inPath, outPath);
		}
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		allDeal();
	}

}
