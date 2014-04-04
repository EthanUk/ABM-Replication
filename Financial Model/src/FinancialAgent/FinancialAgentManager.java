package FinancialAgent;

import java.util.ArrayList;

public class FinancialAgentManager {
	
	private  ArrayList<FinancialAgent> financialAgents;
	
	public FinancialAgentManager(){
		this.financialAgents = new ArrayList<FinancialAgent>(100);
	}

	public void addAgent(int numberToBeCreated, int agentType)
	{
		int currentMaxId;
		currentMaxId = financialAgents.size();
		for(int i = 0; i < numberToBeCreated; i++)
		{
			FinancialAgent financialAgent = new FinancialAgent(agentType,currentMaxId);
			financialAgents.add(financialAgent);
			currentMaxId++;
		}
	}
	
	public  FinancialAgent getFinancialAgent(int index){
		return (FinancialAgent) financialAgents.get(index);
	}
	
	public  int getNumberOfAgent(){
		return financialAgents.size();
	}
	
	public  double getTotalAssets(){
		
		double totalAssets = 0.0;
		for(FinancialAgent item : financialAgents)
		{
			totalAssets += item.getTotalAssets(); 
		}
		return totalAssets;
	}
	
	
	
	
}//End of FinancialAgentManager class
