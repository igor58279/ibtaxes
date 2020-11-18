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
	
	
	SimpleDateFormat formatter, exchFormatter;  
	
	
	public Utils() {
		super();
		formatter = new SimpleDateFormat("yyyy-MM-dd") ;
		exchFormatter = new SimpleDateFormat("MM/dd/yy") ;

		
		
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
				trade.setTicketName(stripQuotes(line[5]));
				//should be USD
				trade.setActiveCurrency(stripQuotes(line[3]));
				
				// Buy = 40
				trade.setbBuySell(stripQuotes(line[40]).matches("BUY")? true: false);
				
				trade.setQuantity(Float.parseFloat(stripQuotes(line[41])));
				
				trade.setDealPrice(Float.parseFloat(stripQuotes(line[42])));
				
				trade.setCommission(Float.parseFloat(stripQuotes(line[45])));
				
				trade.setActiveClass(stripQuotes(line[4]));
				
				Date dealDate=formatter.parse(stripQuotes(line[38]));
				trade.setDealDate(dealDate);
				
				tmpStr = stripQuotes(line[37]);
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
				Float exchRate = Float.parseFloat(line[2]);
				Date date = exchFormatter.parse(line[1]);
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
