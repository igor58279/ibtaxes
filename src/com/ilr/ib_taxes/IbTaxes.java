package com.ilr.ib_taxes;

import java.util.List;

import com.ilr.ib_taxes.trades.Trade;
import com.ilr.ib_taxes.trades.Trades;
import com.ilr.ib_taxes.utils.CurrencyRate;
import com.ilr.ib_taxes.utils.CurrencyRates;
import com.ilr.ib_taxes.utils.Utils;

public class IbTaxes {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Utils utils = new Utils();
		
		Trades trades = new Trades();
		CurrencyRates curRates = new CurrencyRates();
		Float exRate;
		
		
		String splitter =";";
		for(int files=0;files < args.length; files++) {
			if (files>0)
				splitter=",";
			List<String[]> csvData = utils.getCsv(args[files],splitter);
			
			System.out.println( "Processing " + args[files]);
			
			//skip header
			for (int i=1; i<csvData.size();i++ ) {
				String[] line = csvData.get(i);
				if(files == 0) {
					//currency rates
					CurrencyRate  exchRate = utils.getCurrencyRateFromLine(i, line);
					if(exchRate !=null) {
						System.out.println(exchRate);
						curRates.addRate(exchRate.getDate(), exchRate.getExchRate());
					}
					
				}
				else {
					Trade trade = utils.getTradeFromLine(i,line);
					if(trade !=null) {
						exRate = curRates.getRate(trade.getDealDate());
						trade.setExchRate(exRate);
						System.out.println(trade);
						trades.AddTrade(trade);
					}
					else {
						String field;
						for(int j=0; j<line.length; j++) {
							field = utils.stripQuotes(line[j]);
							if(field!=null)
								System.out.print(field  +" ");
							else
								System.out.print("empty" + j +" ");
						}
					
						System.out.println( "\n");
					}
				}
			}
		}
		trades.printMap();
		
		trades.printTax("GILD");
		trades.printTax("RDS A");
		trades.printTax("GILD");
		trades.printTax("fff");
		trades.printTax("KSS");
		trades.printTax("XEC");
		trades.printTax("CMA");
	}

}
