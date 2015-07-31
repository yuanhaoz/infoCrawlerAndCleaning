package xjtu.sky.quora;

public class Test {
	/**
	 * Method: main Description: 主函数 爬取Quora网站相关主题的所有数据
	 */
	public static void main(String[] args) throws Exception {
		PageDownMore urlresult = new PageDownMore();
		//data_structures
		String[] data_structures = { "B-Trees", "Binary-Trees" };
		for (int i = 0; i < data_structures.length; i++) {
			urlresult.GetFirstPage(data_structures[i]);
			int[] num = urlresult.GetChildPages(data_structures[i]);
			urlresult.SaveQuestion2Excel(data_structures[i]);
			urlresult.Down2Excel(data_structures[i], num);
		}
	}
}
