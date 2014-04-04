package FinancialAgent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
public class FinancialAgentModelMain {

		public static void main(String[] args) {

			String inputVars;
			System.out.println("Please Enter in the following order RNG function, Sentiment Time Scale ,Forecast Time Scale and Training Time");
			System.out.println("for example: mathrand,1,7,400");
		       Scanner scanIn = new Scanner(System.in);
		       inputVars = scanIn.nextLine();
		       scanIn.close();
		     String[] _inputVars = inputVars.split(",");
			
			long 	lStartTime = new Date().getTime();
  			SQLConnect SQLConnect = new SQLConnect();
  			Helper.initilizeRNG(200000000, _inputVars[0]);

			FinancialAgentManager financialAgentManager = new FinancialAgentManager();
			financialAgentManager.addAgent(30, 1);	//Create 30 Individual investor agents
			financialAgentManager.addAgent(20, 2);	//Create 20 Hedge Funds agents
			financialAgentManager.addAgent(49, 3);	//Create 49 Banks investor agents
			financialAgentManager.addAgent(1, 4); 	//Create a single Central Bank agent
			
			
				int 	_sentimentTimeScale = Integer.parseInt(_inputVars[1]),  //Changes the period used by the sentiment calc
						_predictionTimeScale = Integer.parseInt(_inputVars[2]), // The number of forecast days
						_maxAgents = financialAgentManager.getNumberOfAgent();
				
				double 	_PredictionMaxTemp = Integer.parseInt(_inputVars[3]), //Simulated Annealing Training Temperature					
						_totalAssets = financialAgentManager.getTotalAssets(),
						_runId = 0,
						_MAPE,
						_TotalMape = 0,
						_TotalMAPE = 0;
					
			Prediction currentSolution = new Prediction(_maxAgents);
			currentSolution.createStart(financialAgentManager);	
			ArrayList<Double> ClosingPricesTest = new ArrayList<Double>();
			ArrayList<Double> BestClosingPrices = new ArrayList<Double>();
			ArrayList<Double> _MAPES = new ArrayList<Double>();
			ArrayList closingPriceMap = new ArrayList();
			
			Simulation simulation = new Simulation(_sentimentTimeScale, _predictionTimeScale, _PredictionMaxTemp,_totalAssets ,SQLConnect);
			_runId = simulation.startSimulation(true, ClosingPricesTest, currentSolution);
			currentSolution.setAllRiskRewards((int) _runId );
			
			
			for (int i = 0; i < 100; i++)
			{
				ClosingPricesTest = new ArrayList<Double>();
				
				_MAPE = simulation.startSimulation(false, ClosingPricesTest, currentSolution);
				_MAPES.add(_MAPE);
				_TotalMape += _MAPE;
				_TotalMAPE =  _TotalMAPE + _MAPE;
				closingPriceMap.add(ClosingPricesTest);
				
			}
			
			double 	_averageMAPE = _TotalMape/100,
					_tempAverager = 10000000;
			int 	bestAverage = 1000;
			
			
			System.out.println("=========Average MAPES For Run=========");
			
			for (Double object: _MAPES)
			{
				System.out.println(object);
			}
			
			
			for (int j = 0; j < 100; j++)
			{
				double temp = _MAPES.get(j) - _averageMAPE;
				if (temp < 0)
					temp = temp * (-1);
				
				if (temp < _tempAverager)
				{
					_tempAverager = temp;
					bestAverage = j;
				}
			}
			
			
			System.out.println("=========Average Mape Closing Prices=========");
			ArrayList<Double> indivClosingPrice =  (ArrayList<Double>) closingPriceMap.get(bestAverage);
			
			for(Double object: indivClosingPrice)
			{
				  System.out.println(object);
			}
			System.out.println("===---===");
			System.out.println("Run ID: " + _runId + " Avg: " + _averageMAPE + " Found Avg: " + _MAPES.get(bestAverage));
		}
		

}
