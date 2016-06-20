package com.rb.accounts.service.pdf;

public class TableData {

	private String sno;
	private String itemdesc;
	private String mrp;
	private String qty;
	private String rate;

	public TableData(String sno, String itemdesc, String mrp, String qty, String rate) {
		this.sno = sno;
		this.itemdesc = itemdesc;
		this.mrp = mrp;
		this.qty = qty;
		this.rate = rate;
	}

	public String getSno() {
		return sno;
	}

	public void setSno(String sno) {
		this.sno = sno;
	}

	public String getItemdesc() {
		return itemdesc;
	}

	public void setItemdesc(String itemdesc) {
		this.itemdesc = itemdesc;
	}

	public String getMrp() {
		return mrp;
	}

	public void setMrp(String mrp) {
		this.mrp = mrp;
	}

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

}
