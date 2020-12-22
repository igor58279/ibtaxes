package com.ilr.ib_taxes.trades;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Trade implements Comparable<Trade>{
	private static final String DATE_FORMAT = "dd/MM/yyyy";
	private String ticketName;
	private String m_ticketDesctiption;
	
	//it's possible to have fractional shares
	private float quantity;
	private float dealPrice;
	private float exchRate;
	
	Locale m_locale; 
	NumberFormat m_nf;
	DecimalFormat m_df;

	
	
	public float getExchRate() {
		return exchRate;
	}
	
	public void setExchRate(float exchRate) {
		this.exchRate = exchRate;
	}
	private boolean bBuySell;
	private float  fCommission;
	private Date dealDate;
	private Date settleDate;
	private String activeClass;
	private String activeCurrency;
	private String m_action;
	
	private boolean m_bCorpAction;
	
	public boolean isCorpAction() {
		return m_bCorpAction;
	}

	public void setCorpAction(boolean bCorpAction) {
		m_bCorpAction = bCorpAction;
	}
	DateFormat m_formatter = new SimpleDateFormat(DATE_FORMAT);
	
	
	public String getTicketName() {
		return ticketName;
	}
	
	public String getTicketDescription() {
		return m_ticketDesctiption;
	}
	public void setTicketDescription(String ticketDescription) {
		this.m_ticketDesctiption = ticketDescription;
	}
	public void setTicketName(String ticketName) {
		this.ticketName = ticketName;
	}
	public float getQuantity() {
		return quantity;
	}
	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}
	public float getDealPrice() {
		return dealPrice;
	}
	public void setDealPrice(float dealPrice) {
		this.dealPrice = dealPrice;
	}
	public boolean isbBuySell() {
		return bBuySell;
	}
	public Trade(String ticketDescription,String ticketName, float quantity, float dealPrice, float exchRate, boolean bBuySell,
			float fCommission, Date dealDate, Date settleDate, String activeClass, String activeCurrency, boolean bCorpAction) {
		super();
		this.ticketName = ticketName;
		this.m_ticketDesctiption = ticketDescription;
		this.quantity = quantity;
		this.dealPrice = dealPrice;
		this.exchRate = exchRate;
		
		setbBuySell(bBuySell);
		
		
			
		this.fCommission = fCommission;
		this.dealDate = dealDate;
		this.settleDate = settleDate;
		this.activeClass = activeClass;
		this.activeCurrency = activeCurrency;
		
		m_bCorpAction = bCorpAction;
		setLocaleDecimalSeparator();
   		
	}
	
	public Trade(String ticketDescription,String ticketName, float quantity, float dealPrice, boolean bBuySell,
			Date dealDate, String activeClass, String activeCurrency, boolean bCorpAction) {
		super();
		this.ticketName = ticketName;
		this.m_ticketDesctiption = ticketDescription;
		this.quantity = quantity;
		this.dealPrice = dealPrice;
		
		setbBuySell(bBuySell);
		
		
			
		this.dealDate = dealDate;
		this.settleDate = dealDate;
		this.activeClass = activeClass;
		this.activeCurrency = activeCurrency;
		
		setLocaleDecimalSeparator();
   		
		m_bCorpAction = bCorpAction;
	}
	
	public String getAction() {
		return m_action;
	}

	public void setAction(String action) {
		this.m_action = action;
	}

	private void setLocaleDecimalSeparator() {
		m_locale =new Locale("ru", "RU");
		m_nf = NumberFormat.getNumberInstance(m_locale);
	    m_df = (DecimalFormat)m_nf;
	    m_df.setGroupingUsed(false);
	}
	public Trade() {
		m_bCorpAction = false;
		setLocaleDecimalSeparator(); 
	}
	public void setbBuySell(boolean bBuySell) {
		this.bBuySell = bBuySell;
		if(bBuySell)
			m_action = "Покупка";
		else
			m_action = "Продажа";
	}
	public float getCommission() {
		return fCommission;
	}
	public void setCommission(float commission) {
		this.fCommission = commission;
	}
	public Date getDealDate() {
		return dealDate;
	}
	public void setDealDate(Date dealDate) {
		this.dealDate = dealDate;
	}
	public Date getSettleDate() {
		return settleDate;
	}
	public void setSettleDate(Date settleDate) {
		this.settleDate = settleDate;
	}
	
	public String getActiveClass() {
		return activeClass;
	}
	public void setActiveClass(String activeClass) {
		this.activeClass = activeClass;
	}
	public String getActiveCurrency() {
		return activeCurrency;
	}
	public void setActiveCurrency(String activeCurrency) {
		this.activeCurrency = activeCurrency;
	}
	@Override
	public String toString() {
		return "Trade [ticketName=" + ticketName + ", quantity=" + quantity + ", dealPrice=" + dealPrice + ", exchRate="
				+ exchRate + ", bBuySell=" + bBuySell + ", commission=" + fCommission + ", dealDate=" + dealDate
				+ ", settleDate=" + settleDate + ", activeClass=" + activeClass + ", activeCurrency=" + activeCurrency
				+ "]";
	}
	public String toTaxString() {
		String date;
		DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		date = formatter.format(this.getDealDate());
		String activeTradeString =  getActiveClassName()+  ";"
			                   + getActiveCurrency() + ";" + getLocaleNubmer(getExchRate())+ ";"
			                   + getTicketDescription()+ ";"
			                   + getTicketName() + ";"+ date + ";" + getAction() + ";"
			                   + getLocaleNubmer(getQuantity())+ ";"
			                   + getLocaleNubmer(getDealPrice()) + ";"
			                   + getLocaleNubmer(getDealPrice()* getExchRate())+ ";"
			                   + getLocaleNubmer(getCommission()) + ";" + getLocaleNubmer(getCommission()* getExchRate())+ ";"
			                   + getLocaleNubmer(getQuantity()* getDealPrice())+ ";"
			                   + getLocaleNubmer(getQuantity()* getDealPrice() * getExchRate())+ "\n";
			          
			return activeTradeString;
			                   		
	}
	public int compareTo(Trade t) {
	    
	    return getDealDate().compareTo(t.getDealDate());
	  }
	public float getAmount() {
		
		float k = 1;
		
		if(activeClass.equals("OPT"))
			k = 100;

			
		return k * quantity * dealPrice ;
	}
	public float getAmountCur2() {
		
		float k = 1;
		
		if(activeClass.equals("OPT"))
			k = 100;

			
		return k * quantity * dealPrice * exchRate;
		
	}
	
	public boolean adjustQuantity(float reduce) {
		
	    if(quantity > 0 )
	    	quantity -= reduce;
	    else
	    	quantity += reduce;
	    
	    //We've used commission in reduced list
	    //now we zero it
	    fCommission = 0.0f;
	    	
		return quantity == 0? true:false;
		
	}
	public String getActiveClassName() {
		return getActiveClass().equals("STK")?"Акция":"Опцион";
	}
	
	public String getLocaleNubmer(float num) {
		return m_df.format(num);
	}
}
