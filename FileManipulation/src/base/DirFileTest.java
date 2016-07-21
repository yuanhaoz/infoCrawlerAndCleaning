package base;

import static org.junit.Assert.*;

import org.junit.Test;

public class DirFileTest {

	@Test
	public void testMain() {
//		fail("Not yet implemented");
	}

	@Test
	public void testGetFileNamesFromDirectory() {
//		fail("Not yet implemented");
	}

	@Test
	public void testGetStringFromPathFile() {
//		fail("Not yet implemented");
		String txtFile = "testdata/simple.txt";
		String txtFile2 = "testdata/simple_out.txt";
		String text=DirFile.getStringFromPathFile(txtFile);
		DirFile.storeString2File(text, txtFile2);
	}

	@Test
	public void testStoreString2FileStringString() {
//		fail("Not yet implemented");
	}

	@Test
	public void testStoreString2FileStringBufferString() {
//		fail("Not yet implemented");
	}

	@Test
	public void testDelFile() {
//		fail("Not yet implemented");
	}

}
