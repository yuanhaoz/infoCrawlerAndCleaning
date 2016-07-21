/**
 * CNN实验1：不使用wiki百科作为基准，直接对标注好的QA文本进行词向量生成，并进行文本分类。（weka实验作为baseline）
 * txt1Tokenizer：QorA指问题和回答分开分析
 * txt2Tokenizer：QA指一个问题和一个回答作为一个整体分析
 * txt3Tokenizer：QAS指一个问题和它下面的所有回答作为一个整体分析
 * 
 * 本科毕设实验：基于CQA网站的upvote等社区用户之间的交互特征，进行质量分类，实验结果如本科毕设总结所示。
 * 1.在本科的数据上进行CNN分类
 * 2.加上wiki数据，采用弱监督进行CNN分类
 * 

1.file0_source：《数据结构》18个主题的标注数据（源数据）
2.file1_predeal：对file0_source中文件进行去除停用词、标点、空行等操作
3.file2_wordtovec：对file1_predeal中的文件处理得到对应的词向量（虚拟机）
4.file3_postdeal_step1：对file2_wordtovec中文件去除第一个词和空行的后期处理
5.file3_postdeal_step2：对file3_postdeal_step1中文件去除最后一行数据不全的行
6.file4_matrixdeal_step1 ：将file3_postdeal_step2中所有的词向量文件合并到一个文件夹中
7.file4_matrixdeal_step1All(Add1)：将file4_matrixdeal_step1中文件进行“加1”拓展
8.file4_matrixdeal_step1All(tile)：将file4_matrixdeal_step1中文件进行“平铺”拓展
9.file4_matrixdeal_step2(add)：将file3_postdeal_step2中文件进行“加1”拓展
10.file4_matrixdeal_step2(tile)：将file3_postdeal_step2中文件进行“平铺”拓展
11.file5_data_step1(tile)：将file4_matrixdeal_step2(tile)中文件的矩阵处理成一行
12.file5_data_step2(tile)：将file5_data_step1(tile)中所有一行的文件合并到一个文件中
13.file5_tag_step1(tile)：将file4_matrixdeal_step2(tile)中文件生成对应标签（0-9）
14.file5_tag_step2(tile)：将file5_tag_step1(tile)中所有一行的标签文件合并到一个文件中
15.file6_dataCNN(tile)：将file5_data_step2(tile)和file5_tag_step2(tile)所有数据和标签文件放到一起

 */
/**
 * @author yuanhao
 *
 */
package exp1;