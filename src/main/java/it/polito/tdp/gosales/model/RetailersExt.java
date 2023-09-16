package it.polito.tdp.gosales.model;

import java.util.Objects;

public class RetailersExt implements Comparable<RetailersExt>{

	private int code;
	private String name;
	private String type;
	private String country;
	private int volume;
	
	public RetailersExt(int code, String name, String type, String country) {
		super();
		this.code = code;
		this.name = name;
		this.type = type;
		this.country = country;
		this.volume = 0;
	}
	
	public RetailersExt(Retailers other, int volume) {
		super();
		this.code = other.getCode();
		this.name = other.getName();
		this.type = other.getType();
		this.country = other.getCountry();
		this.volume = volume;
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	@Override
	public int hashCode() {
		return Objects.hash(code);
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RetailersExt other = (RetailersExt) obj;
		return code == other.code;
	}

	@Override
	public String toString() {
		return name + " --> " + volume;
	}

	@Override
	public int compareTo(RetailersExt o) {
		return o.volume - this.volume;
	}
	
	
	

	
	
}