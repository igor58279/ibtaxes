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
import java.util.List;
import java.util.Locale;





//!!!Stock dividends and taxes on it not included
public class CashTransactions {
	//Read parameters from configuration
	private static final String[] HEADER_LINES = {"\n\n","Период;01/01/20;12/31/20\n",		
	"Имя брокера;Interactive Brokers;Адрес брокера;Two Pickwick Plaza,Greenwich,CT 06830\n",	
	"Имя;Ройтман;Игорь;Львович\n"	,
	"Имя(английский);Roitman;Igor\n",		
	"Счет;U3298246\n",		
	"Базовая валюта;USD\n",		
	"Тип счета;Индивидуальный\n",	
	"Возможности счета;Маржа","\n\n"	};		
	
	//Header lines for cash and divs transactions
	private static final String DIVS_LINE = "Валюта;Курс рубля ЦБ РФ;Символ;Дата Выплаты;Комиссия USD;Комиссия РУБ;Сумма USD;Сумма РУБ;Налог у брокера %;% к уплате Россия;Налог к уплате 13%\n";
	private static final String CASH_LINE = "Валюта;Курс рубля ЦБ РФ;Действие;Дата выплаты; Сумма USD;Сумма РУБ;Налог к уплате/вычету 13%\n";

	private static final String DATE_FORMAT = "dd/MM/yyyy";
	private static final float TAX_RATE = 0.13f;

	SimpleDateFormat formatter;
	
	List<CashTransaction> m_cashActivities;
	

	Locale m_locale; 
	NumberFormat m_nf;
	DecimalFormat m_df;
	
	
	public CashTransactions(){
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		m_cashActivities = new ArrayList<CashTransaction>();
		setLocaleDecimalSeparator();
	}
	public void AddCashTransaction(CashTransaction cash) {
		//add transaction here
		m_cashActivities.add(cash);
		
		
		return;
	}
	
	private void sortTransactions() {
		Collections.sort( m_cashActivities); 
	}
	
	
	//calculates all and prints into the file all tax line
	//reduces all closed positions from m_activeTrades
	public void printAllCashActivities(String fileNameCash,String fileNameDivs)
	{	
		try {
			FileWriter writerCash = new FileWriter(fileNameCash);
			BufferedWriter bwCash = new BufferedWriter(writerCash);
			printHeader(false,bwCash);
			
			FileWriter writerDivs = new FileWriter(fileNameDivs);
			BufferedWriter bwDivs = new BufferedWriter(writerDivs);
			printHeader(true,bwDivs);
			
			
			sortTransactions();
			
			CashTransaction cashTransaction;
			for(int i =0; i<m_cashActivities.size();i++) {
				
				cashTransaction = m_cashActivities.get(i);
				if(cashTransaction.getTransType().equals("Broker Interest Paid")
				|| cashTransaction.getTransType().equals("Broker Interest Received")
				|| (cashTransaction.getTicker()== null && cashTransaction.getTransType().equals("Other Fees"))) {
					//cash action
					bwCash.write(toCashTaxStr(cashTransaction));
				}
				else {
					//tax and dividend are in different transactions
					//moreover - it's possible that there is no tax at all
					//the solution:
					// create div object - put it into the map by ticker + date
					//collect the map and print it out
				}
			}
			bwCash.close();
			bwDivs.close();
		}catch (IOException e) {
	        System.err.format("IOException: %s%n", e);
	    }
	}
	
	public void printHeader(boolean bDivs, BufferedWriter bw) {
		try {
				for(int i=0;i<HEADER_LINES.length;i++)
					bw.write(HEADER_LINES[i]);
				if(bDivs)
					bw.write(DIVS_LINE);
				else
					bw.write(CASH_LINE);
				
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
