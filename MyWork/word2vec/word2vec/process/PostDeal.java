package word2vec.process;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
/**
 * 由于生成词向量矩阵的过程中，最后一个或者两个单词向量不完整，因此直接去除，简化替代增添操作。
 * @author bella
 *
 */
public class PostDeal {

	public static void deleteWordandLine(String sourcePath, String targetPath) throws Exception{

		File file = new File(sourcePath);
		File[] subFile = file.listFiles();   //当前文件夹的所有文件，放到File数组里，然后下面是遍历每一个文件
		for (int i = 0; i < subFile.length; i++) {
			String fileName = subFile[i].getName();
			BufferedReader br = new BufferedReader(new InputStreamReader
					(new FileInputStream(sourcePath + "\\" + fileName)));
			BufferedWriter bw = new BufferedWriter(new FileWriter(targetPath + "\\" + fileName));
			// 写自己的处理文本的程序，下面是读取文件的例子
			String input = null;
			while((input = br.readLine()) != null)
			{
				if(input.equals("")){
					continue;
				}
				String string = input;
				string = string.substring(string.indexOf(" ")+1);
				bw.write(string);
				bw.newLine();
			}
			br.close();//一定要随时关闭
			bw.close();
		}	
	}

	@SuppressWarnings("resource")
	public static void deleteLastline(String inputCatalog, String outputCatalog) throws Exception{
		String encoding = "UTF-8";
		File fileCatalog = new File(inputCatalog);
		File[] files = fileCatalog.listFiles();

		for(int i = 0; i < files.length; i++){
			String filename = files[i].getName();
			String inputFilepath = inputCatalog + "\\" +  filename;
			String outputFilepath = outputCatalog + "\\" + filename;
			File file = new File(inputFilepath); 
			FileWriter writer = new FileWriter(outputFilepath);
			if(file.isFile() && file.exists()){
				InputStreamReader read1 = new InputStreamReader(new FileInputStream(file), encoding);
				InputStreamReader read2 = new InputStreamReader(new FileInputStream(file), encoding);
				BufferedReader bufferedReader1 = new BufferedReader(read1);
				BufferedReader bufferedReader2 = new BufferedReader(read2);

				//计算文本行数
				int num = 0;
				while ((bufferedReader1.readLine()) != null) {
					num++;
				}

				for(int n = 0; n < num - 1; n++){
					writer.write(bufferedReader2.readLine());
					writer.write("\r\n");
				}
			}
			writer.close();
		}

	}

	public static void main(String arg[]) throws Exception{
//		String inputCatalog2 = "F:\\word2vec\\data_structure_wiki\\file2_wordtovec\\Array_data_structure";
//		String outputCatalog2 = "F:\\word2vec\\data_structure_wiki\\file3_postdeal\\Array_data_structure";
//		PostDeal.deleteWordandLine(inputCatalog2, outputCatalog2);
//		PostDeal.deleteLastline(inputCatalog2, outputCatalog2);
	}

}
