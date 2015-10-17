package convolutionalNeuralNetwork;

public class Maps {
	private int width;
	private int height;
	private double[][] matrix;
	
	public Maps(int height,int width){
		this.width = width;
		this.height = height;
		matrix = new double[height][width];
		for(int i = 0;i < height;i++){
			for(int j = 0;j < width;j++){
				this.matrix[i][j] = 0;
			}
		}
	}
	
	public Maps(int width,int height,double[][] matrix){
		this.width = width;
		this.height = height;
		matrix = new double[height][width];
		for(int i = 0;i < height;i++){
			for(int j = 0;j < width;j++){
				this.matrix[i][j] = matrix[i][j];
			}
		}
	}
	
	public double calculateConvolutionalPoint(int row,int column,ConvolutionalKernel ck){
		double temp = 0;
		for(int i = 0;i < 5;i++){
			for(int j = 0;j < 5;j++){
				temp += matrix[row-2+i][column-2+j] * ck.getWeight(i, j);
			}
		}
		temp += temp + ck.getBias();
		return temp;
	}
	
	public double calculateSubsamplePoint(int row,int column,SubsampleKernel sk){
		double temp = 0;
		temp = (matrix[row][column] + matrix[row][column+1] + matrix[row+1][column] + matrix[row+1][column+1])/4;
		temp  = temp * sk.getBeta() + sk.getBias();
		return temp;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public double getNumber(int i,int j){
		return matrix[i][j];
	}
	
	public void setNumber(int i,int j,double number){
		this.matrix[i][j] = number;
	}
}
