package json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 


import base.DirFile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jayway.jsonpath.JsonPath;

 
public class Jsonpath {
 
    public static void main(String[] args) throws IOException {
        Employee emp = createEmployee();
 
        //设置空格方便打印的好看
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
 
        // read JSON file data as String
        String fileData = new String(Files.readAllBytes(Paths.get("file/jsontest/emp.txt")));
 
        // parse json string to object
        Employee emp1 = gson.fromJson(fileData, Employee.class);
 
        // print object data
        System.out.println(emp1);
        
        System.out.println("=================");

 
        // create JSON String from Object
        String jsonEmp = gson.toJson(emp);
        System.out.print(jsonEmp);
        DirFile.storeString2File(jsonEmp, "file/jsontest/emp1.txt");
        
        
        /**
         * jsonpath解析json
         */
        System.out.println(JsonPath.read(jsonEmp, "$.empID"));
 
    }
 
    public static Employee createEmployee() {
 
        Employee emp = new Employee();
        emp.setId(100);
        emp.setName("David");
        emp.setPermanent(false);
        emp.setPhoneNumbers(new long[] { 123456, 987654 });
        emp.setRole("Manager");
 
        Address add = new Address();
        add.setCity("Bangalore");
        add.setStreet("BTM 1st Stage");
        add.setZipcode(560100);
        emp.setAddress(add);
 
        List<String> cities = new ArrayList<String>();
        cities.add("Los Angeles");
        cities.add("New York");
        emp.setCities(cities);
 
        Map<String, String> props = new HashMap<String, String>();
        props.put("salary", "1000 Rs");
        props.put("age", "28 years");
        emp.setProperties(props);
 
        return emp;
    }
}