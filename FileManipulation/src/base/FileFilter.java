package base;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class FileFilter {

	public FileFilter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public static FilenameFilter extHtml(){
		return new OnlyExt("html,htm".split(","));
	}

}

class OnlyExt implements FilenameFilter {
    Set<String> exts=new HashSet<String>();

    public OnlyExt(String[] exts) {
    	for(String e:exts){
    		 this.exts.add(e.toLowerCase());
    	}
        
    }

    public boolean accept(File dir, String name) {
    	if(name.lastIndexOf('.')>0)
        {
           // get last index for '.' char
           int lastIndex = name.lastIndexOf('.');
           
           // get extension
           String str = name.substring(lastIndex+1);
           
           // match path name extension
           if(exts.contains(str.toLowerCase()))
           {
              return true;
           }
        }
        return false;
    }
}
