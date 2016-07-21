package weka;

/**
 * zhengyuanhao  2015/7/6
 * 简单实现：weka
 * 实现功能：调用weka进行实验
 * 
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GreedyStepwise;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.AttributeSelectedClassifier;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;

public class wekaTest {

    public static Instances getFileInstances(String fileName)
            throws FileNotFoundException, IOException {
        Instances m_Instances = new Instances(new BufferedReader(new FileReader(fileName)));
        //设置分类属性所在行号（第一行为0号），instancesTrain.numAttributes()可以取得属性总数
        m_Instances.setClassIndex(m_Instances.numAttributes() - 1);
        return m_Instances;
    }

    //如果只有训练集，采用十交叉验证的方法
    public static Evaluation crossValidation(Instances m_Instances,
            Classifier classifier, int numFolds) throws Exception {
        Evaluation evaluation = new Evaluation(m_Instances);
        evaluation.crossValidateModel(classifier, m_Instances, numFolds, new Random(1));
        return evaluation;
    }
    
    //有训练集和测试集
    public static Evaluation evaluateTestData(Instances m_Instances, Classifier classifier) throws Exception {
        int split = (int) (m_Instances.numInstances() * 0.6);
        Instances traindata = new Instances(m_Instances, 0, split);
        Instances testdata = new Instances(m_Instances, split, m_Instances.numInstances() - split);
        classifier.buildClassifier(traindata);  //在训练集上训练一个分类器
        //下面一行是m_Instances,或traindata，或testdata都没关系，因为Evaluation构造方法要的只是instance的结构，比如属性
        Evaluation evaluation = new Evaluation(m_Instances);
        evaluation.evaluateModel(classifier, testdata);      //在测试集上测试数据集
        return evaluation;
    }
    
    public static Instances selectAttrUseFilter(Instances m_Instances) throws Exception {
        AttributeSelection filter = new AttributeSelection();
        filter.setEvaluator(new CfsSubsetEval());
        filter.setSearch(new GreedyStepwise());
        filter.setInputFormat(m_Instances);
        return Filter.useFilter(m_Instances, filter);
    }
    
    public static void selectAttrUseMC(Instances m_Instances, Classifier base) throws Exception {
        AttributeSelectedClassifier classifier = new AttributeSelectedClassifier();
        classifier.setClassifier(base);
        classifier.setEvaluator(new CfsSubsetEval());
        classifier.setSearch(new GreedyStepwise());
        Evaluation evaluation = new Evaluation(m_Instances);
        evaluation.crossValidateModel(classifier, m_Instances, 10, new Random(1));
        System.out.println(evaluation.toSummaryString());
    }
    
    public static void printEvalDetail(Evaluation evaluation) throws Exception {
    	//Evaluation类提供的多种输出方法，显示结果和使用weka分析的结果很相似
        System.out.println(evaluation.toClassDetailsString());
        System.out.println(evaluation.toSummaryString());
        System.out.println(evaluation.toMatrixString());
    }

    public static void main(String[] args) throws Exception {
        
//        Instances data = getFileInstances("F:\\Radix+sort.arff");
//        Instances data = getFileInstances("F:\\问题总表.arff");
        Instances data = getFileInstances("file\\wekaAnalysis\\Data_structure\\回答总表.arff");
        
        System.out.println("===============交叉验证================");
        //交叉验证   new NaiveBayes();  new IBk();  new SMO();  new J48();
        NaiveBayes a = new NaiveBayes();
//        IBk a = new IBk();
//        SMO a = new SMO();
//        J48 a = new J48();
        Evaluation crossEvaluation = crossValidation(data, a, 10);
        printEvalDetail(crossEvaluation);
        
        System.out.println("==============分类器分类===============");
        //一般分类器分类，部分数据用于train，部分用于test
        Evaluation testEvaluation = evaluateTestData(data, a);
        printEvalDetail(testEvaluation);
        
//        System.out.println("===============特征筛选================");
//        //特征筛选
//        Instances newData = selectAttrUseFilter(data);
//        System.out.println("Oral data:" + data.numAttributes());
//        System.out.println("selected data:" + newData.numAttributes());
//        testEvaluation = evaluateTestData(newData, new NaiveBayes());
//        printEvalDetail(testEvaluation);
//        
//        System.out.println("=====================================");
//        selectAttrUseMC(data, new J48());

        System.out.println("===============输出每个样例被分到的类别=================");
        NaiveBayes classifer = new NaiveBayes();
        classifer.buildClassifier(data);
        for (int i = 0; i < data.numInstances(); i++) {
        	//输出每个样例被分到的类别，如果是二分，分别表示为0和1
            System.out.println(data.instance(i) + " === " 
            		+ classifer.classifyInstance(data.instance(i)));
        }
    }
}