package com.ilr.ib_taxes.trades;

import java.util.Date;

public class ClosedTrade extends Trade {

	
	
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
	
	public void calculateResult()
	{
		dealResultUsd = -1 *(getClosingAmount() + getAmount()) + closingCommission + getCommission();
		dealResult = -1 *( getClosingAmountCur2() + getAmountCur2())+ closingCommission*closingRate + getCommission()*getExchRate();

	}
	public float getDealResultUsd() {
		return dealResultUsd;
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
		
		return closingAction?getQuantity()  * closingPrice : -1 * getQuantity()  * closingPrice;
	}
	public float getClosingAmountCur2() {
		return closingAction? getQuantity()  * closingPrice * closingRate: -1 * getQuantity()  * closingPrice * closingRate;
	}
	public void printClosedDeal() {
		String closedDeal = getTicketName() + "," + getQuantity() + ", " + (isbBuySell()?"BUY":"SELL") +"," + "DATE1" + "," + getExchRate() + "," + getCommission()
					    + (isbBuySell()?"SELL":"BUY")+ "," + "DATE2" + ",closing price="+ closingPrice +",closingRate=" + closingRate + "," + closingCommission 
					    + ", resultUsd="+ dealResultUsd +",closing result=" + dealResult;
		System.out.println( closedDeal);
	}
		
}
