package com.sbigeneral.LoginCapatchePoc.Entity;

import javax.persistence.*;

@Entity
@Table(name = "tbl_Vendor_Master")
public class UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private int id;
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "VENDORCODE")
	private String employeeId;
	
	@Column(name = "EMAILID")
	private String emailId;
	
	@Column(name = "PASSWORD")
	private String password;
	
	@Column(name="MOBILENUMBER")
	private String mobileNumber;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public UserDetails() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserDetails(int id, String name, String employeeId, String emailId, String password, String mobileNumber) {
		super();
		this.id = id;
		this.name = name;
		this.employeeId = employeeId;
		this.emailId = emailId;
		this.password = password;
		this.mobileNumber = mobileNumber;
	}

	@Override
	public String toString() {
		return "UserDetails [id=" + id + ", name=" + name + ", employeeId=" + employeeId + ", emailId=" + emailId
				+ ", password=" + password + ", mobileNumber=" + mobileNumber + "]";
	}
	
	
	
	
	
}
