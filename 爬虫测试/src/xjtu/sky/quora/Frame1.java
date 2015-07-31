package xjtu.sky.quora;

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

public class Frame1 extends JPanel implements ActionListener {

	/**
	 * ���ݲɼ���Ԥ���� PreDeal����
	 */
	private static final long serialVersionUID = -4034136806675760042L;
	// relation file select
	private JPanel jpTop = new JPanel();
	private JLabel keywordlabel = new JLabel("�ؼ��ʣ�");
	private JLabel subjectlabel = new JLabel("ѧ�ƣ�");
	private JTextField keywordtext = new JTextField();
	private JButton crawler = new JButton("��ȡ����");
	private JButton analysis = new JButton("��������");
	//
	private CheckboxGroup g = new CheckboxGroup();
	private Checkbox cb1, cb2, cb3;

	private Border resultBorder = BorderFactory.createEtchedBorder(Color.white,
			Color.gray);
	private Border resultTitle = BorderFactory.createTitledBorder(resultBorder,
			"������ȡ���չʾ---��ʾ��ȡ��Ϣ", TitledBorder.LEFT, TitledBorder.TOP); // srcĿ¼
	private Border resultTitle2 = BorderFactory.createTitledBorder(
			resultBorder, "��ҳ�������չʾ---��ʾ��������", TitledBorder.LEFT,
			TitledBorder.TOP);
	private JEditorPane resultPane = new JEditorPane();
	private JEditorPane resultPane2 = new JEditorPane();
	private JScrollPane jsp = new JScrollPane(resultPane);
	private JScrollPane jsp2 = new JScrollPane(resultPane2);
	static String[] testresult = { "aa", "bb" };

	private JPanel imagePanel;
	private ImageIcon background;
	
	public void back(){
		background = new ImageIcon("file/background.jpg");   //����ͼƬ
		JLabel label = new JLabel(background);              //�ѱ���ͼƬ��ʾ��һ����ǩ����
		//�ѱ���ͼƬ��ӵ��ֲ㴰�����ײ���Ϊ����
		this.getRootPane().getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));	
		//�ѱ�ǩ�Ĵ�Сλ������ΪͼƬ�պ�����������
		label.setBounds(0, 0, background.getIconWidth(), background.getIconHeight());
			
		Container c = this.getRootPane();
//		c.setLayout(new BorderLayout());
		imagePanel = (JPanel)c;
		imagePanel.setOpaque(false);
	}
	
	public Frame1() {
		
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
		
		keywordlabel.setFont(new Font("����", Font.BOLD, 20));
		subjectlabel.setFont(new Font("����", Font.BOLD, 20));
		crawler.setFont(new Font("����", Font.BOLD, 20));
		analysis.setFont(new Font("����", Font.BOLD, 20));
		cb1.setFont(new Font("����", Font.BOLD, 18));
		cb2.setFont(new Font("����", Font.BOLD, 18));
		cb3.setFont(new Font("����", Font.BOLD, 18));
		
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
			resultPane.setText("��ʼ��ȡ��ҳ��...");
			try {
				tag2 t = new tag2();
				File file0 = new File("F:/����/�γ�����/" + course);
				File[] files = file0.listFiles();
				String view = "��ʼ��ȡ�γ̣�" + course + "�����ݼ�...\n";
				for (int i = 0; i < files.length; i++) {
					String filename = files[i].getName();
					if (filename.contains(".html")) {
						String keyword = filename.substring(0,filename.length() - 5);
//						System.out.println("�õ���ȡ���⣺" + keyword);
						view = view + "�õ���ȡ���⣺" + keyword + "\n";
						String url = "http://www.quora.com/search?q=" + keyword;
//						System.out.println("��ȡ���������Ϊ��" + url);
						view = view + "��ȡ���������Ϊ��" + url + "\n";
						PageDown a = new PageDown();
						
						String path = t.GetCatalog(keyword);
						String filepath = path + keyword + ".html";
//						System.out.println("����ҳ��浽��ҳ����·��Ϊ: " + filepath);
						view = view + "����ҳ��浽��ҳ����·��Ϊ: " + filepath + "\n";
						a.pagedown(keyword, url); // ��ȡ�õ���������ĵ�һ��
//						System.out.println("������ҳ��" + keyword + "��ȡ����...");
						view = view + "������ҳ��" + keyword + "��ȡ����..." + "\n" + "\n";

						String[] urls = tag2.GetChildUrls(keyword);
						
						for (int j = 0; j < urls.length; j++) {
//							System.out.println("��ʼ��ȡ����ҳ�棬����Ϊ��" + urls[j]);
							view = view + "��ʼ��ȡ����ҳ�棬����Ϊ�� " + urls[j] + "\n";
							String filepath1 = path + keyword + j + ".html";
							a.pagedown2(keyword, j, urls[j]); // ��ȡ�����й����������ҳ
//							System.out.println("����ҳ��浽��ҳ����·��Ϊ: " + filepath1);
							view = view + "����ҳ��浽��ҳ����·��Ϊ: " + filepath1 + "\n";
							view = view + "������ҳ��" + urls[j] + "��ȡ����..." + "\n" + "\n";
								
//							System.out.println("��ʼ��ȡ����ҳ���µ���������ҳ��...");
							view = view + "��ʼ��ȡ����ҳ���µ���������ҳ��..." + "\n";
							String author = t.GetAuthorPagesName(keyword, j);        // ��ȡ��������ҳ��
//							System.out.println("����ҳ��浽��ҳ����·��Ϊ: " + author);
							view = view + "����ҳ��浽��ҳ����·��Ϊ: " + author + "\n";
							t.GetAuthorPages(keyword, j);
//							System.out.println(keyword + j + "������ҳ��ȡ����...");
							view = view + keyword + j + "������ҳ��ȡ����..." + "\n" + "\n";
								
							//��ʾ��ȡ������Ϣ
							resultPane.setText(view); 
							resultPane.setFont(new Font("����", Font.BOLD, 18));
						}
					}
				}
			} catch (Exception e1) {
				System.out.println("��ȡ" + course + "�Ĺ����г���δ֪����...");
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
				tag2 t = new tag2();
				File file0 = new File("F:/����/�γ�����/" + course);
				File[] files = file0.listFiles();
				String viewanalysis = "��ʼ�����γ̣�" + course + "�����ݼ�...\n";
				for (int i = 0; i < files.length; i++) {
					String filename = files[i].getName();
					if (filename.contains(".html")) {
						String keyword = filename.substring(0,filename.length() - 5);
						
						int pagelength = t.PageLength(keyword);
//						t.Down2Excel(keyword, pagelength);
//						t.match3(keyword);
						//�����ľ������
						String catalog = t.GetCatalog2(keyword);
						WritableWorkbook workbook = Workbook.createWorkbook(new File(catalog + keyword + "-tag.xls"));
						WritableSheet sheet = workbook.createSheet("��ǩ", 0);
						/** ************���õ�Ԫ������************** */
						// ����
						WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
						/** ************�������ü��ָ�ʽ�ĵ�Ԫ��************ */
						WritableCellFormat wcf_center = new WritableCellFormat(NormalFont);
//						wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN);
						wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE);
						wcf_center.setAlignment(Alignment.CENTRE);
//						wcf_center.setBackground(Colour.YELLOW);
						wcf_center.setWrap(true);
						/** ************��Ԫ���ʽ�������****************** */
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
						sheet.setRowView(0, 700, false); // �����и�
						int number = 1;
						for (int j = 0; j < pagelength; j++) {
							String path1 = catalog + keyword + j + ".html";
							File file = new File(path1);
							if (!file.exists()) {
								System.out.println(path1 + "  ������");
							} else {
								System.out.println("\n��ʼ������ " + path1);
								viewanalysis = viewanalysis + "��������ҳ�棺 " + path1 + "\n";
								Document doc = tag2.parsePathText(path1);
								String questionname = tag2.QuestionName(doc);
								String questionexpand = tag2.QuestionExpand(doc);
								int questionlength2 = tag2.QuestionLength2(doc);
								int questionlength = tag2.QuestionLength(doc);
								String want_answer = PageAnalysis.Want_Answers(doc);   //��֪������𰸵�����
								String question_comment = PageAnalysis.Comment_Numbers_question(doc); //��������
								System.out.println(pagelength);
								viewanalysis = viewanalysis + "��������Ϊ��" + questionname + "\n";
								viewanalysis = viewanalysis + "��������Ϊ��" + questionexpand + "\n";
								viewanalysis = viewanalysis + "�����ַ�Ϊ��" + questionlength2 + "\n";
								viewanalysis = viewanalysis + "���ⵥ��Ϊ��" + questionlength + "\n";
								viewanalysis = viewanalysis + "�����ע����Ϊ��" + want_answer + "\n";
								viewanalysis = viewanalysis + "��������Ϊ��" + question_comment + "\n";
								
								
								sheet.addCell(new Label(0, number, keyword + j, wcf_center));
								sheet.addCell(new Label(1, number, questionname + "\n" + "Expanded information��" + questionexpand, wcf_center));
								sheet.addCell(new Label(2, number, questionlength2 + "", wcf_center));
								sheet.addCell(new Label(3, number, questionlength + "", wcf_center));
								sheet.addCell(new Label(4, number, "1", wcf_center));
								sheet.addCell(new Label(5, number, "1", wcf_center));
								sheet.addCell(new Label(6, number, want_answer, wcf_center));  
								sheet.addCell(new Label(7, number, "0", wcf_center));      //Ĭ��û������
								sheet.addCell(new Label(8, number, question_comment, wcf_center));
									
								sheet.setRowView(number, 1300, false); // �����и�
								sheet.setColumnView(0, 20);
								sheet.setColumnView(1, 60);
								int realanswernumber = PageAnalysis.RealAnswerNumber(doc);
								for (int m = number; m < number + realanswernumber; m++) {
									sheet.setRowView(m + 1, 1300, false); // �����и�
									String answercontent = PageAnalysis.AnswerContent(doc, m - number);
									int contentlength2 = PageAnalysis.Content_Length2(doc, m - number); // �ַ�����
									int contentlength = PageAnalysis.Content_Length(doc, m - number);   // ���ʳ���
									String upvote = PageAnalysis.Upvotes(doc, m - number);              // ֧��Ʊ�� 
									String url1 = PageAnalysis.URLs(doc, m - number);                    // ��������
									String comment = PageAnalysis.Comment_Numbers(doc, m - number);     // ��������
									
									viewanalysis = viewanalysis + "������Ϊ��" + answercontent + "\n";
									viewanalysis = viewanalysis + "���ַ�Ϊ��" + contentlength2 + "\n";
									viewanalysis = viewanalysis + "�𰸵���Ϊ��" + contentlength + "\n";
									viewanalysis = viewanalysis + "��upvoteΪ��" + upvote + "\n";
									viewanalysis = viewanalysis + "���Ƿ��������Ϊ��" + url1 + "\n";
									viewanalysis = viewanalysis + "��������Ϊ��" + comment + "\n";
									
									String follower = null;
									String info = null;
									String know = null;
									String answer = null;
									String authorpath = catalog + keyword + j + "_author_" + (m - number) + ".html";
									viewanalysis = viewanalysis + "\n" + "��������ҳ����µ�����ҳ�棺 " + authorpath + "\n";
									
									File file1 = new File(authorpath);
									if(!file1.exists()){
										System.out.println(authorpath + "   ������");
									}else{
										Document doc1 = tag2.parsePathText(authorpath);  //��������ҳ��·��
									
										follower = PageAnalysis.Author_Followers(doc1, m - number);   // ���߷�˿
										info = PageAnalysis.Author_Info(doc, m - number);            // ���߼���
										know = PageAnalysis.Author_KnowAbout(doc1, m - number);       // ��������
										answer = PageAnalysis.Author_Answers(doc1, m - number);       // �ش�����
										
										viewanalysis = viewanalysis + "���߷�˿��Ϊ��" + follower + "\n";
										viewanalysis = viewanalysis + "���߼���Ϊ��" + info + "\n";
										viewanalysis = viewanalysis + "��������Ϊ��" + know + "\n";
										viewanalysis = viewanalysis + "���߻ش�����Ϊ��" + answer + "\n";
									}
										
									sheet.addCell(new Label(0, m + 1, (m - number + 1) + " ",wcf_center));
									sheet.addCell(new Label(1, m + 1, answercontent, wcf_center));
									sheet.addCell(new Label(2, m + 1, contentlength2 + "",wcf_center));
									sheet.addCell(new Label(3, m + 1, contentlength + "",wcf_center));
									sheet.addCell(new Label(4, m + 1, "1", wcf_center));
									sheet.addCell(new Label(5, m + 1, "0", wcf_center));
									sheet.addCell(new Label(6, m + 1, upvote, wcf_center));
									sheet.addCell(new Label(7, m + 1, url1, wcf_center));
									sheet.addCell(new Label(8, m + 1, comment, wcf_center));
									sheet.addCell(new Label(9, m + 1, follower, wcf_center));
									sheet.addCell(new Label(10, m + 1, info, wcf_center));
									sheet.addCell(new Label(11, m + 1, know, wcf_center));
									sheet.addCell(new Label(12, m + 1, answer, wcf_center));
								}
								number = number + realanswernumber + 1;
							}
							String path = t.GetCatalog(keyword);
							System.out.println(path + " �Ѿ��ɹ������� " + catalog + keyword + "-tag.xls");
							viewanalysis = viewanalysis + "�Ѿ��ɹ������� " + catalog + keyword + "-tag.xls" + "\n" + "\n";
						}
						workbook.write(); // д��������д��Ԫ�����Բ��ܷ���ѭ�����棬����������
						/** *********�ر��ļ�************* */
						workbook.close();
						
						//��ʾ����������Ϣ
						resultPane2.setText(viewanalysis);
						resultPane2.setFont(new Font("����", Font.BOLD, 18));
					}
					
				}
				
			} catch (Exception e1) {
				System.out.println("��ȡ" + course + "�Ĺ����г���δ֪����...");
				e1.printStackTrace();
			}
			
		}
	}

	public static void main(String[] args) {
		// new SearchPanel();
	}
	
	/**
	 * ������ҳ
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
