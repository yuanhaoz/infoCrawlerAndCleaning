package xjtu.sky.quora;

public class Main {
	/**
	 * Method: main Description: 主函数 爬取Quora网站相关主题的所有数据
	 */
	public static void main(String[] args) throws Exception {
		PageDownMore urlresult = new PageDownMore();
		//data_structures
		String[] data_structures = { "B-Trees", "Tries", "Linked-Lists",
				"Binary-Trees", "Strings-data-structure", "Skip-Lists",
				"Ontologies", "Quora-Topic-Ontology", "Dbpedia", "Freebase",
				"Hierarchy", "Trees-data-structures",
				"Graphs-computer-science", "Graph-Databases", "OrientDB",
				"Neo4j", "Gremlin", "Sparsity-Technologies",
				"Titan-graph-database", "Cassandra-database", "HBase",
				"Berkeley-DB", "Apache-Hadoopg", "Apache-Hive", "HDFS",
				"Apache-Hama-1", "Bulk-Synchronous-Parallel-Computing",
				"Pregel", "Qubole", "Apache-2-0-License",
				"Attributes-computer-sciences",
				"Extended-Attributes-Computer-Science", "Unstructured-Data",
				"Theory-of-Data-Structures", "Probabilistic-Data-Structures",
				"Bloom-Filters" };
		for (int i = 0; i < data_structures.length; i++) {
			urlresult.GetFirstPage(data_structures[i]);
			int[] num = urlresult.GetChildPages(data_structures[i]);
			urlresult.SaveQuestion2Excel(data_structures[i]);
			urlresult.Down2Excel(data_structures[i], num);
		}
		//data_mining
		String[] data_mining = { "Data-Mining-Startups", "Rapleaf",
				"Linked-Lists", "Captain-Dash", "Color-Labs-startup",
				"Color-for-Facebook-application",
				"Color-Sale-and-Shutdown-Rumors-October-2012",
				"Funding-of-Color-Labs", "Junyo", "Hotlist", "Anametrix",
				"Text-Analytics", "Sentiment-Analysis", "Tagging",
				"Semantic-Annotation", "People-Tagging",
				"Facebook-Photo-Tagging", "Collaborative-Tagging",
				"Semantic-Search", "Text-Processing",
				"Named-Entity-Recognition", "MAXQDA", "Data-Mining-Books",
				"Python-Data-Mining", "Educational-Data-Mining" };
		for (int i = 0; i < data_mining.length; i++) {
			urlresult.GetFirstPage(data_mining[i]);
			int[] num = urlresult.GetChildPages(data_mining[i]);
			urlresult.SaveQuestion2Excel(data_mining[i]);
			urlresult.Down2Excel(data_mining[i], num);
		}
        //computer_networking
		String[] computer_networking = { "Ubiquitous-Computing",
				"Cloud-Computing", "Fibre-to-the-Cabinet",
				"Self-Organizing-Networks", "Routing", "RJ-45",
				"Vision-Technology-Management-LLC",
				"Software-defined-Networking", "Distributed-Social-Networks",
				"The-Internet-2", "Wireless-Technology" };
		for (int i = 0; i < computer_networking.length; i++) {
			urlresult.GetFirstPage(computer_networking[i]);
			int[] num = urlresult.GetChildPages(computer_networking[i]);
			urlresult.SaveQuestion2Excel(computer_networking[i]);
			urlresult.Down2Excel(computer_networking[i], num);
		}
	}
}
