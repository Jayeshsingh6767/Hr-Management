package com.thinking.machines.hr.dl.dao;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
import java.io.*;
import java.util.*;
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
File file=new File(FILE_NAME);
RandomAccessFile randomAccessFile;
randomAccessFile=new RandomAccessFile(file,"rw");
int lastGeneratedCode=0;
int noOfRecords=0;
String lastGeneratedCodeString="0";
String noOfRecordsString="0";
if(randomAccessFile.length()==0)
{
while(lastGeneratedCodeString.length()<10)lastGeneratedCodeString+=" ";
while(noOfRecordsString.length()<10)noOfRecordsString+=" ";
randomAccessFile.writeBytes(lastGeneratedCodeString);
randomAccessFile.writeBytes("\n");
randomAccessFile.writeBytes(noOfRecordsString);
randomAccessFile.writeBytes("\n");
}
else
{
lastGeneratedCode=Integer.parseInt(randomAccessFile.readLine().trim());
noOfRecords=Integer.parseInt(randomAccessFile.readLine().trim());
}
int fCode;
String fTitle;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fCode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
if(fTitle.equalsIgnoreCase(title))
{
randomAccessFile.close();
throw new DAOException("Designation Exists :"+title);
}
}
lastGeneratedCode++;
noOfRecords++;
randomAccessFile.writeBytes(String.valueOf(lastGeneratedCode));
randomAccessFile.writeBytes("\n");
randomAccessFile.writeBytes(title);
randomAccessFile.writeBytes("\n");
randomAccessFile.seek(0);
designationDTO.setCode(lastGeneratedCode);
lastGeneratedCodeString=String.valueOf(lastGeneratedCode);
noOfRecordsString=String.valueOf(noOfRecords);
while(lastGeneratedCodeString.length()<10)lastGeneratedCodeString+=" ";
while(noOfRecordsString.length()<10)noOfRecordsString+=" ";
randomAccessFile.writeBytes(lastGeneratedCodeString);
randomAccessFile.writeBytes("\n");
randomAccessFile.writeBytes(noOfRecordsString);
randomAccessFile.writeBytes("\n");
randomAccessFile.close();
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public void delete(int code)throws DAOException
{
if(code<=0)throw new DAOException("Invalid code");
try
{
File file=new File(FILE_NAME);
if(file.exists()==false)throw new DAOException("Invalid code");
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new DAOException("Invalid code");
}
randomAccessFile.readLine();
randomAccessFile.readLine();
int fCode;
String fTitle;
boolean found=false;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fCode=Integer.parseInt(randomAccessFile.readLine());
randomAccessFile.readLine();
if(fCode==code)
{
found=true;
break;
}
}
if(found==false)
{
randomAccessFile.close();
throw new DAOException("Invalid code");
}
File tmpFile=new File("tmp.tmp");
RandomAccessFile tmpRandomAccessFile=new RandomAccessFile(tmpFile,"rw");
if(tmpRandomAccessFile.length()!=0)tmpRandomAccessFile.setLength(0);
randomAccessFile.seek(0);
int recordCount;
String recordCountString;
tmpRandomAccessFile.writeBytes(randomAccessFile.readLine());
tmpRandomAccessFile.writeBytes("\n");
recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
recordCount--;
recordCountString=String.valueOf(recordCount);
for(;recordCountString.length()<10;recordCountString+=" ");
tmpRandomAccessFile.writeBytes(recordCountString);
tmpRandomAccessFile.writeBytes("\n");
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fCode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
if(fCode!=code)
{
tmpRandomAccessFile.writeBytes(fCode+"\n");
tmpRandomAccessFile.writeBytes(fTitle+"\n");
}
}
randomAccessFile.seek(0);
tmpRandomAccessFile.seek(0);
while(tmpRandomAccessFile.getFilePointer()<tmpRandomAccessFile.length())
{
randomAccessFile.writeBytes(tmpRandomAccessFile.readLine()+"\n");
randomAccessFile.writeBytes(tmpRandomAccessFile.readLine()+"\n");
}
randomAccessFile.setLength(tmpRandomAccessFile.length());
tmpRandomAccessFile.setLength(0);
randomAccessFile.close();
tmpRandomAccessFile.close();
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
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
File file=new File(FILE_NAME);
if(file.exists()==false)throw new DAOException("Invalid code");
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new DAOException("Invalid code");
}
randomAccessFile.readLine();
randomAccessFile.readLine();
int fCode;
String fTitle;
boolean found=false;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fCode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
if(fTitle.equalsIgnoreCase(title) && fCode!=code)
{
randomAccessFile.close();
throw new DAOException("Title Exists: "+title);
}
if(fCode==code)
{
found=true;
}
}
if(found==false)
{
randomAccessFile.close();
throw new DAOException("Invalid code");
}
File tmpFile=new File("tmp.tmp");
RandomAccessFile tmpRandomAccessFile=new RandomAccessFile(tmpFile,"rw");
if(tmpRandomAccessFile.length()!=0)tmpRandomAccessFile.setLength(0);
randomAccessFile.seek(0);
tmpRandomAccessFile.writeBytes(randomAccessFile.readLine());
tmpRandomAccessFile.writeBytes("\n");
tmpRandomAccessFile.writeBytes(randomAccessFile.readLine());
tmpRandomAccessFile.writeBytes("\n");
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fCode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
if(fCode!=code)
{
tmpRandomAccessFile.writeBytes(fCode+"\n");
tmpRandomAccessFile.writeBytes(fTitle+"\n");
}
else
{
tmpRandomAccessFile.writeBytes(code+"\n");
tmpRandomAccessFile.writeBytes(title+"\n");
}
}
randomAccessFile.seek(0);
tmpRandomAccessFile.seek(0);
while(tmpRandomAccessFile.getFilePointer()<tmpRandomAccessFile.length())
{
randomAccessFile.writeBytes(tmpRandomAccessFile.readLine()+"\n");
randomAccessFile.writeBytes(tmpRandomAccessFile.readLine()+"\n");
}
randomAccessFile.setLength(tmpRandomAccessFile.length());
tmpRandomAccessFile.setLength(0);
randomAccessFile.close();
tmpRandomAccessFile.close();
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public Set<DesignationDTOInterface> getAll()throws DAOException
{
Set<DesignationDTOInterface> designations=new TreeSet<>();
try
{
File file=new File(FILE_NAME);
if(file.exists()==false) return designations;
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
return designations;
}
randomAccessFile.readLine();
randomAccessFile.readLine();
DesignationDTOInterface designationDTO;
int fCode;
String fTitle;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fCode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
designationDTO=new DesignationDTO();
designationDTO.setCode(fCode);
designationDTO.setTitle(fTitle);
designations.add(designationDTO);
}
randomAccessFile.close();
return designations;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
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