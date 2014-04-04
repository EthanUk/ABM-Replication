package FinancialAgent;

public class AgentRiskRewardDataSet {
	
	private int ID;
	private int agentID;
	private double riskReward;
	
	public AgentRiskRewardDataSet() {}
	
	public AgentRiskRewardDataSet(int ID, int agentID, double riskReward) 
	{
		this.ID = ID;
		this.agentID = agentID;
		this.riskReward = riskReward;
	}
	
	public int getID(){
		return ID;
	}
	
	public void setID(int ID){
		this.ID = ID;
	}
	
	public int getAgentID() {
		return agentID;
	}
	
	public void setAgentId(int agentID)
	{
		this.agentID = agentID;
	}

	public double getRiskReward() {
		return riskReward;
	}
	
	public void setRiskReward(double riskReward)
	{
		this.riskReward = riskReward;
	}


}
