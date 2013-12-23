package com.msafiullah.splitbill.entity;

import java.util.ArrayList;
import java.util.List;

public class BillItemEntity {

	private int _serialNo;
	private ArrayList<String> _buyers = new ArrayList<String>();
	private float _price;

	/**
	 * Construct a new Bill Item Entity with the particular serial number,
	 * buyers and price.
	 * 
	 * @param serialNo
	 * @param buyers
	 * @param price
	 */
	public BillItemEntity(int serialNo, ArrayList<String> buyers, float price) {
		_serialNo = serialNo;
		_buyers = buyers;
		_price = price;
	}

	/**
	 * Construct a new Bill Item Entity with the particular serial number. Use
	 * this when you want to construct a new Bill Item Entity and probably want
	 * to set price and buyers later.
	 * 
	 * @param serialNo
	 */
	public BillItemEntity(int serialNo) {
		_serialNo = serialNo;
	}

	public int getSerialNo() {
		return _serialNo;
	}

	public void setSerialNo(int serialNo) {
		_serialNo = serialNo;
	}

	public List<String> getBuyers() {
		return _buyers;
	}

	public void addBuyer(String buyer) {
		if (!_buyers.contains(buyer))
			_buyers.add(buyer);
	}

	public boolean hasBuyer(String buyer) {
		return _buyers.contains(buyer);
	}

	public void removeBuyer(String buyer) {
		_buyers.remove(buyer);
	}

	public void removeAllBuyers() {
		_buyers.clear();
	}

	public int getNumBuyers() {
		return _buyers.size();
	}

	public float getPrice() {
		return _price;
	}

	public void setPrice(float price) {
		_price = price;
	}

	/**
	 * Checks if this bill item entity is empty. A bill item is empty if price
	 * is zero AND it doesn't have any buyer.
	 * 
	 * @return true if the bill is empty; false if either the item's price or
	 *         number of buyers are more than zero.
	 */
	public boolean isEmpty() {
		return _buyers.isEmpty() && _price == 0;
	}

	/**
	 * Checks if this bill item is complete (i.e. has BOTH the price AND at
	 * least one buyer).
	 * 
	 * @return true if complete; false if either the price or buyer is missing.
	 */
	public boolean isComplete() {
		return !_buyers.isEmpty() && _price != 0;
	}
}
