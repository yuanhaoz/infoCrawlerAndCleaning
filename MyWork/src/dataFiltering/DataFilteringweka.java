package dataFiltering;

import weka.wekaTest;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;
import weka.core.Instances;

public class DataFilteringweka {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			nbResult();
			svmResult();
			ibkResult();
			j48Result();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static int[] nbResult() throws Exception {
		Instances data = wekaTest
				.getFileInstances("file\\wekaAnalysis\\Data_structure\\Radix+sort.arff");
		System.out.println("===============输出每个样例被分到的类别=================");

		// 建立分类器进行分类
		NaiveBayes classifer = new NaiveBayes();
		classifer.buildClassifier(data);

		// 保存结果的变量定义
		String[] result = new String[data.numInstances()];
		int[] classifyResult = new int[data.numInstances()];
		System.out.println("数据量为：" + data.numInstances());

		// 控制台显示分类结果并保存到变量中
		for (int i = 0; i < data.numInstances(); i++) {
			
			// 输出每个样例被分到的类别，如果是二分，分别表示为0和1
			classifyResult[i] = (int) classifer.classifyInstance(data.instance(i));
			
			// 控制台显示分类效果
			if (classifer.classifyInstance(data.instance(i)) == 1.0) {
				result[i] = "no";
			} else {
				result[i] = "yes";
			}
			System.out.println(data.instance(i) + "=>" + result[i]);
			
		}
		return classifyResult;
	}

	public static int[] svmResult() throws Exception {
		Instances data = wekaTest
				.getFileInstances("file\\wekaAnalysis\\Data_structure\\Radix+sort.arff");
		System.out.println("===============输出每个样例被分到的类别=================");
		
		// 建立分类器进行分类
		SMO classifer = new SMO();
		classifer.buildClassifier(data);
		
		// 保存结果的变量定义
		String[] result = new String[data.numInstances()];
		int[] classifyResult = new int[data.numInstances()];
		System.out.println("数据量为：" + data.numInstances());
		
		// 控制台显示分类结果并保存到变量中
		for (int i = 0; i < data.numInstances(); i++) {
			
			// 输出每个样例被分到的类别，如果是二分，分别表示为0和1
			classifyResult[i] = (int) classifer.classifyInstance(data.instance(i));
			
			// 控制台显示分类效果
			if (classifer.classifyInstance(data.instance(i)) == 1.0) {
				result[i] = "no";
			} else {
				result[i] = "yes";
			}
			System.out.println(data.instance(i) + "=>" + result[i]);
			
		}
		return classifyResult;
		
	}

	public static int[] ibkResult() throws Exception {
		Instances data = wekaTest
				.getFileInstances("file\\wekaAnalysis\\Data_structure\\Radix+sort.arff");
		System.out.println("===============输出每个样例被分到的类别=================");
		
		// 建立分类器进行分类
		IBk classifer = new IBk();
		classifer.buildClassifier(data);
		
		// 保存结果的变量定义
		String[] result = new String[data.numInstances()];
		int[] classifyResult = new int[data.numInstances()];
		System.out.println("数据量为：" + data.numInstances());
		
		// 控制台显示分类结果并保存到变量中
		for (int i = 0; i < data.numInstances(); i++) {
			
			// 输出每个样例被分到的类别，如果是二分，分别表示为0和1
			classifyResult[i] = (int) classifer.classifyInstance(data
					.instance(i));
			
			// 控制台显示分类效果
			if (classifer.classifyInstance(data.instance(i)) == 1.0) {
				result[i] = "no";
			} else {
				result[i] = "yes";
			}
			System.out.println(data.instance(i) + "=>" + result[i]);
			
		}
		return classifyResult;
		
	}

	public static int[] j48Result() throws Exception {
		Instances data = wekaTest
				.getFileInstances("file\\wekaAnalysis\\Data_structure\\Radix+sort.arff");
		System.out.println("===============输出每个样例被分到的类别=================");
		
		// 建立分类器进行分类
		J48 classifer = new J48();
		classifer.buildClassifier(data);
		
		// 保存结果的变量定义
		String[] result = new String[data.numInstances()];
		int[] classifyresult = new int[data.numInstances()];
		System.out.println("数据量为：" + data.numInstances());
		
		// 控制台显示分类结果并保存到变量中
		for (int i = 0; i < data.numInstances(); i++) {
			
			// 输出每个样例被分到的类别，如果是二分，分别表示为0和1
			classifyresult[i] = (int) classifer.classifyInstance(data
					.instance(i));
			
			// 控制台显示分类效果
			if (classifer.classifyInstance(data.instance(i)) == 1.0) {
				result[i] = "no";
			} else {
				result[i] = "yes";
			}
			System.out.println(data.instance(i) + "=>" + result[i]);
		}
		return classifyresult;
	}

	public static void test() throws Exception {
		Instances data = wekaTest
				.getFileInstances("file\\wekaAnalysis\\Data_structure\\Radix+sort.arff");

		System.out.println("===============交叉验证================");
		// 交叉验证 new NaiveBayes(); new IBk(); new SMO(); new J48();
		NaiveBayes a = new NaiveBayes();
		// IBk a = new IBk();
		// SMO a = new SMO();
		// J48 a = new J48();
		Evaluation crossEvaluation = wekaTest.crossValidation(data, a, 10);
		wekaTest.printEvalDetail(crossEvaluation);

		System.out.println("==============分类器分类===============");
		// 一般分类器分类，部分数据用于train，部分用于test
		Evaluation testEvaluation = wekaTest.evaluateTestData(data, a);
		wekaTest.printEvalDetail(testEvaluation);

		System.out.println("===============输出每个样例被分到的类别=================");
		NaiveBayes classifer = new NaiveBayes();
		classifer.buildClassifier(data);
		String[] result = new String[data.numInstances()];
		System.out.println("数据量为：" + data.numInstances());
		for (int i = 0; i < data.numInstances(); i++) {
			// 输出每个样例被分到的类别，如果是二分，分别表示为0和1
			// System.out.println(data.instance(i) + "=>" +
			// classifer.classifyInstance(data.instance(i)));
			if (classifer.classifyInstance(data.instance(i)) == 1.0) {
				result[i] = "no";
			} else {
				result[i] = "yes";
			}
			System.out.println(data.instance(i) + "=>" + result[i]);
		}
	}

}
