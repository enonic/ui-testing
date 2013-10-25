package com.enonic.autotests.facets;

import javax.xml.bind.annotation.XmlType;

@XmlType( name = "person")
public class Person
{

	private String firstname;

	private String lastname;
	private String birthdate;
	private String nationality;
	private int balance;
	private String gender;
	public String getFirstname()
	{
		return firstname;
	}
	public void setFirstname(String firstname)
	{
		this.firstname = firstname;
	}
	public String getLastname()
	{
		return lastname;
	}
	public void setLastname(String lastname)
	{
		this.lastname = lastname;
	}
	public String getBirthdate()
	{
		return birthdate;
	}
	public void setBirthdate(String birthdate)
	{
		this.birthdate = birthdate;
	}
	public String getNationality()
	{
		return nationality;
	}
	public void setNationality(String nationality)
	{
		this.nationality = nationality;
	}
	public int getBalance()
	{
		return balance;
	}
	public void setBalance(int balance)
	{
		this.balance = balance;
	}
	public String getGender()
	{
		return gender;
	}
	public void setGender(String gender)
	{
		this.gender = gender;
	}

}
