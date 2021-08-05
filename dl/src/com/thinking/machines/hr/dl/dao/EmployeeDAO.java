package com.thinking.machines.hr.dl.dao;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.enums.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.text.*;
public class EmployeeDAO implements EmployeeDAOInterface
{
private final static String FILE_NAME="employee.data";
public void add(EmployeeDTOInterface employeeDTO)throws DAOException
{
if(employeeDTO==null)throw new DAOException("Employee is null");
String employeeId;
String name=employeeDTO.getName();
if(name==null) throw new DAOException("Name is null");
name=name.trim();
if(name.length()==0)throw new DAOException("Length of name is zero");
int designationCode=employeeDTO.getDesignationCode();
DesignationDAOInterface designationDAO=new DesignationDAO();
//if(!designationDAO.codeExists(designationCode))throw new DAOException("Invalid designation code"+designationCode);
Date dateOfBirth=employeeDTO.getDateOfBirth();
if(dateOfBirth==null)throw new DAOException("Date of birth is null");
char gender=employeeDTO.getGender();
boolean isIndian=employeeDTO.getIsIndian();
BigDecimal basicSalary=employeeDTO.getBasicSalary();
if(basicSalary==null)throw new DAOException("Basic Salary is null");
if(basicSalary.signum()==-1)throw new DAOException("Basic salary is nagative");
String panNumber=employeeDTO.getPANNumber();
if(panNumber==null) throw new DAOException("pan number is null");
panNumber=panNumber.trim();
if(panNumber.length()==0)throw new DAOException("Length of panNumber is zero");
String aadharCardNumber=employeeDTO.getAadharCardNumber();
if(aadharCardNumber==null) throw new DAOException("aadhar card number is null");
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0)throw new DAOException("Length of aadhar card number is zero");
try
{
int lastGeneratedEmployeeId=0;
String lastGeneratedEmployeeIdString="";
int recordCount=0;
String recordCountString="";
File file=new File(FILE_NAME);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
lastGeneratedEmployeeIdString=String.format("%-10s","10000000");
randomAccessFile.writeBytes(lastGeneratedEmployeeIdString+"\n");
recordCountString=String.format("%-10s","0");
randomAccessFile.writeBytes(recordCountString+"\n");
randomAccessFile.seek(0);
}
lastGeneratedEmployeeId=Integer.parseInt(randomAccessFile.readLine().trim());
recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
String fPanNumber="";
String fAadharCardNumber="";
int x;
boolean panNumberFound=false;
boolean aadharCardNumberFound=false;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
for(x=1;x<=7;x++)randomAccessFile.readLine();
fPanNumber=randomAccessFile.readLine();
fAadharCardNumber=randomAccessFile.readLine();
if(panNumberFound==false && fPanNumber.equalsIgnoreCase(panNumber))
{
panNumberFound=true;
}
if(aadharCardNumberFound==false && fAadharCardNumber.equalsIgnoreCase(aadharCardNumber))
{
aadharCardNumberFound=true;
}
}
if(panNumberFound && aadharCardNumberFound)
{
randomAccessFile.close();
throw new DAOException("Pan number exists "+panNumber+" Aadhar card number exists "+aadharCardNumber);
}
if(aadharCardNumberFound)
{
randomAccessFile.close();
throw new DAOException("Aadhar card number exists"+aadharCardNumber);
}
if(panNumberFound)
{
randomAccessFile.close();
throw new DAOException("Pan number exists"+panNumber);
}
SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
lastGeneratedEmployeeId++;
recordCount++;
employeeId="A"+lastGeneratedEmployeeId;
randomAccessFile.writeBytes(employeeId+"\n");
randomAccessFile.writeBytes(name+"\n");
randomAccessFile.writeBytes(designationCode+"\n");
randomAccessFile.writeBytes(simpleDateFormat.format(dateOfBirth)+"\n");
randomAccessFile.writeBytes(gender+"\n");
randomAccessFile.writeBytes(isIndian+"\n");
randomAccessFile.writeBytes(basicSalary.toPlainString()+"\n");
randomAccessFile.writeBytes(panNumber+"\n");
randomAccessFile.writeBytes(aadharCardNumber+"\n");
randomAccessFile.seek(0);
lastGeneratedEmployeeIdString=String.valueOf(lastGeneratedEmployeeId);
lastGeneratedEmployeeIdString=String.format("%-10s",lastGeneratedEmployeeIdString);
recordCountString=String.valueOf(recordCount);
recordCountString=String.format("%-10s",recordCountString);
randomAccessFile.writeBytes(lastGeneratedEmployeeIdString+"\n");
randomAccessFile.writeBytes(recordCountString+"\n");
randomAccessFile.close();
employeeDTO.setEmployeeId(employeeId);
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public void delete(String employeeId)throws DAOException
{
if(employeeId==null) throw new DAOException("employee id is null");
employeeId=employeeId.trim();
if(employeeId.length()==0)throw new DAOException("Length of employee id is zero");
try
{
File file=new File(FILE_NAME);
if(file.exists()==false)throw new DAOException("Invalid Employee id");
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new DAOException("Invalid Employee id");
}
boolean employeeIdFound=false;
String fEmployeeId;
String recordCountString;
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
long filePointer=0;
int x;
while(randomAccessFile.getFilePointer()<randomAccessFile.length()) 
{
filePointer=randomAccessFile.getFilePointer();
fEmployeeId=randomAccessFile.readLine();
for(x=1;x<=8;x++)randomAccessFile.readLine();
if(employeeIdFound==false && fEmployeeId.equalsIgnoreCase(employeeId))
{
employeeIdFound=true;
break;
}
}
if(employeeIdFound==false)
{
randomAccessFile.close();
throw new DAOException("Invalid Employee id");
}
recordCount--;
recordCountString=String.valueOf(recordCount);
for(;recordCountString.length()<10;recordCountString+=" ");
File tmpFile=new File("tmp.tmp");
RandomAccessFile tmpRandomAccessFile=new RandomAccessFile(tmpFile,"rw");
tmpRandomAccessFile.setLength(0);
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
tmpRandomAccessFile.writeBytes(randomAccessFile.readLine()+"\n");
}
randomAccessFile.seek(filePointer);
tmpRandomAccessFile.seek(0);
while(tmpRandomAccessFile.getFilePointer()<tmpRandomAccessFile.length())
{
randomAccessFile.writeBytes(tmpRandomAccessFile.readLine()+"\n");
}
randomAccessFile.setLength(randomAccessFile.getFilePointer());
randomAccessFile.seek(0);
randomAccessFile.readLine();
randomAccessFile.writeBytes(recordCountString+"\n");
randomAccessFile.close();
tmpRandomAccessFile.setLength(0);
tmpRandomAccessFile.close();
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public void update(EmployeeDTOInterface employeeDTO)throws DAOException
{
if(employeeDTO==null)throw new DAOException("Employee is null");
String employeeId=employeeDTO.getEmployeeId();
if(employeeId==null) throw new DAOException("employee id is null");
employeeId=employeeId.trim();
if(employeeId.length()==0)throw new DAOException("Length of employee id is zero");
String name=employeeDTO.getName();
if(name==null) throw new DAOException("Name is null");
name=name.trim();
if(name.length()==0)throw new DAOException("Length of name is zero");
int designationCode=employeeDTO.getDesignationCode();
DesignationDAOInterface designationDAO=new DesignationDAO();
//if(!designationDAO.codeExists(designationCode))throw new DAOException("Invalid designation code"+designationCode);
Date dateOfBirth=employeeDTO.getDateOfBirth();
if(dateOfBirth==null)throw new DAOException("Date of birth is null");
char gender=employeeDTO.getGender();
boolean isIndian=employeeDTO.getIsIndian();
BigDecimal basicSalary=employeeDTO.getBasicSalary();
if(basicSalary==null)throw new DAOException("Basic Salary is null");
if(basicSalary.signum()==-1)throw new DAOException("Basic salary is nagative");
String panNumber=employeeDTO.getPANNumber();
if(panNumber==null) throw new DAOException("pan number is null");
panNumber=panNumber.trim();
if(panNumber.length()==0)throw new DAOException("Length of panNumber is zero");
String aadharCardNumber=employeeDTO.getAadharCardNumber();
if(aadharCardNumber==null) throw new DAOException("aadhar card number is null");
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0)throw new DAOException("Length of aadhar card number is zero");
try
{
File file=new File(FILE_NAME);
if(file.exists()==false)throw new DAOException("Invalid Employee id");
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new DAOException("Invalid Employee id");
}
boolean employeeIdFound=false;
boolean panNumberFound=false;
boolean aadharCardNumberFound=false;
String fEmployeeId;
String fPANNumber;
String fAadharCardNumber;
SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
randomAccessFile.readLine();
randomAccessFile.readLine();
long filePointer=0;
int x;
while(randomAccessFile.getFilePointer()<randomAccessFile.length()) 
{
if(employeeIdFound==false)filePointer=randomAccessFile.getFilePointer();
fEmployeeId=randomAccessFile.readLine();
for(x=1;x<=6;x++)randomAccessFile.readLine();
fPANNumber=randomAccessFile.readLine();
fAadharCardNumber=randomAccessFile.readLine();
if(employeeIdFound==false && fEmployeeId.equalsIgnoreCase(employeeId))employeeIdFound=true;
if(panNumberFound==false && fPANNumber.equalsIgnoreCase(panNumber) && fEmployeeId.equalsIgnoreCase(employeeId)==false)panNumberFound=true;
if(aadharCardNumberFound==false && fAadharCardNumber.equalsIgnoreCase(aadharCardNumber) && fEmployeeId.equalsIgnoreCase(employeeId)==false)aadharCardNumberFound=true;
if(employeeIdFound && panNumberFound && aadharCardNumberFound) break;
}
if(employeeIdFound==false)
{
randomAccessFile.close();
throw new DAOException("Invalid Employee id");
}
if(aadharCardNumberFound && panNumberFound)
{
randomAccessFile.close();
throw new DAOException("Pan number exists("+panNumber+") and Aadhar card number exists("+aadharCardNumber+")");
}
if(panNumberFound)
{
randomAccessFile.close();
throw new DAOException("Pan number exists("+panNumber+")");
}
if(aadharCardNumberFound)
{
randomAccessFile.close();
throw new DAOException("Aadhar card number exists("+aadharCardNumber+")");
}
randomAccessFile.seek(filePointer);
for(x=1;x<=9;x++)randomAccessFile.readLine();
File tmpFile=new File("tmp.tmp");
RandomAccessFile tmpRandomAccessFile=new RandomAccessFile(tmpFile,"rw");
tmpRandomAccessFile.setLength(0);
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
tmpRandomAccessFile.writeBytes(randomAccessFile.readLine()+"\n");
}
randomAccessFile.seek(filePointer);
randomAccessFile.writeBytes(employeeId+"\n");
randomAccessFile.writeBytes(name+"\n");
randomAccessFile.writeBytes(designationCode+"\n");
randomAccessFile.writeBytes(simpleDateFormat.format(dateOfBirth)+"\n");
randomAccessFile.writeBytes(gender+"\n");
randomAccessFile.writeBytes(isIndian+"\n");
randomAccessFile.writeBytes(basicSalary.toPlainString()+"\n");
randomAccessFile.writeBytes(panNumber+"\n");
randomAccessFile.writeBytes(aadharCardNumber+"\n");
tmpRandomAccessFile.seek(0);
while(tmpRandomAccessFile.getFilePointer()<tmpRandomAccessFile.length())
{
randomAccessFile.writeBytes(tmpRandomAccessFile.readLine()+"\n");
}
randomAccessFile.setLength(randomAccessFile.getFilePointer());
randomAccessFile.close();
tmpRandomAccessFile.setLength(0);
tmpRandomAccessFile.close();
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public Set<EmployeeDTOInterface> getAll()throws DAOException
{
Set<EmployeeDTOInterface> employees=new TreeSet<>();
try
{
File file=new File(FILE_NAME);
if(!file.exists()) return employees;
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
return employees;
}
char fGender;
EmployeeDTOInterface employeeDTO;
randomAccessFile.readLine();
randomAccessFile.readLine();
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId(randomAccessFile.readLine());
employeeDTO.setName(randomAccessFile.readLine());
employeeDTO.setDesignationCode(Integer.parseInt(randomAccessFile.readLine()));
employeeDTO.setDateOfBirth(new Date(randomAccessFile.readLine()));
fGender=randomAccessFile.readLine().charAt(0);
if(fGender=='M'||fGender=='m')employeeDTO.setGender(GENDER.MALE);
else if(fGender=='F' || fGender=='f')employeeDTO.setGender(GENDER.FEMALE);
employeeDTO.setIsIndian(Boolean.parseBoolean(randomAccessFile.readLine()));
employeeDTO.setBasicSalary(new BigDecimal(randomAccessFile.readLine()));
employeeDTO.setPANNumber(randomAccessFile.readLine());
employeeDTO.setAadharCardNumber(randomAccessFile.readLine());
employees.add(employeeDTO);
}
randomAccessFile.close();
return employees;
}catch(IOException ioxception)
{
throw new DAOException(ioxception.getMessage());
}
}
public Set<EmployeeDTOInterface> getByDesignationCode(int designationCode)throws DAOException
{
if(designationCode<=0)throw new DAOException("Invalid designation code");
DesignationDAOInterface designationDAO=new DesignationDAO();
//if(designationDAO.codeExists(designationCode)==false)throw new DAOException("Invalid designation code");
Set<EmployeeDTOInterface> employees=new TreeSet<>();
try
{
File file=new File(FILE_NAME);
if(!file.exists()) return employees;
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
return employees;
}
char fGender;
EmployeeDTOInterface employeeDTO;
randomAccessFile.readLine();
randomAccessFile.readLine();
int x;
String fEmployeeId;
String fName;
int fDesignationCode;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fEmployeeId=randomAccessFile.readLine();
fName=randomAccessFile.readLine();
fDesignationCode=Integer.parseInt(randomAccessFile.readLine());
if(fDesignationCode==designationCode)
{
employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId(fEmployeeId);
employeeDTO.setName(fName);
employeeDTO.setDesignationCode(fDesignationCode);
employeeDTO.setDateOfBirth(new Date(randomAccessFile.readLine()));
fGender=randomAccessFile.readLine().charAt(0);
if(fGender=='M'||fGender=='m')employeeDTO.setGender(GENDER.MALE);
else if(fGender=='F' || fGender=='f')employeeDTO.setGender(GENDER.FEMALE);
employeeDTO.setIsIndian(Boolean.parseBoolean(randomAccessFile.readLine()));
employeeDTO.setBasicSalary(new BigDecimal(randomAccessFile.readLine()));
employeeDTO.setPANNumber(randomAccessFile.readLine());
employeeDTO.setAadharCardNumber(randomAccessFile.readLine());
employees.add(employeeDTO);
}
else
{
for(x=1;x<=6;x++)randomAccessFile.readLine();
}
}
randomAccessFile.close();
return employees;
}catch(IOException ioxception)
{
throw new DAOException(ioxception.getMessage());
}
}
public boolean isDesignationAlloted(int designationCode)throws DAOException
{
throw new DAOException("not yet implement");
}
public EmployeeDTOInterface getByEmployeeId(String employeeId)throws DAOException
{
if(employeeId==null)throw new DAOException("Employee id is null");
employeeId=employeeId.trim();
if(employeeId.length()==0)throw new DAOException("length of employee id is zero");
try
{
File file=new File(FILE_NAME);
if(!file.exists())throw new DAOException("Invalid employee id");
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new DAOException("Invalid employee id");
}
String fEmployeeId;
char fGender;
int x;
randomAccessFile.readLine();
randomAccessFile.readLine();
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fEmployeeId=randomAccessFile.readLine();
if(fEmployeeId.equalsIgnoreCase(employeeId))
{
EmployeeDTOInterface employeeDTO=new EmployeeDTO(); 
employeeDTO.setEmployeeId(fEmployeeId);
employeeDTO.setName(randomAccessFile.readLine());
employeeDTO.setDesignationCode(Integer.parseInt(randomAccessFile.readLine()));
employeeDTO.setDateOfBirth(new Date(randomAccessFile.readLine()));
fGender=randomAccessFile.readLine().charAt(0);
if(fGender=='M'||fGender=='m')employeeDTO.setGender(GENDER.MALE);
else if(fGender=='F' || fGender=='f')employeeDTO.setGender(GENDER.FEMALE);
employeeDTO.setIsIndian(Boolean.parseBoolean(randomAccessFile.readLine()));
employeeDTO.setBasicSalary(new BigDecimal(randomAccessFile.readLine()));
employeeDTO.setPANNumber(randomAccessFile.readLine());
employeeDTO.setAadharCardNumber(randomAccessFile.readLine());
randomAccessFile.close();
return employeeDTO;
}
for(x=1;x<=8;x++)randomAccessFile.readLine();
}
randomAccessFile.close();
throw new DAOException("Invalid Employee id");
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public EmployeeDTOInterface getByPANNumber(String panNumber)throws DAOException
{
if(panNumber==null)throw new DAOException("Pan number is null");
panNumber=panNumber.trim();
if(panNumber.length()==0)throw new DAOException("length of pan number is zero");
try
{
File file=new File(FILE_NAME);
if(!file.exists())throw new DAOException("Invalid pan number");
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new DAOException("Invalid pan number");
}
String fEmployeeId;
String fName;
int fDesignationCode;
String fDateOfBirthString;
char fGender;
boolean fIsIndian;
String fBasicSalaryString;
String fPANNumber;
String fAadharCardNumber;
int x;
randomAccessFile.readLine();
randomAccessFile.readLine();
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fEmployeeId=randomAccessFile.readLine();
fName=randomAccessFile.readLine();
fDesignationCode=Integer.parseInt(randomAccessFile.readLine());
fDateOfBirthString=randomAccessFile.readLine();
fGender=randomAccessFile.readLine().charAt(0);
fIsIndian=Boolean.parseBoolean(randomAccessFile.readLine());
fBasicSalaryString=randomAccessFile.readLine();
fPANNumber=randomAccessFile.readLine();
fAadharCardNumber=randomAccessFile.readLine();
if(fPANNumber.equalsIgnoreCase(panNumber))
{
EmployeeDTOInterface employeeDTO=new EmployeeDTO(); 
employeeDTO.setEmployeeId(fEmployeeId);
employeeDTO.setName(fName);
employeeDTO.setDesignationCode(fDesignationCode);
employeeDTO.setDateOfBirth(new Date(fDateOfBirthString));
if(fGender=='M'||fGender=='m')employeeDTO.setGender(GENDER.MALE);
else if(fGender=='F' || fGender=='f')employeeDTO.setGender(GENDER.FEMALE);
employeeDTO.setIsIndian(fIsIndian);
employeeDTO.setBasicSalary(new BigDecimal(fBasicSalaryString));
employeeDTO.setPANNumber(fPANNumber);
employeeDTO.setAadharCardNumber(fAadharCardNumber);
randomAccessFile.close();
return employeeDTO;
}
}
randomAccessFile.close();
throw new DAOException("Invalid pan number");
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public EmployeeDTOInterface getByAadharCardNumber(String aadharCardNumber)throws DAOException
{
if(aadharCardNumber==null)throw new DAOException("Aadhar card number is null");
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0)throw new DAOException("length of aadhar card number is zero");
try
{
File file=new File(FILE_NAME);
if(!file.exists())throw new DAOException("Invalid aadhar card number");
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new DAOException("Invalid aadhar card number");
}
String fEmployeeId;
String fName;
int fDesignationCode;
String fDateOfBirthString;
char fGender;
boolean fIsIndian;
String fBasicSalaryString;
String fPANNumber;
String fAadharCardNumber;
int x;
randomAccessFile.readLine();
randomAccessFile.readLine();
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fEmployeeId=randomAccessFile.readLine();
fName=randomAccessFile.readLine();
fDesignationCode=Integer.parseInt(randomAccessFile.readLine());
fDateOfBirthString=randomAccessFile.readLine();
fGender=randomAccessFile.readLine().charAt(0);
fIsIndian=Boolean.parseBoolean(randomAccessFile.readLine());
fBasicSalaryString=randomAccessFile.readLine();
fPANNumber=randomAccessFile.readLine();
fAadharCardNumber=randomAccessFile.readLine();
if(fAadharCardNumber.equalsIgnoreCase(aadharCardNumber))
{
EmployeeDTOInterface employeeDTO=new EmployeeDTO(); 
employeeDTO.setEmployeeId(fEmployeeId);
employeeDTO.setName(fName);
employeeDTO.setDesignationCode(fDesignationCode);
employeeDTO.setDateOfBirth(new Date(fDateOfBirthString));
if(fGender=='M'||fGender=='m')employeeDTO.setGender(GENDER.MALE);
else if(fGender=='F' || fGender=='f')employeeDTO.setGender(GENDER.FEMALE);
employeeDTO.setIsIndian(fIsIndian);
employeeDTO.setBasicSalary(new BigDecimal(fBasicSalaryString));
employeeDTO.setPANNumber(fPANNumber);
employeeDTO.setAadharCardNumber(fAadharCardNumber);
randomAccessFile.close();
return employeeDTO;
}
}
randomAccessFile.close();
throw new DAOException("Invalid aadhar card number");
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public boolean employeeIdExists(String employeeId)throws DAOException
{
if(employeeId==null)return false;
employeeId=employeeId.trim();
if(employeeId.length()==0)return false;
try
{
File file=new File(FILE_NAME);
if(!file.exists())return false;
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
return false;
}
String fEmployeeId;
char fGender;
int x;
randomAccessFile.readLine();
randomAccessFile.readLine();
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fEmployeeId=randomAccessFile.readLine();
if(fEmployeeId.equalsIgnoreCase(employeeId))
{
randomAccessFile.close();
return true;
}
for(x=1;x<=8;x++)randomAccessFile.readLine();
}
randomAccessFile.close();
return false;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public boolean panNumberExists(String panNumber)throws DAOException
{
if(panNumber==null)return false;
panNumber=panNumber.trim();
if(panNumber.length()==0)return false;
try
{
File file=new File(FILE_NAME);
if(!file.exists())return false;
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
return false;
}
String fPANNumber;
int x;
randomAccessFile.readLine();
randomAccessFile.readLine();
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
for(x=1;x<=7;x++)randomAccessFile.readLine();
fPANNumber=randomAccessFile.readLine();
if(fPANNumber.equalsIgnoreCase(panNumber))
{
randomAccessFile.close();
return true;
}
randomAccessFile.readLine();
}
randomAccessFile.close();
return false; 
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public boolean aadharCardNumberExists(String aadharCardNumber)throws DAOException
{
if(aadharCardNumber==null)return false;
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0)return false;
try
{
File file=new File(FILE_NAME);
if(!file.exists())return false;
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
return false;
}
String fAadharCardNumber;
int x;
randomAccessFile.readLine();
randomAccessFile.readLine();
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
for(x=1;x<=8;x++)randomAccessFile.readLine();
fAadharCardNumber=randomAccessFile.readLine();
if(fAadharCardNumber.equalsIgnoreCase(aadharCardNumber))
{
randomAccessFile.close();
return true;
}
}
randomAccessFile.close();
return false;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public int getCount()throws DAOException
{
try
{
File file=new File(FILE_NAME);
if(file.exists()==false)return 0;
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
return 0;
}
int recordCount;
randomAccessFile.readLine();
recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
randomAccessFile.readLine();
return recordCount;
}catch(IOException ioexception)
{
throw new DAOException(ioexception.getMessage());
}
}
public int getCountByDesignationCode(int designationCode)throws DAOException
{
if(designationCode<=0)throw new DAOException("Invalid designation code");
DesignationDAOInterface designationDAO=new DesignationDAO();
//if(designationDAO.codeExists(designationCode)==false)throw new DAOException("Invalid designation code");
try
{
File file=new File(FILE_NAME);
if(!file.exists()) return 0;
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
return 0;
}
randomAccessFile.readLine();
randomAccessFile.readLine();
int x;
int count=0;
int fDesignationCode;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
randomAccessFile.readLine();
randomAccessFile.readLine();
fDesignationCode=Integer.parseInt(randomAccessFile.readLine());
if(fDesignationCode==designationCode)
{
count++;
}
for(x=1;x<=6;x++)randomAccessFile.readLine();
}
randomAccessFile.close();
return count;
}catch(IOException ioxception)
{
throw new DAOException(ioxception.getMessage());
}
}
}