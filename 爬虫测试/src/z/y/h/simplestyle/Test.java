package z.y.h.simplestyle;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Crawler crawler = new Crawler();
		try {
			crawler.crawler("file/simplestyle/1.html", "http://www.simple-style.com/page/1");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
