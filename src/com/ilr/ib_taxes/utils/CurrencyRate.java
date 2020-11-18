package com.ilr.ib_taxes.utils;

import java.util.Date;

public class CurrencyRate {
	Date date;
	Float exchRate;
	public CurrencyRate(Date date, Float exchRate) {
		super();
		this.date = date;
		this.exchRate = exchRate;
	}
	@Override
	public String toString() {
		return "CurrencyRate [date=" + date + ", exchRate=" + exchRate + "]";
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Float getExchRate() {
		return exchRate;
	}
	public void setExchRate(Float exchRate) {
		this.exchRate = exchRate;
	}
	
}
