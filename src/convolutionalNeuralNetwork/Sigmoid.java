package convolutionalNeuralNetwork;

public class Sigmoid {
	public static double sigmoid(double input){
		return (1 / (1 + Math.exp(-input)));
	}
}
