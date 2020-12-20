package com.ilr.ib_taxes.payments;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;





//!!!Stock dividends and taxes on it not included
public class CashTransactions {
	
	
	
	//Header lines for cash and divs transactions
	private static final String DIVS_LINE = "Валюта;Курс рубля ЦБ РФ;Название;Символ;Дата Выплаты;Комиссия USD;Комиссия РУБ;Сумма USD;Сумма РУБ;Удержан налог брокером,USD;Налог у брокера %;% к уплате Россия;Налог к уплате 13%\n";
	private static final String CASH_LINE = "Валюта;Курс рубля ЦБ РФ;Действие;Дата выплаты; Сумма USD;Сумма РУБ;Налог к уплате/вычету 13%\n";
	private static final String MOVE_LINE = "Валюта;Действие;Дата операции; Сумма USD\n";

	private static final String DATE_FORMAT = "dd/MM/yyyy";
	private static final float TAX_RATE = 0.13f;

	SimpleDateFormat formatter;
	
	List<CashTransaction> m_cashActivities;
	
	Map<String,ClosedDivTransaction> m_divActivity;
	private String[] m_header_lines;

	Locale m_locale; 
	NumberFormat m_nf;
	DecimalFormat m_df;
	
	
	private Date m_start,m_end;
	
	public CashTransactions(String[] headerLines, Date start, Date end){
		
		
			
		m_header_lines = headerLines;
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		m_cashActivities = new ArrayList<CashTransaction>();
		m_divActivity = new HashMap<String,ClosedDivTransaction>();
		
		setLocaleDecimalSeparator();
		m_start = start;
		m_end = end;
	}
	public void AddCashTransaction(CashTransaction cash) {
		//add transaction here
		m_cashActivities.add(cash);
		
		
		return;
	}
	
	private void sortTransactions() {
		Collections.sort( m_cashActivities); 
	}
	
	public void addDividendActivity( CashTransaction cash) throws Exception{
		
	    String divKey = cash.getTicker() + formatter.format( cash.getCashDate());  
	    
	    ClosedDivTransaction closedDiv = m_divActivity.get(divKey);
	    if(closedDiv == null) {
	    	//first entry of dividend activity for this stock on this day.
	    	closedDiv = new ClosedDivTransaction(cash.getTicker(), cash.getDescription(), cash.getExchRate(), cash.getAmount(),
	    			cash.getCashDate(), cash.getTransType(), cash.getCurrency());
	    	m_divActivity.put(divKey, closedDiv);  
	    }
	    else if (closedDiv.getClosedTransType() == null) {
	    	//second time entry
	    	if(closedDiv.getAmount() * cash.getAmount() >= 0){
		    	// in the case of zero tax it still works
		    	// if result  > 0 it means that both are same sign transactions -payments or taxes
	    		//just sum the amounts 
	    		closedDiv.setAmount(closedDiv.getAmount() + cash.getAmount());
	    	}
	    	else {
	    		//we have different sign transaction
	    		closedDiv.setClosedAmount(cash.getAmount());
	    		closedDiv.setClosedTransType(cash.getTransType());
	    		closedDiv.setClosedDescription(cash.getDescription());	
	    	}
	    }
	    else {

	    	  //both actions are ready - find the correct one and add to it the amount
	    	if(closedDiv.getAmount() * cash.getAmount() >= 0)
	    		closedDiv.setAmount(closedDiv.getAmount() + cash.getAmount());
	    	else
	    		closedDiv.setClosedAmount(closedDiv.getClosedAmount() + cash.getAmount());
	    }
	    
	}
	//calculates all and prints into the file all tax line
	//reduces all closed positions from m_activeTrades
	public void printAllCashActivities(String fileNameCash,String fileNameDivs,String fileNameMove) throws Exception
	{	
		try {
			FileWriter writerCash = new FileWriter(fileNameCash);
			BufferedWriter bwCash = new BufferedWriter(writerCash);
			printHeader(1,bwCash);
			
			FileWriter writerMove = new FileWriter(fileNameMove);
			BufferedWriter bwMove = new BufferedWriter(writerMove);
			printHeader(2,bwMove);
			
			FileWriter writerDivs = new FileWriter(fileNameDivs);
			BufferedWriter bwDivs = new BufferedWriter(writerDivs);
			printHeader(0,bwDivs);
			
			
			sortTransactions();
			
			CashTransaction cashTransaction;
			for(int i = 0; i < m_cashActivities.size();i++) {
				
				cashTransaction = m_cashActivities.get(i);
				if(cashTransaction.getTransType().equals("Broker Interest Paid")
				|| cashTransaction.getTransType().equals("Broker Interest Received")
				|| cashTransaction.getTransType().equals("Commission Adjustments") // это уточнение брокерской комиссии и не учитывается в дивидентах
				|| cashTransaction.getTransType().equals("Other Fees")) {          // put it here even if it's related to some stock
					
					//cash action
					if(!cashTransaction.getCashDate().before(m_start) && !cashTransaction.getCashDate().after(m_end) ) {
						bwCash.write(toCashTaxStr(cashTransaction));
					}
					else {
						//not in period
						System.out.println( "Another cash period:" + cashTransaction.toString());
					}
				}
				else if(cashTransaction.getTransType().equals("Payment In Lieu Of Dividends")
						|| cashTransaction.getTransType().equals("Dividends")
						|| cashTransaction.getTransType().equals("Withholding Tax") ) {
					//tax and dividend are in different transactions
					//moreover - it's possible that there is no tax at all
					//the solution:
					// create div object - put it into the map by ticker + date
					//collect the map and print it out
					addDividendActivity(cashTransaction);
					
				}
				else if (cashTransaction.getTransType().equals("Deposits/Withdrawals")) {
					//Save deposit transaction
					//Don't check cash period for deposits!
//					if(!cashTransaction.getCashDate().before(m_start) && !cashTransaction.getCashDate().after(m_end) ) {
						bwMove.write(toCashMoveStr(cashTransaction));
//					}
//					else {
//						//not in period
//						System.out.println( "Another deposit/withdraw period:" + cashTransaction.toString());
//					}
					
				}
				else {
					//debug purposes -what's left
					System.out.println(cashTransaction);
				}
			}
			//Now all brokerage interest is already printed and we have HashMap with all dividends and commissions
			// lets calculate it one by one , put in to ArrayList, sort it and print into the dividends file
			List<ClosedDivTransaction> lstDividends = new ArrayList<ClosedDivTransaction>();
			
			ClosedDivTransaction closedTransaction;
			for (Map.Entry<String, ClosedDivTransaction> set : m_divActivity.entrySet()) {
				closedTransaction = set.getValue();
				closedTransaction.calculateTax();
				if(!closedTransaction.getCashDate().before(m_start) && !closedTransaction.getCashDate().after(m_end)) {
					
					lstDividends.add(closedTransaction);
				}else {
					System.out.println("Another dividend period:" + closedTransaction);
				}
			}	
			Collections.sort( lstDividends);
			for (int i=0; i < lstDividends.size();i++) {
				bwDivs.write(toDividendTaxStr(lstDividends.get(i)));
			}
			
			
			bwCash.close();
			bwDivs.close();
			bwMove.close();
			
		}catch (IOException e) {
	        System.err.format("IOException: %s%n", e);
	    }
	}
	
	public void printHeader(int nHeaderType, BufferedWriter bw) {
		try {
				for(int i=0;i<m_header_lines.length;i++)
					bw.write(m_header_lines[i]);
				switch(nHeaderType) {
					case 0: bw.write(DIVS_LINE);
					        return;
					case 1: bw.write(CASH_LINE);
					        return;
					case 2: bw.write(MOVE_LINE);
			        		return;     
				    default:
				    	System.out.println("Unknown header type:" + nHeaderType);
				}
			} catch (IOException e) {
				System.err.format("IOException: %s%n", e);
		}
		
	}
	
	public String toCashTaxStr(CashTransaction cash) {
		
		//"Валюта;Курс рубля ЦБ РФ;Действие;Дата выплаты; Сумма USD;Сумма РУБ;Налог к уплате/вычету 13%\n";
		
		String date;
		DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		
		date = formatter.format(cash.getCashDate());
		
		
		String cashTaxStr = cash.getCurrency() + ";" + getLocaleNubmer(cash.getExchRate()) + ";"
		                   + cash.getTransType() +";"
		                   + date +";" + getLocaleNubmer(cash.getAmount()) + ";" 
		                   + getLocaleNubmer(cash.getAmount() * cash.getExchRate()) +";"
		                   + getLocaleNubmer(cash.getAmount() * cash.getExchRate() * TAX_RATE) + "\n";
		return cashTaxStr;
	}
	
public String toCashMoveStr(CashTransaction cash) {
		
		//"Валюта;Действие;Дата операции; Сумма USD\n";
		
		String date;
		DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		
		date = formatter.format(cash.getCashDate());
		
		
		String cashTaxStr = cash.getCurrency() + ";" 
		                   + cash.getTransType() +";"
		                   + date +";" + getLocaleNubmer(cash.getAmount())  + "\n";
		return cashTaxStr;
	}
	
public String toDividendTaxStr(ClosedDivTransaction closedCash) {
		
		//"Валюта;Курс рубля ЦБ РФ;Символ;Дата Выплаты;Комиссия USD;Комиссия РУБ;Сумма USD;Сумма РУБ;Удержан налог брокером,USD;Налог у брокера %;% к уплате Россия;Налог к уплате 13%\n"
		
		String date;
		DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		
		date = formatter.format(closedCash.getCashDate());
		
		
		String cashTaxStr = closedCash.getCurrency() + ";" + getLocaleNubmer(closedCash.getExchRate()) + ";"
						   + closedCash.getDescription() +";"
		                   + closedCash.getTicker() +";"
		                   + date +";" + 0.0f + ";" + 0.0f + ";"  //for now zero commissions
		                   + getLocaleNubmer(closedCash.getPaidAmount()) + ";" 
		                   + getLocaleNubmer(closedCash.getPaidAmount() * closedCash.getExchRate()) + ";"
		                   + getLocaleNubmer(closedCash.getTaxAtPlace()) + ";"
		                   + getLocaleNubmer(closedCash.getClosedTaxBrokerRate()) + ";"
		                   + getLocaleNubmer(closedCash.getClosedTaxRate()) + ";" 
		                   + getLocaleNubmer(closedCash.getClosedTax()) + "\n";
		return cashTaxStr;
	}
	
	public String getLocaleNubmer(float num) {
		return m_df.format(num);
	}
	private void setLocaleDecimalSeparator() {
		m_locale =new Locale("ru", "RU");
		m_nf = NumberFormat.getNumberInstance(m_locale);
	    m_df = (DecimalFormat)m_nf;
	    m_df.setGroupingUsed(false);
	}
	
}
