package com.ilr.ib_taxes.payments;

import java.util.Date;


public class CashTransaction implements Comparable<CashTransaction> {
	
	private String m_ticker;
	private String m_description;
	private float m_exchRate;
	private float m_amount;
	private Date m_cashDate;
	private String m_transType;
	private String m_Currency;
	
	
	public CashTransaction(String m_ticker, String m_description, float m_exchRate, float m_amount, Date m_cashDate,
			String m_transType, String m_Currency) {
		super();
		this.m_ticker = m_ticker;
		this.m_description = m_description;
		this.m_exchRate = m_exchRate;
		this.m_amount = m_amount;
		this.m_cashDate = m_cashDate;
		this.m_transType = m_transType;
		this.m_Currency = m_Currency;
	}
	public CashTransaction()
	{
		
	}
	
	public String getTicker() {
		return m_ticker;
	}
	public void setTicker(String ticker) {
		this.m_ticker = ticker;
	}
	public String getDescription() {
		return m_description;
	}
	public void setDescription(String description) {
		this.m_description = description;
	}
	public float getExchRate() {
		return m_exchRate;
	}
	public void setExchRate(float exchRate) {
		this.m_exchRate = exchRate;
	}
	public float getAmount() {
		return m_amount;
	}
	public void setAmount(float amount) {
		this.m_amount = amount;
	}
	public Date getCashDate() {
		return m_cashDate;
	}
	public void setCashDate(Date cashDate) {
		this.m_cashDate = cashDate;
	}
	public String getTransType() {
		return m_transType;
	}
	public void setTransType(String transType) {
		this.m_transType = transType;
	}
	public String getCurrency() {
		return m_Currency;
	}
	public void setCurrency(String currency) {
		this.m_Currency = currency;
	}
	@Override
	public String toString() {
		return "CashTransaction [m_ticker=" + m_ticker + ", m_description=" + m_description + ", m_exchRate="
				+ m_exchRate + ", m_amount=" + m_amount + ", m_cashDate=" + m_cashDate + ", m_transType=" + m_transType
				+ ", m_Currency=" + m_Currency + "]";
	}
	
	
	public int compareTo(CashTransaction t) {
	    
	    return getCashDate().compareTo(t.getCashDate());
	  }
}
