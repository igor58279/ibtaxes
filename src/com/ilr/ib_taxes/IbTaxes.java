package com.ilr.ib_taxes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import com.ilr.ib_taxes.payments.CashTransaction;
import com.ilr.ib_taxes.payments.CashTransactions;
import com.ilr.ib_taxes.trades.Trade;
import com.ilr.ib_taxes.trades.Trades;
import com.ilr.ib_taxes.utils.CurrencyRate;
import com.ilr.ib_taxes.utils.CurrencyRates;
import com.ilr.ib_taxes.utils.PersonalData;
import com.ilr.ib_taxes.utils.Utils;

public class IbTaxes {

	public static void main(String[] args) throws Exception {
		
		
		//Get config file and read parameters
		String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		String appConfigPath = rootPath + "app1.cfg";
		
		
		System.out.println( "Reading configuration from: " + appConfigPath);
		 
		Properties appProps = new Properties();
		try {
			appProps.load(new FileInputStream(appConfigPath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		String splitter =";";
		  
		String statements_dir = appProps.getProperty("statements_dir");
		
		String currency_files = appProps.getProperty("exchange_files");
		String[] exchange_rates_files  = currency_files.split(splitter);
		
		
		String cash_files = appProps.getProperty("cash_files");
		String[] cash_activity_files  = cash_files.split(splitter);
		
		String statements_files = appProps.getProperty("statements_files");
		String[] ib_statements_files  = statements_files.split(splitter);
		
		
		PersonalData persData = new PersonalData(appProps);
		
		Utils utils = new Utils();
		
		
		CurrencyRates curRates = new CurrencyRates();
		for(int files=0;files < exchange_rates_files.length; files++) {
			List<String[]> csvData = utils.getCsv(statements_dir + exchange_rates_files[files],splitter);
			
			System.out.println( "Processing " + statements_dir + exchange_rates_files[files]);
			
			//skip header line
			for (int i=1; i < csvData.size();i++ ) {
				String[] line = csvData.get(i);
				CurrencyRate  exchRate = utils.getCurrencyRateFromLine(i, line);
				if(exchRate != null) {
					curRates.addRate(exchRate.getDate(), exchRate.getExchRate());
				}
			}
		}
		
		
		splitter=",";
		Float exRate;
		Trades trades = new Trades(persData.getHeaderLines(),persData.getStart(),persData.getEnd());
		for(int files = 0;files < ib_statements_files.length; files++) {
			List<String[]> csvData = utils.getCsv(statements_dir + ib_statements_files[files],splitter);
			System.out.println( "Processing " + statements_dir + ib_statements_files[files]);
			
			//skip header line
			for (int i=1; i<csvData.size();i++ ) {
				String[] line = csvData.get(i);
				
				Trade trade = utils.getTradeFromLine(i,line);
				if(trade != null) {
					exRate = curRates.getRate(trade.getDealDate());
					trade.setExchRate(exRate);
					trades.AddTrade(trade);
				}
				else {
					//something went wrong - print the line with "empty" for empty fields for debugging
					String field;
					for(int j=0; j<line.length; j++) {
						field = utils.stripQuotes(line[j]);
						if(field != null)
							System.out.print(field  +" ");
						else
							System.out.print("empty" + j +" ");
					}
					System.out.println( "\n");	
				}
			}
		}
		
		
		CashTransactions cashTransactions = new CashTransactions(persData.getHeaderLines(),persData.getStart(),persData.getEnd());
		
		for(int files = 0;files < cash_activity_files.length; files++) {
			List<String[]> csvData = utils.getCsv(statements_dir + cash_activity_files[files],splitter);
			

			
			//skip header line
			for (int i = 1; i < csvData.size();i++ ) {
				String[] line = csvData.get(i);

				CashTransaction  cash = utils.getCashTransactionFromLine(i,line);
				if(cash != null) {
					exRate = curRates.getRate(cash.getCashDate());
					cash.setExchRate(exRate);
					cashTransactions.AddCashTransaction(cash);
				}
				else {
					String field;
					for(int j=0; j<line.length; j++) {
						field = utils.stripQuotes(line[j]);
						if(field != null)
							System.out.print(field  +" ");
						else
							System.out.print("empty" + j +" ");
					}
					System.out.println( "\n");
				}
			}
		}
		//trades.printMap();	
		//trades.printTickerTaxes("/Users/igor/Brokerage/IB/taxes_test.csv","FANG") ;
		
		//generate file with taxes for trades
		trades.printAllTaxes(statements_dir + "taxes.csv");
		
		//open positions for verification purposes
		trades.printActive(statements_dir +  "open.csv");
		
		//generate files with taxes for cash transactions and dividends
		cashTransactions.printAllCashActivities(statements_dir +  "cash.csv",statements_dir +  "divs.csv");
		
	}
}
