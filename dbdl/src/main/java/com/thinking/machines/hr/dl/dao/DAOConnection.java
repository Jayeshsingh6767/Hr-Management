package com.thinking.machines.hr.dl.dao;
import com.thinking.machines.hr.dl.exceptions.*;
import java.sql.*;
class DAOConnection
{
private DAOConnection()
{

}
public static Connection getConnection()throws DAOException
{
Connection connection;
try
{
Class.forName("com.mysql.cj.jdbc.Driver");
connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/hrdb","hr","hr");
}catch(Exception exception)
{
throw new DAOException(exception.getMessage());
}
return connection;
}

}