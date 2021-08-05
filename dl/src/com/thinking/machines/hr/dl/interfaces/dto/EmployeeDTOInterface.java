package com.thinking.machines.hr.dl.interfaces.dto;
import java.math.*;
import java.util.*;
import com.thinking.machines.enums.*;
public interface EmployeeDTOInterface extends Comparable<EmployeeDTOInterface>,java.io.Serializable
{
public void setEmployeeId(String employeeId);
public void setName(String name);
public void setDesignationCode(int designationCode);
public void setDateOfBirth(Date dateOfBirth);
public void setGender(GENDER gender);
public void setIsIndian(boolean isIndian);
public void setBasicSalary(BigDecimal basicSalary);
public void setPANNumber(String panNumber);
public void setAadharCardNumber(String aadharCardNumber);
public String getEmployeeId();
public String getName();
public int getDesignationCode();
public Date getDateOfBirth();
public char getGender();
public boolean getIsIndian();
public BigDecimal getBasicSalary();
public String getPANNumber();
public String getAadharCardNumber();
}