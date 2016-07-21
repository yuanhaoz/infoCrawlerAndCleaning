package exp1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import base.DirFile;

public class Base2_CountTxt {

	private static String cata = "02-CQA网站中问题答案质量评估";
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		workflow();
	}
	
	public static void workflow() throws IOException{
		
		String path1 = "F:\\"+cata+"\\数据结构标注20主题_Step2\\txt1Tokenizer\\file5_data_step1(tile)";
		String path2 = "F:\\"+cata+"\\数据结构标注20主题_Step2\\txt2Tokenizer\\file5_data_step1(tile)";
		String path3 = "F:\\"+cata+"\\数据结构标注20主题_Step2\\txt3Tokenizer\\file5_data_step1(tile)";
		HashMap<String, Integer> map1 = getTxtNumber(path1);
		HashMap<String, Integer> map2 = getTxtNumber(path2);
		HashMap<String, Integer> map3 = getTxtNumber(path3);
		String txtStorePath1 = "F:\\"+cata+"\\数据结构标注20主题_Step2\\txt1Tokenizer\\数据信息统计1.txt";
		String txtStorePath2 = "F:\\"+cata+"\\数据结构标注20主题_Step2\\txt2Tokenizer\\数据信息统计2.txt";
		String txtStorePath3 = "F:\\"+cata+"\\数据结构标注20主题_Step2\\txt3Tokenizer\\数据信息统计3.txt";
		writeInfo(map1, txtStorePath1);
		writeInfo(map2, txtStorePath2);
		writeInfo(map3, txtStorePath3);
	}
	
	/**
	 * 得到样本数据数量
	 */
	public static HashMap<String, Integer> getTxtNumber(String path){
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		ArrayList<String> list = DirFile.getFolderFileNamesFromDirectorybyArraylist(path);
		for(int i = 0; i < list.size(); i++){
			String listName = list.get(i);
			String childPath = path + "\\" + listName;
			File[] txt = new File(childPath).listFiles();
			int txtNumber = txt.length;
//			System.out.println(listName + "含有样本数据为：" + txtNumber);
			map.put(listName, txtNumber);
		}
		return map;
	}
	
	/**
	 * 将数据信息写到txt中
	 * @throws IOException 
	 */
	public static void writeInfo(HashMap<String, Integer> map, String path) throws IOException{
		for(Entry<String, Integer> entry : map.entrySet()){
			String keyword = entry.getKey();
			int txtNumber = entry.getValue();
			String info = keyword + ": " + txtNumber + "\n";
			FileWriter output = new FileWriter(path, true);// 在原有基础上写
			output.write(info);
			output.close();
		}
	}
}
