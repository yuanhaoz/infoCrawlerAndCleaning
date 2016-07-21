//package json;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
// 
//
//
//import base.DirFile;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.jayway.jsonpath.JsonPath;
//
// 
//public class Jsonpath_Topic {
// 
//    public static void main(String[] args) throws IOException {
//        Employee emp = createEmployee();
// 
//        //���ÿո񷽱��ӡ�ĺÿ�
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
// 
//        // read JSON file data as String
//        String fileData = new String(Files.readAllBytes(Paths.get("file/jsontest/emp.txt")));
// 
//        // parse json string to object
//        Employee emp1 = gson.fromJson(fileData, Employee.class);
// 
//        // print object data
//        System.out.println(emp1);
//        
//        System.out.println("=================");
//
// 
//        // create JSON String from Object
//        String jsonEmp = gson.toJson(emp);
//        System.out.print(jsonEmp);
//        DirFile.storeString2File(jsonEmp, "file/jsontest/emp1.txt");
//        
//        
//        /**
//         * jsonpath����json
//         */
//        System.out.println(JsonPath.read(jsonEmp, "$.empID"));
// 
//    }
// 
//    public static Topic createEmployee() {
// 
//        Topic topic = new Topic();
//        topic.setId(100);
// 
//        return topic;
//    }
//}