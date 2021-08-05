package com.thinking.machines.hr.dl.dao;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
import java.io.*;
import java.util.*;
import java.sql.*;
public class DesignationDAO implements DesignationDAOInterface
{
private final static String FILE_NAME="designation.dat";
public void add(DesignationDTOInterface designationDTO)throws DAOException
{
if(designationDTO==null) throw new DAOException("Designation is null");
String title=designationDTO.getTitle().trim();
if(title==null)throw new DAOException("Designation is null");
if(title.length()==0)throw new DAOException("Length of Designation is zero");
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement statement=connection.prepareStatement("select code from designation where title=?");
statement.setString(1,title);
ResultSet r=statement.executeQuery();
if(r.next())
{
r.close();
statement.close();
connection.close();
throw new DAOException("Designation Exists :"+title);
}
r.close();
statement.close();
statement=connection.prepareStatement("insert into designation (title) values(?)",Statement.RETURN_GENERATED_KEYS);
statement.setString(1,title);
statement.executeUpdate();
r=statement.getGeneratedKeys();
r.next();
int code=r.getInt(1);
designationDTO.setCode(code);
r.close();
statement.close();
connection.close();
}catch(SQLException sqlException)
{ 
throw new DAOException(sqlException.getMessage());
}
}
public void delete(int code)throws DAOException
{
if(code<=0)throw new DAOException("Invalid code");
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement=connection.prepareStatement("select * from designation where code=?");
preparedStatement.setInt(1,code);
ResultSet resultSet=preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("code: "+code+" does not exist.");
}
String title=resultSet.getString("title").trim();
resultSet.close();
preparedStatement.close();

preparedStatement=connection.prepareStatement("select gender from employee where designation_code=?");
preparedStatement.setInt(1,code);
resultSet=preparedStatement.executeQuery();
if(resultSet.next())
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Cannot delete designation: "+title+"as it has been alloted to employees");
}
resultSet.close();
preparedStatement.close();
preparedStatement=connection.prepareStatement("delete from designation where code=?");
preparedStatement.setInt(1,code);
preparedStatement.executeUpdate();
preparedStatement.close();
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}
public void update(DesignationDTOInterface designationDTO)throws DAOException
{
if(designationDTO==null)throw new DAOException("Designation required");
int code=designationDTO.getCode();
String title=designationDTO.getTitle();
if(code<=0)throw new DAOException("Invalid code");
if(title==null)throw new DAOException("Designtion is null");
title=title.trim();
if(title.length()==0)throw new DAOException("length of designation is null");
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement=connection.prepareStatement("select code from designation where code=?");
preparedStatement.setInt(1,code);
ResultSet resultSet=preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("code: "+code+" does not exist.");
}
resultSet.close();
preparedStatement.close();

preparedStatement=connection.prepareStatement("select code from designation where title=? and code<>?");
preparedStatement.setString(1,title);
preparedStatement.setInt(2,code);
resultSet=preparedStatement.executeQuery();
if(resultSet.next())
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Designatin: "+title+" exists.");
}
resultSet.close();
preparedStatement.close();

preparedStatement=connection.prepareStatement("update designation set title=? where code=?");
preparedStatement.setString(1,title);
preparedStatement.setInt(2,code);
preparedStatement.executeUpdate();
preparedStatement.close();
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}
public Set<DesignationDTOInterface> getAll()throws DAOException
{
Set<DesignationDTOInterface> designations=new TreeSet<>();
try
{
Connection connection=DAOConnection.getConnection();
Statement statement=connection.createStatement();
ResultSet resultSet=statement.executeQuery("select * from designation");
DesignationDTOInterface designationDTO;
while(resultSet.next())
{
designationDTO=new DesignationDTO();
designationDTO.setCode(resultSet.getInt("code"));
designationDTO.setTitle(resultSet.getString("title").trim());
designations.add(designationDTO);
}
resultSet.close();
statement.close();
connection.close();
return designations;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}
public DesignationDTOInterface getByCode(int code)throws DAOException
{
throw new DAOException("not yet Implement");
}
public DesignationDTOInterface getByTitle(String title)throws DAOException
{
throw new DAOException("not yet Implement");
}
public boolean codeExists(int code)throws DAOException
{
throw new DAOException("not yet Implement");
}
public boolean titleExists(String title)throws DAOException
{
throw new DAOException("not yet Implement");
}
public int getCount()throws DAOException
{
throw new DAOException("not yet Implement");
}
}