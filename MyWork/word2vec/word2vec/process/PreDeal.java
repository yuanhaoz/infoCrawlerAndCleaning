package word2vec.process;

import java.io.File;
import java.io.IOException;

import util.FileIO;
import util.StringUtil;

/**
 * prepare data for the experiment
 */
public class PreDeal {
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static String readAndProcessText(String  inFileName,String outFileName) throws IOException{
		String fileStr=FileIO.readFile(inFileName);
		fileStr=fileStr.toLowerCase(); //小写
		fileStr=fileStr.replaceAll("�", " ");
		fileStr=fileStr.replaceAll("-", " ");
		fileStr=fileStr.replaceAll("(\\s[\u4E00-\u9FA5]+)|([\u4E00-\u9FA5]+\\s)", "");
		fileStr=StringUtil.removePunct(fileStr, " ");//标点符号
		fileStr=StringUtil.replaceBlank(fileStr);//空格
		fileStr=StringUtil.trimLeft(fileStr); //左
		fileStr=StringUtil.trimRight(fileStr); //右
		StringUtil su=new StringUtil();
		fileStr=su.rmStopWords(fileStr); //停用词
//		fileStr=StringUtil.lemmatize(fileStr);//还原
//		fileStr=StringUtil.rmDup(fileStr); 
		fileStr=StringUtil.trimRight(fileStr); //右
		fileStr=fileStr.replaceAll("\\s{1,}", " ");
//		fileStr=StringUtil.removePunct("�", " ");
//		System.out.println(fileStr);
		if(fileStr!=""){//空
			FileIO.writeFile(fileStr,outFileName);
		}
		return fileStr;
	}
	//统计 最初文件长度  做初步处理
	public static String readAndProcessText_src(String  inFileName,String outFileName) throws IOException{
		String fileStr=FileIO.readFile(inFileName);
		fileStr=fileStr.toLowerCase(); //小写
		fileStr=StringUtil.removePunct(fileStr, " ");//标点符号
		fileStr=StringUtil.replaceBlank(fileStr);//空格
		fileStr=StringUtil.trimLeft(fileStr); //左
		fileStr=StringUtil.trimRight(fileStr); //右
		
		if(fileStr!=""){//空
			FileIO.writeFile(fileStr,outFileName);
		}
		return fileStr;
	}
	//
	public static void readAndProcessList(String inFilePath,String outFilePath) throws IOException{
		File f = new File(inFilePath);
		File childs[] = f.listFiles();
		for(int i = 0;i < childs.length; i++){
			String inFileName = childs[i].getName();
			File fil = new File(outFilePath);
			if(!fil.exists()){
				fil.mkdir();
			}
			String outFileName = outFilePath + "\\" + inFileName;
			inFileName = inFilePath + "\\" + inFileName;
//			System.out.println("inFName:"+inFileName);
//			System.out.println("outFName:"+outFileName);
			readAndProcessText(inFileName,outFileName);
		}
	}
	//统计 最初文件长度  做初步处理
	public static void readAndProcessList_src(String inFilePath,String outFilePath) throws IOException{
		File f=new File(inFilePath);
		File childs[]=f.listFiles();
		for(int i=0;i<childs.length;i++){
			String inFileName=childs[i].getName();
			File fil=new File(outFilePath);
			if(!fil.exists()){
				fil.mkdir();
			}
			String outFileName=outFilePath+inFileName;
			inFileName=inFilePath+inFileName;
//			System.out.println("inFName:"+inFileName);
//			System.out.println("outFName:"+outFileName);
			readAndProcessText_src(inFileName,outFileName);
		}
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//String t="add";
		//String inFilePath="E:\\temporary\\"+t+"\\";
		//String outFilePath="E:\\temporary\\"+t+"_src\\";
//		String t = "Tree_traversal";
//		String inFilePath = "D:\\wiki_Data_labels\\DataStructure\\Data_structure_4\\"+t+"\\";
//		String outFilePath = "D:\\wiki_Data_labels\\DataStructure\\Data_structure_5\\"+t+"\\";
		String inFilePath = "D:\\quora_Data\\data_structure_tag_txt\\dataStructureAll_exceptarray\\";
		String outFilePath = "D:\\quora_Data\\data_structure_tag_txt\\dataStructureAll_exceptarray_PreDeal\\";
		long start=System.currentTimeMillis();
		PreDeal.readAndProcessList(inFilePath,outFilePath);
//		PreDeal.readAndProcessList_src(inFilePath,outFilePath);
		long end=System.currentTimeMillis();
		System.out.println("time used:"+(end-start)+"ms");
		System.out.println("finished");
	}
}
