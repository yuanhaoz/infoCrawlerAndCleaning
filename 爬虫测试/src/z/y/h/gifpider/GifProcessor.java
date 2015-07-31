package z.y.h.gifpider;

public class GifProcessor implements Runnable {

	private String imgName;
	private String imgUrl;

	public GifProcessor(String name, String url) {
		this.imgName = name;
		this.imgUrl = url;
	}

	@Override
	public void run() {
		FileProcessor fp = new FileProcessor(this.imgName, this.imgUrl);
		try {
			System.out.println("œ¬‘ÿ±£¥ÊÕº∆¨url£∫" + this.imgUrl);
			fp.saveGif();

		} catch (Exception e) {
			System.out.println("œ¬‘ÿ±£¥ÊÕº∆¨ ß∞‹£¨url£∫" + this.imgUrl);
			e.printStackTrace();
		}

	}

}
