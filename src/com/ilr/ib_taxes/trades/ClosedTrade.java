package com.ilr.ib_taxes.trades;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClosedTrade extends Trade {

	private static final String DATE_FORMAT = "dd/MM/yyyy";
	private static final float TAX_RATE = 0.13f;
	
	public ClosedTrade(String ticketName, float quantity, float dealPrice, float exchRate, boolean bBuySell,
			float fCommission, Date dealDate, Date settleDate, String activeClass, String activeCurrency,
			float closingPrice, float closingRate, boolean closingAction, float closingCommission, Date closingDealDate,
			Date closingSettleDate) {
		super(ticketName, quantity, dealPrice, exchRate, bBuySell, fCommission, dealDate, settleDate, activeClass,
				activeCurrency);
		this.closingPrice = closingPrice;
		this.closingRate = closingRate;
		this.closingAction = closingAction;
		this.closingCommission = closingCommission;
		this.closingDealDate = closingDealDate;
		this.closingSettleDate = closingSettleDate;
		
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
			
		return closingAmount * getQuantity()  * closingPrice ;
	}
	public float getClosingAmountCur2() {
		float closingAmount = 1;
		
		if(getActiveClass().equals("OPT"))
			closingAmount = 100;
		if(!closingAction)
			closingAmount *= -1;
		return closingAmount*  getQuantity()  * closingPrice * closingRate;
	}
	public String toClosedTaxStr() {
		String date1, date2;
		
		DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		
		date1 = formatter.format(this.getDealDate());
		date2 = formatter.format(this.closingDealDate);
		
		String closedDealStr = getActiveClassName()+  ";"
		                   + getActiveCurrency() + ";" + getLocaleNubmer(getExchRate()) + ";"
		                   + getTicketName() + ";"+ date1 + ";"+ getLocaleNubmer(getQuantity()) + ";"
		                   + getLocaleNubmer(getDealPrice()) + ";" + getLocaleNubmer(getDealPrice()* getExchRate()) + ";"
		                   + getLocaleNubmer(getCommission()) + ";" + getLocaleNubmer(getCommission()* getExchRate()) + ";"
		                   + getLocaleNubmer(getQuantity()* getDealPrice())+ ";"
		                   + getLocaleNubmer(getQuantity()* getDealPrice() * getExchRate())
		                   + "\n ;;" + getLocaleNubmer(getClosingRate())+";;"
		                   + date2 +";" + getLocaleNubmer(-1 * getQuantity()) +";"
		                   + getLocaleNubmer(getClosingPrice()) + ";" + getLocaleNubmer(getClosingPrice()* getClosingRate()) + ";"
		                   + getLocaleNubmer(getClosingCommission()) + ";" 
		                   + getLocaleNubmer(getClosingCommission()* getClosingRate())+";"
		                   + getLocaleNubmer(-1 * getQuantity()* getClosingPrice()) + ";"
		                   + getLocaleNubmer(-1 * getQuantity()* getClosingPrice() * getClosingRate()) + ";"
		                   + getLocaleNubmer(getDealResultUsd()) + ";" + getLocaleNubmer(getDealResult())+ ";"
		                   + getLocaleNubmer(getTax()) + "\n";
		return closedDealStr;
	}

}
