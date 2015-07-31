package xjtu.sky.weka;

import java.io.File;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class test1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			File inputFile = new File("F:\\Radix+sort.arff");// 训练语料文件
			ArffLoader atf = new ArffLoader();
			atf.setFile(inputFile);
			Instances instancesTrain = atf.getDataSet(); // 读入训练文件

			inputFile = new File("F:\\Randomized+algorithm.arff");// 测试语料文件
			atf.setFile(inputFile);
			Instances instancesTest = atf.getDataSet(); // 读入测试文件
			//设置分类属性所在行号（第一行为0号），instancesTrain.numAttributes()可以取得属性总数
			instancesTest.setClassIndex(instancesTest.numAttributes()-1);
			instancesTrain.setClassIndex(instancesTrain.numAttributes()-1);
			
			// 朴素贝叶斯算法
			NaiveBayes classifier1 = new NaiveBayes();
			classifier1.buildClassifier(instancesTrain);
			//根据训练文件得到分类器（得到参数）
			Evaluation eval = new Evaluation(instancesTrain);
			//由训练文件得到分类器对测试文件进行分类测试，得到错误率
			eval.evaluateModel(classifier1, instancesTest);
			printEvalDetail(eval);
			System.out.println(eval.errorRate());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void printEvalDetail(Evaluation evaluation) throws Exception {
        System.out.println(evaluation.toClassDetailsString());
        System.out.println(evaluation.toSummaryString());
        System.out.println(evaluation.toMatrixString());
	}
}
