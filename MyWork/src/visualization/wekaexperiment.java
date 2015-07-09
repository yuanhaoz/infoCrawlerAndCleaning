package visualization;

/**
 * zhengyuanhao  2015/7/6
 * 简单实现：quora
 * 实现功能：数据weka实验
 * 
 */

import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import weka.wekatest;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;
import weka.core.Instances;

public class wekaexperiment extends JPanel implements ActionListener {

	/**
	 * weka实验实现过滤
	 */
	private static final long serialVersionUID = -4034136806675760042L;
	// relation file select
	private JPanel jpTop = new JPanel();
	private JLabel keywordlabel = new JLabel("关键词: ");
	private JLabel subjectlabel = new JLabel("学    科：");
	private JLabel classlabel   = new JLabel("分类算法：");
	private JTextField keywordtext = new JTextField();
	private JButton cross = new JButton("构建模型");
	private JButton test = new JButton("模型测试");
	//
	private CheckboxGroup g = new CheckboxGroup();
	private CheckboxGroup g1 = new CheckboxGroup();
	private Checkbox cb1, cb2, cb3, cb4, cb5, cb6;
	
	private Border resultBorder = BorderFactory.createEtchedBorder(Color.white,
			Color.gray);
	private Border resultTitle = BorderFactory.createTitledBorder(resultBorder,
			"统计分类模型构建及测试---交叉验证", TitledBorder.LEFT, TitledBorder.TOP); // 测试集
	private Border resultTitle2 = BorderFactory.createTitledBorder(
			resultBorder, "统计分类模型构建及测试---分类器", TitledBorder.LEFT,
			TitledBorder.TOP); // 测试集和训练集
	private Border resultTitle3 = BorderFactory.createTitledBorder(
			resultBorder, "统计分类模型构建及测试---分类结果", TitledBorder.LEFT,
			TitledBorder.TOP);
	private JEditorPane resultPane = new JEditorPane();
	private JEditorPane resultPane2 = new JEditorPane();
	private JEditorPane resultPane3 = new JEditorPane();
	private JScrollPane jsp = new JScrollPane(resultPane);
	private JScrollPane jsp2 = new JScrollPane(resultPane2);
	private JScrollPane jsp3 = new JScrollPane(resultPane3);
	static String[] testresult = { "aa", "bb" };

	public wekaexperiment() {
		setLayout(null);
		this.setBackground(Color.white);
		// relation file select
		this.add(jpTop);
		jpTop.setLayout(null);
		jpTop.setBackground(new Color(230, 239, 248));
		jpTop.setBounds(5, 5, 1000, 95);
		jpTop.add(keywordlabel);
		jpTop.add(subjectlabel);
		jpTop.add(classlabel);
		jpTop.add(keywordtext);
		jpTop.add(cross);
		jpTop.add(test);
		cross.addActionListener(this);
		test.addActionListener(this);
		keywordlabel.setBounds(180, 15, 110, 30);
		subjectlabel.setBounds(180, 40, 110, 30);
		classlabel.setBounds(180, 65, 110, 30);
		keywordtext.setBounds(305, 15, 200, 30);
		cross.setBounds(550, 15, 150, 30);
		test.setBounds(710, 15, 150, 30);
		keywordtext.setText("Radix+sort");
		cb1 = new Checkbox("Data mining", g, false);
		cb2 = new Checkbox("Data structure", g, true);
		cb3 = new Checkbox("Computer network", g, false);
		cb4 = new Checkbox("NaiveBayes", g1, true);
		cb5 = new Checkbox("C4.5", g1, false);
		cb6 = new Checkbox("SVM", g1, false);
		jpTop.add(cb1);
		jpTop.add(cb2);
		jpTop.add(cb3);
		jpTop.add(cb4);
		jpTop.add(cb5);
		jpTop.add(cb6);
		cb1.setBounds(300, 40, 200, 30);
		cb2.setBounds(500, 40, 200, 30);
		cb3.setBounds(700, 40, 200, 30);
		cb4.setBounds(300, 65, 200, 30);
		cb5.setBounds(500, 65, 200, 30);
		cb6.setBounds(700, 65, 200, 30);
		
		keywordlabel.setFont(new Font("宋体", Font.BOLD, 20));
		subjectlabel.setFont(new Font("宋体", Font.BOLD, 20));
		classlabel.setFont(new Font("宋体", Font.BOLD, 20));
		cross.setFont(new Font("宋体", Font.BOLD, 20));
		test.setFont(new Font("宋体", Font.BOLD, 20));
		cb1.setFont(new Font("宋体", Font.BOLD, 18));
		cb2.setFont(new Font("宋体", Font.BOLD, 18));
		cb3.setFont(new Font("宋体", Font.BOLD, 18));
		cb4.setFont(new Font("宋体", Font.BOLD, 18));
		cb5.setFont(new Font("宋体", Font.BOLD, 18));
		cb6.setFont(new Font("宋体", Font.BOLD, 18));
		
		// tw.init(this);
		add(jsp);
		jsp.setBounds(20, 110, 480, 325);
		jsp.setBorder(resultTitle);
		add(jsp2);
		jsp2.setBounds(510, 110, 480, 325);
		jsp2.setBorder(resultTitle2);
		add(jsp3);
		jsp3.setBounds(20, 445, 970, 325);
		jsp3.setBorder(resultTitle3);
	}

	/**
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cross) {
			String kw = keywordtext.getText();
			String course = "Data mining";
			NaiveBayes nb = null;
//			IBk ibk = null;
			SMO smo = null;
			String show = "===============交叉验证================" + "\n";
			String show2 = "===============分类器分类================" + "\n";
			if (cb1.getState()) {
				course = "Data_mining";
			} else if (cb2.getState()) {
				course = "Data_structure";
			} else if (cb3.getState()) {
				course = "Computer_network";
			}
			String filepath = "file\\wekaAnalysis\\" + course + "\\" + kw + ".arff";
			try {
				Instances data = wekatest.getFileInstances(filepath);
				Evaluation crossEvaluation = null, testEvaluation = null;
				System.out.println("===============交叉验证================");
				// 交叉验证 new NaiveBayes(); new IBk(); new SMO(); new J48();
				if (cb4.getState()) {
					nb = new NaiveBayes();
					crossEvaluation = wekatest.crossValidation(data, nb, 10);
					testEvaluation = wekatest.evaluateTestData(data, nb);
				} else if (cb5.getState()) {
					J48 j= new J48();
					crossEvaluation = wekatest.crossValidation(data, j, 10);
					testEvaluation = wekatest.evaluateTestData(data, j);
					
				} else if (cb6.getState()) {
					smo = new SMO();
					crossEvaluation = wekatest.crossValidation(data, smo, 10);
					testEvaluation = wekatest.evaluateTestData(data, smo);
				}
				
				wekatest.printEvalDetail(crossEvaluation);   //控制台显示
		        show = show + crossEvaluation.toClassDetailsString() + 
		        		crossEvaluation.toSummaryString() + 
		        		crossEvaluation.toMatrixString();       //resultPane显示
		        
				wekatest.printEvalDetail(testEvaluation);   //控制台显示
		        show2 = show2 + testEvaluation.toClassDetailsString() + 
		        		testEvaluation.toSummaryString() + 
		        		testEvaluation.toMatrixString();    //resultPane显示
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(this,"分析出错，请更换分类算法！");
			}
			resultPane.setText(show);
			resultPane2.setText(show2);
			resultPane.setFont(new Font("宋体", Font.BOLD, 16));
			resultPane2.setFont(new Font("宋体", Font.BOLD, 16));
		}

		if (e.getSource() == test) {
			String kw = keywordtext.getText();
			String course = "Data mining";
			String show = "===============输出每个样例被分到的类别================" + "\n";
			if (cb1.getState()) {
				course = "Data_mining";
			} else if (cb2.getState()) {
				course = "Data_structure";
			} else if (cb3.getState()) {
				course = "Computer_network";
			}
			
			String filepath = "file\\wekaAnalysis\\" + course + "\\" + kw + ".arff";
			try {
				Instances data = wekatest.getFileInstances(filepath);
		        System.out.println("===============输出每个样例被分到的类别=================");
		        if (cb4.getState()) {
					NaiveBayes classifer = new NaiveBayes();
			        classifer.buildClassifier(data);
			        for (int i = 0; i < data.numInstances(); i++) {
			        	//输出每个样例被分到的类别，如果是二分，分别表示为0和1
			            System.out.println(data.instance(i) + " === " 
			            		+ classifer.classifyInstance(data.instance(i)));
			            if(i%2 == 0){
			            	show = show + data.instance(i) + " === " 
				            		+ classifer.classifyInstance(data.instance(i)) + "                         ";
			            }else{
			            	show = show + data.instance(i) + " === " 
				            		+ classifer.classifyInstance(data.instance(i)) + "\n";
			            }
			        }
				} else if (cb5.getState()) {
					IBk classifer = new IBk();
			        classifer.buildClassifier(data);
			        for (int i = 0; i < data.numInstances(); i++) {
			        	//输出每个样例被分到的类别，如果是二分，分别表示为0和1
			            System.out.println(data.instance(i) + " === " 
			            		+ classifer.classifyInstance(data.instance(i)));
			            if(i%2 == 0){
			            	show = show + data.instance(i) + " === " 
				            		+ classifer.classifyInstance(data.instance(i)) + "                         ";
			            }else{
			            	show = show + data.instance(i) + " === " 
				            		+ classifer.classifyInstance(data.instance(i)) + "\n";
			            }
			        }
					
				} else if (cb6.getState()) {
					SMO classifer = new SMO();
			        classifer.buildClassifier(data);
			        for (int i = 0; i < data.numInstances(); i++) {
			        	//输出每个样例被分到的类别，如果是二分，分别表示为0和1
			            System.out.println(data.instance(i) + " === " 
			            		+ classifer.classifyInstance(data.instance(i)));
			            if(i%2 == 0){
			            	show = show + data.instance(i) + " === " 
				            		+ classifer.classifyInstance(data.instance(i)) + "                         ";
			            }else{
			            	show = show + data.instance(i) + " === " 
				            		+ classifer.classifyInstance(data.instance(i)) + "\n";
			            }
			        }
				}
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(this,"测试出错，请更换分类算法！");
			}
			resultPane3.setText(show);    //resultPane3 显示
			resultPane3.setFont(new Font("宋体", Font.BOLD, 16));
		}
	}

	public static void main(String[] args) {
		// new SearchPanel();
	}

	/**
	 * 保存网页
	 * 
	 * @author zhengtaishuai
	 */
	public static void saveHtml(String filepath, String str) {
		try {
			OutputStreamWriter outs = new OutputStreamWriter(
					new FileOutputStream(filepath, true), "utf-8");
			outs.write(str);
			outs.close();
		} catch (IOException e) {
			System.out.println("Error at save html...");
			e.printStackTrace();
		}
	}

}
