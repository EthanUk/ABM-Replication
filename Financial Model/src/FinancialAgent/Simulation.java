package FinancialAgent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Simulation 
{

	private double					PredictionTemp,
									PredictionMaxTemp,
									currentSentiment,
									totalAssets;
	
	private int						predictionTimeScale,
									sentimentTimeScale,
									runId;
	
	private boolean					terminateDays;
	
	private Date					modelStartDate = null,
									modelEndDate = null,
									predictionStartDate = null,
									predictionEndDate = null,
									actualDate = null;
	
	private MapDataSet				mapDataSet;
	private SQLConnect				sqlConnect;
	
	public Simulation(int sentimentTimeScale, int predictionTimeScale, double PredictionMaxTemp, double totalAssets,SQLConnect sqlConnect)
	{
		this.mapDataSet = new MapDataSet();
		this.mapDataSet.loadDataSet("DJIADataSet");
		this.totalAssets = totalAssets;
		this.sentimentTimeScale = sentimentTimeScale;
		this.predictionTimeScale = predictionTimeScale;
		long 	lStartTime = new Date().getTime();
		this.runId = (int) (lStartTime / 10);
		this.PredictionMaxTemp = PredictionMaxTemp;
		this.PredictionTemp = 0;
		this.sqlConnect = sqlConnect;
	}
	
	
	public double startSimulation (boolean trainingRound, ArrayList<Double> closingPrices, Prediction startingPoint)
	{
		
		if (trainingRound)
		{
			modelStartDate = Helper.setDate(1994, 01, 03);
			modelEndDate = Helper.setDate(2003, 12, 15);
		}
		else
		{
			modelStartDate = Helper.setDate(2003, 12, 18);
			modelEndDate = Helper.setDate(2006, 10, 19);
		}

		//=========Resets
		PredictionTemp = 0;
		terminateDays = true;
		predictionStartDate = null;

		//=========DataSet Up
		DJIAActualDataSet actualTimeSeriesDataSet;
		List<DJIAActualDataSet> PredictionDataSet = new ArrayList<DJIAActualDataSet>();
		
		Prediction currentSolution = startingPoint;
		Prediction bestSolution = new Prediction(currentSolution.getfinancialAgentArray());
		
		int totalPredicitionDay = 0;
		
		double		currentSentiment = 0,
					totalInvestedAssets,
					totalMape = 0.0,
					averageMape = 1000000000000.0,
					startPrice;
		
		double[] 	predictedTimeSeriesArray,
					currentDayTimeSeriesArray,
					predictedTimeSeriesSentiment = new double[sentimentTimeScale],
					actualTimeSeriesSentiment = new double[sentimentTimeScale],
					actualTempCopy = new double[sentimentTimeScale],
					predictedTempCopy = new double[sentimentTimeScale];
		//==================
		
		
		while (PredictionTemp < PredictionMaxTemp) {
			
			while(terminateDays) {	
				if (predictionStartDate != null)
				{
					predictionStartDate = Helper.alterDate(predictionStartDate, predictionTimeScale); 					//The start date of the previous prediction
					predictionEndDate = Helper.alterDate(predictionStartDate, (predictionTimeScale -1)); 				//The end date of the previous prediction
					actualDate = Helper.alterDate(predictionStartDate, predictionTimeScale); 							//The date of the current day

					predictedTimeSeriesArray = mapDataSet.getClosingPrices(predictionStartDate, predictionTimeScale);	//Retrieve previous days Data series from DB 
					actualTimeSeriesDataSet = PredictionDataSet.get(PredictionDataSet.size() - 1);                  	//Retrieve previous days predicted data	
					currentDayTimeSeriesArray = mapDataSet.getClosingPrices(actualDate, predictionTimeScale);			//Retrieve the actual data from todays market
					
					Date compare = Helper.blankTime(modelEndDate);
					Date compare2 = Helper.blankTime(predictionStartDate);

					
					if (Helper.compareDate(compare, compare2))
						{terminateDays = false;}
					
					if(predictedTimeSeriesArray != null && currentDayTimeSeriesArray != null) 
					{
						if (sentimentTimeScale > 1)
						{
							for (int j = sentimentTimeScale -1; j > 0; j--)
							{
								predictedTempCopy[j-1] = predictedTimeSeriesSentiment[j];
								actualTempCopy[j-1] = actualTimeSeriesSentiment[j];
							}
							predictedTempCopy[sentimentTimeScale -1] = predictedTimeSeriesArray[0];
							actualTempCopy[sentimentTimeScale -1] = actualTimeSeriesDataSet.getClosingPrice();
							for(int k = 0; k < sentimentTimeScale; k++)
							{
								predictedTimeSeriesSentiment[k] = predictedTempCopy[k];
								actualTimeSeriesSentiment[k] = actualTempCopy[k];
							}
						}
						else
						{
							predictedTimeSeriesSentiment[sentimentTimeScale -1] = predictedTimeSeriesArray[0];
							actualTimeSeriesSentiment[sentimentTimeScale -1] = actualTimeSeriesDataSet.getClosingPrice();
						}
	
						currentSentiment = Helper.calcSentiment(predictedTimeSeriesSentiment, actualTimeSeriesSentiment);

						double	currentSolutionPrice = currentSolution.calcFinancialAgentsInvestedAssets(currentSentiment),
								currentDayClosePrice = Helper.calcMAVG(currentDayTimeSeriesArray), 
								currentSolutionPredictedPrice = currentSolutionPrice / totalAssets,
								currentMAPE = Helper.getPositiveMAPE(currentSolutionPredictedPrice,currentDayClosePrice), 
								percentageMape,
								predictedPrice; 	
								
						//=======================Training Area=======================//
						if(trainingRound)
						{
							Prediction newSolution = new Prediction(currentSolution.getfinancialAgentArray()); 
							int		randomAgentIndex = (int) (newSolution.getAgentArraySize() * Math.random()); //Get the index of a random agent
							double	newRiskReward = Math.random();
							newSolution.setRandomAgentRiskReward(randomAgentIndex, newRiskReward);
							
							double	newSolutionPrice = currentSolution.calcFinancialAgentsInvestedAssets(currentSentiment), 
									newSolutionPredictionPrice = newSolutionPrice /totalAssets, 
									newSolutionMape = Helper.getPositiveMAPE(newSolutionPredictionPrice, currentDayClosePrice);
							
							if (Helper.acceptanceProbability(currentMAPE,newSolutionMape, (PredictionTemp/PredictionMaxTemp)) > Math.random())
							{
								currentSolution = new Prediction(newSolution.getfinancialAgentArray());
								predictedPrice = newSolutionPredictionPrice;
								percentageMape = newSolutionMape * 100;
							}
							else
							{
								predictedPrice = currentSolutionPredictedPrice;
								percentageMape = currentMAPE * 100;
							}
						}
						
						//========================Testing Area========================//	
						else
						{
							predictedPrice = currentSolutionPredictedPrice;
							percentageMape = currentMAPE * 100; 
						}

						//========================Finalising Area========================//
						Date savePredictionDate;
						savePredictionDate = Helper.alterDate(predictionStartDate, predictionTimeScale);
						DJIAActualDataSet dataSet = new DJIAActualDataSet(savePredictionDate.toString(),predictedPrice,predictionTimeScale,runId); 
						closingPrices.add(predictedPrice);
						
						PredictionDataSet.add(dataSet);
						totalMape += percentageMape;
						totalPredicitionDay++;
						if(predictionTimeScale == 5) predictionStartDate = Helper.alterDate(predictionStartDate, 2);
					}
					else 
					{
						if(predictionTimeScale == 5) predictionStartDate = Helper.alterDate(predictionStartDate, 2); 
					}
				}
				else if (predictionStartDate == null)
				{
					//Inject Assets to inflate the starting price
					ArrayList<FinancialAgent> currentSolutionFinancialAgentArray;//Array of financial Agents 
					currentSolutionFinancialAgentArray = currentSolution.getfinancialAgentArray();
					startPrice = Helper.calcMAVG(mapDataSet.getClosingPrices(modelStartDate, predictionTimeScale));

					for(FinancialAgent item : currentSolutionFinancialAgentArray)
					{
						long agentGivenAgent = item.getTotalAssets();
						agentGivenAgent = (long) (agentGivenAgent * startPrice);
						item.setinvestedAssets(agentGivenAgent);
					}
					
					for (int j = 0; j < sentimentTimeScale; j++ )
					{
						predictedTimeSeriesSentiment[j] = startPrice;
						actualTimeSeriesSentiment[j] = startPrice;
					}
					
					predictionStartDate = modelStartDate;
					DJIAActualDataSet dataSet = new DJIAActualDataSet(predictionStartDate.toString(),startPrice,predictionTimeScale,runId); 
					PredictionDataSet.add(dataSet); //Save the First data "prediction"
					predictionStartDate = Helper.alterDate(modelStartDate, ((-1) * predictionTimeScale));
				}
			}//End of For Loop for the chosen number of prediction days
			
			//==========================End of training loop==========================
			if (trainingRound){
				PredictionTemp++; //When the training is ongoing gradually cool the system
				PredictionDataSet = new ArrayList<DJIAActualDataSet>();
				double newAverageMape = totalMape / totalPredicitionDay;
				
				if(newAverageMape < averageMape) {
					averageMape = newAverageMape;
					bestSolution = new Prediction(currentSolution.getfinancialAgentArray());
				}
				totalMape = 0;
				totalPredicitionDay = 0;
			}
			else{
				double newAverageMape = totalMape / totalPredicitionDay;
				averageMape = newAverageMape;
				PredictionTemp = PredictionMaxTemp; //If its not training just end after a single run
			}
			
		}//End of Training Loop
		
		ArrayList<FinancialAgent> bestSolutionDataSet = bestSolution.getfinancialAgentArray();
		if (trainingRound)
		{
			sqlConnect.saveAgentRiskRewards(runId,bestSolutionDataSet);	
			return runId;
		}

		return averageMape;
	}
	
	
}//End of Class

