package FinancialAgent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MapDataSet {

	private SQLConnect SQLConnect = new SQLConnect();
	private HashMap<String, Double> mapDataSet = new HashMap<String, Double>();
	private HashMap<String, Double> riskRewardMapDataSet = new HashMap<String, Double>();
	
	private String 	dataSetDateString,
					agentIDString;
	
	public MapDataSet(){}
	
	public void loadDataSet(String tableName)
	{
		String orderBy = "Date";
		List<DJIADataSet> entireDataSet = SQLConnect.getAll(tableName, orderBy);//change this to getAllMarketData
		for(DJIADataSet item : entireDataSet)
		{
			Date dataSetDate = item.getDate();
			dataSetDateString = new SimpleDateFormat("DD-MM-YYYY").format(dataSetDate);
			double dataSetClosePrice = item.getClosePrice();
			mapDataSet.put(dataSetDateString, new Double(dataSetClosePrice));
		}
	}
	
	public void loadAgentRiskRewards(int ID)
	{
		for(int i = 0; i< 100; i++)
		{
			List<AgentRiskRewardDataSet> entireDataSet = SQLConnect.getAgentRiskReward(ID, i);
			agentIDString = "AgentID:" + i;
			double riskReward = entireDataSet.get(0).getRiskReward();
			riskRewardMapDataSet.put(agentIDString, new Double(riskReward)); //check whether this need "new double"
		}	
	}
	
	public double getAgentRiskReward(int agentID)
	{
		agentIDString = "AgentID:" + agentID;
		if (riskRewardMapDataSet.containsKey(agentIDString))
		{
			return riskRewardMapDataSet.get(agentIDString).doubleValue();
		}
		double empty = -1.0;
		return empty;
	}
	
	public double[] getClosingPrices(Date startDate, int days)
	{
		int size = 1;
		double[] closingPricesArray;
		closingPricesArray = new double[size];
		
		if (days > 1)
		{
			dataSetDateString = new SimpleDateFormat("DD-MM-YYYY").format(startDate);
			if (mapDataSet.containsKey(dataSetDateString))
			{
				double closePrice = mapDataSet.get(dataSetDateString).doubleValue();
				closingPricesArray[0] = closePrice;
			}
			else
			{
				Date startDateExtra = Helper.alterDate(startDate, 1);
				dataSetDateString = new SimpleDateFormat("DD-MM-YYYY").format(startDateExtra);
				if (mapDataSet.containsKey(dataSetDateString))
				{
					double closePrice = mapDataSet.get(dataSetDateString).doubleValue();
					closingPricesArray[0] = closePrice;
				}
				else
				{
					return null;
				}
			}
		}
		else
		{
			dataSetDateString = new SimpleDateFormat("DD-MM-YYYY").format(startDate);
			if (mapDataSet.containsKey(dataSetDateString))
			{
				double closePrice = mapDataSet.get(dataSetDateString).doubleValue();
				closingPricesArray[0] = closePrice;
			}
			else
			{
				return null;
			}
		}
		
		return closingPricesArray;
	}
}