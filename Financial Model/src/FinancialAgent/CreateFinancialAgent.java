package FinancialAgent;
import java.util.List;

public class CreateFinancialAgent {

	public static void createFinancialAgent(List<FinancialAgent> financialAgentArray, int numberToBeCreated, int agentType)
	{
		int currentMaxId;
		currentMaxId = financialAgentArray.size();
		for(int i = 0; i < numberToBeCreated; i++)
		{
			FinancialAgent financialAgent = new FinancialAgent(agentType,currentMaxId);
			financialAgentArray.add(financialAgent);
			currentMaxId++;
		}
	}

}
