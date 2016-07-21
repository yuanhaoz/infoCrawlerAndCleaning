package excel;

import java.io.FileNotFoundException;
import java.io.IOException;



public abstract class SheetBase extends SpreadFile {

	public SheetBase() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String RdWrFile;
		RdWrFile="F:/SysCfg/Admin/Desktop/节点/POS-RAW-0105.xls";		
		SheetBase ss;		
//		ss = new SheetBase();
//		ss.init(RdWrFile);
//		ss.go();
	}
	
	
	public void init(String file){
//		init(file,file);
		openFile(file);
	}
	
	public void go(){
		printConfigInfo();
		processing();
		save();
		System.out.println("go() end");
		
	}
	
	abstract protected void processing();
}
