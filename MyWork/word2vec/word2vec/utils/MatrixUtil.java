package word2vec.utils;
import Jama.*;

/** Example of use of Matrix Class, featuring magic squares. **/

public class MatrixUtil {
	
	public static void example(){
		//随机生成两个一千乘一千的矩阵
        Matrix m1 = Matrix.random(1000, 1000);
        Matrix m2 = Matrix.random(1000, 1000);
        System.out.println(m1.getColumnDimension() + "," + m1.getRowDimension());
         
        //计算结果
        Matrix result1 = m1.plus(m2);//+
        Matrix result2 = m1.times(m2);//*
         
        System.out.println(result1.get(0, 0));
        System.out.println(result2.get(0, 0));
	}
	
	public static void example2(){
		
	}
}