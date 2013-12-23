package com.msafiullah.splitbill.entity;

import java.util.ArrayList;

public class BillItemDTO {
	private int itemID;
	private float price;
	private ArrayList<String> buyers;

	public BillItemDTO(int itemID, float price, ArrayList<String> buyers) {
		super();
		this.buyers = buyers;
		this.price = price;
		this.itemID = itemID;
	}

	public ArrayList<String> getBuyers() {
		return buyers;
	}

	public float getPrice() {
		return price;
	}

	public int getItemID() {
		return itemID;
	}

	@Override
	public String toString() {
		return "ItemEntity ["
				+ (buyers != null ? "buyers=" + buyers + ", " : "") + "price="
				+ price + ", itemID=" + itemID + "]";
	}

}
