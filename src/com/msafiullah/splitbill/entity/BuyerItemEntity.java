package com.msafiullah.splitbill.entity;

public class BuyerItemEntity {
	private String _buyer;
	private float _totalAmtPayable;

	public BuyerItemEntity(String buyer, float totalAmtPayable) {
		super();
		_buyer = buyer;
		_totalAmtPayable = totalAmtPayable;
	}

	public String getBuyer() {
		return _buyer;
	}

	public float getAmountPayable() {
		return _totalAmtPayable;
	}

	@Override
	public String toString() {
		return "BuyerItemEntity [_buyer=" + _buyer + ", _totalAmtPayable="
				+ _totalAmtPayable + "]";
	}

}
