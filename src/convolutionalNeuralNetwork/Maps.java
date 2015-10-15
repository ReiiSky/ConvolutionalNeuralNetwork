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
