package visualization;

/**
 * zhengyuanhao  2015/7/6
 * 简单实现：weka
 * 实现功能：数据采集及解析
 * 
 */

import informationextraction.FeatureExtraction;
import informationextraction.InformationExtraction2Excel;

import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.jsoup.nodes.Document;

import dataCollection.DataCollection;
import webpage.QuoraWebPage;
import Jsoup.JsoupParse;
import basic.KeywordCatalogDesign;

public class DataCollectionAndCleaning extends JPanel implements ActionListener {

	/**
	 * 数据采集及预处理 PreDeal函数
	 */
	private static final long serialVersionUID = -4034136806675760042L;
	// relation file select
	private JPanel jpTop = new JPanel();
	private JLabel keywordlabel = new JLabel("关键词：");
	private JLabel subjectlabel = new JLabel("学科：");
	private JTextField keywordtext = new JTextField();
	private JButton crawler = new JButton("爬取数据");
	private JButton analysis = new JButton("解析数据");
	//
	private CheckboxGroup g = new CheckboxGroup();
	private Checkbox cb1, cb2, cb3;

	private Border resultBorder = BorderFactory.createEtchedBorder(Color.white,
			Color.gray);
	private Border resultTitle = BorderFactory.createTitledBorder(resultBorder,
			"数据爬取结果展示---显示爬取信息", TitledBorder.LEFT, TitledBorder.TOP); // src目录
	private Border resultTitle2 = BorderFactory.createTitledBorder(
			resultBorder, "网页解析结果展示---显示解析过程", TitledBorder.LEFT,
			TitledBorder.TOP);
	private JEditorPane resultPane = new JEditorPane();
	private JEditorPane resultPane2 = new JEditorPane();
	private JScrollPane jsp = new JScrollPane(resultPane);
	private JScrollPane jsp2 = new JScrollPane(resultPane2);
	static String[] testresult = { "aa", "bb" };

	private JPanel imagePanel;
	private ImageIcon background;
	
	public void back(){
		background = new ImageIcon("file/background.jpg");   //背景图片
		JLabel label = new JLabel(background);              //把背景图片显示在一个标签里面
		//把背景图片添加到分层窗格的最底层作为背景
		this.getRootPane().getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));	
		//把标签的大小位置设置为图片刚好填充整个面板
		label.setBounds(0, 0, background.getIconWidth(), background.getIconHeight());
			
		Container c = this.getRootPane();
//		c.setLayout(new BorderLayout());
		imagePanel = (JPanel)c;
		imagePanel.setOpaque(false);
	}
	
	public DataCollectionAndCleaning() {
		
//		back();
		setLayout(null);
		this.setBackground(Color.white);

		// relation file select
		this.add(jpTop);
		jpTop.setLayout(null);
		jpTop.setBackground(new Color(230, 239, 248));
		jpTop.setBounds(5, 5, 1000, 90);
		jpTop.add(keywordlabel);
		jpTop.add(subjectlabel);
		jpTop.add(keywordtext);
		jpTop.add(crawler);
		jpTop.add(analysis);
		crawler.addActionListener(this);
		analysis.addActionListener(this);
		keywordlabel.setBounds(200, 15, 90, 30);
		subjectlabel.setBounds(200, 40, 90, 50);
		keywordtext.setBounds(305, 15, 200, 30);
		crawler.setBounds(550, 15, 150, 30);
		analysis.setBounds(710, 15, 150, 30);
		keywordtext.setText("SVM");
		cb1 = new Checkbox("Data mining", g, true);
		cb2 = new Checkbox("Data structure", g, false);
		cb3 = new Checkbox("Computer network", g, false);
		jpTop.add(cb1);
		jpTop.add(cb2);
		jpTop.add(cb3);
		cb1.setBounds(300, 40, 200, 50);
		cb2.setBounds(500, 40, 200, 50);
		cb3.setBounds(700, 40, 200, 50);
		
		keywordlabel.setFont(new Font("宋体", Font.BOLD, 20));
		subjectlabel.setFont(new Font("宋体", Font.BOLD, 20));
		crawler.setFont(new Font("宋体", Font.BOLD, 20));
		analysis.setFont(new Font("宋体", Font.BOLD, 20));
		cb1.setFont(new Font("宋体", Font.BOLD, 18));
		cb2.setFont(new Font("宋体", Font.BOLD, 18));
		cb3.setFont(new Font("宋体", Font.BOLD, 18));
		
		// tw.init(this);
		add(jsp);
		// jsp.setBounds(200, 140, 580, 630);
		jsp.setBounds(20, 110, 480, 660);
		jsp.setBorder(resultTitle);
		add(jsp2);
		jsp2.setBounds(510, 110, 480, 660);
		jsp2.setBorder(resultTitle2);
	}

	/**
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == crawler) {
			String course = "Data mining";
			if (cb1.getState()) {
//				course = "Data_mining";
				course = "test";
			} else if (cb2.getState()) {
				course = "Data_structure";
			} else if (cb3.getState()) {
				course = "Computer_network";
			}
			resultPane.setText("开始爬取网页：...");
			try {
				File file0 = new File("file/datacollection/" + course);
				File[] files = file0.listFiles();
				String view = "开始爬取课程：" + course + "的数据集...\n";
				for (int i = 0; i < files.length; i++) {
					String filename = files[i].getName();
					if (filename.contains(".html")) {
						String keyword = filename.substring(0,filename.length() - 5);
//						System.out.println("得到爬取主题：" + keyword);
						view = view + "得到爬取主题：" + keyword + "\n";
						String url = "http://www.quora.com/search?q=" + keyword;
//						System.out.println("爬取主题的链接为：" + url);
						view = view + "爬取主题的链接为：" + url + "\n";
						
						String path = KeywordCatalogDesign.GetKeywordCatalog(keyword);
						String filepath = path + keyword + ".html";
//						System.out.println("主题页面存到网页本地路径为: " + filepath);
						view = view + "主题页面存到网页本地路径为: " + filepath + "\n";
						QuoraWebPage.seleniumCrawlerSubject(keyword, url); // 爬取得到搜索主题的第一层
//						System.out.println("主题网页：" + keyword + "爬取结束...");
						view = view + "主题网页：" + keyword + "爬取结束..." + "\n" + "\n";

						String[] urls = DataCollection.getQuestionURLs(keyword);
						
						for (int j = 0; j < urls.length; j++) {
//							System.out.println("开始爬取问题页面，链接为：" + urls[j]);
							view = view + "开始爬取问题页面，链接为： " + urls[j] + "\n";
							String filepath1 = path + keyword + j + ".html";
							QuoraWebPage.seleniumCrawlerSubject(filepath1, urls[j]); // 爬取所有有关问题的子网页
//							System.out.println("问题页面存到网页本地路径为: " + filepath1);
							view = view + "问题页面存到网页本地路径为: " + filepath1 + "\n";
							view = view + "问题网页：" + urls[j] + "爬取结束..." + "\n" + "\n";
								
//							System.out.println("开始爬取问题页面下的所有作者页面...");
							view = view + "开始爬取问题页面下的所有作者页面..." + "\n";
//							String author = t.GetAuthorPagesName(keyword, j);        // 爬取所有作者页面
//							System.out.println("作者页面存到网页本地路径为: " + author);
//							view = view + "作者页面存到网页本地路径为: " + author + "\n";
							DataCollection.crawlerAuthorPages(keyword, j);
//							System.out.println(keyword + j + "作者网页爬取结束...");
							view = view + keyword + j + "作者网页爬取结束..." + "\n" + "\n";
								
							//显示爬取过程信息
							resultPane.setText(view); 
							resultPane.setFont(new Font("宋体", Font.BOLD, 18));
						}
					}
				}
			} catch (Exception e1) {
				System.out.println("爬取" + course + "的过程中出现未知错误...");
				e1.printStackTrace();
			}
			
		}
		
		if (e.getSource() == analysis) {
			String course = "Data mining";
			if (cb1.getState()) {
//				course = "Data_mining";
				course = "test";
			} else if (cb2.getState()) {
				course = "Data_structure";
			} else if (cb3.getState()) {
				course = "Computer_network";
			}
//			resultPane.setText("11");
			try {
				File file0 = new File("file/datacollection/" + course);
				File[] files = file0.listFiles();
				String viewanalysis = "开始解析课程：" + course + "的数据集...\n";
				for (int i = 0; i < files.length; i++) {
					String filename = files[i].getName();
					if (filename.contains(".html")) {
						String keyword = filename.substring(0,filename.length() - 5);
						
						int pagelength = InformationExtraction2Excel.questionPageNumber(keyword);
//						t.Down2Excel(keyword, pagelength);
//						t.match3(keyword);
						//解析的具体代码
						String catalog = KeywordCatalogDesign.GetKeywordCatalog(keyword);
						WritableWorkbook workbook = Workbook.createWorkbook(new File(catalog + keyword + "-tag.xls"));
						WritableSheet sheet = workbook.createSheet("标签", 0);
						/** ************设置单元格字体************** */
						// 字体
						WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
						/** ************以下设置几种格式的单元格************ */
						WritableCellFormat wcf_center = new WritableCellFormat(NormalFont);
//						wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN);
						wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE);
						wcf_center.setAlignment(Alignment.CENTRE);
//						wcf_center.setBackground(Colour.YELLOW);
						wcf_center.setWrap(true);
						/** ************单元格格式设置完成****************** */
						sheet.addCell(new Label(0, 0, "QA", wcf_center));
						sheet.addCell(new Label(1, 0, "Content", wcf_center));
						sheet.addCell(new Label(2, 0, "Char", wcf_center));
						sheet.addCell(new Label(3, 0, "Word", wcf_center));
						sheet.addCell(new Label(4, 0, "Exist(1)", wcf_center));
						sheet.addCell(new Label(5, 0, "QT(1) or AS(0)", wcf_center));
						sheet.addCell(new Label(6, 0, "Upvote", wcf_center));
						sheet.addCell(new Label(7, 0, "Link", wcf_center));
						sheet.addCell(new Label(8, 0, "Comment", wcf_center));
						sheet.addCell(new Label(9, 0, "Follower", wcf_center));
						sheet.addCell(new Label(10, 0, "Profession", wcf_center));
						sheet.addCell(new Label(11, 0, "KnowAbout", wcf_center));
						sheet.addCell(new Label(12, 0, "AnswerSum", wcf_center));
						sheet.setRowView(0, 700, false); // 设置行高
						int number = 1;
						for (int j = 0; j < pagelength; j++) {
							String path1 = catalog + keyword + j + ".html";
							File file = new File(path1);
							if (!file.exists()) {
								System.out.println(path1 + "  不存在");
							} else {
								System.out.println("\n开始解析： " + path1);
								viewanalysis = viewanalysis + "解析问题页面： " + path1 + "\n";
								Document doc = JsoupParse.parsePathText(path1);
								String questionname = FeatureExtraction.questionContent(doc);
								String questionexpand = FeatureExtraction.questionExpandInfo(doc);
								int questionlength2 = FeatureExtraction.questionContentWordLength(doc);
								int questionlength = FeatureExtraction.questionContentCharLength(doc);
								String want_answer = FeatureExtraction.questionWantAnswers(doc);   //想知道问题答案的人数
								String question_comment = FeatureExtraction.questionContent(doc); //答案评论数
								System.out.println(pagelength);
								viewanalysis = viewanalysis + "问题名字为：" + questionname + "\n";
								viewanalysis = viewanalysis + "问题描述为：" + questionexpand + "\n";
								viewanalysis = viewanalysis + "问题字符为：" + questionlength2 + "\n";
								viewanalysis = viewanalysis + "问题单词为：" + questionlength + "\n";
								viewanalysis = viewanalysis + "问题关注人数为：" + want_answer + "\n";
								viewanalysis = viewanalysis + "问题评论为：" + question_comment + "\n";
								
								
								sheet.addCell(new Label(0, number, keyword + j, wcf_center));
								sheet.addCell(new Label(1, number, questionname + "\n" + "Expanded information：" + questionexpand, wcf_center));
								sheet.addCell(new Label(2, number, questionlength2 + "", wcf_center));
								sheet.addCell(new Label(3, number, questionlength + "", wcf_center));
								sheet.addCell(new Label(4, number, "1", wcf_center));
								sheet.addCell(new Label(5, number, "1", wcf_center));
								sheet.addCell(new Label(6, number, want_answer, wcf_center));  
								sheet.addCell(new Label(7, number, "0", wcf_center));      //默认没有链接
								sheet.addCell(new Label(8, number, question_comment, wcf_center));
									
								sheet.setRowView(number, 1300, false); // 设置行高
								sheet.setColumnView(0, 20);
								sheet.setColumnView(1, 60);
								int realanswernumber = FeatureExtraction.countRealAnswerNumber(doc);
								for (int m = number; m < number + realanswernumber; m++) {
									sheet.setRowView(m + 1, 1300, false); // 设置行高
									String answercontent = FeatureExtraction.answerContent(doc, m - number);
									int contentlength2 = FeatureExtraction.answerContentWordLength(doc, m - number);// 字符长度
									int contentlength = FeatureExtraction.answerContentCharLength(doc, m - number); // 单词长度
									String upvote = FeatureExtraction.answerUpvotes(doc, m - number);                  // 支持票数 
									String url1 = FeatureExtraction.answerURLs(doc, m - number);                    // 链接有无
									String comment = FeatureExtraction.answerCommentNumbers(doc, m - number);       // 评论数量
									
									viewanalysis = viewanalysis + "答案内容为：" + answercontent + "\n";
									viewanalysis = viewanalysis + "答案字符为：" + contentlength2 + "\n";
									viewanalysis = viewanalysis + "答案单词为：" + contentlength + "\n";
									viewanalysis = viewanalysis + "答案upvote为：" + upvote + "\n";
									viewanalysis = viewanalysis + "答案是否存在链接为：" + url1 + "\n";
									viewanalysis = viewanalysis + "答案评论数为：" + comment + "\n";
									
									String follower = null;
									String info = null;
									String know = null;
									String answer = null;
									String authorpath = catalog + keyword + j + "_author_" + (m - number) + ".html";
									viewanalysis = viewanalysis + "\n" + "解析问题页面的下的作者页面： " + authorpath + "\n";
									
									File file1 = new File(authorpath);
									if(!file1.exists()){
										System.out.println(authorpath + "   不存在");
									}else{
										Document doc1 = JsoupParse.parsePathText(authorpath);  //解析作者页面路径
									
										follower = FeatureExtraction.authorFollowers(doc1, m - number);   // 作者粉丝
										info = FeatureExtraction.authorInfo(doc, m - number);            // 作者简历
										know = FeatureExtraction.authorKnowAbout(doc1, m - number);       // 作者领域
										answer = FeatureExtraction.authorAnswers(doc1, m - number);       // 回答总数
										
										viewanalysis = viewanalysis + "作者粉丝数为：" + follower + "\n";
										viewanalysis = viewanalysis + "作者简历为：" + info + "\n";
										viewanalysis = viewanalysis + "作者领域为：" + know + "\n";
										viewanalysis = viewanalysis + "作者回答总数为：" + answer + "\n";
									}
										
									sheet.addCell(new Label(0, m + 1, (m - number + 1) + " ",wcf_center));
									sheet.addCell(new Label(1, m + 1, answercontent, wcf_center));
									sheet.addCell(new Label(2, m + 1, contentlength2 + "",wcf_center));
									sheet.addCell(new Label(3, m + 1, contentlength + "",wcf_center));
									sheet.addCell(new Label(4, m + 1, "1", wcf_center));
									sheet.addCell(new Label(5, m + 1, "0", wcf_center));
									sheet.addCell(new Label(6, m + 1, upvote + "", wcf_center));
									sheet.addCell(new Label(7, m + 1, url1, wcf_center));
									sheet.addCell(new Label(8, m + 1, comment, wcf_center));
									sheet.addCell(new Label(9, m + 1, follower, wcf_center));
									sheet.addCell(new Label(10, m + 1, info, wcf_center));
									sheet.addCell(new Label(11, m + 1, know, wcf_center));
									sheet.addCell(new Label(12, m + 1, answer, wcf_center));
								}
								number = number + realanswernumber + 1;
							}
							String path = KeywordCatalogDesign.GetKeywordCatalog(keyword);
							System.out.println(path + " 已经成功解析到 " + catalog + keyword + "-tag.xls");
							viewanalysis = viewanalysis + "已经成功解析到 " + catalog + keyword + "-tag.xls" + "\n" + "\n";
						}
						workbook.write(); // 写表格而不是写单元格，所以不能放在循环里面，必须放在最后
						/** *********关闭文件************* */
						workbook.close();
						
						//显示解析过程信息
						resultPane2.setText(viewanalysis);
						resultPane2.setFont(new Font("宋体", Font.BOLD, 18));
					}
					
				}
				
			} catch (Exception e1) {
				System.out.println("爬取" + course + "的过程中出现未知错误...");
				e1.printStackTrace();
			}
			
		}
	}

	public static void main(String[] args) {
		// new SearchPanel();
	}
	
	/**
	 * 保存网页
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
