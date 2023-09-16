package it.polito.tdp.gosales.model;

import java.util.Objects;

public class Arco implements Comparable<Arco>{
	
	private RetailersExt retailer1;
	private RetailersExt retailer2;
	private int peso;
	
	public Arco(RetailersExt retailer1, RetailersExt retailer2, int peso) {
		super();
		this.retailer1 = retailer1;
		this.retailer2 = retailer2;
		this.peso = peso;
	}

	public RetailersExt getRetailer1() {
		return retailer1;
	}

	public void setRetailer1(RetailersExt retailer1) {
		this.retailer1 = retailer1;
	}

	public RetailersExt getRetailer2() {
		return retailer2;
	}

	public void setRetailer2(RetailersExt retailer2) {
		this.retailer2 = retailer2;
	}

	public int getPeso() {
		return peso;
	}

	public void setPeso(int peso) {
		this.peso = peso;
	}

	@Override
	public int hashCode() {
		return Objects.hash(peso, retailer1, retailer2);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Arco other = (Arco) obj;
		return peso == other.peso && retailer1 == other.retailer1 && retailer2 == other.retailer2;
	}

	@Override
	public int compareTo(Arco o) {
		return o.peso - this.peso ;
	}

	@Override
	public String toString() {
		return retailer1.getName() + "   <--->    " + retailer2.getName() + "    |  " + peso;
	}
	
	
	
	
	
	

}
