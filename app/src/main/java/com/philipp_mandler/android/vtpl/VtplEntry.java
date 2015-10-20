package com.philipp_mandler.android.vtpl;

import java.io.Serializable;

public class VtplEntry implements Serializable {
	private static final long serialVersionUID = -2462483326344897841L;
	private Date m_date;
	private String m_lesson;
	private String m_teacher;
	private String m_room;
	private String m_schoolClass;
	private String m_supplyTeacher;
	private String m_supplyRoom;
	private String m_attribute;
	private String m_info;	
	
	public VtplEntry(Date date, String lesson, String teacher, String room, String schoolClass, String supplyTeacher, String supplyRoom, String attribute, String info) {
		m_date = date;
		m_lesson = lesson;
		m_teacher = teacher;
		m_room = room;
		m_supplyRoom = supplyRoom;
		m_schoolClass = schoolClass;
		m_supplyTeacher = supplyTeacher;
		m_attribute = attribute;
		m_info = info;
	}
	
	public VtplEntry() {
		
	}
	
	public String toString() {
		return m_date.toString() + " " + m_lesson + " " + m_teacher + " " + m_room + " " + m_schoolClass + " " + m_supplyTeacher + " " + m_supplyRoom + " " + m_attribute + " " + m_info;
	}

	public Date getDate() {
		return m_date;
	}

	public void setDate(Date date) {
		m_date = date;
	}

	public String getLesson() {
		return m_lesson;
	}

	public void setLesson(String lesson) {
		m_lesson = lesson;
	}

	public String getTeacher() { return m_teacher; }

	public void setTeacher(String teacher) {
		m_teacher = teacher;
	}

	public String getRoom() {
		return m_room;
	}

	public void setRoom(String room) {
		m_room = room;
	}

	public String getSchoolClass() {
		return m_schoolClass;
	}

	public void setSchoolClass(String schoolClass) {
		m_schoolClass = schoolClass;
	}

	public String getSupplyTeacher() {
		return m_supplyTeacher;
	}

	public void setSupplyTeacher(String supplyTeacher) {
		m_supplyTeacher = supplyTeacher;
	}

	public String getSupplyRoom() {
		return m_supplyRoom;
	}

	public void setSupplyRoom(String supplyRoom) {
		m_supplyRoom = supplyRoom;
	}

	public String getAttribute() {
		return m_attribute;
	}

	public void setAttribute(String attribute) {
		m_attribute = attribute;
	}

	public String getInfo() {
		return m_info;
	}

	public void setInfo(String info) {
		m_info = info;
	}
	
	
}
