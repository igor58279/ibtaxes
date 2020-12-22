package com.ilr.ib_taxes.trades;

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
import java.util.List;
import java.util.Locale;

public class CorpActions {

	private static final String DATE_FORMAT = "dd/MM/yyyy";
	
	SimpleDateFormat m_formatter;
	
	//Read parameters from configuration
	private static final String HEADER_LINE = 
	"Класс актива;Валюта;Символ;Описание События;Дата События;Количество\n";		
	
	private String[] m_header_lines;
	private Date m_start,m_end;
	
	
	Locale m_locale; 
	NumberFormat m_nf;
	DecimalFormat m_df;

    List<CorpAction> m_corpActions;
	
	public CorpActions(String[] headerLines, Date start, Date end) {
		
		m_header_lines = headerLines;
		m_start = start;
		m_end = end;
		m_corpActions = new ArrayList<CorpAction>();
		
		
		m_formatter = new SimpleDateFormat("yyyy-MM-dd");
				
		setLocaleDecimalSeparator();
	}
	
	public  void AddAction(CorpAction action) {
		
		m_corpActions.add(action);
		return;
	}

	public void printAllCorpActions(String fileNameActions) {
		try {
			FileWriter writerActions = new FileWriter(fileNameActions);
			BufferedWriter bwAction = new BufferedWriter(writerActions);
			printHeader(bwAction);
			
			
			
			sortCorpActions();
			
			CorpAction  corpAction;
			for(int i = 0; i < m_corpActions.size();i++) {
				
				corpAction = m_corpActions.get(i);
					
					//cash action
					if(!corpAction.getActionDate().before(m_start) && !corpAction.getActionDate().after(m_end) ) {
						bwAction.write(toCorpActionStr(corpAction));
					}
					else {
						//not in period
						System.out.println( "Another cash period:" + corpAction.toString());
					}
			}
			bwAction.close();
		}catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		
		}
		
	}	
	
	private String toCorpActionStr(CorpAction corpAction) {
		//"Класс актива;Валюта;Символ;Описание События;Дата События;Количество\n";
		
		String date;
		DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		
		date = formatter.format(corpAction.getActionDate());
		
		
		String corpActionStr = corpAction.getAssetClass() + ";" + corpAction.getCurrency() + ";"
		                   + corpAction.getTicketName() +";"
		                   + corpAction.getActionDescription() +";"
		                   + date +";" + getLocaleNubmer(corpAction.getQuantity())  
		                   + "\n";
		return corpActionStr;
	}
	
	private void sortCorpActions() {
		Collections.sort( m_corpActions); 
		
	}
	public String getLocaleNubmer(float num) {
		return m_df.format(num);
	}
	public void printHeader(BufferedWriter bw) {
		try {
				for(int i=0;i<m_header_lines.length;i++)
					bw.write(m_header_lines[i]);
				
				bw.write(HEADER_LINE);

			} catch (IOException e) {
				System.err.format("IOException: %s%n", e);
		}
	}
	
	private void setLocaleDecimalSeparator() {
		m_locale =new Locale("ru", "RU");
		m_nf = NumberFormat.getNumberInstance(m_locale);
	    m_df = (DecimalFormat)m_nf;
	    m_df.setGroupingUsed(false);
	}
	
}
