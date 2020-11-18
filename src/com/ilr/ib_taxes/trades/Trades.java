package com.ilr.ib_taxes.trades;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Trades {
	Map<String,List<Trade>> activeTrades;
	
	public Trades() {
		activeTrades = new HashMap<String,List<Trade>>();
	}
	
	public boolean AddTrade(Trade trade) {
		
		String key = trade.getTicketName();
		
		List<Trade> lstTrade = activeTrades.get(key);
		
		if(lstTrade == null) {
			
			lstTrade = new ArrayList<Trade>();
			activeTrades.put(key,lstTrade);
		};
		
		lstTrade.add(trade);
		return true;
	}
	
	public void printMap() {
		List<Trade> lstTrade;
		
		for (Map.Entry<String, List<Trade>> set : activeTrades.entrySet()) {
		    System.out.println(set.getKey());
		    lstTrade = set.getValue();
		    
		    Collections.sort( lstTrade);
		    for(int i =0; i<lstTrade.size();i++) {
		    	System.out.println(lstTrade.get(i));
		    }
		    System.out.println("---------------------------------------");
		}	
	}
	public void printTax(String key) {
		List<Trade> lstTrade = activeTrades.get(key);
		if(lstTrade != null) {
			TaxStatement  taxStatement = new TaxStatement((ArrayList<Trade>)lstTrade);
			taxStatement.calculateTax();
			
		}
	}
}
