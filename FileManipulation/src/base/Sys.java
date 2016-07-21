package base;

import java.io.File;

public class Sys {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String wnhome = System.getenv("WNHOME");
		String path = wnhome + File.separator + "dict";
	}

}
