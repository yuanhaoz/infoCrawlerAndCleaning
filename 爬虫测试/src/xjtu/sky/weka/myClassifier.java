package xjtu.sky.weka;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;






import weka.classifiers.Classifier;
//import weka.fileHandle;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.meta.Bagging;
import weka.classifiers.rules.OneR;
import weka.classifiers.rules.PART;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.REPTree;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils.DataSink;

/**
 *
 * @author Libing
 */
public class myClassifier {
	
    public static double NB(Instances data) throws Exception {
        double sum = 0;
        int runPass = 1;
        for (int i = 1; i <= runPass; i++) {
            Evaluation evaluation = new Evaluation(data);
            evaluation.errorRate();
            NaiveBayes nb = new NaiveBayes();
            int randomSeed = i;
            Random rand = new Random(randomSeed);

            evaluation.crossValidateModel(nb, data, data.numInstances(), rand);
//        System.out.println(evaluation.correct()+"\t"+evaluation.toSummaryString());
//        System.out.println(evaluation.pctCorrect());
            sum = sum + evaluation.pctCorrect();
        }
//       System.out.println(sum/runPass);
        return (sum / runPass);
    }

    public static double DT(Instances data) throws Exception {
        double sum = 0;
        int runPass = 1;
        for (int i = 1; i <= runPass; i++) {
            Evaluation evaluation = new Evaluation(data);
            J48 dt = new J48();
            int randomSeed = i;
            Random rand = new Random(randomSeed);
            DecisionStump ds=new DecisionStump();
            evaluation.crossValidateModel(dt, data, 10, rand);
            sum = sum + evaluation.pctCorrect();
        }
//        System.out.println(evaluation.correct()+"\t"+evaluation.toSummaryString());
//        System.out.println(sum / 1 * data.numInstances() / 100);
        return (sum/runPass);
    }

    public static double IBK(Instances data) throws Exception {
        double sum = 0;
        int runPass = 1;
        Evaluation evaluation;
        for (int i = 1; i <= runPass; i++) {
            evaluation = new Evaluation(data);
            IBk ibk = new IBk();
            ibk.setKNN(1);
            int randomSeed = i;
            Random rand = new Random(randomSeed);
            evaluation.crossValidateModel(ibk, data, 10, rand);
            sum = sum + evaluation.pctCorrect();
        }
//        System.out.println(evaluation.correct() + "\t" + evaluation.toSummaryString());
//        System.out.println(evaluation.pctCorrect() * data.numInstances() / 100);
        return (sum/runPass);
    }

    public static double PART(Instances data) throws Exception {
        double sum = 0;
        int runPass = 1;
        for (int i = 1; i <= runPass; i++) {
            Evaluation evaluation = new Evaluation(data);
            PART part = new PART();

            int randomSeed = i;
            Random rand = new Random(randomSeed);

            evaluation.crossValidateModel(part, data, 10, rand);
//        System.out.println(evaluation.correct() + "\t" + evaluation.toSummaryString());
//        System.out.println(evaluation.pctCorrect());
            sum = sum + evaluation.pctCorrect();
        }

//        System.out.println(sum / 5);
        return (sum/runPass);
    }

    public static void DTExample(String fileName,int numOfClass) throws IOException, Exception{
    	double errorRate=0;
        double accRate=0;
        Evaluation evaluation;
    	for (int i = 1; i <=numOfClass; i++) {
//            System.out.print(fileName+",");
            File file = new File(fileName);
            ArffLoader arffLoader = new ArffLoader();
            arffLoader.setFile(file);
            Instances data = arffLoader.getDataSet();
            data.setClassIndex(data.numAttributes() - i);
            evaluation = new Evaluation(data);
            J48 dt=new J48();
           
            int randomSeed = i;
            Random rand = new Random(randomSeed);
            evaluation.crossValidateModel(dt, data, 10, rand);
            printEvalDetail(evaluation);
            errorRate+=evaluation.errorRate();
            accRate+=evaluation.pctCorrect();
//            double acc = fourDecimal(DT(data));
        }
    	System.out.println("J48 errorRate:"+fourDecimal(errorRate/numOfClass)+"  accRate:"+accRate/numOfClass);
    }

    public static void NBExample(String fileName,int numOfClass) throws IOException, Exception{
    	double errorRate=0;
        double accRate=0;
        double fmeasure=0;
        double precision=0;
        double recall=0;
        Evaluation evaluation;
    	for (int i = 1; i <= numOfClass; i++) {
//            System.out.print(fileName+",");
            File file = new File(fileName);
            ArffLoader arffLoader = new ArffLoader();
            arffLoader.setFile(file);
            Instances data = arffLoader.getDataSet(); 
            /*CSVLoader csvLoader = new CSVLoader();
            csvLoader.setFile(file);
            Instances data = csvLoader.getDataSet();*/
            /*CSVLoader csvLoader = new CSVLoader();
            csvLoader.setSource(file);
            Instances data = csvLoader.getDataSet();*/
            data.setClassIndex(data.numAttributes() - i);
            evaluation = new Evaluation(data);
            NaiveBayes nb=new NaiveBayes();
            int randomSeed = i;
            Random rand = new Random(randomSeed);
            evaluation.crossValidateModel(nb, data, 10, rand);
            printEvalDetail(evaluation);
            errorRate+=evaluation.errorRate();
            accRate+=evaluation.pctCorrect();
//            double acc = fourDecimal(DT(data));
            fmeasure+=evaluation.fMeasure(--i);
//            precision+=evaluation.precision(i);
//            recall+=evaluation.recall(i);
        }
    	System.out.println("NB errorRate:"+fourDecimal(errorRate/numOfClass)+"  accRate:"+fourDecimal(accRate/numOfClass));
    	System.out.println("NB precision:"+fourDecimal(precision/numOfClass)+"  recall:"+fourDecimal(recall/numOfClass)+
    			"  f_measure:"+fourDecimal(fmeasure/numOfClass));
    }
    public static void IB1Example(String fileName,int numOfClass) throws IOException, Exception{
        double errorRate=0;
        Evaluation evaluation;
        double accRate=0;
        for (int i = 1; i <=numOfClass; i++) {
//            System.out.print(fileNames[i]+",");
            File file = new File(fileName);
            ArffLoader arffLoader = new ArffLoader();
            arffLoader.setFile(file);
            Instances data = arffLoader.getDataSet();
            data.setClassIndex(data.numAttributes() - i);
            evaluation = new Evaluation(data);
            IBk ibk = new IBk();
            ibk.setKNN(1);
            int randomSeed = i;
            Random rand = new Random(randomSeed);
            evaluation.crossValidateModel(ibk, data, 10, rand);
            printEvalDetail(evaluation);
            errorRate+=evaluation.errorRate();
            accRate+=evaluation.pctCorrect();
            
          /*  System.out.println("=====================================");
            J48 classifer = new J48();
            classifer.buildClassifier(data);
            for (int j = 0; j < data.numInstances(); j++) {
            	//输出每个样例被分到的类别，如果是二分，分别表示为0和1
                System.out.println(data.instance(j) + " === " + classifer.classifyInstance(data.instance(j)));
            }
            */
            /*File file1 = new File("F:\\Randomized+algorithm.arff");
            ArffLoader arffLoader1 = new ArffLoader();
            arffLoader1.setFile(file);
            Instances test = arffLoader1.getDataSet();
            Classifier cls = new J48();
            cls.buildClassifier(data);
            // evaluate classifier and print some statistics
            Evaluation eval = new Evaluation(data);
            eval.evaluateModel(cls, test);
            System.out.println(eval.toSummaryString("\nResults\n======\n",
             false));*/
        }
        System.out.println("IBk errorRate:"+fourDecimal(errorRate/numOfClass)+"  accRate:"+fourDecimal(accRate/numOfClass));
    }

//    public static void PARTExample(String folderName) throws IOException, Exception{
//        fileHandle fh = new fileHandle();
//        String[] fileNames = fh.getFileNames(filePath+folderName+"\\", "arff");
//        for (int i = 0; i < fileNames.length; i++) {
//            System.out.print(fileNames[i]+",");
//            File file = new File(filePath + folderName + "\\" + fileNames[i]);
//            ArffLoader arffLoader = new ArffLoader();
//            arffLoader.setFile(file);
//            Instances data = arffLoader.getDataSet();
//            data.setClassIndex(data.numAttributes() - 1);
//            double acc = fourDecimal(PART(data));
//            System.out.println(acc);
//        }
//    }

    public static void SMOExample(String fileName,int numOfClass) throws IOException, Exception {
        double errorRate=0;
        double accRate=0;
        for (int i = 1; i <= numOfClass; i++) {
            File file = new File(fileName);
            ArffLoader arffLoader = new ArffLoader();
            arffLoader.setFile(file);
            Instances data = arffLoader.getDataSet();
            data.setClassIndex(data.numAttributes() - i);
//            double acc = fourDecimal(SMO(data));
            Evaluation evaluation = new Evaluation(data);
            SMO smo = new SMO();
            int randomSeed = i;
            Random rand = new Random(randomSeed);
            evaluation.crossValidateModel(smo, data, 10, rand);
            printEvalDetail(evaluation);
            errorRate+=evaluation.errorRate();
//            System.out.println(errorRate);
            accRate+=evaluation.pctCorrect();
        }
        System.out.println("SMO errorRate:"+fourDecimal(errorRate/numOfClass) +" accRate:"+accRate/numOfClass);
    }

    public static void OneR(Instances data) throws Exception {
        double sum = 0;
        for (int i = 1; i <= 1; i++) {
            Evaluation evaluation = new Evaluation(data);
            OneR oner = new OneR();
            int randomSeed = i;
            Random rand = new Random(randomSeed);
            evaluation.crossValidateModel(oner, data, 10, rand);
            printEvalDetail(evaluation);
//        System.out.println(evaluation.pctCorrect());
            sum = sum + evaluation.pctCorrect();
        }
        System.out.println(sum / 1);
    }

    public static double Boost(Instances data) throws Exception {
        double sum = 0;
        for (int i = 1; i <= 1; i++) {
            Evaluation evaluation = new Evaluation(data);
            AdaBoostM1 boost = new AdaBoostM1();
            NaiveBayes nb = new NaiveBayes();
//       	 OneR oner = new OneR();
            boost.setClassifier(nb);
            int randomSeed = i;
            Random rand = new Random(randomSeed);
            evaluation.crossValidateModel(boost, data, 10, rand);
            sum = sum + evaluation.pctCorrect();
        }
        return sum / 1;
    }
    public static void BoostExample(String fileName,int numOfClasses) throws Exception{
    	File file = new File(fileName);
        ArffLoader arffLoader = new ArffLoader();
        arffLoader.setFile(file);
        Instances data = arffLoader.getDataSet();
        double errorRate1=0;
        double errorRate2=0;
        double errorRate3=0;
        double errorRate4=0;
        double errorRate5=0;
        double acc=0;
        for(int i=1;i<=numOfClasses;i++){
	        data.setClassIndex(data.numAttributes() - i);
	        Evaluation evaluation = new Evaluation(data);
	        AdaBoostM1 boost = new AdaBoostM1();
	        J48 dt = new J48();
	        boost.setClassifier(dt);
	        int randomSeed = 1;
	        Random rand = new Random(randomSeed);
	        evaluation.crossValidateModel(boost, data, 10, rand);
//	        System.out.println("========="+i+"=========");
//	        double correct = evaluation.pctCorrect();
//	        System.out.println("correct percentage:"+correct);
//	        double incorrect=evaluation.pctIncorrect();
//	        System.out.println("incorrect percentage:"+incorrect);
//	        acc+=fourDecimal(Boost(data));
	        errorRate1+=evaluation.errorRate();
//	        System.out.println("errorRate:"+errorRate);
//	        System.out.println(evaluation.toSummaryString());
        }
        for(int i=1;i<=numOfClasses;i++){
	        data.setClassIndex(data.numAttributes() - i);
	        Evaluation evaluation = new Evaluation(data);
	        AdaBoostM1 boost = new AdaBoostM1();
	        NaiveBayes nb = new NaiveBayes();
	        boost.setClassifier(nb);
	        int randomSeed = 1;
	        Random rand = new Random(randomSeed);
	        evaluation.crossValidateModel(boost, data, 10, rand);
//	        System.out.println("========="+i+"=========");
//	        double correct = evaluation.pctCorrect();
//	        System.out.println("correct percentage:"+correct);
//	        double incorrect=evaluation.pctIncorrect();
//	        System.out.println("incorrect percentage:"+incorrect);
//	        acc+=fourDecimal(Boost(data));
	        errorRate2+=evaluation.errorRate();
//	        System.out.println("errorRate:"+errorRate);
//	        System.out.println(evaluation.toSummaryString());
        }
        for(int i=1;i<=numOfClasses;i++){
	        data.setClassIndex(data.numAttributes() - i);
	        Evaluation evaluation = new Evaluation(data);
	        AdaBoostM1 boost = new AdaBoostM1();
	        SMO smo=new SMO();
	        boost.setClassifier(smo);
	        int randomSeed = 1;
	        Random rand = new Random(randomSeed);
	        evaluation.crossValidateModel(boost, data, 10, rand);
//	        System.out.println("========="+i+"=========");
//	        double correct = evaluation.pctCorrect();
//	        System.out.println("correct percentage:"+correct);
//	        double incorrect=evaluation.pctIncorrect();
//	        System.out.println("incorrect percentage:"+incorrect);
//	        acc+=fourDecimal(Boost(data));
	        errorRate3+=evaluation.errorRate();
//	        System.out.println("errorRate:"+errorRate);
//	        System.out.println(evaluation.toSummaryString());
        }
        for(int i=1;i<=numOfClasses;i++){
	        data.setClassIndex(data.numAttributes() - i);
	        Evaluation evaluation = new Evaluation(data);
	        AdaBoostM1 boost = new AdaBoostM1();
	        NaiveBayes nb = new NaiveBayes();
	        IBk ibk=new IBk();
	        boost.setClassifier(ibk);
	        int randomSeed = 1;
	        Random rand = new Random(randomSeed);
	        evaluation.crossValidateModel(boost, data, 10, rand);
//	        System.out.println("========="+i+"=========");
//	        double correct = evaluation.pctCorrect();
//	        System.out.println("correct percentage:"+correct);
//	        double incorrect=evaluation.pctIncorrect();
//	        System.out.println("incorrect percentage:"+incorrect);
//	        acc+=fourDecimal(Boost(data));
	        errorRate4+=evaluation.errorRate();
//	        System.out.println("errorRate:"+errorRate);
//	        System.out.println(evaluation.toSummaryString());
        }
        for(int i=1;i<=numOfClasses;i++){
	        data.setClassIndex(data.numAttributes() - i);
	        Evaluation evaluation = new Evaluation(data);
	        AdaBoostM1 boost = new AdaBoostM1();
	        NaiveBayes nb = new NaiveBayes();
	        OneR oner = new OneR();
	        boost.setClassifier(oner);
	        int randomSeed = 1;
	        Random rand = new Random(randomSeed);
	        evaluation.crossValidateModel(boost, data, 10, rand);
//	        System.out.println("========="+i+"=========");
//	        double correct = evaluation.pctCorrect();
//	        System.out.println("correct percentage:"+correct);
//	        double incorrect=evaluation.pctIncorrect();
//	        System.out.println("incorrect percentage:"+incorrect);
//	        acc+=fourDecimal(Boost(data));
	        errorRate5+=evaluation.errorRate();
//	        System.out.println("errorRate:"+errorRate);
//	        System.out.println(evaluation.toSummaryString());
        }
        errorRate1/=numOfClasses;
        errorRate2/=numOfClasses;
        errorRate3/=numOfClasses;
        errorRate4/=numOfClasses;
        errorRate5/=numOfClasses;
        System.out.println(fileName);
        System.out.println("======AdaBoost=====");
        System.out.println("dt:errorRate:"+errorRate1);
        System.out.println("nb:errorRate:"+errorRate2);
        System.out.println("smo:errorRate:"+errorRate3);
        System.out.println("ibk:errorRate:"+errorRate4);
        System.out.println("oner:errorRate:"+errorRate5);
    }

    public static void Bagging(Instances data) throws Exception {
        double sum = 0;
        for (int i = 1; i <= 1; i++) {
            Evaluation evaluation = new Evaluation(data);
            Bagging bag = new Bagging();
//        OneR oner = new OneR();
//         NaiveBayes nb = new NaiveBayes();
            J48 dt = new J48();
//        REPTree reptree = new REPTree();
            bag.setClassifier(dt);
            int randomSeed = i;
            Random rand = new Random(randomSeed);
            evaluation.crossValidateModel(bag, data, 10, rand);
            sum = sum + evaluation.pctCorrect();
        }
        System.out.println(sum / 1);
    }

    public static double fourDecimal(double d){
        return Math.floor(d*10000+0.5)/10000;
    }
    
	public static void CSV2Arff(String input, String output) throws IOException {
		if (input.length() != 2) {
			System.out.println("\nUsage: CSV2Arff <input.csv> <output.arff>\n");
			// load CSV
			CSVLoader loader = new CSVLoader();
			loader.setSource(new File(input));
			Instances data = loader.getDataSet();

			// save ARFF
			ArffSaver saver = new ArffSaver();
			saver.setInstances(data);
			saver.setFile(new File(output));
			saver.setDestination(new File(output));
			saver.writeBatch();
		}
	}
    
    
    public static void printEvalDetail(Evaluation evaluation) throws Exception {
        System.out.println(evaluation.toClassDetailsString());
        System.out.println(evaluation.toSummaryString());
        System.out.println(evaluation.toMatrixString());
    }

    public static void main(String[] args) throws IOException, Exception {
//        SMOExample();
//    	  IB1Example();
//    	  String fileName="E:\\dataStructure\\tfidf_multiclass\\queue_multiclass.arff";
    	
//          CSV2Arff("F:\\Radix+sort.csv", "F:\\Radix+sort2.arff");
          String fileName="F:\\Radix+sort.arff";
    	  int numOfClasses=2;
//    	  BoostExample(fileName,numOfClasses);
    	  
//    	  String filename="E:\\dataStructure\\tfidf_multiclass\\array_multiclass.arff";
//    	  DTExample(fileName,numOfClasses);
//    	  NBExample(fileName,numOfClasses);
//    	  SMOExample(fileName,numOfClasses);
    	  IB1Example(fileName,numOfClasses);
    }
}
