package com.thinking.machines.hr.bl.interfaces.pojo;
import java.math.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import java.util.*;
import com.thinking.machines.enums.*;
public interface EmployeeInterface extends Comparable<EmployeeInterface>,java.io.Serializable
{
public void setEmployeeId(String employeeId);
public void setName(String name);
public void setDesignation(DesignationInterface designation);
public void setDateOfBirth(Date dateOfBirth);
public void setGender(GENDER gender);
public void setIsIndian(boolean isIndian);
public void setBasicSalary(BigDecimal basicSalary);
public void setPANNumber(String panNumber);
public void setAadharCardNumber(String aadharCardNumber);
public String getEmployeeId();
public String getName();
public DesignationInterface getDesignation();
public Date getDateOfBirth();
public char getGender();
public boolean getIsIndian();
public BigDecimal getBasicSalary();
public String getPANNumber();
public String getAadharCardNumber();
}