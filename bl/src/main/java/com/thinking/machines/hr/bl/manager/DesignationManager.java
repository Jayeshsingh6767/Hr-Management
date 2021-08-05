package com.thinking.machines.hr.bl.manager;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.dl.exceptions.*;
import java.util.*;
public class DesignationManager implements DesignationManagerInterface
{
private Map<Integer,DesignationInterface> codeWiseDesignationsMap;
private Map<String,DesignationInterface> titleWiseDesignationsMap;
private Set<DesignationInterface> designationsSet;
private static DesignationManager designationManager=null;
private DesignationManager() throws BLException
{	
populateDataStructures();
} 
private void populateDataStructures() throws BLException
{
codeWiseDesignationsMap=new HashMap<>();
titleWiseDesignationsMap=new HashMap<>();
designationsSet=new TreeSet<>();
Set<DesignationDTOInterface> dlDesignations;
try
{
dlDesignations=new DesignationDAO().getAll();
DesignationInterface designation;
for(DesignationDTOInterface dlDesignation:dlDesignations)
{
designation=new Designation();
designation.setCode(dlDesignation.getCode());
designation.setTitle(dlDesignation.getTitle());
codeWiseDesignationsMap.put(designation.getCode(),designation);
titleWiseDesignationsMap.put(designation.getTitle().toUpperCase(),designation);
designationsSet.add(designation);
}
}catch(DAOException daoException)
{
BLException blException=new BLException();
blException.setGenericException(daoException.getMessage());
throw blException;
}
}
public static DesignationManagerInterface getDesignationManager()throws BLException
{
if(designationManager==null)designationManager=new DesignationManager();
return designationManager;
}


public void addDesignation(DesignationInterface designation)throws BLException
{
BLException blException=new BLException();
if(designation==null) 
{
blException.setGenericException("Designation required");
throw blException;
}
int code=designation.getCode();
String title=designation.getTitle();
if(code!=0)blException.addException("code","code should be zero");
if(title==null)
{
blException.addException("title","title required");
}else
{
title=title.trim();
if(title.length()==0)blException.addException("title","title required");
}
if(titleWiseDesignationsMap.containsKey(title))
{
blException.addException("title","title exists: "+title);
}
if(blException.hasExceptions())
{
throw blException;
}
try
{
DesignationDTOInterface designationDTO=new DesignationDTO();
designationDTO.setTitle(title);
DesignationDAOInterface designationDAO=new DesignationDAO();
designationDAO.add(designationDTO);
code=designationDTO.getCode();
designation.setCode(code);
DesignationInterface designationPojo=new Designation();
designationPojo.setCode(code);
designationPojo.setTitle(title);
codeWiseDesignationsMap.put(code,designationPojo);
titleWiseDesignationsMap.put(title,designationPojo);
designationsSet.add(designationPojo);
}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
throw blException;
}
}
public void updateDesignation(DesignationInterface designation)throws BLException
{
BLException blException=new BLException();
if(designation==null) 
{
blException.setGenericException("Designation required");
throw blException;
}
int code=designation.getCode();
String title=designation.getTitle();
if(code<=0)
{
blException.addException("code","invalid code: "+code);
throw blException;
}
if(codeWiseDesignationsMap.containsKey(code)==false)
{
blException.addException("code","invalid code: "+code);
throw blException;
}
if(title==null)
{
blException.addException("title","title required");
title="";
}else
{
title=title.trim();
if(title.length()==0)blException.addException("title","title required");
}
if(title.length()>0)
{
DesignationInterface d;
d=titleWiseDesignationsMap.get(title.toUpperCase());
if(d!=null)
{
if(d.getCode()!=code)
{
blException.addException("title","Designation "+title+" exists.");
}
}
}
if(blException.hasExceptions())
{
throw blException;
}
try
{
DesignationInterface dsDesignation=codeWiseDesignationsMap.get(code);
DesignationDTOInterface designationDTO=new DesignationDTO();
designationDTO.setCode(code);
designationDTO.setTitle(title);
new DesignationDAO().update(designationDTO);
//deleting in DS
codeWiseDesignationsMap.remove(code);
titleWiseDesignationsMap.remove(dsDesignation.getTitle().toUpperCase());
designationsSet.remove(dsDesignation);
//update DS
dsDesignation.setTitle(title);
codeWiseDesignationsMap.put(code,dsDesignation);
titleWiseDesignationsMap.put(title,dsDesignation);
designationsSet.add(dsDesignation);
}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
throw blException;
}
}
public void removeDesignation(int code)throws BLException
{
BLException blException=new BLException();
if(code<=0)
{
blException.addException("code","invalid code: "+code);
throw blException;
}
if(codeWiseDesignationsMap.containsKey(code)==false)
{
blException.addException("code","invalid code: "+code);
throw blException;
}
try
{
DesignationInterface dsDesignation=codeWiseDesignationsMap.get(code);
new DesignationDAO().delete(code);
//deleting in DS
codeWiseDesignationsMap.remove(code);
titleWiseDesignationsMap.remove(dsDesignation.getTitle().toUpperCase());
designationsSet.remove(dsDesignation);
}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
throw blException;
}
}

DesignationInterface getDSDesignationByCode(int code)
{
DesignationInterface designation=codeWiseDesignationsMap.get(code);
return designation;
}

public DesignationInterface getDesignationByCode(int code)throws BLException
{
BLException blException=new BLException();
if(code<=0)
{
blException.addException("code","Invalid code");
throw blException;
}
DesignationInterface designation=codeWiseDesignationsMap.get(code);
if(designation==null)
{
blException.addException("code","Invalid code");
throw blException;
}
DesignationInterface d=new Designation();
d.setCode(designation.getCode());
d.setTitle(designation.getTitle());
return designation;
}
public DesignationInterface getDesignationByTitle(String title)throws BLException
{
BLException blException=new BLException();

if(title==null)
{
blException.addException("title","Designation is null");
throw blException;
}
title=title.trim();
if(title.length()==0)
{
blException.addException("title","Length of Designation is zero");
throw blException;
}
DesignationInterface designation=titleWiseDesignationsMap.get(title.toUpperCase());
if(designation==null)
{
blException.addException("code","Invalid Designation");
throw blException;
}
DesignationInterface d=new Designation();
d.setCode(designation.getCode());
d.setTitle(designation.getTitle());
return d;
}
public boolean designationCodeExists(int code)
{
if(code<=0)
{
return false;
}
return codeWiseDesignationsMap.containsKey(code);
}
public boolean designationTitleExists(String title)
{
if(title==null)return false;
title=title.trim();
if(title.length()==0)return false;
return titleWiseDesignationsMap.containsKey(title.toUpperCase());
}
public int getDesignationCount()
{
return designationsSet.size();
}
public Set<DesignationInterface> getDesignations()
{
return designationsSet;
}
}
 