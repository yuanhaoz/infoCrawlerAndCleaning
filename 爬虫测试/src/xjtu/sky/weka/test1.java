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
			File inputFile = new File("F:\\Radix+sort.arff");// ѵ�������ļ�
			ArffLoader atf = new ArffLoader();
			atf.setFile(inputFile);
			Instances instancesTrain = atf.getDataSet(); // ����ѵ���ļ�

			inputFile = new File("F:\\Randomized+algorithm.arff");// ���������ļ�
			atf.setFile(inputFile);
			Instances instancesTest = atf.getDataSet(); // ��������ļ�
			//���÷������������кţ���һ��Ϊ0�ţ���instancesTrain.numAttributes()����ȡ����������
			instancesTest.setClassIndex(instancesTest.numAttributes()-1);
			instancesTrain.setClassIndex(instancesTrain.numAttributes()-1);
			
			// ���ر�Ҷ˹�㷨
			NaiveBayes classifier1 = new NaiveBayes();
			classifier1.buildClassifier(instancesTrain);
			//����ѵ���ļ��õ����������õ�������
			Evaluation eval = new Evaluation(instancesTrain);
			//��ѵ���ļ��õ��������Բ����ļ����з�����ԣ��õ�������
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
