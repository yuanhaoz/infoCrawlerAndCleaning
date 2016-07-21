package author;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserTest {

	private String filePath = "file/datacollection/Data_structure_excel59/2-3+heap/2-3+heap0.html"; 
	private String filePath1 = "file/datacollection/Data_structure_excel59/2-3+heap/2-3+heap1.html"; 
	private String filePath2 = "file/datacollection/Data_structure_excel59/2-3+tree/2-3+tree0.html"; 
	private String filePath3 = "file/datacollection/Data_structure_excel59/2-3+tree/2-3+tree1.html"; 
	private String filePath4 = "file/datacollection/Data_structure_excel59/Bloom+filter/Bloom+filter1.html"; 
	
	@Before
	public void setUp() throws Exception {
//		System.out.println("开始测试》》》》》》》》》》");
	}

	@After
	public void tearDown() throws Exception {
//		System.out.println("结束测试》》》》》》》》》》");
	}

	@Test
	public void testGetCrawlerTime() {
		try {
			User.getCrawlerTime(filePath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testCountAnswerNumber() {
		try {
			File input = new File(filePath);
			Document doc = Jsoup.parse(input, "UTF-8", "http://www.quora.com");
			User.countAnswerNumber(doc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testCountRealAnswerNumber() {
		try {
			File input = new File(filePath2);
			Document doc = Jsoup.parse(input, "UTF-8", "http://www.quora.com");
			User.countRealAnswerNumber(doc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testQuestionContent() {
		try {
			File input = new File(filePath3);
			Document doc = Jsoup.parse(input, "UTF-8", "http://www.quora.com");
			User.questionContent(doc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testQuestionExpandInfo() {
		try {
			File input = new File(filePath3);
			Document doc = Jsoup.parse(input, "UTF-8", "http://www.quora.com");
			User.questionExpandInfo(doc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testQuestionLength() {
		try {
			File input = new File(filePath3);
			Document doc = Jsoup.parse(input, "UTF-8", "http://www.quora.com");
			User.questionLength(doc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testQuestionFollow() {
		try {
			File input = new File(filePath);
			Document doc = Jsoup.parse(input, "UTF-8", "http://www.quora.com");
			User.questionFollow(doc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testQuestionComment() {
		try {
			File input = new File(filePath4);
			Document doc = Jsoup.parse(input, "UTF-8", "http://www.quora.com");
			User.questionComment(doc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testQuestionShare() {
		try {
			File input = new File(filePath4);
			Document doc = Jsoup.parse(input, "UTF-8", "http://www.quora.com");
			User.questionShare(doc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testAnswerContent() {
		try {
			File input = new File(filePath);
			Document doc = Jsoup.parse(input, "UTF-8", "http://www.quora.com");
			for(int i = 0; i < 31; i++){
				User.answerContent(doc, i);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testAnswerLength() {
		try {
			File input = new File(filePath);
			Document doc = Jsoup.parse(input, "UTF-8", "http://www.quora.com");
			for(int i = 0; i < 31; i++){
				User.answerLength(doc, i);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testAnswerUpvote() {
		try {
			File input = new File(filePath);
			Document doc = Jsoup.parse(input, "UTF-8", "http://www.quora.com");
			for(int i = 0; i < 31; i++){
				User.answerUpvote(doc, i);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testAnswerComment() {
		try {
			File input = new File(filePath);
			Document doc = Jsoup.parse(input, "UTF-8", "http://www.quora.com");
			for(int i = 0; i < 31; i++){
				User.answerComment(doc, i);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAnswerShare() {
		try {
			File input = new File(filePath);
			Document doc = Jsoup.parse(input, "UTF-8", "http://www.quora.com");
			for(int i = 0; i < 31; i++){
				User.answerShare(doc, i);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testAnswerURL() {
		try {
			File input = new File(filePath);
			Document doc = Jsoup.parse(input, "UTF-8", "http://www.quora.com");
			for(int i = 0; i < 31; i++){
				User.answerURL(doc, i);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testAuthorName() {
		try {
			File input = new File(filePath);
			Document doc = Jsoup.parse(input, "UTF-8", "http://www.quora.com");
			for(int i = 0; i < 31; i++){
				User.authorName(doc, i);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testAuthorAnswer() {
		try {
			String authorFilePath = "file\\datacollection\\Data_structure_excel59\\"
					+ "2-3+heap\\2-3+heap0_author_0.html";
			File input = new File(authorFilePath);
			Document doc = Jsoup.parse(input, "UTF-8", "http://www.quora.com");
			User.authorAnswer(doc, 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testAuthorQuestion() {
		try {
			String authorFilePath = "file\\datacollection\\Data_structure_excel59\\"
					+ "2-3+heap\\2-3+heap0_author_0.html";
			File input = new File(authorFilePath);
			Document doc = Jsoup.parse(input, "UTF-8", "http://www.quora.com");
			User.authorQuestion(doc, 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testAuthorFollower() {
		try {
			String authorFilePath = "file\\datacollection\\Data_structure_excel59\\"
					+ "2-3+heap\\2-3+heap0_author_0.html";
			File input = new File(authorFilePath);
			Document doc = Jsoup.parse(input, "UTF-8", "http://www.quora.com");
			User.authorFollower(doc, 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testAuthorFollowing() {
		try {
			String authorFilePath = "file\\datacollection\\Data_structure_excel59\\"
					+ "2-3+heap\\2-3+heap0_author_0.html";
			File input = new File(authorFilePath);
			Document doc = Jsoup.parse(input, "UTF-8", "http://www.quora.com");
			User.authorFollowing(doc, 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testAuthorAminusQ() {
		try {
			String authorFilePath = "file\\datacollection\\Data_structure_excel59\\"
					+ "2-3+heap\\2-3+heap0_author_1.html";
			File input = new File(authorFilePath);
			Document doc = Jsoup.parse(input, "UTF-8", "http://www.quora.com");
			User.authorAminusQ(doc, 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
