package com.ilr.ib_taxes.trades;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClosedTrade extends Trade {

	private static final String DATE_FORMAT = "dd/MM/yyyy";
	private static final float TAX_RATE = 0.13f;
	
	public ClosedTrade(String ticketDescription, String ticketName, float quantity, float dealPrice, float exchRate, boolean bBuySell,
			float fCommission, Date dealDate, Date settleDate, String activeClass, String activeCurrency,
			float closingPrice, float closingRate, boolean closingAction, float closingCommission, Date closingDealDate,
			Date closingSettleDate) {
		super(ticketDescription,ticketName, quantity, dealPrice, exchRate, bBuySell, fCommission, dealDate, settleDate, activeClass,
				activeCurrency, false);
		this.closingPrice = closingPrice;
		this.closingRate = closingRate;
		this.closingAction = closingAction;
		this.closingCommission = closingCommission;
		this.closingDealDate = closingDealDate;
		this.closingSettleDate = closingSettleDate;
		
		if(closingAction)
			m_closingAction = "Покупка";
		else
			m_closingAction = "Продажа";

		
		calculateResult();
	}


	public float getClosingPrice() {
		return closingPrice;
	}
	public void setClosingPrice(float closingPrice) {
		this.closingPrice = closingPrice;
	}
	public float getClosingRate() {
		return closingRate;
	}
	public void setClosingRate(float closingRate) {
		this.closingRate = closingRate;
	}
	public boolean isClosingAction() {
		return closingAction;
	}
	public void setClosingAction(boolean closingAction) {
		this.closingAction = closingAction;
	}
	public float getClosingCommission() {
		return closingCommission;
	}
	public void setClosingCommission(float closingCommission) {
		this.closingCommission = closingCommission;
	}
	public Date getClosingDealDate() {
		return closingDealDate;
	}
	public void setClosingDealDate(Date closingDealDate) {
		this.closingDealDate = closingDealDate;
	}
	public Date getClosingSettleDate() {
		return closingSettleDate;
	}
	public void setClosingSettleDate(Date closingSettleDate) {
		this.closingSettleDate = closingSettleDate;
	}
	private float closingPrice;
	private float closingRate;
	
	private boolean closingAction;
	private String m_closingAction;
	private float  closingCommission;
	private Date closingDealDate;
	private Date closingSettleDate;
	
	
	private float dealResultUsd;
	private float dealResult;
	private float m_tax = 0;
	
	public void calculateResult()
	{
		dealResultUsd = -1 *(getClosingAmount() + getAmount()) + closingCommission + getCommission();
		dealResult = -1 *( getClosingAmountCur2() + getAmountCur2())+ closingCommission*closingRate + getCommission()*getExchRate();
		
		m_tax = dealResult * TAX_RATE;
	
	}
	public float getDealResultUsd() {
		return dealResultUsd;
	}
	public float getTax() {
		return m_tax;
	}
	public void setDealResultUsd(float dealResultUsd) {
		this.dealResultUsd = dealResultUsd;
	}
	public float getDealResult() {
		return dealResult;
	}
	public void setDealResult(float dealResult) {
		this.dealResult = dealResult;
	}
	
	public float getClosingAmount() {
		
		float closingAmount = 1;
		
		if(getActiveClass().equals("OPT"))
			closingAmount = 100;
		if(!closingAction)
			closingAmount *= -1;
			
		return closingAmount * Math.abs(getQuantity())  * closingPrice ;
	}
	public float getClosingAmountCur2() {
		float closingAmount = 1;
		
		if(getActiveClass().equals("OPT"))
			closingAmount = 100;
		if(!closingAction)
			closingAmount *= -1;
		return closingAmount *   Math.abs(getQuantity())  * closingPrice * closingRate;
	}
	public String toClosedTaxStr(Date start,Date end) {
		String date1, date2;
		
		DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		
		date1 = formatter.format(this.getDealDate());
		date2 = formatter.format(this.closingDealDate);
		
		if(isClosingDateInRange(this.getDealDate(),this.closingDealDate,start,end)) {
		
			String closedDealStr = getActiveClassName()+  ";"
		                   + getActiveCurrency() + ";" + getLocaleNubmer(getExchRate()) + ";"
		                   + getTicketDescription() + ";"
		                   + getTicketName() + ";"+ date1 + ";" + getAction() + ";"
		                   + getLocaleNubmer(getQuantity()) + ";"
		                   + getLocaleNubmer(getDealPrice()) + ";" + getLocaleNubmer(getDealPrice()* getExchRate()) + ";"
		                   + getLocaleNubmer(getCommission()) + ";" + getLocaleNubmer(getCommission()* getExchRate()) + ";"
		                   + getLocaleNubmer(getAmount())+ ";"
		                   + getLocaleNubmer(this.getAmountCur2())
		                   + "\n ;;" + getLocaleNubmer(getClosingRate())+";;"+ getTicketName() + ";"
		                   + date2 +";" + m_closingAction+ ";" + getLocaleNubmer(-1 * getQuantity()) +";"
		                   + getLocaleNubmer(getClosingPrice()) + ";" + getLocaleNubmer(getClosingPrice()* getClosingRate()) + ";"
		                   + getLocaleNubmer(getClosingCommission()) + ";" 
		                   + getLocaleNubmer(getClosingCommission()* getClosingRate())+";"  
		                   + getLocaleNubmer(getClosingAmount()) + ";"
		                   + getLocaleNubmer(getClosingAmountCur2()) + ";"
		                   + getLocaleNubmer(getDealResultUsd()) + ";" + getLocaleNubmer(getDealResult())+ ";"
		                   + getLocaleNubmer(getTax()) + "\n";
			return closedDealStr;
		}
		else 
			return null;
	}

	public boolean isClosingDateInRange(Date d1,Date d2, Date start, Date end) {
		
		//Get the max date
		Date maxDate = (d1.after(d2)) ? d1 : d2;
		
		if (!maxDate.before (start) && !maxDate.after (end))
			return true;
		else 
			return false;
		
	}
}
