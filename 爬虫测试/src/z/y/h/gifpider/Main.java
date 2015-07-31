package z.y.h.gifpider;

import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
	public static void main(String[] args) {
		init();

	}

	public static void init() {
		Properties pro = PropertyUtil.getProperties();
		int startPage = Integer.parseInt(pro.getProperty("startPage"));
		int endPage = Integer.parseInt(pro.getProperty("endPage"));
		String url = pro.getProperty("url");
		int count = endPage - startPage + 1;
		BlockingQueue<String> queue = new LinkedBlockingQueue<String>(count);
		for (int i = 1; i <= count; i++) {
			queue.add(String.format(url, i));
		}
		int spiderCount = Integer.parseInt(pro.getProperty("spiderThread"));
		for (int i = 0; i < spiderCount; i++) {
			GifSpider spider = new GifSpider(queue);
			Thread t = new Thread(spider);
			t.start();
		}
	}

}
