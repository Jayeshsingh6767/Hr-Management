package com.thinking.machines.hr.dl.dto;
import com.thinking.machines.hr.dl.interfaces.dto.*;
public class DesignationDTO implements DesignationDTOInterface
{
private int code;
private String title;
public void setCode(int code)
{
this.code=code;
}
public void setTitle(String title)
{
this.title=title;
}
public int getCode()
{
return this.code;
}
public String getTitle()
{
return this.title;
}
public boolean equals(Object other)
{
if(!(other instanceof DesignationDTOInterface)) return false;
DesignationDTOInterface designationDTOInterface=(DesignationDTOInterface)other;
return this.code==designationDTOInterface.getCode();
}
public int compareTo(DesignationDTOInterface DesignationDTO)
{
return this.code-DesignationDTO.getCode();
}
public int hashCode()
{
return this.code;
}
}