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
import java.sql.*;
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

Connection connection=null;
PreparedStatement preparedStatement;
ResultSet resultSet;
try
{
connection=DAOConnection.getConnection();
preparedStatement=connection.prepareStatement("select code from designation where code=?");
preparedStatement.setInt(1,designationCode);
resultSet=preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Invalid designation code"+designationCode);
}
resultSet.close();
preparedStatement.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}




//DesignationDAOInterface designationDAO=new DesignationDAO();
//if(!designationDAO.codeExists(designationCode))throw new DAOException("Invalid designation code"+designationCode);
java.util.Date dateOfBirth=employeeDTO.getDateOfBirth();
if(dateOfBirth==null)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("Date of birth is null");
}
char gender=employeeDTO.getGender();
boolean isIndian=employeeDTO.getIsIndian();
BigDecimal basicSalary=employeeDTO.getBasicSalary();
if(basicSalary==null)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("Basic Salary is null");
}
if(basicSalary.signum()==-1)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("Basic salary is nagative");
}
String panNumber=employeeDTO.getPANNumber();
if(panNumber==null)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("pan number is null");
}
panNumber=panNumber.trim();
if(panNumber.length()==0)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("Length of panNumber is zero");
}
String aadharCardNumber=employeeDTO.getAadharCardNumber();
if(aadharCardNumber==null) 
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("aadhar card number is null");
}
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("Length of aadhar card number is zero");
}
try
{
preparedStatement=connection.prepareStatement("select gender from employee where pan_number=?");
preparedStatement.setString(1,panNumber);
resultSet=preparedStatement.executeQuery();
boolean panNumberExists=resultSet.next();
resultSet.close();
preparedStatement.close();
preparedStatement=connection.prepareStatement("select gender from employee where aadhar_number=?");
preparedStatement.setString(1,aadharCardNumber);
resultSet=preparedStatement.executeQuery();
boolean aadharCardNumberExists=resultSet.next();
resultSet.close();
preparedStatement.close();
if(panNumberExists && aadharCardNumberExists)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("Pan number exists "+panNumber+" Aadhar card number exists "+aadharCardNumber);
}
if(aadharCardNumberExists)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("Aadhar card number exists"+aadharCardNumber);
}
if(panNumberExists)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("Pan number exists"+panNumber);
}
preparedStatement=connection.prepareStatement("insert into employee (name,designation_code,date_of_birth,gender,is_indian,basic_salary,pan_number,aadhar_number) values(?,?,?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
preparedStatement.setString(1,name);
preparedStatement.setInt(2,designationCode);
java.sql.Date sqlDate=new java.sql.Date(dateOfBirth.getYear(),dateOfBirth.getMonth(),dateOfBirth.getDate());
preparedStatement.setDate(3,sqlDate);
preparedStatement.setString(4,String.valueOf(gender));
preparedStatement.setBoolean(5,isIndian);
preparedStatement.setBigDecimal(6,basicSalary);
preparedStatement.setString(7,panNumber);
preparedStatement.setString(8,aadharCardNumber);
preparedStatement.executeUpdate();
resultSet=preparedStatement.getGeneratedKeys();
resultSet.next();
int generatedEmployeeId=resultSet.getInt(1);
resultSet.close();
preparedStatement.close();
connection.close();
employeeDTO.setEmployeeId("A"+(1000000+generatedEmployeeId));
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}
public void delete(String employeeId)throws DAOException
{
if(employeeId==null) throw new DAOException("employee id is null");
employeeId=employeeId.trim();
if(employeeId.length()==0)throw new DAOException("Length of employee id is zero");
int actualEmployeeId;
try
{
actualEmployeeId=Integer.parseInt(employeeId.substring(1))-1000000;
}catch(Exception exception)
{
throw new DAOException("invalid employee id");
}
Connection connection=DAOConnection.getConnection();
ResultSet resultSet;
PreparedStatement preparedStatement;
try
{
preparedStatement=connection.prepareStatement("select gender from employee where employee_id=?");
preparedStatement.setInt(1,actualEmployeeId);
resultSet=preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Invalid employee Id."+employeeId);
}
resultSet.close();
preparedStatement.close();
preparedStatement=connection.prepareStatement("delete from employee where employee_id=?");
preparedStatement.setInt(1,actualEmployeeId);
preparedStatement.executeUpdate();
preparedStatement.close();
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}
public void update(EmployeeDTOInterface employeeDTO)throws DAOException
{
if(employeeDTO==null)throw new DAOException("Employee is null");
String employeeId=employeeDTO.getEmployeeId();
if(employeeId==null) throw new DAOException("employee id. is null");
employeeId=employeeId.trim();
if(employeeId.length()==0)throw new DAOException("Length of employee id. is zero");
int actualEmployeeId;
try
{
actualEmployeeId=Integer.parseInt(employeeId.substring(1))-1000000;
}catch(Exception exception)
{
throw new DAOException("invalid employee id");
}
String name=employeeDTO.getName();
if(name==null) throw new DAOException("Name is null");
name=name.trim();
if(name.length()==0)throw new DAOException("Length of name is zero");
int designationCode=employeeDTO.getDesignationCode();

Connection connection=null;
PreparedStatement preparedStatement;
ResultSet resultSet;
try
{
connection=DAOConnection.getConnection();
preparedStatement=connection.prepareStatement("select code from designation where code=?");
preparedStatement.setInt(1,designationCode);
resultSet=preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Invalid designation code"+designationCode);
}
resultSet.close();
preparedStatement.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}




//DesignationDAOInterface designationDAO=new DesignationDAO();
//if(!designationDAO.codeExists(designationCode))throw new DAOException("Invalid designation code"+designationCode);
java.util.Date dateOfBirth=employeeDTO.getDateOfBirth();
if(dateOfBirth==null)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("Date of birth is null");
}
char gender=employeeDTO.getGender();
boolean isIndian=employeeDTO.getIsIndian();
BigDecimal basicSalary=employeeDTO.getBasicSalary();
if(basicSalary==null)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("Basic Salary is null");
}
if(basicSalary.signum()==-1)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("Basic salary is nagative");
}
String panNumber=employeeDTO.getPANNumber();
if(panNumber==null)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("pan number is null");
}
panNumber=panNumber.trim();
if(panNumber.length()==0)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("Length of panNumber is zero");
}
String aadharCardNumber=employeeDTO.getAadharCardNumber();
if(aadharCardNumber==null) 
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("aadhar card number is null");
}
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("Length of aadhar card number is zero");
}
try
{
preparedStatement=connection.prepareStatement("select gender from employee where pan_number=? and employee_id<>?");
preparedStatement.setString(1,panNumber);
preparedStatement.setInt(2,actualEmployeeId);
resultSet=preparedStatement.executeQuery();
boolean panNumberExists=resultSet.next();
resultSet.close();
preparedStatement.close();
preparedStatement=connection.prepareStatement("select gender from employee where aadhar_number=? and employee_id<>?");
preparedStatement.setString(1,aadharCardNumber);
preparedStatement.setInt(2,actualEmployeeId);
resultSet=preparedStatement.executeQuery();
boolean aadharCardNumberExists=resultSet.next();
resultSet.close();
preparedStatement.close();
if(panNumberExists && aadharCardNumberExists)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("Pan number exists "+panNumber+" Aadhar card number exists "+aadharCardNumber);
}
if(aadharCardNumberExists)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("Aadhar card number exists"+aadharCardNumber);
}
if(panNumberExists)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("Pan number exists"+panNumber);
}
preparedStatement=connection.prepareStatement("update employee set name=?,designation_code=?,date_of_birth=?,gender=?,is_indian=?,basic_salary=?,pan_number=?,aadhar_number=? where employee_id=?");
preparedStatement.setString(1,name);
preparedStatement.setInt(2,designationCode);
java.sql.Date sqlDate=new java.sql.Date(dateOfBirth.getYear(),dateOfBirth.getMonth(),dateOfBirth.getDate());
preparedStatement.setDate(3,sqlDate);
preparedStatement.setString(4,String.valueOf(gender));
preparedStatement.setBoolean(5,isIndian);
preparedStatement.setBigDecimal(6,basicSalary);
preparedStatement.setString(7,panNumber);
preparedStatement.setString(8,aadharCardNumber);
preparedStatement.setInt(9,actualEmployeeId);
preparedStatement.executeUpdate();
preparedStatement.close();
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}
public Set<EmployeeDTOInterface> getAll()throws DAOException
{
Set<EmployeeDTOInterface> employees=new TreeSet<>();
try
{
Connection connection=DAOConnection.getConnection();
Statement statement=connection.createStatement();
ResultSet resultSet=statement.executeQuery("select * from employee");
EmployeeDTOInterface employeeDTO;
java.sql.Date sqlDate;
java.util.Date utilDate;
String gender;
while(resultSet.next())
{
employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId("A"+(resultSet.getInt("employee_id")+1000000));
employeeDTO.setName(resultSet.getString("name").trim());
employeeDTO.setDesignationCode(resultSet.getInt("designation_code"));
sqlDate=resultSet.getDate("date_of_birth");
utilDate=new java.util.Date(sqlDate.getYear(),sqlDate.getMonth(),sqlDate.getDate());
employeeDTO.setDateOfBirth(utilDate);
gender=resultSet.getString("gender");
if(gender.equals("M"))employeeDTO.setGender(GENDER.MALE);
else employeeDTO.setGender(GENDER.FEMALE);
employeeDTO.setIsIndian(resultSet.getBoolean("is_indian"));
employeeDTO.setBasicSalary(resultSet.getBigDecimal("basic_salary"));
employeeDTO.setPANNumber(resultSet.getString("pan_number").trim());
employeeDTO.setAadharCardNumber(resultSet.getString("aadhar_number").trim());
employees.add(employeeDTO);
}
resultSet.close();
statement.close();
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
return employees;
}
public Set<EmployeeDTOInterface> getByDesignationCode(int designationCode)throws DAOException
{
Set<EmployeeDTOInterface> employees=new TreeSet<>();
try
{
if(designationCode<=0)throw new DAOException("Invalid designation code");
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement=connection.prepareStatement("select code from designation where code=?");
preparedStatement.setInt(1,designationCode);
ResultSet resultSet=preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Invalid designation code");
}
resultSet.close();
preparedStatement.close();
preparedStatement=connection.prepareStatement("select * from employee where designation_code=?");
preparedStatement.setInt(1,designationCode);
resultSet=preparedStatement.executeQuery();
EmployeeDTOInterface employeeDTO;
java.sql.Date sqlDate;
java.util.Date utilDate;
String gender;
while(resultSet.next())
{
employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId("A"+(resultSet.getInt("employee_id")+1000000));
employeeDTO.setName(resultSet.getString("name").trim());
employeeDTO.setDesignationCode(resultSet.getInt("designation_code"));
sqlDate=resultSet.getDate("date_of_birth");
utilDate=new java.util.Date(sqlDate.getYear(),sqlDate.getMonth(),sqlDate.getDate());
employeeDTO.setDateOfBirth(utilDate);
gender=resultSet.getString("gender");
if(gender.equals("M"))employeeDTO.setGender(GENDER.MALE);
else employeeDTO.setGender(GENDER.FEMALE);
employeeDTO.setIsIndian(resultSet.getBoolean("is_indian"));
employeeDTO.setBasicSalary(resultSet.getBigDecimal("basic_salary"));
employeeDTO.setPANNumber(resultSet.getString("pan_number").trim());
employeeDTO.setAadharCardNumber(resultSet.getString("aadhar_number").trim());
employees.add(employeeDTO);
}
resultSet.close();
preparedStatement.close();
connection.close();
return employees;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}
public boolean isDesignationAlloted(int designationCode)throws DAOException
{
try
{
if(designationCode<=0)throw new DAOException("Invalid designation code");
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement=connection.prepareStatement("select code from designation where code=?");
preparedStatement.setInt(1,designationCode);
ResultSet resultSet=preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Invalid designation code");
}
resultSet.close();
preparedStatement.close();
preparedStatement=connection.prepareStatement("select gender from employee where designation_code=?");
preparedStatement.setInt(1,designationCode);
resultSet=preparedStatement.executeQuery();
boolean designationAlloted=resultSet.next();
resultSet.close();
preparedStatement.close();
connection.close();
return designationAlloted;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}


public EmployeeDTOInterface getByEmployeeId(String employeeId)throws DAOException
{
if(employeeId==null)throw new DAOException("Employee id is null");
employeeId=employeeId.trim();
if(employeeId.length()==0)throw new DAOException("length of employee id is zero");
int actualEmployeeId;
try
{
actualEmployeeId=Integer.parseInt(employeeId.substring(1))-1000000;
}catch(Exception exception)
{
throw new DAOException("Invalid employee Id.");
}
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement=connection.prepareStatement("select * from employee where employee_id=?");
preparedStatement.setInt(1,actualEmployeeId);
ResultSet resultSet=preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Invalid employee Id.");
}
EmployeeDTOInterface employeeDTO;
java.sql.Date sqlDate;
java.util.Date utilDate;
String gender;
employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId(employeeId);
employeeDTO.setName(resultSet.getString("name").trim());
employeeDTO.setDesignationCode(resultSet.getInt("designation_code"));
sqlDate=resultSet.getDate("date_of_birth");
utilDate=new java.util.Date(sqlDate.getYear(),sqlDate.getMonth(),sqlDate.getDate());
employeeDTO.setDateOfBirth(utilDate);
gender=resultSet.getString("gender");
if(gender.equals("M"))employeeDTO.setGender(GENDER.MALE);
else employeeDTO.setGender(GENDER.FEMALE);
employeeDTO.setIsIndian(resultSet.getBoolean("is_indian"));
employeeDTO.setBasicSalary(resultSet.getBigDecimal("basic_salary"));
employeeDTO.setPANNumber(resultSet.getString("pan_number").trim());
employeeDTO.setAadharCardNumber(resultSet.getString("aadhar_number").trim());
resultSet.close();
preparedStatement.close();
connection.close();
return employeeDTO;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}
public EmployeeDTOInterface getByPANNumber(String panNumber)throws DAOException
{
if(panNumber==null)throw new DAOException("Pan number is null");
panNumber=panNumber.trim();
if(panNumber.length()==0)throw new DAOException("length of pan number is zero");
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement=connection.prepareStatement("select * from employee where pan_number=?");
preparedStatement.setString(1,panNumber);
ResultSet resultSet=preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Invalid pan number");
}
EmployeeDTOInterface employeeDTO;
java.sql.Date sqlDate;
java.util.Date utilDate;
String gender;
employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId("A"+(resultSet.getInt("employee_id")+1000000));
employeeDTO.setName(resultSet.getString("name").trim());
employeeDTO.setDesignationCode(resultSet.getInt("designation_code"));
sqlDate=resultSet.getDate("date_of_birth");
utilDate=new java.util.Date(sqlDate.getYear(),sqlDate.getMonth(),sqlDate.getDate());
employeeDTO.setDateOfBirth(utilDate);
gender=resultSet.getString("gender");
if(gender.equals("M"))employeeDTO.setGender(GENDER.MALE);
else employeeDTO.setGender(GENDER.FEMALE);
employeeDTO.setIsIndian(resultSet.getBoolean("is_indian"));
employeeDTO.setBasicSalary(resultSet.getBigDecimal("basic_salary"));
employeeDTO.setPANNumber(resultSet.getString("pan_number").trim());
employeeDTO.setAadharCardNumber(resultSet.getString("aadhar_number").trim());
resultSet.close();
preparedStatement.close();
connection.close();
return employeeDTO;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}
public EmployeeDTOInterface getByAadharCardNumber(String aadharCardNumber)throws DAOException
{
if(aadharCardNumber==null)throw new DAOException("Aadhar card number is null");
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0)throw new DAOException("length of aadhar card number is zero");
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement=connection.prepareStatement("select * from employee where aadhar_number=?");
preparedStatement.setString(1,aadharCardNumber);
ResultSet resultSet=preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Invalid aadhar card number");
}
EmployeeDTOInterface employeeDTO;
java.sql.Date sqlDate;
java.util.Date utilDate;
String gender;
employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId("A"+(resultSet.getInt("employee_id")+1000000));
employeeDTO.setName(resultSet.getString("name").trim());
employeeDTO.setDesignationCode(resultSet.getInt("designation_code"));
sqlDate=resultSet.getDate("date_of_birth");
utilDate=new java.util.Date(sqlDate.getYear(),sqlDate.getMonth(),sqlDate.getDate());
employeeDTO.setDateOfBirth(utilDate);
gender=resultSet.getString("gender");
if(gender.equals("M"))employeeDTO.setGender(GENDER.MALE);
else employeeDTO.setGender(GENDER.FEMALE);
employeeDTO.setIsIndian(resultSet.getBoolean("is_indian"));
employeeDTO.setBasicSalary(resultSet.getBigDecimal("basic_salary"));
employeeDTO.setPANNumber(resultSet.getString("pan_number").trim());
employeeDTO.setAadharCardNumber(resultSet.getString("aadhar_number").trim());
resultSet.close();
preparedStatement.close();
connection.close();
return employeeDTO;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}
public boolean employeeIdExists(String employeeId)throws DAOException
{
if(employeeId==null)return false;
employeeId=employeeId.trim();
if(employeeId.length()==0)return false;
boolean employeeIdExists=false;
int actualEmployeeId;
try
{
actualEmployeeId=Integer.parseInt(employeeId.substring(1))-1000000;
}catch(Exception exception)
{
return false;
}
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement=connection.prepareStatement("select gender from employee where employee_id=?");
preparedStatement.setInt(1,actualEmployeeId);
ResultSet resultSet=preparedStatement.executeQuery();
employeeIdExists=resultSet.next();
resultSet.close();
preparedStatement.close();
connection.close();
return employeeIdExists;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}
public boolean panNumberExists(String panNumber)throws DAOException
{
if(panNumber==null)return false;
panNumber=panNumber.trim();
if(panNumber.length()==0)return false;
boolean panNumberExists=false;
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement=connection.prepareStatement("select gender from employee where pan_number=?");
preparedStatement.setString(1,panNumber);
ResultSet resultSet=preparedStatement.executeQuery();
panNumberExists=resultSet.next();
resultSet.close();
preparedStatement.close();
connection.close();
return panNumberExists;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}
public boolean aadharCardNumberExists(String aadharCardNumber)throws DAOException
{
if(aadharCardNumber==null)return false;
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0)return false;
boolean aadharNumberExists=false;
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement=connection.prepareStatement("select gender from employee where aadhar_number=?");
preparedStatement.setString(1,aadharCardNumber);
ResultSet resultSet=preparedStatement.executeQuery();
aadharNumberExists=resultSet.next();
resultSet.close();
preparedStatement.close();
connection.close();
return aadharNumberExists;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
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