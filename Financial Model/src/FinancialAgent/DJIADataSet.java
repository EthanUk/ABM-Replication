package FinancialAgent;
import java.util.Date;

public class DJIADataSet {
	private int Id;
	private Date date;
	private double closePrice;
	private int missing; 
	
	public DJIADataSet() {}
	
	public DJIADataSet(int Id, Date date, double closePrice, int missing)
	{
		this.Id = Id;
		this.date = date;
		this.closePrice = closePrice;
		this.missing = missing;
	}
	
	public int getId()
	{
		return Id;
	}
	
	public void setId(int Id)
	{
		this.Id = Id;
	}
	
	public Date getDate()
	{
		return date;
	}
	
	public void setDate(Date date)
	{
		this.date = date;
	}
	
	public double getClosePrice()
	{
		return closePrice;
	}
	
	public void setClosePrice(double closePrice)
	{
		this.closePrice = closePrice;
	}
	
	public int getMissing()
	{
		return missing;
	}
	
	public void setMissing(int missing)
	{
		this.missing = missing;
	}
	
	
}
