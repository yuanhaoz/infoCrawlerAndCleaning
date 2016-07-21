package word2vec.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

public class CosinSimilarity {
	private static String inputFile1 = "F:\\word2vec\\quora_Data\\word2vec按标签存储_quora\\Array\\feature\\Array0.txt";
	private static String inputFile2 = "F:\\word2vec\\wiki_Data_labels\\copy6\\word2vec_wiki2\\algorithm.txt";
	private static String outFile = "F:\\SimilarityMatrix1.txt";
	String encoding = "UTF-8";
	
	// 读取TXT文件第i行数据
	public static String readLine(int lineNumber, BufferedReader reader) throws Exception {
		lineNumber = lineNumber + 1;
		String line = "";
		int i = 0;
		while (i < lineNumber) {
			line = reader.readLine();
			i++;
		}
		return line;
	}

	// 计算两个一维数组的余弦相似值
	// 要求：两个数组的长度要相同
	// 输出：cos余弦相似值
	public static double getCosSimilarity(float[] float1, float[] float2) {
		double cosSimilarity =0;
		float float1Modulo = 0;
		float float2Modulo = 0;
		float floatProduct = 0;
		if(float1.length == float2.length){
			for (int m = 0; m < float1.length; m++) {
				float1Modulo += float1[m] * float1[m];
				float2Modulo += float2[m] * float2[m];
				floatProduct += float1[m] * float2[m];
			}
			float1Modulo = (float) Math.sqrt(float1Modulo);
			float2Modulo = (float) Math.sqrt(float2Modulo);
			cosSimilarity = (double) (floatProduct / (float1Modulo * float2Modulo));
		} else {
			System.out.println("两个数组长度维数不同，无法计算余弦相似值...");
		}
		return cosSimilarity;
	}
	
	// 计算两个词向量矩阵的余弦相似矩阵（两个二维数组）
	public static double[][] getCosSimilarityMatrix(float[][] float1, float[][] float2) {
		int row = float1.length;// 相似矩阵的行数为第一个矩阵的行数
		int column = float2.length;// 相似矩阵的列数为第二个矩阵的行数
		double[][] cosSimilarityMatrix = new double[row][column];
		for(int i = 0; i < row; i++){
			for(int j = 0; j < column; j++){
				// Sij = cos(fi*fj)
				cosSimilarityMatrix[i][j] = (double)getCosSimilarity(float1[i], float2[j]);
			}
		}
		System.out.println("----------------------------------------------------");
		System.out.println("余弦相似矩阵计算完毕...");
		return cosSimilarityMatrix;
	}
	
	// 将一个词向量文件中的词向量矩阵写到一个二维数组中
	public static float[][] getWord2VecMatrix(String inputfile) throws Exception{
		BufferedReader rowbufferedReader = new BufferedReader(new FileReader(inputfile));
		BufferedReader columnbufferedReader = new BufferedReader(new FileReader(inputfile));
		// 词向量矩阵行数
		int rowNum = 0;
		while ((rowbufferedReader.readLine()) != null) {
			rowNum++;
		}
		System.out.println(rowNum);
		// 词向量矩阵列数（每行列数相同）
		String firstStr = readLine(0, columnbufferedReader);// 得去文件一行数据
		String[] firstSplit = firstStr.split(" ");
		int columnNum = firstSplit.length;
		// 生成词向量矩阵
		System.out.println(columnNum);
		float[][] Word2VecMatrix = new float[rowNum][columnNum];
		for (int i = 0; i < rowNum; i++) {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(inputfile));
			// 得到每行数据
			String rowData = readLine(i, bufferedReader);
//			System.out.println("第"+(i+1)+"数据为："+rowData);
			if (rowData == null) {
				System.out.println("第"+(i+1)+"行无数据...");
			} else {
				String[] rowDataAll = rowData.split(" ");
				for (int j = 0; j < columnNum; j++) {
					if (rowDataAll[j] == ""){// 某个数据不存在
						continue;
					}
					// 数据写进词向量矩阵
					Word2VecMatrix[i][j] = Float.valueOf(rowDataAll[j]);
				}
//				System.out.println("第"+(i+1)+"行数据写完...");
			}
		}
		System.out.println("----------------------------------------------------");
		System.out.println(inputfile+"词向量矩阵生成完毕...");
		return Word2VecMatrix;
	}

	// 生成两个词向量文件的余弦相似矩阵
	// 将词向量矩阵写到输出文件中
	@SuppressWarnings({ "resource" })
	public static void writeCosSimilarityMatrix(String inputFile1,String inputFile2,String outFile) throws Exception {
		FileWriter similarityMatrix = new FileWriter(outFile);
		// 生成两个词向量文件的词向量矩阵
		float[][] float1 = getWord2VecMatrix(inputFile1);
		float[][] float2 = getWord2VecMatrix(inputFile2);
		double[][] cosSimilarityMatrix = getCosSimilarityMatrix(float1, float2);
		for(int i = 0; i < cosSimilarityMatrix.length; i++){
			for(int j = 0; j < cosSimilarityMatrix[i].length; j++){
				similarityMatrix.write(cosSimilarityMatrix[i][j] + " ");
			}
			// 换行
			similarityMatrix.write("\n");
		}
		System.out.println("----------------------------------------------------");
		System.out.println("相似矩阵成功写入"+outFile);
	}

	// 测试函数
	public static void test() {
		try {
			long start = System.currentTimeMillis();
			writeCosSimilarityMatrix(inputFile1, inputFile2, outFile);
			long end = System.currentTimeMillis();
			System.out.println("生成相似矩阵耗时："+(end-start)/1000+"s");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String argv[]) {
		test();
	}
}
