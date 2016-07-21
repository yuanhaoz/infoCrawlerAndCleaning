package util;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Vector;

/**
 * 对文件（夹）直接按照文件名进行读、写、删除、复制等操作，省去对文件流的操作
 * @author MJ
 * @description
 * @usage 所有方法都是静态方法，所以可以在目标类里直接使用类方法来直接调用，比如FileUtil.readFile(String filePath)
 */
public class FileUtil {

    public static void main(String args[]) throws IOException {
    }

    /**
	 * 获取dir目录下的文件名称集合，去除了后缀名
	 * @param dir
	 * @return
	 */
	public static Vector<String> getDirFileSet(String dir){
		Vector<String> vFile=new Vector<String>();
		File f=new File(dir);
		File childs[]=f.listFiles();
		for(int i=0;i<childs.length;i++){
			String fileName=childs[i].getName();
			String fileNamePrefix=fileName;
			if(fileName.contains("."))
			 fileNamePrefix=fileName.substring(0, fileName.lastIndexOf("."));
			vFile.add(fileNamePrefix);
		}
		return vFile;
	}
	
	/**
	 * 获取dir目录下的文件名称集合，不去除了后缀名
	 * @param dir
	 * @return
	 */
	public static Vector<String> getOriginalDirFileSet(String dir){
		Vector<String> vFile=new Vector<String>();
		File f=new File(dir);
		File childs[]=f.listFiles();
		for(int i=0;i<childs.length;i++){
			String fileName=childs[i].getName();
			vFile.add(fileName);
		}
		return vFile;
	}
	
	/**
	 * copy file
	 * @param sourcePath
	 * @param targetPath
	 */
	public static void copyFile(String sourcePath,String targetPath){
		try{
			copyFile(new File(sourcePath),new File(targetPath));
		}catch(IOException e){
			e.printStackTrace();
		}
	}
    

	/**
	 * copy file
	 * @param sourceFile
	 * @param targetFile
	 * @throws IOException
	 */
    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        } finally {
            // 关闭流
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }

    /**
     * copy directiory
     * @param sourceDir
     * @param targetDir
     * @throws IOException
     */
    public static void copyDirectiory(String sourceDir, String targetDir) throws IOException {
        // 新建目标目录
        (new File(targetDir)).mkdirs();
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(sourceDir)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                // 源文件
                File sourceFile = file[i];
                // 目标文件
                File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());
                System.out.println("copy:"+(i+1)+"/"+file.length);
                copyFile(sourceFile, targetFile);
            }
            if (file[i].isDirectory()) {
                // 准备复制的源文件夹
                String dir1 = sourceDir + "/" + file[i].getName();
                // 准备复制的目标文件夹
                String dir2 = targetDir + "/" + file[i].getName();
                copyDirectiory(dir1, dir2);
            }
        }
    }

    /**
     * 按照指定编码拷贝文件
     * @param srcFileName
     * @param destFileName
     * @param srcCoding
     * @param destCoding
     * @throws IOException
     */
    public static void copyFile(File srcFileName, File destFileName, String srcCoding, String destCoding) throws IOException {// 把文件转换为GBK文件
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(srcFileName), srcCoding));
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destFileName), destCoding));
            char[] cbuf = new char[1024 * 5];
            int len = cbuf.length;
            int off = 0;
            int ret = 0;
            while ((ret = br.read(cbuf, off, len)) > 0) {
                off += ret;
                len -= ret;
            }
            bw.write(cbuf, 0, off);
            bw.flush();
        } finally {
            if (br != null)
                br.close();
            if (bw != null)
                bw.close();
        }
    }

    /**
     * 删除文件
     * @param filepath
     * @throws IOException
     */
    public static void del(String filepath) {
        File f = new File(filepath);// 定义文件路径
        if (f.exists() && f.isDirectory()) {// 判断是文件还是目录
            if (f.listFiles().length == 0) {// 若目录下没有文件则直接删除
                f.delete();
            } else {// 若有则把文件放进数组，并判断是否有下级目录
                File delFile[] = f.listFiles();
                int i = f.listFiles().length;
                for (int j = 0; j < i; j++) {
                    if (delFile[j].isDirectory()) {
                        del(delFile[j].getAbsolutePath());// 递归调用del方法并取得子目录路径
                    }
                    delFile[j].delete();// 删除文件
                }
            }
        }
        else f.delete();
    }
    
    /**
     * 将filepath指定的文件读取到字符串中，不包含换行符
     * @param filePath 要读取的文件路径
     * @return 文件字符串
     */
    public static String readFile(String filePath){
    	FileReader fr;
    	StringBuffer sb=new StringBuffer();
    	File f=new File(filePath);
    	if(f.exists()){
    		try {
    			fr = new FileReader(filePath);
    			BufferedReader br=new BufferedReader(fr);
    			String temp=br.readLine();
    			while(temp!=null){
    				sb=sb.append(temp);
    				temp=br.readLine();
    			}
    			br.close();
    			fr.close();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
    	String s=sb.toString();
    	sb.delete(0, sb.length());
		return s;
    }
    
    /**
     * 读取文件并指定编码格式
     * @param filePath 要读取的文件路径
     * @param Charset 读取文件的编码
     * @return 文件字符串
     */
    public static String readFile(String filePath,String Charset){
    	StringBuffer sb=new StringBuffer();
    	File f=new File(filePath);
    	if(f.exists()){
    		try {
    			InputStreamReader read = new InputStreamReader (new FileInputStream(f),Charset);
    			BufferedReader br = new BufferedReader(read);
    			String temp=br.readLine();
    			while(temp!=null){
    				sb=sb.append(temp);
    				temp=br.readLine();
    			}
    			br.close();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
    	String s=sb.toString();
    	sb.delete(0, sb.length());
		return s;
    }
    
    /**
     * 读取文件并保留文件的换行符
     * @param filePath 要读取的文件路径
     * @return 文件字符串，保留换行
     */
    public static String readOrignalFile(String filePath){
    	StringBuffer sb=new StringBuffer();
    	File f=new File(filePath);
    	if(f.exists()){
    		try {
    			InputStreamReader read = new InputStreamReader (new FileInputStream(f));
    			BufferedReader br = new BufferedReader(read);
    			String temp=br.readLine();
    			while(temp!=null){
    				sb=sb.append(temp);
    				temp=br.readLine();
    				if(temp!=null)
    					temp+="\n";
    			}
    			br.close();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
    	String s=sb.toString();
    	sb.delete(0, sb.length());
		return s;
    }
    
    /**
     * 
     * @param filePath 要读取的文件路径
     * @param Charset 读取文件的编码
     * @return 文件字符串，保留换行
     */
    public static String readOrignalFile(String filePath,String Charset){
    	StringBuffer sb=new StringBuffer();
    	File f=new File(filePath);
    	if(f.exists()){
    		try {
    			InputStreamReader read = new InputStreamReader (new FileInputStream(f),Charset);
    			BufferedReader br = new BufferedReader(read);
    			String temp=br.readLine();
    			while(temp!=null){
    				sb=sb.append(temp);
    				temp=br.readLine();
    				if(temp!=null)
    					temp+="\n";
    			}
    			br.close();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
    	String s=sb.toString();
    	sb.delete(0, sb.length());
		return s;
    }
    
    /**
     * 将s写到filePath里面，并且指定编码
     * @param s
     * @param Charset 读取文件的编码
     * @param filePath
     */
    public static void writeStringFile(String s,String filePath,String Charset){
    	try{
    		File f=new File(filePath);
    		OutputStreamWriter writer = new OutputStreamWriter (new FileOutputStream(f),Charset);
			BufferedWriter bw = new BufferedWriter(writer);
        	bw.write(s);
        	bw.close();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    /**
     * 将字符串写到filePath里面
     * @param s
     * @param filePath
     */
    public static void writeStringFile(String s,String filePath){
    	try{
    		File f=new File(filePath);
    		OutputStreamWriter writer = new OutputStreamWriter (new FileOutputStream(f));
			BufferedWriter bw = new BufferedWriter(writer);
        	bw.write(s);
        	bw.close();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    public static void appendStringFile(String s,String filePath){
    	try{
    		FileWriter fw=new FileWriter(filePath,true);
        	BufferedWriter bw=new BufferedWriter(fw);
        	bw.write(s);
        	bw.newLine();
        	bw.close();
        	fw.close();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    /**
     * 根据给定术语集合拷贝文件集合
     * @param termSetFile
     * @param srcDir
     * @param desDir
     * @param suffix 包含.
     */
    public static void copyFileAccordingSet(String termSetFile,String srcDir,String desDir,String suffix){
    	File f=new File(desDir);
    	f.mkdirs();
    	Vector<String> vTerm=SetUtil.readSetFromFile(termSetFile);
    	for(String term:vTerm){
    		File fSrc=new File(srcDir+"/"+term+suffix);
    		if(fSrc.exists()){
    			try {
    				copyFile(fSrc,new File(desDir+"/"+term+suffix));
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		}
    	}
    }
}

