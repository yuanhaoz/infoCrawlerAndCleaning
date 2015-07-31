package xjtu.sky.quora;

/**
 * ��дfragment��
 * ��дfragment_term��
 * ��дassemble��
 * @author zhengtaishuai
 */

import java.io.File;
import java.net.URLDecoder;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.jsoup.nodes.Document;


public class mysql {

	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		analysis("Data_structure");
//		facetanalysis("Data_structure");
		long end = System.currentTimeMillis();
		System.out.println("�ܹ���ʱ�� " + (end - start) / 1000 + " ��...");
	}
	
	/**
	 * ��дfragment�� �� fragment_term��
	 * @author zhengtaishuai
	 */
	public static void analysis(String course) throws Exception {
		tag2 t = new tag2();
		mysql m = new mysql();
		File file0 = new File("F:/����/�γ�����/" + course);
		File[] files = file0.listFiles();
		for (int i = 0; i < files.length; i++) {
			String keyword = files[i].getName();
			if(!keyword.contains(".html")){
				int pagelength = t.PageLength(keyword);
//				m.fragment(keyword, pagelength);            //��дfragment��
//				m.fragment_filtering(keyword, pagelength);  //��дfragment�����˴����ģ�
				m.fragment_term(keyword, pagelength);       //��дfragment_term��
				System.out.println(keyword + "����Ϣ�Ѿ����浽���ݿ�...");
			}
		}
	}
	/**
	 * ��дassembleװ�����֪ʶ��Ƭ�����֮��Ĺ�ϵд����
	 * @author zhengtaishuai
	 */
	public static void facetanalysis(String course) throws Exception {
		mysql m = new mysql();
		
		//��дBinary+tree��װ���
		m.assemble("Binary+tree");
		
		//��д���������װ���
//		File file0 = new File("F:/����/�γ�����/" + course);
//		File[] files = file0.listFiles();
//		for (int i = 0; i < files.length; i++) {
//			String keyword = files[i].getName();
//			if(!keyword.contains(".html")){
//				m.assemble(keyword);
//				System.out.println(keyword + "֪ʶ��Ƭ�����Ӧ��ϵ��д���...");
//			}
//		}
	}

	/**
	 * ����������ҳ�����䱣�浽���ݿ⣬��дfragment������ǰ�ģ�
	 */
	public void fragment(String keyword, int pagelength) throws Exception {
		try {
			Connection conn;
			PreparedStatement pstmt;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			// ������MySQL�����ӣ�ע�����ݿ�IP��
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/knowledgeforest", "root","199306");
//			conn = DriverManager.getConnection("jdbc:mysql://202.117.16.39:3306/knowledgeforest", "e-learning","knowledgeforest");
//			conn = DriverManager.getConnection("jdbc:mysql://202.117.16.39:3306/knowledgeforestlocal", "e-learning","knowledgeforest");
//			conn = DriverManager.getConnection("jdbc:mysql://202.117.54.43:3306/knowledgeforest", "e-learning","knowledgeforest");
			
			//�������ݣ����ݼӵ����ݿ�����
			tag2 t = new tag2();
			String catalog = t.GetCatalog2(keyword);
			
			//�õ�������ҳ������
			String[] QuestionUrls = tag2.GetChildUrls(keyword);
//			System.out.println("������ĿΪ��" + pagelength);
			
			//����ÿ��������ҳ������ͻش�
			for (int j = 0; j < pagelength; j++) {
				String path = catalog + keyword + j + ".html";
				File file = new File(path);
				if (!file.exists()) {
					System.out.println(path + "  �����ڣ���������ȡ����...");
				} else {
					System.out.println("\n��ʼ������ " + path);
					Document doc = tag2.parsePathText(path);
					
					//�õ�����ĸ��ֶ���Ϣ��û������ID����ȡʱ��Ϊ��ǰ�������ݵ�ʱ��
					String keywordstore = keyword.replaceAll("\\+", "\\_");
//					System.out.println("ת���Ժ�" + keywordstore);
					String QuestionId = keywordstore + j + "";  //�õ�����ID
					String SourceType = "Quora";           //�õ�������Դ
					String URL = QuestionUrls[j];  //�õ���Ƭ����URL
//					System.out.println("urlΪ��" + URL);
					String QuestionContent = tag2.QuestionName(doc) + "\n" 
							+ "Expanded information��" + tag2.QuestionExpand(doc);  //�õ���Ƭ����
					SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//�������ڸ�ʽ 
					String CrawlerTime = df.format(new Date());   // new Date()Ϊ��ȡ��ǰϵͳʱ��
					System.out.println("CrawlerTime is ��" + CrawlerTime);
					String AuthorID = "0";
					String media_type = "text";          //�������ı����͵�text
					String evaluation = "1";            //�����Ƿ���ã�Ĭ�϶���1
					
					//��MYSQL������Ӽ�¼
					String sql = "replace into fragment values (?, ?, ?, ?, ?, ?, ?, ?)";
					pstmt = conn.prepareStatement(sql);
				    pstmt.setString(1, QuestionId);
				    pstmt.setString(2, SourceType);
				    pstmt.setString(3, URL);
				    pstmt.setString(4, QuestionContent);
				    pstmt.setString(5, CrawlerTime);
				    pstmt.setString(6, AuthorID);
				    pstmt.setString(7, media_type);
				    pstmt.setString(8, evaluation);
					try{
					    pstmt.executeUpdate();
					}catch(Exception e ){
						e.printStackTrace();
//						System.out.println("�������");
					}
				    
					//����̨��ʾ��Ϣ
//					System.out.println("��ʼ�������⣺");
//					System.out.println("QuestionId is : " + QuestionId);
//					System.out.println("SourceType is : " + SourceType);
//					System.out.println("questionname is ��" + questionname);
//					System.out.println("url is : " + URL);
//					System.out.println("QuestionContent is : " + QuestionContent);
//					System.out.println("��ʼ�����𰸣�");
					
				    //�õ�������
					int realanswernumber = PageAnalysis.RealAnswerNumber(doc);   
					for (int m = 0; m < realanswernumber; m++) {
						
						//�õ��𰸵ĸ��ֶ���Ϣ��û������ID����ȡʱ��Ϊ��ǰ�������ݵ�ʱ��
						String AnswerId = QuestionId + "_" + m;  //�õ���ID
						String AnswerContent = PageAnalysis.AnswerContent(doc, m);;  //�õ���Ƭ����
						
						//��MYSQL������Ӽ�¼
						pstmt = conn.prepareStatement(sql);
					    pstmt.setString(1, AnswerId);
					    pstmt.setString(2, SourceType);
					    pstmt.setString(3, URL);
					    pstmt.setString(4, AnswerContent);
					    pstmt.setString(5, CrawlerTime);
					    pstmt.setString(6, AuthorID);
					    pstmt.setString(7, media_type);
					    pstmt.setString(8, evaluation);
					    try{
						    pstmt.executeUpdate();
						}catch(Exception e ){
							e.printStackTrace();
//							System.out.println("�������");
						}
					    
						//����̨��ʾ��Ϣ
//						System.out.println("AnswerId is : " + AnswerId);
//						System.out.println("SourceType is : " + SourceType);
//						System.out.println("questionname is ��" + questionname);
//						System.out.println("url is : " + URL);
//						System.out.println("AnswerContent is : " + AnswerContent);
//						System.out.println("CrawlerTime1 is ��" + CrawlerTime1);
					}
				}
			}
		} catch (Exception ex) {
			System.out.println("Error : " + ex.toString());
		}
	}
	
	/**
	 * ���ݹ���
	 */
	public String keywordandlength(String content, String[] keywordarray){
		int count = 0;
		for (int j = 0; j < keywordarray.length; j++) {
			// ���ַ������ҹؼ��ʳ��ֵĴ���
			Pattern p = Pattern.compile("(?i)" + keywordarray[j]);    //���Դ�Сд
			Matcher m = p.matcher(content);
			while (m.find()) {
				count++;
			}
		}
//		System.out.println("�ؼ��ʳ��ִ���: " + count);
		int length = content.length();
		if (count != 0 && length < 500) {
			return "1";
		} else {
			return "0";
		}
	}
	
	/**
	 * ��дfragment�����˺�ģ�
	 */
	public void fragment_filtering(String keyword, int pagelength) throws Exception {
		try {
			Connection conn;
			PreparedStatement pstmt;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			// ������MySQL�����ӣ�ע�����ݿ�IP��
//			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/knowledgeforest", "root","199306");
			conn = DriverManager.getConnection("jdbc:mysql://202.117.16.39:3306/knowledgeforest", "e-learning","knowledgeforest");
//			conn = DriverManager.getConnection("jdbc:mysql://202.117.16.39:3306/knowledgeforestlocal", "e-learning","knowledgeforest");
//			conn = DriverManager.getConnection("jdbc:mysql://202.117.54.43:3306/knowledgeforest", "e-learning","knowledgeforest");
			
			//�������ݣ����ݼӵ����ݿ�����
			tag2 t = new tag2();
			String catalog = t.GetCatalog2(keyword);
			
			//�õ�������ҳ������
			String[] QuestionUrls = tag2.GetChildUrls(keyword);
			
			//���ؼ�����+�ֿ��浽�������棬����ƥ���ı�
			String[] keywordarray = keyword.split("\\+");      
			
			//����ÿ��������ҳ������ͻش�
			for (int j = 0; j < pagelength; j++) {
				String path = catalog + keyword + j + ".html";
				File file = new File(path);
				if (!file.exists()) {
					System.out.println(path + "  �����ڣ���������ȡ����...");
				} else {
					System.out.println("\n��ʼ������ " + path);
					Document doc = tag2.parsePathText(path);
					
					//�õ�����ĸ��ֶ���Ϣ��û������ID����ȡʱ��Ϊ��ǰ�������ݵ�ʱ��
					String keywordstore = keyword.replaceAll("\\+", "\\_");
					String QuestionId = keywordstore + j + "";        //�õ�����ID
					String SourceType = "Quora";                      //�õ�������Դ
					String URL = QuestionUrls[j];                     //�õ���Ƭ����URL
					String QuestionContent = tag2.QuestionName(doc) + "\n" 
							+ "Expanded information��" + tag2.QuestionExpand(doc);      //�õ���Ƭ����
					SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");  //�������ڸ�ʽ 
					String CrawlerTime = df.format(new Date());                         //new Date()Ϊ��ȡ��ǰϵͳʱ��
					System.out.println("CrawlerTime is ��" + CrawlerTime);
					String AuthorID = "0";
					String media_type = "text";                       //�������ı����͵�text
					
					//�ж����ݹ��ˣ��ؼ���ƥ��ͳ����ж�
					mysql ms = new mysql();
					String questionevaluation = ms.keywordandlength(QuestionContent, keywordarray);                
					
					//��MYSQL������Ӽ�¼
					String sql = "replace into fragment values (?, ?, ?, ?, ?, ?, ?, ?)";
					pstmt = conn.prepareStatement(sql);
				    pstmt.setString(1, QuestionId);
				    pstmt.setString(2, SourceType);
				    pstmt.setString(3, URL);
				    pstmt.setString(4, QuestionContent);
				    pstmt.setString(5, CrawlerTime);
				    pstmt.setString(6, AuthorID);
				    pstmt.setString(7, media_type);
				    pstmt.setString(8, questionevaluation);
					try{
					    pstmt.executeUpdate();
					}catch(Exception e ){
						e.printStackTrace();
					}
					
				    //�õ�������
					int realanswernumber = PageAnalysis.RealAnswerNumber(doc);   
					for (int m = 0; m < realanswernumber; m++) {
						
						//�õ��𰸵ĸ��ֶ���Ϣ��û������ID����ȡʱ��Ϊ��ǰ�������ݵ�ʱ��
						String AnswerId = QuestionId + "_" + m;  //�õ���ID
						String AnswerContent = PageAnalysis.AnswerContent(doc, m);;  //�õ���Ƭ����
						
						//�ж����ݹ��ˣ��ؼ���ƥ��ͳ����ж�
						String answerevaluation = ms.keywordandlength(AnswerContent, keywordarray); 
						
						//��MYSQL������Ӽ�¼
						pstmt = conn.prepareStatement(sql);
					    pstmt.setString(1, AnswerId);
					    pstmt.setString(2, SourceType);
					    pstmt.setString(3, URL);
					    pstmt.setString(4, AnswerContent);
					    pstmt.setString(5, CrawlerTime);
					    pstmt.setString(6, AuthorID);
					    pstmt.setString(7, media_type);
					    pstmt.setString(8, answerevaluation);
					    try{
						    pstmt.executeUpdate();
						}catch(Exception e ){
							e.printStackTrace();
						}
					    
					}
				}
			}
		} catch (Exception ex) {
			System.out.println("Error : " + ex.toString());
		}
	}

	
	/**
	 * ������Ƭ�����֮��Ĺ�ϵ����дassemble�����仯��
	 * ����Զ�����Binary_tree��
	 */
	public void assemble(String keyword) throws Exception {
		try {
			Connection conn;
			PreparedStatement pstmt;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			// ������MySQL�����ӣ�ע�����ݿ�IP��
//			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/knowledgeforest", "root","199306");
			conn = DriverManager.getConnection("jdbc:mysql://202.117.16.39:3306/knowledgeforest", "e-learning","knowledgeforest");
//			conn = DriverManager.getConnection("jdbc:mysql://202.117.16.39:3306/knowledgeforestlocal", "e-learning","knowledgeforest");
//			conn = DriverManager.getConnection("jdbc:mysql://202.117.54.43:3306/knowledgeforest", "e-learning","knowledgeforest");
			
			//�������ݣ����ݼӵ����ݿ�����
			tag2 t = new tag2();
			String catalog = t.GetCatalog2(keyword);
			String path = catalog + keyword + "-tag_changed.xls";
			File file = new File(path);
			if (!file.exists()) {
				System.out.println(path + "  �����ڣ����������ɽ������...");
			} else {
				System.out.println("\n��ʼ��Ӧ�� " + path);
				Workbook book = Workbook.getWorkbook(file);
				Sheet sheet = book.getSheet(0);
				int row = sheet.getRows();
				String[] facetname = {"definition", "feature", "implementation", "example", "operation"
						, "application", "method", "type", "relevant", "history", "description"
						, "purpose", "explanation", "storage", "simulator"};
				for (int i = 0; i < row; i++) {
					Cell cell0 = sheet.getCell(0, i);
					Cell cell12 = sheet.getCell(12, i);
					if (cell12.getContents().equals("1")) {
						String fragmentid = cell0.getContents().replaceAll("\\+", "\\_");
						int facetid;
						Cell cell13 = sheet.getCell(13, i);
						Cell cell14 = sheet.getCell(14, i);
						Cell cell15 = sheet.getCell(15, i);
						Cell cell16 = sheet.getCell(16, i);
						String[] tag = {cell14.getContents(), cell15.getContents(), cell16.getContents(), cell13.getContents()}; 
						for(int m = 0; m < tag.length; m++){
							for(int n = 0; n < facetname.length; n++){
								if(tag[m].equals(facetname[n])){
									facetid = n + 1; 
									
									// ��MYSQL������Ӽ�¼
									String sql1 = "replace into assemble values (?, ?, ?, ?, ?)";
									pstmt = conn.prepareStatement(sql1);
									pstmt.setInt(1, 52);
									pstmt.setInt(2, facetid);
									pstmt.setInt(3, 1);
									pstmt.setString(4, fragmentid);
									pstmt.setString(5, " ");
									pstmt.executeUpdate();
									System.out.println(fragmentid + "   " + facetid + "   52");
								}
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			System.out.println("Error : " + ex.toString());
		}
	}
	
	/**
	 * ��дfragment_term����дfragment����ǰ������ʵĹ�ϵ��
	 * ��ȡterm��
	 * @throws Exception 
	 */
	public void fragment_term(String keyword, int pagelength) throws Exception {
		Connection conn;
		PreparedStatement pstmt;
		PreparedStatement ps;
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		
		// ������MySQL�����ӣ�ע�����ݿ�IP��
//		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/knowledgeforest", "root","199306");
		conn = DriverManager.getConnection("jdbc:mysql://202.117.16.39:3306/knowledgeforest", "e-learning","knowledgeforest");
//		conn = DriverManager.getConnection("jdbc:mysql://202.117.16.39:3306/knowledgeforestlocal", "e-learning","knowledgeforest");
//		conn = DriverManager.getConnection("jdbc:mysql://202.117.54.43:3306/knowledgeforest", "e-learning","knowledgeforest");
		
		//��ѯterm���õ��ؼ��ʱ�
		ps = conn.prepareStatement("select * from term");
		ResultSet rs = ps.executeQuery();
		int length = 0;
		while(rs.next()){
			length++;            //�õ�term����
		}    
		System.out.println("����term��ĿΪ��" + length);
		
		//�õ�term�����������Ͷ�Ӧid
		String[] topic = new String[length];
		int[] term = new int[length];
		ResultSet rs1 = ps.executeQuery();
		int index = 0;
		while(rs1.next()){
		        int term_id = rs1.getInt(1);
		        String domterm_name = rs1.getString(2);
		        topic[index] = domterm_name;
		        term[index] = term_id;
		        index++;
		}
		
		//�ȽϹؼ��ʺ�����ʣ��жϹؼ����Ƿ���term�����Ƿ����
		String keywordchange = keyword.replaceAll("\\+", "\\_");
		for(int i = 0; i < topic.length; i++){
			if(keywordchange.equals(topic[i])){
				tag2 t = new tag2();
				String catalog = t.GetCatalog2(keyword);
				for (int j = 0; j < pagelength; j++) {
					String path = catalog + keyword + j + ".html";
					File file = new File(path);
					Document doc = tag2.parsePathText(path);
					if (!file.exists()) {
						System.out.println(path + "  �����ڣ���������ȡ����...");
					} else {
						String QuestionId = keywordchange + j + "";  //�õ�����ID
						int term_id = term[i];
//						System.out.println(QuestionId + "   " + term_id);
						String sql = "replace into fragment_term values (?, ?)";
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, QuestionId);
						pstmt.setInt(2, term_id);
						pstmt.executeUpdate();
					}
					int realanswernumber = PageAnalysis.RealAnswerNumber(doc);   
					for (int m = 0; m < realanswernumber; m++) {
						String AnswerId = keywordchange + j + "_" + m;  //�õ���ID
						int term_id = term[i];
//						System.out.println(AnswerId + "   " + term_id);
						String sql = "replace into fragment_term values (?, ?)";
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, AnswerId);
						pstmt.setInt(2, term_id);
						pstmt.executeUpdate();
					}
				}
			}
		}
	}
	
	
	/**
		 * ��д����ʱ��Լ���ȡ�����ݽṹ��54������ʣ�
		 * 
		 * ����ʱû���ô���
		 * 
		 * @throws Exception 
		 */
		public static void topic(String course) throws Exception {
			Connection conn;
			PreparedStatement pstmt;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			// ������MySQL�����ӣ�ע�����ݿ�IP��
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/knowledgeforest", "root","199306");
	//		conn = DriverManager.getConnection("jdbc:mysql://202.117.16.39:3306/knowledgeforest", "e-learning","knowledgeforest");
	//		conn = DriverManager.getConnection("jdbc:mysql://202.117.16.39:3306/knowledgeforestlocal", "e-learning","knowledgeforest");
	//		conn = DriverManager.getConnection("jdbc:mysql://202.117.54.43:3306/knowledgeforest", "e-learning","knowledgeforest");
			
			
			//���
			File file0 = new File("f:/����/�γ�����/" + course);
			File[] files = file0.listFiles();
			for (int i = 0; i < files.length; i++) {
				int topic_id = i + 1;
				String topic_name = files[i].getName();
				topic_name = URLDecoder.decode(topic_name, "UTF-8");
				
				// ��MYSQL������Ӽ�¼
				String sql = "insert into domain_topic values (?, ?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, topic_id);
				pstmt.setString(2, topic_name);
				pstmt.executeUpdate();
				System.out.println(topic_id + "   " + topic_name);
			}
		}

	public void test(){
		try {
			Connection conn;
			Statement stmt;

			// ����Connector/J����
			// ��һ��Ҳ��дΪ��Class.forName("com.mysql.jdbc.Driver");
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			// ������MySQL������
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/knowledgeforest", "root","199306");
			// ִ��SQL���
			stmt = conn.createStatement();
			stmt.execute("insert into fragment value ('1', 'Quora', 'http://www.quora.com/search?q=binary+trees', 'hello world', '2015/05/05', '2')");

		} catch (Exception ex) {
			System.out.println("Error : " + ex.toString());
		}

	}
}
