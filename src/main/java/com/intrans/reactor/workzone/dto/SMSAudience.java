package com.intrans.reactor.workzone.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * SMS receiver information.
 * 
 * @author Vamsi Krishna J <br />
 *         <b>Date:</b> Apr 4, 2017
 *
 */
public class SMSAudience implements Serializable {

	private static final long serialVersionUID = -7763932787006986500L;

	private String firstName;
	private String lastName;
	private String email;
	private String mobileNumber;
	private List<String> workzones = new ArrayList<String>();
	private boolean sendAll;

	public SMSAudience() {
	}

	public SMSAudience(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public List<String> getWorkzones() {
		return workzones;
	}

	public void setWorkzones(List<String> workzones) {
		if (workzones != null) {
			this.workzones = workzones;
		}
	}

	public boolean isSendAll() {
		return sendAll;
	}

	public void setSendAll(boolean sendAll) {
		this.sendAll = sendAll;
	}

}
