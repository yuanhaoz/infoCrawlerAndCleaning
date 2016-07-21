package json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import java.util.Random;

import base.DirFile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jayway.jsonpath.JsonPath;

 
public class JsonPathTree {
 
    public static void main(String[] args) throws IOException {
        //设置空格方便打印的好看
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
//        // read JSON file data as String
//        String fileData = new String(Files.readAllBytes(Paths.get("file/jsontest/emp.txt")));
//        // parse json string to object
//        Employee emp1 = gson.fromJson(fileData, Employee.class);
//        // print object data
//        System.out.println(emp1);
//        System.out.println("=================");
        // create JSON String from Object
        NodeTree tree = createTree();
        String jsonEmp = gson.toJson(tree);
        System.out.print(jsonEmp);
        DirFile.storeString2File(jsonEmp, "file/jsontest/tree1.json");
        DirFile.storeString2File(jsonEmp, "D:\\Program Files\\Apache Software Foundation\\Tomcat 7.0\\webapps\\d3\\knTree_z\\data_6_01\\tree1.json");
        /**
         * jsonpath解析json
         */
//        System.out.println(JsonPath.read(jsonEmp, "$.empID"));
 
    }
 
    public static NodeTree createTree() {
 
    	NodeTree tree = new NodeTree();
    	tree.setTotalbranchlevel(2);
    	tree.setTotalbranchnum(10);
    	tree.setBranchnum(8);
    	tree.setTerm_id(52);
    	tree.setName("Binary_tree");
    	tree.setFacet_id(0);
    	tree.setTotalleafnum(107);
 
        List<Sortedbranchlist> branches = new ArrayList<Sortedbranchlist>();// 分支信息总结对象
        int[] leafnum = {38, 32, 9, 8, 7, 4, 1, 1};
        int[] facetid = {7, 2, 6, 3, 1, 5, 8, 9};
        System.out.println("hello");
        for(int i = 0; i < 8; i++){// 8为分支数目
//        	System.out.println("hello" + i);
        	Sortedbranchlist branch1 = new Sortedbranchlist();
            branch1.setFacet_id(facetid[i]);
            branch1.setTotalleafnum(3);
//            branch1.setTotalleafnum(leafnum[i]);
            branches.add(branch1);
        }
        tree.setNodeBranchSums(branches);
        
        List<NodeBranch> branch = new ArrayList<NodeBranch>();// 8个分支
        int branchlevel = 0;
        int branchnum = branchlevel;
        String[] facetname = {"method", "definition", "application", "operation", 
        		"type", "implementation", "relevant", "feature"};
        int totalbranchnum = branchlevel;
        String type = "branch";
        
        List<ArrayList<NodeLeaf>> leaf = new ArrayList<ArrayList<NodeLeaf>>();// 8个分支中每个分支对应的叶子
        for(int i = 0; i < 8; i++){// 8为分支数目
        	ArrayList<NodeLeaf> lf = new ArrayList<NodeLeaf>();
        	for(int j = 0; j < 3; j++){// 添加每个分支下面的叶子，3为叶子数目
        		NodeLeaf l = new NodeLeaf();
            	l.setUrl("http://www.quora.com/How-do-I-uniformly-sample-a-node-from-a-binary-tree?");
            	l.setContent("How do I uniformly sample a node from a binary tree?\nExpanded information");
            	l.setFragment_id("Binary+tree10");
            	l.setLeaf("leaf");
            	l.setName("How do I unif...");
            	lf.add(l);
        	}
        	leaf.add(lf);
        }
        for(int i = 0; i < 8; i++){// 8为分支数目
        	NodeBranch branch1 = new NodeBranch();
            branch1.setTotalbranchlevel(branchlevel);
            branch1.setBranchnum(branchnum);
            branch1.setFacet_name(facetname[i]);
            branch1.setTotalbranchnum(totalbranchnum);
            branch1.setFacet_id(facetid[i]);
            branch1.setType(type);
            branch1.setNodeLeaf(leaf.get(i));
            branch1.setTotalleafnum(3);
//            branch1.setTotalleafnum(leafnum[i]);
            branch.add(branch1);
        }
        tree.setNodeBranch(branch);
        
        return tree;
    }
}