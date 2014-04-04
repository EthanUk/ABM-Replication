package FinancialAgent;
public class FinancialAgent {

	private long totalAssets;
	private long investedAssets;
	private double riskReward;
	private int agentType;
	private int agentId;
	
	public FinancialAgent(int agentType, int agentId)
	{
		this.agentId = agentId;
		this.agentType = agentType;
		this.investedAssets = 0;
		this.totalAssets = getTotalAssets(agentType);
		this.riskReward = Math.random();
	}

	private long getTotalAssets(int agentType) {
		
		long totalAssets;
		switch (agentType) {
            case 1:		totalAssets = 100000;  //Value = 100,000
            		break;
            case 2:		totalAssets = 100000000; //Value = 100,000,000
            		break;
            case 3:		totalAssets = 1000000000; //Value = 1,000,000,000
            		break;
            case 4:		totalAssets = 10000000000L;  //Value = 10,000,000,000
            		break;							
            default: totalAssets = 0;
            		break;
		}
		return totalAssets;
	}
	
	public long getTotalAssets() {return totalAssets;}
	public void setTotalAssets(long totalAssets) 
	{ 
		this.totalAssets = totalAssets;
	}
	public long getinvestedAssets() {return investedAssets;}
	public void setinvestedAssets(long investedAssets)
	{
		this.investedAssets = investedAssets;
	}
	public double getRiskRewardRate() {return riskReward;}
	public void setRiskRewardRate(double riskReward)
	{
		this.riskReward = riskReward;
	}
	public int getAgentId() {return agentId;}
	public int getAgentType() {return agentType;}


	
	


}
