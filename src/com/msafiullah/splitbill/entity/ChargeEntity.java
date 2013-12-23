package com.msafiullah.splitbill.entity;

public class ChargeEntity {

	public static final int CHARGE_TYPE_PERCENT = 21;
	public static final int CHARGE_TYPE_DOLLAR = 22;

	private static final float HUNDRED_PERCENT = 100.0f;

	private float _value; // charge value expressed in dollars or percentage
	private int _type;

	public ChargeEntity() {
		_value = 0.0f;
		_type = CHARGE_TYPE_PERCENT;
	}

	public ChargeEntity(float value, int type) {
		_value = value;
		_type = type;
	}

	public float getValue() {
		return _value;
	}

	public void setValue(float value) {
		_value = value;
	}

	public int getType() {
		return _type;
	}

	public void setType(int type) {
		_type = type;
	}

	public float computeChargeForTotal(float total) {
		switch (_type) {
		default:
		case CHARGE_TYPE_DOLLAR:
			return _value;
		case CHARGE_TYPE_PERCENT:
			return _value / HUNDRED_PERCENT * total;
		}
	}

	public float computeChargePerBuyerForTotal(int numBuyersForBill, float total) {
		try {
			switch (_type) {
			default:
			case CHARGE_TYPE_DOLLAR:
				return _value / numBuyersForBill;
			case CHARGE_TYPE_PERCENT:
				return _value / HUNDRED_PERCENT * total;
			}
		} catch (ArithmeticException ae) {
			return 0;
		}

	}

	/**
	 * Get the alternate value of this Charge Entity expressed either in
	 * percentage or dollars.
	 * 
	 * @param total
	 * @return value - If the type of this Charge Entity is PERCENT, the value
	 *         returned is in dollars. If the type of this Charge Entity is
	 *         DOLLARS, the value returned is in percentage. If total is zero or
	 *         negative zero is returned;
	 */
	public float computeAltValueForTotal(float total) {
		if (total > 0) {
			switch (_type) {
			default:
			case CHARGE_TYPE_DOLLAR:
				return _value / total * HUNDRED_PERCENT; // return in percentage
			case CHARGE_TYPE_PERCENT:
				return _value / HUNDRED_PERCENT * total; // return in dollars
			}
		}
		return 0;
	}

	@Override
	public String toString() {
		return "ChargeEntity [_value=" + _value + ", _type=" + _type + "]";
	}

}
