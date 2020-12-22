package com.ilr.ib_taxes.trades;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


//Now it looks  extremely close to Trade class
//But it still can hold surprises
//But anyway classes should be designed normally and Copy/Paste is only my laziness 
// and absence of coding practice

public class CorpAction implements Comparable<CorpAction> {
	
	private static final String DATE_FORMAT = "dd/MM/yyyy";
	private String m_assetClass;
	private String m_ticketName;
	private String m_actionDescription;
	private String m_currency;
	
	private float m_exchRate;
	
	//it's possible to have fractional shares
	private float m_quantity;
	private float m_value;
	
	Locale m_locale; 
	NumberFormat m_nf;
	DecimalFormat m_df;

	private Date m_ActionDate;
	DateFormat m_formatter = new SimpleDateFormat(DATE_FORMAT);
	


	private void setLocaleDecimalSeparator() {
		m_locale =new Locale("ru", "RU");
		m_nf = NumberFormat.getNumberInstance(m_locale);
	    m_df = (DecimalFormat)m_nf;
	    m_df.setGroupingUsed(false);
	}
	
	public CorpAction() {
		setLocaleDecimalSeparator(); 
	}


	public void setCurrency(String currency) {
		m_currency = currency;
		
	}
	public String getCurrency() {
		return m_currency ;
		
	}
	
	public void setActionDate(Date actionDate) {
		m_ActionDate = actionDate;
		
	}
	public Date getActionDate() {
		
		return m_ActionDate;
	}

	@Override
	public int compareTo(CorpAction o) {
		return getActionDate().compareTo(o.getActionDate());
	}

	public String getTicketName() {
		return m_ticketName;
	}
	
	public float getExchRate() {
		return m_exchRate;
	}
	
	public void setExchRate(float exchRate) {
		m_exchRate = exchRate;
	}
	public void setTicketName(String ticketName) {
		m_ticketName = ticketName;
	}
	public String getActionDescription() {
		return m_actionDescription;
	}
	public void setActionDescription(String actionDescription) {
		this.m_actionDescription = actionDescription;
	}
	
	public float getQuantity() {
		return m_quantity;
	}
	public void setQuantity(float quantity) {
		this.m_quantity = quantity;
	}
	public float getValue() {
		return m_value;
	}
	public void setValue(float value) {
		m_value = value;
	}	
	

	

	@Override
	public String toString() {
		return "CorpAction [m_assetClass=" + m_assetClass + ", m_ticketName=" + m_ticketName + ", m_actionDescription="
				+ m_actionDescription + ", m_currency=" + m_currency + ", m_quantity=" + m_quantity + ", m_value="
				+ m_value + ", m_ActionDate=" + m_ActionDate + "]";
	}

	public void setAssetClass(String assetClass) {
		m_assetClass = assetClass;
		
	}
	public String getAssetClass() {
		return m_assetClass;
	}
}
