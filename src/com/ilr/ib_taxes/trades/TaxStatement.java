package com.ilr.ib_taxes.trades;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaxStatement {
	
	List<Trade> m_lstTrade;
	List<ClosedTrade> m_lstClosed;
	boolean m_bPos;
	
	Date m_start,m_end;
	
	public TaxStatement(ArrayList<Trade> trades,Date start,Date end) {
		m_lstTrade = trades;
		m_lstClosed = null;
		m_start = start;
		m_end = end;
	}
	
	//calculates tax for specific ticker and returns closed statement tax strings only
	public ArrayList<String> doTax() {

		ArrayList<String> taxStrList = null;
		boolean bClosedTrade;
		do {
			bClosedTrade = closeTrades();
		}
		while(!m_lstTrade.isEmpty() && bClosedTrade);
		
		//Now we have in m_lstTrade open positions
		// and m_lstClosed - closed positions
			
		//we have two lists  - open positions and closed ones
		// just print for debugging purposes
		
		
		
		if(m_lstClosed != null) {
			taxStrList = new ArrayList<String>();
			for(int i =0; i<m_lstClosed.size();i++) {
				
				
				String closedTrade = m_lstClosed.get(i).toClosedTaxStr(m_start,m_end);
				if(closedTrade != null )
					taxStrList.add( closedTrade);
				else //debug 
					System.out.println( "Another period:" + m_lstClosed.get(i));
			}
		}	
		return taxStrList;	
	}
	
	public boolean closeTrades() {
		boolean bClosedTrade = false;
		
		boolean bBuySell = m_lstTrade.get(0).isbBuySell(); 
		
		for(int i =1 ; i< m_lstTrade.size(); i++) {
			//walk through the list
			if(bBuySell != m_lstTrade.get(i).isbBuySell()) {
				//we have trade in another direction
				//we can close it
				float fReduce = Math.min (Math.abs(m_lstTrade.get(0).getQuantity()),Math.abs(m_lstTrade.get(i).getQuantity()));
			
			    ClosedTrade closedTrade = new ClosedTrade (m_lstTrade.get(0).getTicketDescription(),
			    		m_lstTrade.get(0).getTicketName(),
			    		m_lstTrade.get(0).isbBuySell()?fReduce: -1 * fReduce,
			    		m_lstTrade.get(0).getDealPrice(),
			    		m_lstTrade.get(0).getExchRate(),
			    		m_lstTrade.get(0).isbBuySell(),
			    		m_lstTrade.get(0).getCommission(),
			    		m_lstTrade.get(0).getDealDate(),
			    		m_lstTrade.get(0).getSettleDate(),
			    		m_lstTrade.get(0).getActiveClass(),
			    		m_lstTrade.get(0).getActiveCurrency(),
			    		m_lstTrade.get(i).getDealPrice(),
			    		m_lstTrade.get(i).getExchRate(),
			    		m_lstTrade.get(i).isbBuySell(),
			    		m_lstTrade.get(i).getCommission(),
			    		m_lstTrade.get(i).getDealDate(),
			    		m_lstTrade.get(i).getSettleDate());
			    
			    
			    //Add new closed trade
			    if(m_lstClosed == null)
			    	m_lstClosed = new ArrayList<ClosedTrade>();
			    m_lstClosed.add(closedTrade);
			    
			    //Reduce list with open trades and commissions
			    boolean b1 = m_lstTrade.get(0).adjustQuantity(fReduce);
			    boolean b2 = m_lstTrade.get(i).adjustQuantity(fReduce);
			    
			    if(b2)
			    	m_lstTrade.remove(i);
			    if(b1)
			    	m_lstTrade.remove(0);
			    
			    return true;			    
			}
		}
		return bClosedTrade;
	}
}
