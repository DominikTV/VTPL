package com.philipp_mandler.android.vtpl;

import java.io.Serializable;

public class Date implements Serializable {

	private static final long serialVersionUID = -3092371141685591787L;
	private int m_day;
	private int m_month;
	private int m_year;
	private Weekday m_weekday;
	
	
	public Date() {
		m_day = 0;
		m_month = 0;
		m_year = 0;
		calculateWeekday();
	}
	
	public Date(String date) throws Exception {
		String[] parsed = date.split("\\.");
		if(parsed.length == 3) {
			if(Integer.valueOf(parsed[0]) == null || Integer.valueOf(parsed[1]) == null || Integer.valueOf(parsed[2]) == null) {
				throw new Exception("String wrong formated.");
			}
			
			m_day = Integer.valueOf(parsed[0]);
			m_month = Integer.valueOf(parsed[1]);
			m_year = Integer.valueOf(parsed[2]);
			calculateWeekday();
		}		
	}
	
	public Date(int day, int month, int year) {
		m_day = day;
		m_month = month;
		m_year = year;
		calculateWeekday();		
	}
	
	private void calculateWeekday() {
		
		int month, year = m_year;
		 
		if(m_month <= 2) {
			month = m_month + 10;
			year = m_year - 1;
		}
		else
			month = m_month - 2;
 
		int c = year / 100;
		int y = year % 100;
 
		int h = (((26 * month - 2) / 10) + m_day + y + y / 4 + c / 4 - 2 * c) % 7;
 
		if(h < 0)
			h = h + 7;
 
		switch(h)
		{
		case 0 : m_weekday = Weekday.Sunday; break;
		case 1 : m_weekday = Weekday.Monday; break;
		case 2 : m_weekday = Weekday.Tuesday; break;
		case 3 : m_weekday = Weekday.Wednesday; break;
		case 4 : m_weekday = Weekday.Thursday; break;
		case 5 : m_weekday = Weekday.Friday; break;
		case 6 : m_weekday = Weekday.Saturday; break;     
		}
	}
	
	public void setDate(String date) throws Exception {
		String[] parsed = date.split("\\.");
		if(parsed.length == 3) {
			if(Integer.getInteger(parsed[0]) == null || Integer.getInteger(parsed[1]) == null || Integer.getInteger(parsed[2])== null) {
				throw new Exception("String wrong formated.");
			}
			
			m_day = Integer.getInteger(parsed[0]);
			m_month = Integer.getInteger(parsed[1]);
			m_year = Integer.getInteger(parsed[2]);
			calculateWeekday();
		}		
	}
	
	public int getDay() {
		return m_day;
	}
	
	public void setDay(int day) {
		m_day = day;
		calculateWeekday();
	}
	
	public int getMonth() {
		return m_month;
	}
	
	public void setMonth(int month) {
		m_month = month;
		calculateWeekday();
	}
	
	public int getYear() {
		return m_year;
	}
	public void setYear(int year) {
		m_year = year;
		calculateWeekday();
	}

	public Weekday getWeekday() {
		return m_weekday;
	}
	
	public String toString() {
		return m_day + "." + m_month + "." + m_year;
	}
	
	public boolean equals(Date date) {
		return m_year == date.m_year && m_month == date.m_month && m_day == date.m_day;
	}
}
