package basic;
 
import java.sql.ResultSet;
import java.sql.SQLException;
 
import org.junit.Test;
 
public class mysqloperationexample {
     
    //导入mysqloperation类
    mysqlconnection mysqlCon = new mysqlconnection();
     
    //准备sql语句
    private String sql;
     
    //影响行数（数据变更后，影响行数都是大于0，等于0时没变更，所以说如果变更失败，那么影响行数必定为负）
    private int i = -1;
     
    //结果集
    private ResultSet rs;
     
    /*
     * 插入数据
     * */
    @Test
    public void insert() {
        // TODO Auto-generated method stub
         
        //创建sql语句
        sql = "insert into user(name, password) values(?,?)";
         
        //创建object数组
        Object[] object = new Object[]{"admin","123456"};
         
        //执行sql语句
        mysqlCon.doSql(sql, object);
         
        //获取影响行数
        i = mysqlCon.getUpdateCount();
         
        //判断是否插入成功
        if(i != -1){  
            System.out.println("数据插入成功！");
        }else{
            System.out.println("数据插入失败！");
         
        }
         
        //关闭链接
        mysqlCon.getClose();
         
    }
     
    /*
     * 删除数据
     * */
    @Test
    public void delete() {
        // TODO Auto-generated method stub
         
        //创建sql语句
        sql = "delete from user where name=?";
         
        //创建object数组
        Object[] object = new Object[]{"admin"};
         
        //执行sql语句
        mysqlCon.doSql(sql, object);
         
        //获取影响行数
        i = mysqlCon.getUpdateCount();
         
        //判断是否删除成功
        if(i != -1){
            System.out.println("数据删除成功！");
        }else{
            System.out.println("数据删除失败！");
        }
         
        //关闭链接
        mysqlCon.getClose();
         
    }
     
    /*
     * 更新数据
     * */
    @Test
    public void update() {
        // TODO Auto-generated method stub
         
        //创建sql语句
        sql = "update user set password=? where name=?";
         
        //创建Object数组
        Object[] object = new Object[]{"11", "admin"};
         
        //执行sql语句
        mysqlCon.doSql(sql, object);
         
        //获取影响行数
        i = mysqlCon.getUpdateCount();
         
        //判断数据是否更新成功
        if(i != -1){
            System.out.println("数据更新成功！");
        }else{
            System.out.println("数据更新失败！");
        }
         
        //关闭链接
        mysqlCon.getClose();
         
    }
     
    /*
     * 遍历数据
     * */
    @Test
    public void select() {
        // TODO Auto-generated method stub
         
        //创建sql语句
        sql = "select * from user";
         
        //执行sql语句
        mysqlCon.doSql(sql, null);
         
        //获取结果集
        rs = mysqlCon.getRS();
         
        //判断结果集是否为空
        if(rs != null){
            try {
                 
                //将光标移动到结果集末端,注意此处不能使用rs.afterLast();否则为空值。
                rs.last();
                 
                //获取结果集行数
                i = rs.getRow();
                 
                //判断结果集是否存在
                if(i > 0){
                     
                    //将光标移动到结果集前端
                    rs.beforeFirst();
                     
                    //循环遍历所有行数
                    while(rs.next()){
                         
                        //遍历每行元素的内容
                        String name = rs.getString("name");
                        String password = rs.getString("password");
                         
                        //在控制台打印出结果
                        System.out.println("name:" + name + "   password:" + password);
                    }
                }else{
                    System.out.println("结果集为空！");
                }
                 
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
             
        }else{
            System.out.println("结果集不存在！");
        }
         
        //关闭链接
        mysqlCon.getClose();
         
    }
 
}