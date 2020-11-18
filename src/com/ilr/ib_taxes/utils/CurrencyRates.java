package com.ilr.ib_taxes.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class CurrencyRates {
	
	HashMap<String,Float> exchRates;
	SimpleDateFormat formatter;
	
	public CurrencyRates(){
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		exchRates = new HashMap<String,Float>();
		
	}



	

	public void addRate(Date date, Float exchRate){
		
	    String strDate = formatter.format(date);  
		exchRates.put(strDate, exchRate);
	}
	
	public Float getRate(Date date) {
		Float exchRate = null;
		String strDate;
		Calendar c = Calendar.getInstance();
		Date exchRateDate = date;
		
		do {
			strDate = formatter.format(exchRateDate);
			exchRate= exchRates.get(strDate);
			if(exchRate == null) {
				//go day back to until we get a rate
				c.setTime(exchRateDate);
				c.add(Calendar.DATE, -1);
				exchRateDate = c.getTime();
			}
			
		} while (exchRate == null);
		
		return exchRate;
	}
}