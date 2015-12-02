package lpa;
import java.util.Arrays;

public class LPA {
	static int Vertex=34;//�ڵ���
	static int[] Lable_t=new int[Vertex];//��Ÿ�����ı�ǩ,tʱ��
	static int[] Lable_t_1=new int[Vertex];//��Ÿ�����ı�ǩ,t-1ʱ��
	static int[] Importance_sorting=new int[Vertex];//��Ÿ��������Ҫ��
	static int[] Vertex_neighbour_lable=new int[Vertex];//����ھӽڵ�ı�ǩ��
	static int[] Sum_degree=new int[Vertex];//����ھӽڵ�ı�ǩ��
	//static int[][] Degree_array=new int[Vertex][2];//��ʱû��
	static int [][] Adjmartrix=new int[Vertex][Vertex];//����ڽӾ���	
	/**********************
	 * ���Ź����ÿ��Ԫ�ؽ��б�ǩ��ֵ
	 * �ѹ����ڵ��ų�����
	 * *************************/
	public static void main(String[] args) {
		int Iteration=0; //��������
//		int Edge_graph[][]={{1,2},{1,4},{1,3},{2,3},{2,4},{3,4}};
//		int Edge_graph[][]={{1,2},{1,4},{2,3},{3,6},{4,5},{5,6}};
//		int Edge_graph[][]={{1,2},{1,4},{2,3},{3,6},{5,6},{4,5}};
//		int Edge_graph[][]={{1,2},{1,3},{2,3},{3,4},{4,6},{4,5},{6,5}};
//		int Edge_graph[][]={{1,2},{1,3},{2,3},{3,4},{4,5},{7,5},{6,5},{6,7}};
//		int Edge_graph[][]={{1,2},{1,3},{2,3},{4,5},{5,6}};
//		int Edge_graph[][]={{1,4},{1,5},{1,6},{2,3},{2,7},{3,4},{3,7},{4,6},{5,6},{5,8},{6,7},{6,16},{7,14},{8,9},{8,11},{8,12},{9,10},{10,11},{10,13},{11,12},{11,16},{12,13},{14,15},{14,18},{15,17},{15,19},{15,20},{16,17},{16,20},{16,21},{18,19},{19,20},{19,21},};
		
		int Edge_graph[][]={
				{2,1},{3,1},{3,2},{4,1},{4,2},{4,3},{5,1},{6,1},
				{7,1},{7,5},{7,6},{8,1},{8,2},{8,3},{8,4},{9,1},
				{9,3},{10,3},{11,1},{11,5},{11,6},{12,1},{13,1},{13,4},
				{14,1},{14,2},{14,3},{14,4},{17,6},{17,7},{18,1},{18,2},
				{20,1},{20,2},{22,1},{22,2},{26,24},{26,25},{28,3},{28,24},
				{28,25},{29,3},{30,24},{30,27},{31,2},{31,9},{32,1},{32,29},
				{33,3},{33,9},{33,15},{33,16},{33,19},{33,21},{33,23},{33,24},
				{33,30},{33,31},{33,32},{34,9},{34,10},{34,14},{34,15},{34,16},
				{34,19},{34,20},{34,21},{34,23},{34,24},{34,27},{34,28},{34,29},
				{34,30},{34,31},{34,32},{34,33}
				};            //��������ֵ����ֲ�����   34���ڵ�   2������
		/**********************��ʼ��*************************/
		Initing(Edge_graph);	
		Ininting_Degree_Sorting(Edge_graph);//����Ҫ�Ƚ������������������
		//Sum_degree_vertex(Edge_graph);
		/*****************��ÿһ���ڵ㣬���������ھ���ĳ��ǩ�����ı�ǩֵ*****************/
		while(!Arrays.equals(Lable_t_1,Lable_t)){
			Iteration++;
			Lable_Spread();
			
//			��ʾ���������б�ǩ�ı仯���
			for(int i=0;i<Vertex;i++){
				System.out.print(Lable_t[i]+" ");
			}
			System.out.print("\n");
			for(int i=0;i<Vertex;i++){
				System.out.print(i+" ");
			}
			System.out.print("\n");
			System.out.println("Iteration:"+Iteration);
			System.out.print("\n");
		}
		
//		for(int i=0;i<Vertex;i++){
//			System.out.print(Lable_t[i]+" ");
//		}
//		System.out.print("\n");
//		for(int i=0;i<Vertex;i++){
//			System.out.print(i+" ");
//		}
//		System.out.print("\n");
//		System.out.println("Iteration:"+Iteration);
	}
	/**********************���ݳ�ʼ��*************************/
	public static void Initing(int edge[][]){//
//		System.out.println("length:" + edge.length);
		for(int i=0;i<edge.length;i++){//��ʼ���ڽӾ���
			Adjmartrix[edge[i][0]-1][edge[i][1]-1]=1;
			Adjmartrix[edge[i][1]-1][edge[i][0]-1]=1;
		}
		Arrays.fill(Sum_degree,0);
		for(int i=0;i<Vertex;i++){//��ÿ���ڵ���и�ֵ��ǩ����ʼ��Ϊ�±�ֵ
			Lable_t[i]=i;
			Importance_sorting[i]=0;
			Lable_t_1[i]=0;
			Vertex_neighbour_lable[i]=0;
		}
	}

	/**********************ѡ����²���*************************/
	public static void Lable_Spread(){
		for(int i=0;i<Vertex;i++){
			Lable_t_1[i]=Lable_t[i];	
		}
		Lable_Update();
		//System.out.println("Lable_Spread:");
	}
	/**********************ѡ���ǩ*************************/
	public static int Lable_Select(int v){
		int vertex_lable_select = 0;
		Lable_count(v);//ȷ���ھӽڵ��ǩ����
		int max=-1;
		for(int i=0;i<Vertex;i++){
			if(max<Vertex_neighbour_lable[Importance_sorting[i]]){//ѡ���ھӽڵ����ı�ǩ���£�����Ҫ�ȴӴ�С�����ȼ���
				max=Vertex_neighbour_lable[Importance_sorting[i]];
				vertex_lable_select=Importance_sorting[i];
			}
		}
		for(int i=0;i<Vertex;i++){//��ÿ���ڵ���и�ֵ��ǩ����ʼ��Ϊ�±�ֵ
			Vertex_neighbour_lable[i]=0;
		}
		//System.out.println("Lable_Select:");
		return vertex_lable_select;
	}
	/**********************��ǩ����*************************/
	public static void Lable_Update(){
		for(int i=0;i<Vertex;i++){
			if(!Is_isolated_vertex(Importance_sorting[i])){//�ж��Ƿ�Ϊ�����ڵ㣬��Ϊ��continue,����else
				continue;
			}
			else{
				int temp=Importance_sorting[i];
				Lable_t[temp]=Lable_Select(temp);//ѡ��ÿ���ڵ�����±�ǩ�����ȴӴ�С����
			}		
		}
		//System.out.println("Lable_Update:");
	}
	/**********************���ھӽڵ��ǩ�������г�ʼ��,����ͬ������*************************/
	public static void Lable_count(int v){//
		for(int i=0;i<Adjmartrix.length;i++){
			if(Adjmartrix[v][i]>0){//���ھӽڵ��ǩ�������г�ʼ��
				Vertex_neighbour_lable[Lable_t_1[i]]=Vertex_neighbour_lable[Lable_t_1[i]]+1;//
			}
		}
		//System.out.println("Lable_count:");
	}
	/**********************����Ҫ�Ƚ�������*************************/
	public static void Ininting_Degree_Sorting(int Edge[][]){//��ÿ���ڵ���и�ֵ��ǩ����ʼ��Ϊ�±�ֵ
		int[][] Degree_array_temp=new int[Vertex][2];
		int max=-1;
		int temp=0;
		int[] visited=new int[Vertex];//���ڱ�־�Ƿ񱻷��ʹ�
		for(int i=0;i<Vertex;i++){
			Degree_array_temp[i][0]=0;
			Degree_array_temp[i][1]=i;
			visited[i]=0;
		}
		for(int i=0;i<Edge.length;i++){
			Degree_array_temp[Edge[i][0]-1][0]=Degree_array_temp[Edge[i][0]-1][0]+1;
			Degree_array_temp[Edge[i][1]-1][0]=Degree_array_temp[Edge[i][1]-1][0]+1;
		}
		for(int i=0;i<Degree_array_temp.length;i++){
			for(int j=0;j<Degree_array_temp.length;j++){
				if(max<Degree_array_temp[j][0]&&visited[j]==0){
					max=Degree_array_temp[j][0];
					temp=j;
				}	
			}//ѭ���Ƚϵó������Ľڵ��ǩ��Ȼ������max�������õ��־Ϊ1���´β��Ƚϣ�������Ϊ2,4
			max=-1;
			visited[temp]=1;
			Importance_sorting[i]=temp;
		}
	}
	/**********************ͳ��ÿ�����ȵĸ���*************************/
	public static void Sum_degree_vertex(int [][]edge){
		for(int i=0;i<edge.length;i++){
			Sum_degree[edge[i][0]-1]=Sum_degree[edge[i][0]-1]+1;
			Sum_degree[edge[i][1]-1]=Sum_degree[edge[i][1]-1]+1;
		}
		//System.out.println("Sum_degree_vertex:");
	}
	/**********************�ж��Ƿ��ǹ����ڵ�*************************/
	public static boolean Is_isolated_vertex(int v){
		int temp=0;
		for(int i=0;i<Adjmartrix.length;i++){
			if(Adjmartrix[v][i]>0){//���ھӽڵ��ǩ�������г�ʼ��
				temp++;
			}
		}
		if(temp>0){
//			System.out.println("Not's_isolated_vertex:" + v);
			return true;
		}
		else{
//			System.out.println("Is_isolated_vertex:" + v);
			return false;
		}
	}
}
