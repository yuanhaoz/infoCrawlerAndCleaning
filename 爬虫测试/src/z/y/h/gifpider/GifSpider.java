package z.y.h.gifpider;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GifSpider implements Runnable {

	volatile boolean isRunning = true;
	private ThreadPoolExecutor threadPool;
	BlockingQueue<String> queue;

	public GifSpider(BlockingQueue<String> queue) {
		this.queue = queue;
		this.init();
	}

	/**
	 * �̳߳س�ʼ��
	 */
	private void init() {
		Properties pro = PropertyUtil.getProperties();
		int corePoolSize = Integer.parseInt(pro
				.getProperty("threadpool.corePoolSize"));
		int maxPoolSize = Integer.parseInt(pro
				.getProperty("threadpool.maxPoolSize"));
		int keepAliveSeconds = Integer.parseInt(pro
				.getProperty("threadpool.keepAliveSeconds"));
		int queueCap = Integer.parseInt(pro
				.getProperty("threadpool.queueCapacity"));
		BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(
				queueCap);
		this.threadPool = new ThreadPoolExecutor(corePoolSize, maxPoolSize,
				keepAliveSeconds, TimeUnit.SECONDS, queue);
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	@Override
	public void run() {
		while (this.isRunning) {
			try {

				String url = this.queue.take();
				System.out.println("����url��" + url);
				Document doc = Jsoup.connect(url).get();
				// ��ȡ����<a href>
				Elements s = doc.select("div.pic_list2").first()
						.select("a[href]");
				for (Element e : s) {
					// ��img �� ���� ����href��ָ����ͬ��ͼƬ��ֻ����ͼƬhref������
					Elements s1 = e.select("img");
					if (s1.size() != 0) {
						String imgUrl = e.absUrl("href");
						String text = s1.attr("alt");
						Document doc1 = Jsoup.connect(imgUrl).get();
						Elements e1 = doc1.getElementById("endtext").select("img");
						// ��ҳԴ���������·����Ҫ��ȡ����·��
						String realUrl = e1.attr("abs:src");
						System.out.println("��ȡͼƬurl��" + realUrl);
						// ��ȡ��ͼƬurl���Ӹ��̳߳ش���
						GifProcessor pro = new GifProcessor(text, realUrl);
						this.threadPool.execute(pro);
					}

				}
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
