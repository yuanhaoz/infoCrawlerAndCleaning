package test1;
/**
 * 用于解决字符串乱码问题。可以实现编解码
 */
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class URLdecode {
	public static void main(String[] args) {
		try {
			String codes = URLEncoder.encode("École_Polytechnique_Fédérale_de_Lausanne",
					"UTF-8");
			System.out.println(codes);
			String text = URLDecoder.decode(
					"%C3%89cole_Polytechnique_F%C3%A9d%C3%A9rale_de_Lausanne", "UTF-8");
			System.out.println(text);
			String text2 = URLDecoder.decode(
					"B%2B+tree", "UTF-8");
			System.out.println(text2);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
