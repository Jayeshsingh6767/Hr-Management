package com.thinking.machines.hr.bl.manager;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.manager.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.enums.*;
import java.util.*;
import java.math.*;
public class EmployeeManager implements EmployeeManagerInterface
{
private Map<String,EmployeeInterface> employeeIdWiseEmployeesMap;
private Map<String,EmployeeInterface> panNumberWiseEmployeesMap;
private Map<String,EmployeeInterface> aadharCardNumberWiseEmployeesMap;
private Set<EmployeeInterface> employeesSet;
private Map<Integer,Set<EmployeeInterface>> designationCodeWiseEmployeesMap;
private static EmployeeManager employeeManager=null;
private EmployeeManager() throws BLException
{	
populateDataStructures();
} 
private void populateDataStructures() throws BLException
{
employeeIdWiseEmployeesMap=new HashMap<>();
panNumberWiseEmployeesMap=new HashMap<>();
aadharCardNumberWiseEmployeesMap=new HashMap<>();
employeesSet=new TreeSet<>();
designationCodeWiseEmployeesMap=new HashMap<>();
Set<EmployeeDTOInterface> dlEmployees;
try
{
dlEmployees=new EmployeeDAO().getAll();
EmployeeInterface employee;
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
DesignationInterface designation;
Set<EmployeeInterface> es;
for(EmployeeDTOInterface dlEmployee:dlEmployees)
{
employee=new Employee();
employee.setEmployeeId(dlEmployee.getEmployeeId());
employee.setName(dlEmployee.getName());
designation=((DesignationManager)designationManager).getDSDesignationByCode(dlEmployee.getDesignationCode());
employee.setDesignation(designation); 
employee.setDateOfBirth(dlEmployee.getDateOfBirth());
if(dlEmployee.getGender()=='M')
{
employee.setGender(GENDER.MALE);
}
else
{
employee.setGender(GENDER.FEMALE);
}
employee.setIsIndian(dlEmployee.getIsIndian());
employee.setBasicSalary(dlEmployee.getBasicSalary());
employee.setPANNumber(dlEmployee.getPANNumber());
employee.setAadharCardNumber(dlEmployee.getAadharCardNumber());
employeeIdWiseEmployeesMap.put(employee.getEmployeeId().toUpperCase(),employee);
panNumberWiseEmployeesMap.put(employee.getPANNumber().toUpperCase(),employee);
aadharCardNumberWiseEmployeesMap.put(employee.getAadharCardNumber().toUpperCase(),employee);
employeesSet.add(employee);
es=designationCodeWiseEmployeesMap.get(designation.getCode());
if(es==null)
{
es=new TreeSet();
es.add(employee);
designationCodeWiseEmployeesMap.put(designation.getCode(),es);
}else
{
es.add(employee);
}
}
}catch(DAOException daoException)
{
BLException blException=new BLException();
blException.setGenericException(daoException.getMessage());
throw blException;
}
}
public static EmployeeManagerInterface getEmployeeManager()throws BLException
{
if(employeeManager==null)employeeManager=new EmployeeManager();
return employeeManager;
}







public void addEmployee(EmployeeInterface employee)throws BLException
{
BLException blException=new BLException();
if(employee==null)
{
blException.setGenericException("Employee is null");
throw blException;
}
String name=employee.getName();
if(name==null)
{
blException.addException("name","name required");
}else
{
name=name.trim();
if(name.length()==0)blException.addException("name","name required");
}
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
DesignationInterface designation=employee.getDesignation();
int designationCode=0;
if(designation==null)
{
blException.addException("Designation","Designation required");
}
else
{
designationCode=designation.getCode();
if(designationManager.designationCodeExists(designationCode)==false)
blException.addException("Designation","Invalid Designation");
}
Date dateOfBirth=employee.getDateOfBirth();
if(dateOfBirth==null)
{
blException.addException("dateOfBirth","Date of birth required");
}
char gender=employee.getGender();
if(gender==' ')
{
blException.addException("gender","gender required");
}
boolean isIndian=employee.getIsIndian();
BigDecimal basicSalary=employee.getBasicSalary();
if(basicSalary==null)
{
blException.addException("basicSalary","basic salary required");
}else
{
if(basicSalary.signum()==-1)
{
blException.addException("basicSalary","basic salary cannot be nagative");
}
}
String panNumber=employee.getPANNumber();
if(panNumber==null)
{
blException.addException("panNumber","pan number required");
}else
{
panNumber=panNumber.trim();
if(panNumber.length()==0)blException.addException("panNumber","pan number required");
}
String aadharCardNumber=employee.getAadharCardNumber();
if(aadharCardNumber==null)
{
blException.addException("aadharCardNumber","aadhar card number required");
}else
{
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0)blException.addException("aadharCardNumber","aadhar card number required");
}
if(panNumber!=null && panNumber.length()>0 &&panNumberWiseEmployeesMap.containsKey(panNumber.toUpperCase()))
{
blException.addException("panCard","Pan number "+panNumber+" Exists:");
}
if(aadharCardNumber!=null && aadharCardNumber.length()>0 && aadharCardNumberWiseEmployeesMap.containsKey(aadharCardNumber.toUpperCase()))
{
blException.addException("aadharCard","Aadhar card number "+aadharCardNumber+" Exists:");
}
if(blException.hasExceptions()) throw blException;
try
{
EmployeeDTOInterface dlEmployee=new EmployeeDTO();
Set<EmployeeInterface> es;
dlEmployee.setName(name);
dlEmployee.setDesignationCode(designationCode);
dlEmployee.setDateOfBirth(dateOfBirth);
if(gender=='M')
{
dlEmployee.setGender(GENDER.MALE);
}else if(gender=='F')
{
dlEmployee.setGender(GENDER.FEMALE);
}
dlEmployee.setIsIndian(isIndian);
dlEmployee.setBasicSalary(basicSalary);
dlEmployee.setPANNumber(panNumber);
dlEmployee.setAadharCardNumber(aadharCardNumber);
new EmployeeDAO().add(dlEmployee);
employee.setEmployeeId(dlEmployee.getEmployeeId());
EmployeeInterface blEmployee=new Employee();
blEmployee.setEmployeeId(dlEmployee.getEmployeeId());
blEmployee.setName(name);
blEmployee.setDesignation(((DesignationManager)designationManager).getDSDesignationByCode(designationCode));
blEmployee.setDateOfBirth((Date)dateOfBirth.clone());
if(gender=='M')
{
blEmployee.setGender(GENDER.MALE);
}else if(gender=='F')
{
blEmployee.setGender(GENDER.FEMALE);
}
blEmployee.setIsIndian(isIndian);
blEmployee.setBasicSalary(basicSalary);
blEmployee.setPANNumber(panNumber);
blEmployee.setAadharCardNumber(aadharCardNumber);
employeeIdWiseEmployeesMap.put(blEmployee.getEmployeeId().toUpperCase(),blEmployee);
panNumberWiseEmployeesMap.put(blEmployee.getPANNumber().toUpperCase(),blEmployee);
aadharCardNumberWiseEmployeesMap.put(blEmployee.getAadharCardNumber().toUpperCase(),blEmployee);
employeesSet.add(blEmployee);
es=designationCodeWiseEmployeesMap.get(designationCode);
if(es==null)
{
es=new TreeSet();
es.add(blEmployee);
designationCodeWiseEmployeesMap.put(designationCode,es);
}else
{
es.add(blEmployee);
}
}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
throw blException;
}
}
public void removeEmployee(String employeeId)throws BLException
{
BLException blException=new BLException();
if(employeeId==null)
{
blException.addException("employeeId","Employee id required");
throw blException;
}
else
{
employeeId=employeeId.trim();
if(employeeId.length()==0)
{
blException.addException("employeeId","Employee id required");
throw blException;
}
if(employeeIdWiseEmployeesMap.containsKey(employeeId.toUpperCase())==false)
{
blException.addException("employeeId","Invalid Employee id");
throw blException;
}
try
{
Set<EmployeeInterface> es;
new EmployeeDAO().delete(employeeId);
EmployeeInterface employee=employeeIdWiseEmployeesMap.get(employeeId.toUpperCase());
employeeIdWiseEmployeesMap.remove(employeeId.toUpperCase());
panNumberWiseEmployeesMap.remove(employee.getPANNumber().toUpperCase());
aadharCardNumberWiseEmployeesMap.remove(employee.getAadharCardNumber().toUpperCase());
employeesSet.remove(employee);
int designationCode=(employee.getDesignation()).getCode();
es=designationCodeWiseEmployeesMap.get(designationCode);
es.remove(employee);
if(es.size()==0)
{
designationCodeWiseEmployeesMap.remove(designationCode);
}
}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
throw blException;
}

}
}
public void updateEmployee(EmployeeInterface employee)throws BLException
{
BLException blException=new BLException();
if(employee==null)
{
blException.setGenericException("Employee is null");
throw blException;
}
String employeeId=employee.getEmployeeId();
if(employeeId==null)
{
blException.addException("employeeId","employee id required");
}else
{
employeeId=employeeId.trim();
if(employeeId.length()==0)blException.addException("employeeId","employee id required");
}
if(employeeId!=null && employeeId.length()>0 && employeeIdWiseEmployeesMap.containsKey(employeeId.toUpperCase())==false)
{
blException.addException("employeeId","Invalid employee id");
}
String name=employee.getName();
if(name==null)
{
blException.addException("name","name required");
}else
{
name=name.trim();
if(name.length()==0)blException.addException("name","name required");
}
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
DesignationInterface designation=employee.getDesignation();
int designationCode=0;
if(designation==null)
{
blException.addException("Designation","Designation required");
}
else
{
designationCode=designation.getCode();
if(designationManager.designationCodeExists(designationCode)==false)
blException.addException("Designation","Invalid Designation");
}
Date dateOfBirth=employee.getDateOfBirth();
if(dateOfBirth==null)
{
blException.addException("dateOfBirth","Date of birth required");
}
char gender=employee.getGender();
if(gender==' ')
{
blException.addException("gender","gender required");
}
boolean isIndian=employee.getIsIndian();
BigDecimal basicSalary=employee.getBasicSalary();
if(basicSalary==null)
{
blException.addException("basicSalary","basic salary required");
}else
{
if(basicSalary.signum()==-1)
{
blException.addException("basicSalary","basic salary cannot be nagative");
}
}
String panNumber=employee.getPANNumber();
if(panNumber==null)
{
blException.addException("panNumber","pan number required");
}else
{
panNumber=panNumber.trim();
if(panNumber.length()==0)blException.addException("panNumber","pan number required");
}
String aadharCardNumber=employee.getAadharCardNumber();
if(aadharCardNumber==null)
{
blException.addException("aadharCardNumber","aadhar card number required");
}else
{
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0)blException.addException("aadharCardNumber","aadhar card number required");
}
if(panNumber!=null && panNumber.length()>0)
{
EmployeeInterface employeeByPANNumber=panNumberWiseEmployeesMap.get(panNumber.toUpperCase());
if(employeeByPANNumber!=null && employeeId.equalsIgnoreCase(employeeByPANNumber.getEmployeeId())==false)
{
blException.addException("panNumber","Pan number "+panNumber+" Exists:");
}
}
if(aadharCardNumber!=null && aadharCardNumber.length()>0)
{
EmployeeInterface employeeByAadharCardNumber=aadharCardNumberWiseEmployeesMap.get(aadharCardNumber.toUpperCase());
if(employeeByAadharCardNumber!=null && employeeId.equalsIgnoreCase(employeeByAadharCardNumber.getEmployeeId())==false)
{
blException.addException("aadharCardNumber","Aadhar card number "+aadharCardNumber+" Exists:");
}
}
if(blException.hasExceptions()) throw blException;
try
{
Set<EmployeeInterface> es;
EmployeeDTOInterface dlEmployee=new EmployeeDTO();
dlEmployee.setEmployeeId(employeeId);
dlEmployee.setName(name);
dlEmployee.setDesignationCode(designationCode);
dlEmployee.setDateOfBirth(dateOfBirth);
if(gender=='M')
{
dlEmployee.setGender(GENDER.MALE);
}else if(gender=='F')
{
dlEmployee.setGender(GENDER.FEMALE);
}
dlEmployee.setIsIndian(isIndian);
dlEmployee.setBasicSalary(basicSalary);
dlEmployee.setPANNumber(panNumber);
dlEmployee.setAadharCardNumber(aadharCardNumber);
new EmployeeDAO().update(dlEmployee);
//deleteing from DS
EmployeeInterface blEmployee=employeeIdWiseEmployeesMap.get(employeeId.toUpperCase());
employeeIdWiseEmployeesMap.remove(employeeId.toUpperCase());
panNumberWiseEmployeesMap.remove(blEmployee.getPANNumber().toUpperCase());
aadharCardNumberWiseEmployeesMap.remove(blEmployee.getAadharCardNumber().toUpperCase());
employeesSet.remove(blEmployee);
int dsc=(blEmployee.getDesignation()).getCode();
es=designationCodeWiseEmployeesMap.get(dsc);
es.remove(blEmployee);
if(es.size()==0)
{
designationCodeWiseEmployeesMap.remove(dsc);
}
//updating DS
blEmployee=new Employee();
blEmployee.setEmployeeId(employeeId);
blEmployee.setName(name);
blEmployee.setDesignation(((DesignationManager)designationManager).getDSDesignationByCode(designationCode));
blEmployee.setDateOfBirth((Date)dateOfBirth.clone());
if(gender=='M')
{
blEmployee.setGender(GENDER.MALE);
}else if(gender=='F')
{
blEmployee.setGender(GENDER.FEMALE);
}
blEmployee.setIsIndian(isIndian);
blEmployee.setBasicSalary(basicSalary);
blEmployee.setPANNumber(panNumber);
blEmployee.setAadharCardNumber(aadharCardNumber);
employeeIdWiseEmployeesMap.put(blEmployee.getEmployeeId().toUpperCase(),blEmployee);
panNumberWiseEmployeesMap.put(blEmployee.getPANNumber().toUpperCase(),blEmployee);
aadharCardNumberWiseEmployeesMap.put(blEmployee.getAadharCardNumber().toUpperCase(),blEmployee);
employeesSet.add(blEmployee);
es=designationCodeWiseEmployeesMap.get(designationCode);
if(es==null)
{
es=new TreeSet();
es.add(blEmployee);
designationCodeWiseEmployeesMap.put(designationCode,es);
}else
{
es.add(blEmployee);
}
}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
throw blException;
}
}
public Set<EmployeeInterface> getEmployees()
{
Set<EmployeeInterface> employees=new TreeSet<>();
char gender;
DesignationInterface designation;
DesignationInterface dd;
for(EmployeeInterface employee:employeesSet)
{
EmployeeInterface e=new Employee();
e.setEmployeeId(employee.getEmployeeId());
e.setName(employee.getName());
designation=employee.getDesignation();
dd=new Designation();
dd.setCode(designation.getCode());
dd.setTitle(designation.getTitle());
e.setDesignation(dd);
e.setDateOfBirth((Date)employee.getDateOfBirth().clone());
gender=employee.getGender();
if(gender=='M')e.setGender(GENDER.MALE);
if(gender=='F')e.setGender(GENDER.FEMALE);
e.setIsIndian(employee.getIsIndian());
e.setBasicSalary(employee.getBasicSalary());
e.setPANNumber(employee.getPANNumber());
e.setAadharCardNumber(employee.getAadharCardNumber());
employees.add(e);
}
return employees;
}
public Set<EmployeeInterface> getEmployeesByDesignationCode(int designationCode)throws BLException
{
if(DesignationManager.getDesignationManager().designationCodeExists(designationCode)==false)
{
BLException blException=new BLException();
blException.addException("designationCode","Invalid designation code");
throw blException;
}
Set<EmployeeInterface> employees=new TreeSet<>();
Set<EmployeeInterface> blEmployees;

blEmployees=designationCodeWiseEmployeesMap.get(designationCode);
if(blEmployees==null)
{
return employees;
}
EmployeeInterface e;
DesignationInterface dd,designation;
char gender;
for(EmployeeInterface employee:blEmployees)
{
e=new Employee();
e.setEmployeeId(employee.getEmployeeId());
e.setName(employee.getName());
designation=employee.getDesignation();
dd=new Designation();
dd.setCode(designation.getCode());
dd.setTitle(designation.getTitle());
e.setDesignation(dd);
e.setDateOfBirth((Date)employee.getDateOfBirth().clone());
gender=employee.getGender();
if(gender=='M')e.setGender(GENDER.MALE);
if(gender=='F')e.setGender(GENDER.FEMALE);
e.setIsIndian(employee.getIsIndian());
e.setBasicSalary(employee.getBasicSalary());
e.setPANNumber(employee.getPANNumber());
e.setAadharCardNumber(employee.getAadharCardNumber());
employees.add(e);
}
return employees;
}
public boolean isDesignationAlloted(int designationCode)throws BLException
{
return designationCodeWiseEmployeesMap.containsKey(designationCode);
}
public EmployeeInterface getEmployeeByEmployeeId(String employeeId)throws BLException
{
BLException blException=new BLException();
if(employeeId==null)
{
blException.addException("employeeId","Employee id required");
throw blException;
}
EmployeeInterface employee=employeeIdWiseEmployeesMap.get(employeeId.toUpperCase());
if(employee==null)
{
blException.addException("employeeId","Invalid employee id");
throw blException;
}
EmployeeInterface e=new Employee();
e.setEmployeeId(employee.getEmployeeId());
e.setName(employee.getName());
e.setDesignation(employee.getDesignation());
e.setDateOfBirth((Date)employee.getDateOfBirth().clone());
char gender=employee.getGender();
e.setGender((gender=='M')?GENDER.MALE:GENDER.FEMALE);
e.setIsIndian(employee.getIsIndian());
e.setBasicSalary(employee.getBasicSalary());
e.setPANNumber(employee.getPANNumber());
e.setAadharCardNumber(employee.getAadharCardNumber());
return e;
}
public EmployeeInterface getEmployeeByPANNumber(String panNumber)throws BLException
{
BLException blException=new BLException();
if(panNumber==null)
{
blException.addException("panNumber","Pan number required");
throw blException;
}
EmployeeInterface employee=panNumberWiseEmployeesMap.get(panNumber.toUpperCase());
if(employee==null)
{
blException.addException("panNumber","Invalid pan number");
throw blException;
}
EmployeeInterface e=new Employee();
e.setEmployeeId(employee.getEmployeeId());
e.setName(employee.getName());
e.setDesignation(employee.getDesignation());
e.setDateOfBirth((Date)employee.getDateOfBirth().clone());
char gender=employee.getGender();
e.setGender((gender=='M')?GENDER.MALE:GENDER.FEMALE);
e.setIsIndian(employee.getIsIndian());
e.setBasicSalary(employee.getBasicSalary());
e.setPANNumber(employee.getPANNumber());
e.setAadharCardNumber(employee.getAadharCardNumber());
return e;
}
public EmployeeInterface getEmployeeByAadharCardNumber(String aadharCardNumber)throws BLException
{
BLException blException=new BLException();
if(aadharCardNumber==null)
{
blException.addException("aadharCardNumber","Aadhar card number required");
throw blException;
}
EmployeeInterface employee=aadharCardNumberWiseEmployeesMap.get(aadharCardNumber.toUpperCase());
if(employee==null)
{
blException.addException("aadharCardNumber","Invalid aadhar card number");
throw blException;
}
EmployeeInterface e=new Employee();
e.setEmployeeId(employee.getEmployeeId());
e.setName(employee.getName());
e.setDesignation(employee.getDesignation());
e.setDateOfBirth((Date)employee.getDateOfBirth().clone() );
char gender=employee.getGender();
e.setGender((gender=='M')?GENDER.MALE:GENDER.FEMALE);
e.setIsIndian(employee.getIsIndian());
e.setBasicSalary(employee.getBasicSalary());
e.setPANNumber(employee.getPANNumber());
e.setAadharCardNumber(employee.getAadharCardNumber());
return e;
}
public boolean employeeIdExists(String employeeId)
{
if(employeeId==null)
{
return false;
}
return employeeIdWiseEmployeesMap.containsKey(employeeId.toUpperCase());
}
public boolean panNumberExists(String panNumber)
{
if(panNumber==null)
{
return false;
}
return panNumberWiseEmployeesMap.containsKey(panNumber.toUpperCase());
}
public boolean aadharCardNumberExists(String aadharCardNumber)
{
if(aadharCardNumber==null)
{
return false;
}
return aadharCardNumberWiseEmployeesMap.containsKey(aadharCardNumber.toUpperCase());
}
public int getEmployeeCount()
{
return employeesSet.size();
}
public int getEmployeeCountByDesignationCode(int designationCode)throws BLException
{
Set<EmployeeInterface> employees;
employees=designationCodeWiseEmployeesMap.get(designationCode);
if(employees==null)
{
return 0;
}
return employees.size();
}
}