package com.ilr.ib_taxes.trades;

import java.util.Date;

public class Trade implements Comparable<Trade>{
	
	private String ticketName;
	
	//it's possible to have fractional shares
	private float quantity;
	private float dealPrice;
	private float exchRate;
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
	
	
	public String getTicketName() {
		return ticketName;
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
	public Trade(String ticketName, float quantity, float dealPrice, float exchRate, boolean bBuySell,
			float fCommission, Date dealDate, Date settleDate, String activeClass, String activeCurrency) {
		super();
		this.ticketName = ticketName;
		this.quantity = quantity;
		this.dealPrice = dealPrice;
		this.exchRate = exchRate;
		this.bBuySell = bBuySell;
		this.fCommission = fCommission;
		this.dealDate = dealDate;
		this.settleDate = settleDate;
		this.activeClass = activeClass;
		this.activeCurrency = activeCurrency;
	}
	public Trade() {
		// TODO Auto-generated constructor stub
	}
	public void setbBuySell(boolean bBuySell) {
		this.bBuySell = bBuySell;
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
	
	public int compareTo(Trade t) {
	    
	    return getDealDate().compareTo(t.getDealDate());
	  }
	public float getAmount() {
		
		float k = 1;
		
		if(activeClass.equals("OPT"))
			k = 100;
		if(!bBuySell)
			k *= -1;
			
		return k * quantity *dealPrice ;
	}
	public float getAmountCur2() {
		
		float k = 1;
		
		if(activeClass.equals("OPT"))
			k = 100;
		if(!bBuySell)
			k *= -1;
			
		return k * quantity *dealPrice * exchRate;
		
	}
	
	public boolean adjustQuantity(float reduce) {
		
	    if(quantity > 0 )
	    	quantity -= reduce;
	    else
	    	quantity += reduce;
	    	
		return quantity == 0? true:false;
		
	}
}
