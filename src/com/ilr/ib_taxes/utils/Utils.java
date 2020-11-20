package com.ilr.ib_taxes.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.ilr.ib_taxes.trades.Trade;

public class Utils {
	private static final int TICKET_NAME = 5;
	private static final int CURRENCY = 3;
	private static final int ACTION = 40;
	private static final int QUANTITY = 41;
	private static final int DEAL_PRICE = 42;
	private static final int DEAL_COMISSION = 45;
	private static final int ACTIVE_CLASS = 4;
	private static final int DEAL_DATE = 38;
	private static final int SETTLE_DATE = 37;
	private static final String IB_DATE = "yyyy-MM-dd";
	private static final String CB_DATE = "MM/dd/yy";
	
	
	private static final int CB_RATE = 2;
	private static final int CB_DATE_COL = 1;
	
	SimpleDateFormat formatter, exchFormatter;  
	
	
	
	public Utils() {
		super();
		formatter = new SimpleDateFormat(IB_DATE) ;
		exchFormatter = new SimpleDateFormat(CB_DATE) ;
  
	}
	


	// This method will read a CSV file and return a List of String[]
	public  List<String[]> getCsv(String filename, String splitter ) {
		List<String[]> data = new ArrayList<String[]>();
		String testRow;
		try {
			// Open and read the file
			BufferedReader br = new BufferedReader(new FileReader(filename));
			// Read data as long as it's not empty
			// Parse the data by comma using .split() method
			// Place into a temporary array, then add to List 
			while ((testRow = br.readLine()) != null) {
				String[] line = testRow.split(splitter);
				data.add(line);
			}
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: File not found " + filename);
		} catch (IOException e) {
			System.out.println("ERROR: Could not read " + filename);
		}
		return data;
	}
	
	public Trade getTradeFromLine(int i,String[] line) {
		Trade trade = new Trade();
		String tmpStr;
		try {
			trade.setTicketName(stripQuotes(line[TICKET_NAME]));
			
			trade.setActiveCurrency(stripQuotes(line[CURRENCY]));
			
			trade.setbBuySell(stripQuotes(line[ACTION]).matches("BUY")? true: false);
			
			trade.setQuantity(Float.parseFloat(stripQuotes(line[QUANTITY])));
			
			trade.setDealPrice(Float.parseFloat(stripQuotes(line[DEAL_PRICE])));
			
			trade.setCommission(Float.parseFloat(stripQuotes(line[DEAL_COMISSION])));
			
			trade.setActiveClass(stripQuotes(line[ACTIVE_CLASS]));
			
			Date dealDate=formatter.parse(stripQuotes(line[DEAL_DATE]));
			trade.setDealDate(dealDate);
			
			tmpStr = stripQuotes(line[SETTLE_DATE]);
			if(tmpStr == null) {
				trade.setSettleDate(dealDate);
			}
			else {
				Date settleDate = formatter.parse(tmpStr);
				trade.setSettleDate(settleDate);
			}
		
		}
		catch (Exception e){
			System.out.println("Bad line : " + i + " "+ e.getMessage());
			return null;
		
		}
		return trade;
	}
	
	public CurrencyRate getCurrencyRateFromLine(int i,String[] line) {
		CurrencyRate curRate = null;
		try {
			String processed = line[CB_RATE].replace(",", ".");
			Float exchRate = Float.parseFloat(processed);
			Date date = exchFormatter.parse(line[CB_DATE_COL]);
			curRate = new CurrencyRate(date,exchRate);
			
		}
		catch (Exception e){
			System.out.println("Bad line : " + i + " "+ e.getMessage());
			return null;
		}
		
		return curRate;
	}
	
	
	public String stripQuotes(String s) {
		
		if(s.length()<=2) {
			return null;
		}
		StringBuilder sb = new StringBuilder(s);
		return sb.substring(1, sb.length()-1);

		
	}
	
}
