package FinancialAgent;
import java.util.Date;

public class DJIAActualDataSet {
	
	private String startDate;
	private double closingPrice;
	private int predictionSize;
	private int runId;
	
	public DJIAActualDataSet(){}
	
	public DJIAActualDataSet(String predictionStartDate, double predictedPrice, int predictionTimeScale, int runId)
	{
		this.startDate = predictionStartDate;
		this.closingPrice = predictedPrice;
		this.predictionSize = predictionTimeScale;
		this.runId = runId;
	}
	
	public void setStartDate(String startDate){
		this.startDate = startDate;
	}
	
	public String getStartDate(){
		return startDate;
	}
	
	public void setClosingPrice(double closingPrice){
		this.closingPrice = closingPrice;
	}
	
	public double getClosingPrice(){
		return closingPrice;
	}
	
	public void setPredictionSize(int predictionSize){
		this.predictionSize = predictionSize;
	}
	
	public int getpredictionSize(){
		return predictionSize;
	}
	
	public void setrunId(int runId){
		this.runId = runId;
	}
	
	public int getrunId(){
		return runId;
	}
	
	
	

}
