package com.wibe.backend.requests;

public class Order {
	
	private long num;
	
	private long order;
	
	public Order(long num, long order){
		this.setNum(num);
		this.setOrder(order);
	}

	public long getNum() {
		return num;
	}

	public void setNum(long num) {
		this.num = num;
	}

	public long getOrder() {
		return order;
	}

	public void setOrder(long order) {
		this.order = order;
	}

}
