package com.ilr.ib_taxes.payments;

import java.util.Date;

//ClosedDivTransaction assumes that on one day may be only two cash activities for the ticker dividend and 
//it's probably not the case , but let's start from it
public class ClosedDivTransaction extends CashTransaction{
	
	private static final float TAX_RATE = 0.13f;
	
	private String m_closedDescription;
	private float m_closedAmount;
	private String m_closedTransType;
	
	private float m_closedTaxRate;
	private float m_closedTaxBrokerRate;
	private float m_closedTax;
	private float m_closedTaxUsd;
	
	
	private float m_paidAmount;
	private float m_TaxAtPlace;
	
	
	
	
	
	public ClosedDivTransaction(String ticker, String description, float exchRate, float amount,
			Date cashDate, String transType, String currency) {
		super(ticker, description, exchRate, amount, cashDate, transType, currency);
		m_closedDescription = null;
		m_closedAmount = 0.0f;
		m_closedTransType = null;
		m_closedTaxRate = 0.0f;
		m_closedTax = 0.0f;
		m_closedTaxUsd = 0.0f;
	}
	public String getClosedDescription() {
		return m_closedDescription;
	}
	public void setClosedDescription(String closedDescription) {
		this.m_closedDescription = closedDescription;
	}
	public float getClosedAmount() {
		return m_closedAmount;
	}
	public void setClosedAmount(float closedAmount) {
		this.m_closedAmount = closedAmount;
	}
	public String getClosedTransType() {
		return m_closedTransType;
	}
	public void setClosedTransType(String closedTransType) {
		this.m_closedTransType = closedTransType;
	}
	public float getTaxUsd() {
		return m_closedTaxUsd;
	}
	public void setTaxUsd(float taxUsd) {
		this.m_closedTaxUsd = taxUsd;
	}
	public float getResult() {
		return m_closedTax;
	}
	public void setClosedTax(float closedTax) {
		this.m_closedTax = closedTax;
	}
	
	@Override
	public String toString() {
		return "ClosedDivTransaction [m_closedDescription=" + m_closedDescription + ", m_closedAmount=" + m_closedAmount
				+ ", m_closedTransType=" + m_closedTransType + ", m_closedTaxUsd=" + m_closedTaxUsd + ", m_closedTaxRate="
				+ m_closedTaxRate + ", m_closedTax=" + m_closedTax + ", toString()=" + super.toString() + "]";
	}
	
	public void calculateTax(){
		//Assume that tax is always less that amount
		m_TaxAtPlace = Math.min(Math.abs(m_closedAmount),Math.abs(getAmount()));
		m_paidAmount = Math.max(Math.abs(m_closedAmount),Math.abs(getAmount()));
		
		//later check sign/action type to be sure
		m_closedTaxBrokerRate = m_TaxAtPlace/m_paidAmount;
		m_closedTaxRate = m_closedTaxBrokerRate - TAX_RATE;
		if( m_closedTaxRate < 0) {
			m_closedTaxRate *= -1;
			m_closedTaxUsd = m_paidAmount * m_closedTaxRate;
			m_closedTax = m_closedTaxUsd * getExchRate();
		}
		else {
			m_closedTaxRate = 0;
		}
	}
	public float getPaidAmount() {
		return m_paidAmount;
	}

	public float getTaxAtPlace() {
		return m_TaxAtPlace;
	}
	public float getClosedTaxRate() {
		return m_closedTaxRate;
	}
	public float getClosedTaxBrokerRate() {
		return m_closedTaxBrokerRate;
	}

	public float getClosedTax() {
		return m_closedTax;
	}
	
	
}
