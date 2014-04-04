package FinancialAgent;

import java.util.ArrayList;
import java.util.Random;


public class Prediction {

	ArrayList<FinancialAgent> financialAgentArray = new ArrayList<FinancialAgent>();
	
	public Prediction(int maxAgents){
		
		for (int i = 0; i < maxAgents; i++){
			financialAgentArray.add(null);
		}
	}
	
	public Prediction(ArrayList<FinancialAgent> financialAgentArray){
		this.financialAgentArray = (ArrayList<FinancialAgent>) financialAgentArray.clone();
	}
	
	public void createStart(FinancialAgentManager FinancialAgentManager){
		int max = financialAgentArray.size();
		for (int agentIndex = 0; agentIndex < max;  agentIndex++){
			setFinancialAgent(agentIndex, FinancialAgentManager.getFinancialAgent(agentIndex));
		}
	}
	
	public double calcFinancialAgentsInvestedAssets(double currentSentiment){
		
		double	totalInvestedAssets = 0;
		
		for(FinancialAgent item : financialAgentArray)
		{	
			calcFinancialAgentInvestment(item, currentSentiment);
			totalInvestedAssets += item.getinvestedAssets();
		}
		return totalInvestedAssets;
	}
	
	
	private void calcFinancialAgentInvestment(FinancialAgent financialAgent, double sentiment)
	{
		long	investedAssets = financialAgent.getinvestedAssets();
		double	riskRewardRate = financialAgent.getRiskRewardRate(),
				buyThreshold, sellThreshold,randomNum;
		
		buyThreshold = riskRewardRate * sentiment;
		sellThreshold = (1 - ((1 - buyThreshold) / 2));
		
		//randomNum = Math.random();
		randomNum = Helper.getNextRN();
		
		if (randomNum < buyThreshold)
		{
			investedAssets += investedAssets * 0.02; //Buy an Additional 2% of total Assets
		}
		else if (randomNum > sellThreshold)
		{
			investedAssets -= investedAssets * 0.02; //Sell 2% of Total Assets
		}
		financialAgent.setinvestedAssets(investedAssets); //Updates the agents invested assets at the end of each cycle
		
	}// End of calcFinancialAgentInvestment
	
	
	public void setFinancialAgent(int agentIndex, FinancialAgent FinancialAgent){
		financialAgentArray.set(agentIndex, FinancialAgent);
	}
	
	
	public void setRandomAgentRiskReward(int AgentID, double newRiskReward){
		(financialAgentArray.get(AgentID)).setRiskRewardRate(newRiskReward);
	}
	
	public int getAgentArraySize(){
		return financialAgentArray.size();
	}
	
	public ArrayList<FinancialAgent> getfinancialAgentArray(){
		return financialAgentArray;
	}
	
	public void setAllRiskRewards(int ID)
	{
		double riskReward;
		MapDataSet mapRiskRewardDataSet = new MapDataSet();
		mapRiskRewardDataSet.loadAgentRiskRewards(ID);
		
		for (int agentIndex = 0; agentIndex < financialAgentArray.size(); agentIndex++)
		{
			riskReward = mapRiskRewardDataSet.getAgentRiskReward(agentIndex);
			FinancialAgent agent = financialAgentArray.get(agentIndex);
			agent.setRiskRewardRate(riskReward);
		}
	}	
	
}//End of Prediction Class
