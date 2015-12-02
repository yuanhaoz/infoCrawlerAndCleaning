package test;

public class ValidFileName {

	public static void main(String[] srgs){
		test();
		test2();
	}
	
	public static void test(){
		System.out.println("null(未初始化)" + "\t" + isValidFileName(null)); 
		System.out.println(" .xml" + "\t" + isValidFileName(" .xml")); 
		System.out.println(".xml " + "\t" + isValidFileName(".xml ")); 
		System.out.println(" .xml " + "\t" + isValidFileName(" .xml ")); 
		System.out.println(".xml." + "\t" + isValidFileName(".xml.")); 
		System.out.println(".xml" + "\t" + isValidFileName(".xml")); 
		System.out.println("    .xml(制表符)" + "\t" + isValidFileName("    .xml")); 
		System.out.println(".." + "\t" + isValidFileName("..")); 
		System.out.println("fdsa    fdsa(制表符)" + "\t" + isValidFileName("fdsa    fdsa(制表符)")); 
		System.out.println("a.txt" + "\t" + isValidFileName("a.txt"));
	}
	
	public static boolean isValidFileName(String fileName) { 
	    if (fileName == null || fileName.length() > 255) 
	        return false; 
	    else 
	        return fileName.matches( 
	           "[^\\s\\\\/:\\*\\?\\\"<>\\|](\\x20|[^\\s\\\\/:\\*\\?\\\"<>\\|])*[^\\s\\\\/:\\*\\?\\\"<>\\|\\.]$"); 
	}
	
	public static void test2(){
		String fileName = "Variants of A*.txt";
//		String fileName = "a.txt";
		isValidFileName2(fileName);
	}
	
	public static void isValidFileName2(String fileName) { 
		// 判断是否存在 /\<>*?|" 这些特殊符号
		Boolean a = fileName.matches("[^/\\\\<>*?|\"]+\\.[^/\\\\<>*?|\"]+");
	    if (!a) {
	    	System.out.println("文件名不规范...");
	    	fileName = fileName.replace("*", "-star");
	    	System.out.println("修改后文件名:" + fileName);
	    } else{
	    	System.out.println("文件名规范...");
	    }
	}
}
