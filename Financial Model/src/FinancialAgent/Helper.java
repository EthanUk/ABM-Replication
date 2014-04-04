package FinancialAgent;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import RandomNumbers.Ranlux;
import RandomNumbers.Ranmar;

public class Helper {
	
	private static double[] rngArray;
	private static int counter;
	private static boolean flag = true;
	
	public static void initilizeRNG(int maxSize, String type)
	{
		rngArray = new double[maxSize];
		Date date = new Date();
		System.out.println("Date Used:" + date);
		
		switch (type)
		{
			case "Ranmar":
				Ranmar rngmar = new Ranmar(date);
				rngmar.raw(rngArray,maxSize);
				break;
				
			case "Ranlux":
				Ranlux rnglux = new Ranlux(date);
				rnglux.raw(rngArray,maxSize);
				break;
				
			case "mathrand":
				flag = false;
				break;
				
			default: flag = false;
				break;
				
				
		}
		
		counter = 0;
	}
	
	public static double getNextRN(){
		
		if (flag)  //returns the next RNG from the list if there is a RNG list
		{
			try{
				double nextRN = rngArray[counter];
				counter++;
				return nextRN;
			}
			catch(Exception e)
			{
				System.out.println("No Random Number found!! Has RNG been initilized");
				throw e;
			}
		}
		else //otherwise just return a java RN
		{
			return Math.random();
		}
	}
	
	
	public static double calcMAVG(double[] timeSeriesArray)
	{
		int N = 1; //The number of days in the moving average
		double totalPrice = 0; //The sum price being averaged for all days
		
		for (int i = 0; i < timeSeriesArray.length; i++){
			 totalPrice +=  timeSeriesArray[i];
		}

		N = timeSeriesArray.length;
		return totalPrice/N;
	}
	
	public static Date setDate(int modelStartYear, int modelStartMonth ,int modelStartDay)
	{
		Calendar myCal = Calendar.getInstance();
		myCal.set(Calendar.YEAR, modelStartYear);
		myCal.set(Calendar.MONTH, (modelStartMonth - 1));
		myCal.set(Calendar.DAY_OF_MONTH, modelStartDay);
		return myCal.getTime();
	}
	
	public static Date alterDate(Date date, int timeScale)
	{
		Calendar myCal = Calendar.getInstance();
		myCal.setTime(date);
		myCal.add(Calendar.DATE, (timeScale));  // number of days to add
		return myCal.getTime();
	}
	
	public static Date blankTime(Date date)
	{
		Calendar myCal = Calendar.getInstance();
		myCal.setTime(date);
		myCal.set(Calendar.HOUR_OF_DAY, 0);
		myCal.set(Calendar.MINUTE, 0);
		myCal.set(Calendar.SECOND, 0);
		return myCal.getTime();
	}
	
	public static boolean compareDate(Date dateOne, Date dateTwo)
	{
		if ((dateOne.getDay() == dateTwo.getDay())&&(dateOne.getMonth() == dateTwo.getMonth())&&(dateOne.getYear() == dateTwo.getYear())) return true;
		return false;
	}
	

	public static double getPositiveMAPE(double currentSolutionPredictedPrice, double currentDayClosePrice) {
		double MAPE = 0.0;
		
		MAPE = (currentDayClosePrice-currentSolutionPredictedPrice)/currentDayClosePrice;
		if (MAPE < 0)
		{
			MAPE = MAPE * (-1);
		}
		
		return MAPE;
		
	}
	
	
	public static double acceptanceProbability(double currentMAPE,double newSolutionMape,double predictionTemp) {
		
		//If the new solutions produces a close value always accept it
		if (newSolutionMape < currentMAPE){
			return 1.0;
		}
		return Math.exp((currentMAPE-newSolutionMape)/predictionTemp); //otherwise calculate the chance to be passed back
	}
	
	
	
	public static double calcSentiment(double[] predictedTimeSeriesArray, double[] actualTimeSeries)
	{
		double 	predictedMAVG = 0, 
				actualMAVG = 0,
				highSentiment = 0.65, 
				lowSentiment = 0.30;
		
		predictedMAVG = Helper.calcMAVG(predictedTimeSeriesArray); 
		actualMAVG = Helper.calcMAVG(actualTimeSeries);
		
		if (actualMAVG < predictedMAVG) //bullish market doing better than expected return a positive sentiment
		{
			return highSentiment;
		}
		else
		{
			return lowSentiment;
		}
	}
	
	
	
}
