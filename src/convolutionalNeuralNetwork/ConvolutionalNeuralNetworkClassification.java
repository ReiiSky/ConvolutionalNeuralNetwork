package convolutionalNeuralNetwork;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ConvolutionalNeuralNetworkClassification {
	
	private Maps[] inputLayer;
	private Maps[] c1Layer;
	private Maps[] s2Layer;
	private Maps[] c3Layer;
	private Maps[] s4Layer;
	private Maps[] c5Layer;
	private Maps[] f6Layer;
	private Maps[] outputLayer;
	
	private ConvolutionalKernel[] ck1;
	private SubsampleKernel[] sk2;
	private ConvolutionalKernel[] ck3;
	private SubsampleKernel[] sk4;
	private ConvolutionalKernel[] ck5;
	private IHFormulaParameter[] bp1;
	private HOFormulaParameter[] bp2;
	
	private int learningTimes;
	private int trainingSetSize;
	private int testSetSize;
	private double learningRate;
	private int runSize;
	
	private int guessNumber;
	private int correctCount;
	
	private String weightSavePath = "d:\\PJ1\\part2\\data\\";
	private String dataPath = "D:\\dataset\\";
	private String resultPath = "d:\\Test_Set_Result.txt";
	private String logPath = "d:\\Log_Record.txt";
	
	public ConvolutionalNeuralNetworkClassification(){
		this.learningTimes = 1;
		this.trainingSetSize = 60000;
		this.testSetSize = 10000;
		this.runSize = 799;
		this.learningRate = 0.1;	
		initPara();
		generateLayers();
	}	
 	
	public void initPara(){
		for(int i = 0;i < 5;i++){
			for(int j = 0;j < 5;j++){
				//此处对对第一层与第二层之间的卷积核进行初始化
			}
		}
		//此处对input与c1之间卷积核bias进行初始化
		//此处对ci与s2之间的下采样参数进行初始化
		for(int i = 0;i < 5;i++){
			for(int j = 0;j < 5;j++){
				//此处对对s2与c3的卷积核进行初始化
			}
		}
		//此处对s2与c3之间卷积核bias进行初始化
		//此处对c3与s4之间的下采样参数进行初始化
		for(int i = 0;i < 5;i++){
			for(int j = 0;j < 5;j++){
				//此处对对s4与c5的卷积核进行初始化
			}
		}
		//此处对bp网c5到f6参数进行初始化
		//此处对f6到output参数进行初始化
	}
	
	public void generateLayers(){
		inputLayer = new Maps[1];
		for(int i = 0;i < 1;i++){
			inputLayer[i] = new Maps(32,32);
		}
		c1Layer = new Maps[6];
		for(int i = 0;i < 6;i++){
			c1Layer[i] = new Maps(28,28);
		}
		s2Layer = new Maps[6];
		for(int i = 0;i < 6;i++){
			s2Layer[i] = new Maps(14,14);
		}
		c3Layer = new Maps[16];
		for(int i = 0;i < 16;i++){
			c3Layer[i] = new Maps(10,10);
		}
		s4Layer = new Maps[16];
		for(int i = 0;i < 16;i++){
			s4Layer[i] = new Maps(5,5);
		}
		c5Layer = new Maps[120];
		for(int i = 0;i < 120;i++){
			c5Layer[i] = new Maps(1,1);
		}
		f6Layer = new Maps[84];
		for(int i = 0;i < 84;i++){
			f6Layer[i] = new Maps(1,1);
		}
		outputLayer= new Maps[10];
		for(int i = 0;i < 10;i++){
			outputLayer[i] = new Maps(1,1);
		}
	}
	
	private void readParaFromDisk() throws FileNotFoundException, ClassNotFoundException, IOException{
		
	}
	
	private void saveParaToDisk() throws FileNotFoundException, IOException{

	}
}
