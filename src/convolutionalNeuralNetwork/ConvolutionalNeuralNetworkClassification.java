package convolutionalNeuralNetwork;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Random;

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
	
	private int actualNumber;
	private int guessNumber;
	
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
	
	public void trainingProcedure() throws IOException{
		generateLayers();
		LogRecord.logRecord("[Tip] Initialize Layers Success.",logPath);
		initPara();
		LogRecord.logRecord("[Tip] Initialize Weight Success.",logPath);
		for(int times = 0;times < learningTimes;times++){
			//For every times of training.
			for(int i = 1;i <= trainingSetSize;i++){
				NumberObject numberObj = new NumberObject(i,28,28,dataPath);
				setNowCase(numberObj);
				calculateOutput();
				guessNumberAndSaveAnswer();
				backPropagation();
				if((double)(i % 100) == 0 && i >= 100){
					System.out.println("["+ i + "] Runing......");
				}
				if((double)(i % 10000) == 0 && i >= 10000){
					saveParaToDisk();
					LogRecord.logRecord("[" + i + "] Saved weight to the disk. ",logPath);
				}
			}
		}
		LogRecord.logRecord("[End] Learning Procedure End ",logPath);
	}
	
	public void testingProcedure() throws FileNotFoundException, ClassNotFoundException, IOException {
		LogRecord.logRecord("[Begin] Testing procedure begin ",logPath);
		readParaFromDisk();
		LogRecord.logRecord("[Tip] Read weight from disk complete ",logPath);
		int correct = 0;
		for(int i = 60000;i < 60001 + testSetSize;i++){
			NumberObject numberObj = new NumberObject(i,28,28,dataPath);
			setNowCase(numberObj);
			calculateOutput();
			guessNumberAndSaveAnswer();
			if(actualNumber == guessNumber){
				correct++;
			}
			LogRecord.logRecord("[Test " + i + "] Guess Result:" + guessNumber + " Actual Result：" + actualNumber,logPath);
		}
		LogRecord.logRecord("[Tip] Testing procedure complete",logPath);
		LogRecord.logRecord("[End] Correct rate：" + correct + " / " + testSetSize,logPath);
	}
	
	public void runNetwork() throws IOException, ClassNotFoundException{
		LogRecord.logRecord("[Begin] Real-Run begin ",logPath);
		readParaFromDisk();
		for(int i = 1;i < runSize;i++){
			NumberObject numberObj = new NumberObject(i,28,28,dataPath);
			setNowCase(numberObj);
			calculateOutput();
			guessNumberAndSaveAnswer();
		}
		LogRecord.logRecord("[Tip] Run network end",logPath);
	}
 	
	private void initPara(){
		Random randomgen = new Random();
		//Initialize weight and bias between intput and C1
		double[][] ck1TempWeight = new double[5][5];
		double ck1TempBias;
		this.ck1 = new ConvolutionalKernel[6];
		for(int i = 0;i < 6;i++){
			for(int j = 0;j < 5;j++){
				for(int k = 0;k < 5;k++){
					ck1TempWeight[j][k] = (randomgen.nextDouble() - 0.5) * 2 * 0.2;
				}
			}
			ck1TempBias = (randomgen.nextDouble() - 0.5) * 2 * 0.2;
			ck1[i] = new ConvolutionalKernel(5,5,ck1TempWeight,ck1TempBias,"ck1"+i);
		}
		//Initialize weight and bias between C1 and S2
		double sk2Weight;
		double sk2Bias;
		this.sk2 = new SubsampleKernel[6];
		for(int i = 0;i < 6;i++){
			sk2Weight = (randomgen.nextDouble() - 0.5) * 2 * 0.2;
			sk2Bias = (randomgen.nextDouble() - 0.5) * 2 * 0.2;
			sk2[i] = new SubsampleKernel(sk2Weight,sk2Bias,"sk2" + i);
		}
		//Initialize weight and bias between S2 and C3
		double[][] ck3TempWeight = new double[5][5];
		double ck3TempBias;
		this.ck3 = new ConvolutionalKernel[16];
		for(int i = 0;i < 16;i++){
			for(int j = 0;j < 5;j++){
				for(int k = 0;k < 5;k++){
					ck3TempWeight[j][k] = (randomgen.nextDouble() - 0.5) * 2 * 0.2;
				}
			}
			ck3TempBias = (randomgen.nextDouble() - 0.5) * 2 * 0.2;
			ck3[i] = new ConvolutionalKernel(5,5,ck3TempWeight,ck3TempBias,"ck3"+i);
		}
		
		//Initialize weight and bias between C3 and S4
		double sk4Weight;
		double sk4Bias;
		this.sk4 = new SubsampleKernel[16];
		for(int i = 0;i < 16;i++){
			sk4Weight = (randomgen.nextDouble() - 0.5) * 2 * 0.2;
			sk4Bias = (randomgen.nextDouble() - 0.5) * 2 * 0.2;
			sk4[i] = new SubsampleKernel(sk4Weight,sk4Bias,"sk4" + i);
		}
		//Initialize weight and bias between S4 and C5
		double[][] ck5TempWeight = new double[5][5];
		double ck5TempBias;
		this.ck5 = new ConvolutionalKernel[120];
		for(int i = 0;i < 120;i++){
			for(int j = 0;j < 5;j++){
				for(int k = 0;k < 5;k++){
					ck5TempWeight[j][k] = (randomgen.nextDouble() - 0.5) * 2 * 0.2;
				}
			}
			ck5TempBias = (randomgen.nextDouble() - 0.5) * 2 * 0.2;
			ck5[i] = new ConvolutionalKernel(5,5,ck5TempWeight,ck5TempBias,"ck5"+i);
		}
		//Initialize weight and bias between C5 and F6
		double[] bp1TempWeight = new double[120];
		double bp1TempBias;
		this.bp1 = new IHFormulaParameter[84];
		for(int i = 0;i < 84;i++){
			for(int j = 0;j < 120;j++){
				bp1TempWeight[j] = (randomgen.nextDouble() - 0.5) * 2 / Math.sqrt(120);
			}
			bp1TempBias = randomgen.nextDouble() - 1.0d;
			bp1[i] = new IHFormulaParameter(120,bp1TempWeight,bp1TempBias,i);
		}
		//Initialize weight and bias between F6 and output
		double[] bp2TempWeight = new double[84];
		double bp2TempBias;
		this.bp2 = new HOFormulaParameter[10];
		for(int i = 0;i < 10;i++){
			for(int j = 0;j < 84;j++){
				bp2TempWeight[j] = (randomgen.nextDouble() - 0.5) * 2 / Math.sqrt(84);
			}
			bp2TempBias = (randomgen.nextDouble() - 0.5) * 2 * 0.2;
			bp2[i] = new HOFormulaParameter(84,bp2TempWeight,bp2TempBias,i);
 		}
		
	}
	
	private void generateLayers(){
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
	
	private void setNowCase(NumberObject nb){
		for(int i = 0;i < 28;i++){
			for(int j = 0;j < 28;j++){
				inputLayer[0].setNumber(i+2, j+2, nb.getValue(i, j));
			}
		}
		actualNumber = nb.getActualNumber();
	}
	
	private void calculateOutput(){
		//首先计算第一层:卷积层
		double temp = 0;
		for(int i = 0;i < 6;i++){
			for(int j = 0;j < 28;j++){
				for(int k = 0;k < 28;k++){
					int j2 = j + 2;
					int k2 = k + 2;
					temp = inputLayer[0].calculateConvolutionalPoint(j2, k2, ck1[i]);
					c1Layer[i].setNumber(j, k, temp);
				}
			}
		}
		//计算第二层：子采样层
		for(int i = 0;i < 6;i++){
			for(int j = 0;j < 14;j++){
				for(int k = 0;k < 14;k++){
					int j2 = j * 2;
					int k2 = k * 2;
					temp = c1Layer[i].calculateSubsamplePoint(j2, k2, sk2[i]);
					s2Layer[i].setNumber(j, k, temp);
				}
			}
		}
		//计算第三层：卷积层
		//此处策略需要修正
		for(int i = 0;i < 16;i++){
			for(int j = 0;j < 10;j++){
				for(int k = 0;k < 10;k++){
					int j2 = j + 2;
					int k2 = k + 2;
					temp = s2Layer[i].calculateConvolutionalPoint(j2, k2, ck3[i]);
					c3Layer[i].setNumber(j, k, temp);
				}
			}
		}
		//计算第四层：子采样层
		for(int i = 0;i < 16;i++){
			for(int j = 0;j < 5;j++){
				for(int k = 0;k < 5;k++){
					int j2 = j * 2;
					int k2 = k * 2;
					temp = c3Layer[i].calculateSubsamplePoint(j2, k2, sk4[i]);
					s4Layer[i].setNumber(j, k, temp);
				}
			}
		}
		//计算第五层：卷积层，同时也是bp网输入层
		//此处策略需要修正
		for(int i = 0;i < 120;i++){
			for(int j = 0;j < 1;j++){
				for(int k = 0;k < 1;k++){
					int j2 = j + 2;
					int k2 = k + 2;
					temp = s4Layer[i].calculateConvolutionalPoint(j2, k2, ck5[i]);
					c5Layer[i].setNumber(j, k, temp);
				}
			}
		}
		//计算第六层：bp网络Hidden层
		for(int i = 0;i < 84;i++){
			double tempHidden = 0;
			for(int j = 0;j < 120;j++){
				tempHidden += bp1[i].getWeight(j) * c5Layer[j].getNumber(0, 0);
			}
			tempHidden = Sigmoid.sigmoid(tempHidden + bp1[i].getBias());
			f6Layer[i].setNumber(0, 0, tempHidden); 
		}
		//计算第七层：output层
		for(int i = 0;i < 10;i++){
			double tempOutput = 0;
			for(int j = 0;j < 84;j++){
				tempOutput += bp2[i].getWeight(j) * f6Layer[j].getNumber(0, 0);
			}
			tempOutput = Sigmoid.sigmoid(tempOutput + bp2[i].getBias());
			outputLayer[i].setNumber(0, 0, tempOutput); 
		}
	}


	
	private void guessNumberAndSaveAnswer() throws IOException{
		double max = 0;
		for(int i = 0;i < outputLayer.length;i++){
			if(outputLayer[i].getNumber(0, 0) > max){
				max = outputLayer[i].getNumber(0, 0);
				guessNumber = i;
			}
		}
		File file = new File(resultPath);
		BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
		fw.append("" + guessNumber);
		fw.newLine();
		fw.flush();
		fw.close();
	}
	
	private void backPropagation(){
		
	}
	
	private void readParaFromDisk() throws FileNotFoundException, ClassNotFoundException, IOException{
		for(int i = 0;i < 6;i++){
			ck1[i].readFromDiskCK(weightSavePath);;
		}
		for(int i = 0; i < 6;i++){
			sk2[i].readFromDiskHO(weightSavePath);;
		}
		for(int i = 0;i < 16;i++){
			ck3[i].readFromDiskCK(weightSavePath);;
		}
		for(int i = 0;i < 16;i++){
			sk4[i].readFromDiskHO(weightSavePath);;
		}
		for(int i = 0;i < 120;i++){
			ck5[i].readFromDiskCK(weightSavePath);;
		}
		for(int i = 0;i < 84;i++){
			bp1[i].readFromDiskIH(weightSavePath);;
		}
		for(int i = 0;i < 10;i++){
			bp2[i].readFromDiskHO(weightSavePath);
		}
	}
	
	private void saveParaToDisk() throws FileNotFoundException, IOException{
		for(int i = 0;i < 6;i++){
			ck1[i].writeToDiskCK(weightSavePath);
		}
		for(int i = 0; i < 6;i++){
			sk2[i].writeToDiskSK(weightSavePath);
		}
		for(int i = 0;i < 16;i++){
			ck3[i].writeToDiskCK(weightSavePath);
		}
		for(int i = 0;i < 16;i++){
			sk4[i].writeToDiskSK(weightSavePath);
		}
		for(int i = 0;i < 120;i++){
			ck5[i].writeToDiskCK(weightSavePath);
		}
		for(int i = 0;i < 84;i++){
			bp1[i].writeToDiskIH(weightSavePath);
		}
		for(int i = 0;i < 10;i++){
			bp2[i].writeToDiskHO(weightSavePath);
		}
	}
}
