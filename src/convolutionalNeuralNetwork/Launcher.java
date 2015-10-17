package convolutionalNeuralNetwork;

import java.io.IOException;

public class Launcher {
	public static void main(String[] args){
		ConvolutionalNeuralNetworkClassification cnn = new ConvolutionalNeuralNetworkClassification();
		try {
			cnn.trainingProcedure();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
