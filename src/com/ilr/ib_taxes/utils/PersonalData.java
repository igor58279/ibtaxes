package com.ilr.ib_taxes.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

public class PersonalData {
	
	        private static final String CB_DATE = "MM/dd/yy";
	
			private  String[] HEADER_LINES = {"\n\n","Период;%s;%s\n",		
			"Имя брокера;Interactive Brokers;Адрес брокера;Two Pickwick Plaza,Greenwich,CT 06830\n",	
			"Имя;%s;%s;%s\n"	,
			"Имя(английский);%s;%s\n",		
			"Счет;%s\n",		
			"Базовая валюта;USD\n",		
			"Тип счета;Индивидуальный\n",	
			"Возможности счета;Маржа","\n\n"};		

			private String m_period_start;
			private String m_period_end;
			private String m_account;
			private String m_first_name;
			private String m_last_name;
			private String m_first_name_rus;
			private String m_last_name_rus;
			private String m_middle_name_rus;
			
			private Date m_start;
			private Date m_end;
			
			
			public PersonalData(Properties appProps) {
				
				m_period_start = appProps.getProperty("period_start");
				m_period_end = appProps.getProperty("period_end");
				m_account = appProps.getProperty("account");
				m_first_name = appProps.getProperty("first_name");
				m_last_name = appProps.getProperty("last_name");
				m_first_name_rus = appProps.getProperty("first_name_rus");
				m_last_name_rus = appProps.getProperty("last_name_rus");
				m_middle_name_rus = appProps.getProperty("middle_name_rus");
				
				//Build header lines
				HEADER_LINES[1] = String.format(HEADER_LINES[1], m_period_start,m_period_end);
				HEADER_LINES[3] = String.format(HEADER_LINES[3], m_last_name_rus,m_first_name_rus,m_middle_name_rus);
				HEADER_LINES[4] = String.format(HEADER_LINES[4], m_last_name,m_first_name);
				
				SimpleDateFormat formatter = new SimpleDateFormat(CB_DATE) ;
				
				try {
					m_start = formatter.parse(m_period_start);
					m_end = formatter.parse(m_period_end);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
			
			
			public Date getStart() {
				return m_start;
			}






			public Date getEnd() {
				return m_end;
			}



		



			public String[] getHeaderLines() {
				return HEADER_LINES;
			}



			



			public String getPeriodStart() {
				return m_period_start;
			}
			public void setPeriodStart(String period_start) {
				this.m_period_start = period_start;
			}
			public String getPeriodEnd() {
				return m_period_end;
			}
			public void setPeriodEnd(String period_end) {
				this.m_period_end = period_end;
			}
			public String getAccount() {
				return m_account;
			}
			public void setAccount(String account) {
				this.m_account = account;
			}
			public String getFirstName() {
				return m_first_name;
			}
			public void setFirstName(String first_name) {
				this.m_first_name = first_name;
			}
			public String getLastName() {
				return m_last_name;
			}
			public void setM_last_name(String last_name) {
				this.m_last_name = last_name;
			}
			public String getFirstNameRus() {
				return m_first_name_rus;
			}
			public void setFirstNameRus(String first_name_rus) {
				this.m_first_name_rus = first_name_rus;
			}
			public String getLastNameRus() {
				return m_last_name_rus;
			}
			public void setLastNameRus(String last_name_rus) {
				this.m_last_name_rus = last_name_rus;
			}
			public String getMiddleNameRus() {
				return m_middle_name_rus;
			}
			public void setMiddleNameRus(String middle_name_rus) {
				this.m_middle_name_rus = middle_name_rus;
			}
			
			
			
}
