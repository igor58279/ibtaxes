package com.ilr.ib_taxes.trades;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Trades {
	
	//Read parameters from configuration
	private static final String HEADER_LINE = 
	"Класс актива;Валюта;Курс рубля ЦБ РФ;Символ;Дата Открытия/Закрытия;Вид сделки;Количество;Цена за единицу USD;Цена за единицу РУБ;Комиссия USD;Комиссия РУБ;Стоимость покупки/продажи USD;Стоимость покупки/продажи РУБ;Прибыль/Убыток USD;Прибыль/Убыток РУБ;Налог к уплате/вычету 13%\n";		
	
	private String[] m_header_lines;
	
	Map<String,List<Trade>> activeTrades;
	
	public Trades(String[] headerLines) {
		
		m_header_lines = headerLines;
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
	public ArrayList<String> getTaxLines(String key) {
		List<Trade> lstTrade = activeTrades.get(key);
		if(lstTrade != null) {
			//not required
		    Collections.sort( lstTrade);

			TaxStatement  taxStatement = new TaxStatement((ArrayList<Trade>)lstTrade);
			return taxStatement.doTax();
			
		}
		return null;
	}
	
	//calculates all and prints into the file all tax line
	//reduces all closed positions from m_activeTrades
	public void printAllTaxes(String fileName) {	
		try (FileWriter writer = new FileWriter(fileName);
			BufferedWriter bw = new BufferedWriter(writer)) {
			printHeader(bw);
			
			ArrayList<String> taxStrList;
			for (Map.Entry<String, List<Trade>> set : activeTrades.entrySet()) {
				taxStrList = getTaxLines(set.getKey());
				if(taxStrList != null) {
					for(int i =0; i<taxStrList.size();i++) {
						bw.write(taxStrList.get(i));
				    }
				}
			}	
		}catch (IOException e) {
	        System.err.format("IOException: %s%n", e);
	    }
	}
	
	public void printTickerTaxes(String fileName,String ticker) {	
		try (FileWriter writer = new FileWriter(fileName);
			BufferedWriter bw = new BufferedWriter(writer)) {
			printHeader(bw);
			
			ArrayList<String> taxStrList;
			taxStrList = getTaxLines(ticker);
			
			
			if(taxStrList != null) {
				for(int i =0; i<taxStrList.size();i++) {
					bw.write(taxStrList.get(i));
			    }
			}
		
		}catch (IOException e) {
	        System.err.format("IOException: %s%n", e);
	    }
	}
	
	//prints into the file all remaining active(open) trades
	//from m_activeTrades
	public void printActive(String fileName) {
		try (FileWriter writer = new FileWriter(fileName);
			BufferedWriter bw = new BufferedWriter(writer)) {
			printHeader(bw);
			
			List<Trade> lstTrade;
			for (Map.Entry<String, List<Trade>> set : activeTrades.entrySet()) {
			    lstTrade = set.getValue();
			    for(int i =0; i<lstTrade.size();i++) {
			    	bw.write(lstTrade.get(i).toTaxString());
			    }
			}				
		}catch (IOException e) {
	        System.err.format("IOException: %s%n", e);
	    }
	}
	public void printHeader(BufferedWriter bw) {
		try {
			for(int i = 0;i < m_header_lines.length;i++)
				bw.write(m_header_lines[i]);
			bw.write(HEADER_LINE);
			
			} catch (IOException e) {
				System.err.format("IOException: %s%n", e);
		}
		
	}
}
