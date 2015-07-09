package basic;

public class convertkeyword {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		convert("Binary+tree");
	}
	
	/**
	 * 将Binary+tree转化为Binary_tree"
	 * @param keyword
	 * @return
	 */
	public static String convert(String keyword){
		String keywordstore = keyword.replaceAll("\\+", "\\_");
		System.out.println("转义以后：" + keywordstore);
		return keywordstore;
	}

}
