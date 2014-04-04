package FinancialAgent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List; 
import java.util.Date;

import org.hibernate.HibernateException; 
import org.hibernate.Session; 
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;


public class SQLConnect {
	
	private SessionFactory factory;
	
	public SQLConnect()
	{
		try{
	         factory = new Configuration().configure().buildSessionFactory();
		}
		catch (Throwable ex) { 
	         System.err.println("Failed to create sessionFactory object." + ex);
	         throw new ExceptionInInitializerError(ex); 
	      }
	}

	public List<DJIADataSet> getPredictedTimeSeries(Date startDate, Date endDate) 
	{
		List<DJIADataSet> DJIADataSet = null;
		Session session = factory.openSession();
		
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String 	_StartDate, //rename this
				_EndDate;  //rename this
		_StartDate = format1.format(startDate);
		_EndDate = format1.format(endDate);
	
		
		Transaction tx = null;
		try{
	         tx = session.beginTransaction();
	         String Query = "FROM DJIADataSet WHERE (Date BETWEEN '" + _StartDate + "' AND '" + _EndDate + "')";
	         DJIADataSet = session.createQuery(Query).list(); 
	         tx.commit();
	      }
	      catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }
	      finally {
	         session.close(); 
	         
	      }
	
			return DJIADataSet;
	}
	
	public void saveAgentRiskRewards(int ID, ArrayList<FinancialAgent> bestSolutionDataSet)
	{
		Session session = factory.openSession();
		Transaction tx = null;
		int max = bestSolutionDataSet.size();

		try
		{
			tx = session.beginTransaction();
			for (int i = 0; i < max; i++)
			{
				FinancialAgent tempAgent = bestSolutionDataSet.get(i);
				int agentId = tempAgent.getAgentId();
				double riskReward = tempAgent.getRiskRewardRate();
				AgentRiskRewardDataSet tempRiskReward = new AgentRiskRewardDataSet(ID,agentId,riskReward);
				session.save(tempRiskReward);
				session.flush();
				session.clear();
			}
			tx.commit();
		}
		catch (HibernateException e) 
		{
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	    }
		finally {session.close();}

	}

	public List<DJIADataSet> getAll(String tableName, String orderBy) 
	{
		List<DJIADataSet> DJIADataSet = null;
		Session session = factory.openSession();
		
		Transaction tx = null;
		try{
	         tx = session.beginTransaction();
	         String Query = "FROM " + tableName + " WHERE MISSING = 0 ORDER BY " + orderBy;//
	         DJIADataSet = session.createQuery(Query).list(); 
	         tx.commit();
	      }
	      catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }
	      finally {session.close();}
		
		return DJIADataSet;
	}

	public List<AgentRiskRewardDataSet> getAgentRiskReward(int ID, int AgentID) {
		
		List<AgentRiskRewardDataSet> AgentRiskRewardDataSet = null;
		Session session = factory.openSession();
		
		Transaction tx = null;
		try{
	         tx = session.beginTransaction();
	         String Query = "FROM AgentRiskRewardDataSet WHERE ID = " + ID +" AND AgentID =" + AgentID;//WHERE MISSING = 0
	         AgentRiskRewardDataSet = session.createQuery(Query).list(); 
	         tx.commit();
	      }
	      catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }
	      finally {session.close();}
		
		return AgentRiskRewardDataSet;
	}
	

}



/*
  
 
 
 
*/
