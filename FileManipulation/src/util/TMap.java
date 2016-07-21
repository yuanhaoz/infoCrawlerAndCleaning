/**
 * 
 */
package util;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * @author libing
 *
 */
public class TMap<T,V> extends HashMap<T,List<V>>{
	private static final long serialVersionUID=1L;
	public List<V> vPut(T key,V value){
		if(!super.containsKey(key)){
			super.put(key, new Vector<V>());
		}
		List<V> list =super.get(key);
		list.add(value);
		return super.get(list);
	}
	 @Override  
    public List<V> get(Object key) {  
        return super.get( key ) ;   
    }  
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TMap<String, String> map = new TMap<String, String>() ;  
        map.vPut("1", "2");   
        map.vPut("1", "3");   
        map.vPut("1", "4");     
        map.vPut("1", "5");     
        map.vPut("2", "7");     
        map.vPut("3", "4");     
        map.vPut("2", "3");     
        map.vPut("3", "2");     
        System.out.println( map );   //{3=[4, 2], 2=[7, 3], 1=[2, 3, 4, 5]}
        List<String> rs=map.get("1");
        System.out.println(rs); //[2, 3, 4, 5]
        System.out.println(rs.get(0));//2
	}

}
