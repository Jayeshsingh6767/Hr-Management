package com.thinking.machines.hr.pl.models;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.manager.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
import java.util.*;
import javax.swing.table.*;
import java.io.*;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.io.image.*;
import com.itextpdf.kernel.font.*;
import com.itextpdf.io.font.constants.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.*;
import com.itextpdf.layout.borders.*;

public class DesignationModel extends AbstractTableModel
{
private java.util.List<DesignationInterface> designations;
private String columnTitle[];
private DesignationManagerInterface designationManager;
public DesignationModel()
{
populateDataStructure();
}
private void populateDataStructure()
{
columnTitle=new String[2];
columnTitle[0]="Sr.No.";
columnTitle[1]="Title";
designations=new LinkedList<>();
try
{
designationManager=DesignationManager.getDesignationManager();
}catch(BLException blException)
{
// ?????????what to do
}
Set<DesignationInterface> blDesignations=designationManager.getDesignations();
for(DesignationInterface designation:blDesignations)
{
designations.add(designation);
}
Collections.sort(this.designations,new Comparator<DesignationInterface>(){
public int compare(DesignationInterface left,DesignationInterface right)
{
return left.getTitle().toUpperCase().compareTo(right.getTitle().toUpperCase());
}
});
}

public int getColumnCount()
{
return columnTitle.length;
}
public int getRowCount()
{
return designations.size();
}
public String getColumnName(int columnIndex)
{
return columnTitle[columnIndex];
}
public Object getValueAt(int rowIndex,int columnIndex)
{
if(columnIndex==0)return rowIndex+1;
return designations.get(rowIndex).getTitle();
}
public Class getColumnClass(int columnIndex)
{
if(columnIndex==0)return Integer.class;
return String.class;
}
public boolean isCellEditable(int rowIndex,int columnIndex)
{
return false;
}
public void add(DesignationInterface designation )throws BLException
{
this.designationManager.addDesignation(designation);
this.designations.add(designation);
Collections.sort(this.designations,new Comparator<DesignationInterface>(){
public int compare(DesignationInterface left,DesignationInterface right)
{
return left.getTitle().toUpperCase().compareTo(right.getTitle().toUpperCase());
}
});
fireTableDataChanged();
}
public int indexOfDesignation(DesignationInterface designation)throws BLException
{
Iterator<DesignationInterface> i=designations.iterator();
DesignationInterface d;
int index=0;
while(i.hasNext())
{
d=i.next();
if(d.equals(designation))return index;
index++;
}
BLException blException=new BLException();
blException.setGenericException("Invalid Designation: "+designation.getTitle());
throw blException;
}
public int indexOfTitle(String title,boolean partialLeftSearch)throws BLException
{
Iterator<DesignationInterface> i=designations.iterator();
DesignationInterface d;
int index=0;
while(i.hasNext())
{
d=i.next();
if(partialLeftSearch)
{
if(d.getTitle().toUpperCase().startsWith(title.toUpperCase()))
{
return index;
}
}
else
{
if(title.equalsIgnoreCase(d.getTitle()))return index;
}
index++;
}
BLException blException=new BLException();
blException.setGenericException("Invalid Title: "+title);
throw blException;
}

public void update(DesignationInterface designation )throws BLException
{
this.designationManager.updateDesignation(designation);
this.designations.remove(indexOfDesignation(designation));
this.designations.add(designation);
Collections.sort(this.designations,new Comparator<DesignationInterface>(){
public int compare(DesignationInterface left,DesignationInterface right)
{
return left.getTitle().toUpperCase().compareTo(right.getTitle().toUpperCase());
}
});
fireTableDataChanged();
}
public void remove(int code)throws BLException
{
this.designationManager.removeDesignation(code);
Iterator<DesignationInterface> i=designations.iterator();
DesignationInterface d;
int index=0;
while(i.hasNext())
{
d=i.next();
if(d.getCode()==code)break;
index++;
}
BLException blException=new BLException();
if(index==designations.size())
{
blException.setGenericException("Invalid Designation code"+code);
throw blException;
}
this.designations.remove(index);
fireTableDataChanged();
}
public DesignationInterface getDesignationAt(int rowIndex)throws BLException
{
if(rowIndex<0 || rowIndex>=this.designations.size())
{
BLException blException=new BLException();
blException.setGenericException("Invalid index");
throw blException;
}
return designations.get(rowIndex); 
}
public void exportToPDF(File file)throws BLException
{
try
{
if(file.exists())file.delete();
PdfWriter pdfWriter=new PdfWriter(file);
PdfDocument pdfDocument=new PdfDocument(pdfWriter);
Document document=new Document(pdfDocument);
Paragraph logoPara=new Paragraph();
Image logoImage=new Image(ImageDataFactory.create(this.getClass().getResource("/icons/deer.png")));
logoPara.add(logoImage);
Paragraph companyNamePara=new Paragraph("ABCD Corporation");
PdfFont companyNameFont=PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
companyNamePara.setFont(companyNameFont);
companyNamePara.setFontSize(18);
Paragraph reportTitlePara=new Paragraph("List of Designations");
PdfFont reportTitleFont=PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
reportTitlePara.setFont(reportTitleFont);
reportTitlePara.setFontSize(16);
Paragraph columnTitlePara1=new Paragraph("Sr.no.");
Paragraph columnTitlePara2=new Paragraph("Title");
PdfFont columnTitleFont=PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
columnTitlePara1.setFont(columnTitleFont);
columnTitlePara1.setFontSize(14);
columnTitlePara2.setFont(columnTitleFont);
columnTitlePara2.setFontSize(14);
PdfFont dataFont=PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
PdfFont pageNumberFont=PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
Paragraph softwareByPara=new Paragraph("Software by Jayesh Singh");
softwareByPara.setFont(reportTitleFont);
softwareByPara.setFontSize(15);
Paragraph pageNumberPara;
Paragraph dataPara;
float topTableColumnWidts[]={1,5};
float dataTableColumnWidts[]={1,7};
int sno,x,pageSize;
pageSize=15;
sno=0;
x=0;
boolean newPage=true;
Table dataTable=null;
Table topTable;
Table pageNumberTable;
Cell cell;
int pageNumber=0;
int numberOfPages=this.designations.size()/pageSize;
if((this.designations.size()%pageSize)!=0) numberOfPages++;
DesignationInterface designation;
while(x<this.designations.size())
{
if(newPage)
{
//create new Page header
pageNumber++;
topTable=new Table(UnitValue.createPercentArray(topTableColumnWidts));
cell=new Cell();
cell.setBorder(Border.NO_BORDER);
cell.add(logoPara);
topTable.addCell(cell);
cell=new Cell();
cell.setBorder(Border.NO_BORDER);
cell.add(companyNamePara);
cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
topTable.addCell(cell);
document.add(topTable);
pageNumberPara=new Paragraph("Page no :"+pageNumber+"/"+numberOfPages);
pageNumberPara.setFont(pageNumberFont);
pageNumberPara.setFontSize(13);
pageNumberTable=new Table(1);
pageNumberTable.setWidth(UnitValue.createPercentValue(100));
cell=new Cell();
cell.setBorder(Border.NO_BORDER);
cell.add(pageNumberPara);
cell.setTextAlignment(TextAlignment.RIGHT);
document.add(pageNumberTable);
dataTable=new Table(UnitValue.createPercentArray(dataTableColumnWidts));
dataTable.setWidth(UnitValue.createPercentValue(100));
cell=new Cell(1,2);
cell.add(reportTitlePara);
cell.setTextAlignment(TextAlignment.CENTER);
dataTable.addCell(cell);
cell=new Cell();
cell.add(columnTitlePara1);
dataTable.addCell(cell);
cell=new Cell();
cell.add(columnTitlePara2);
dataTable.addCell(cell);

//dataTable.addHeaderCell(columnTitlePara1);
//dataTable.addHeaderCell(columnTitlePara2);
newPage=false;
}
designation=this.designations.get(x);
//add row to table
sno++;
cell=new Cell();
dataPara=new Paragraph(String.valueOf(sno));
dataPara.setFont(dataFont);
dataPara.setFontSize(14);
cell.add(dataPara);
cell.setTextAlignment(TextAlignment.RIGHT);
dataTable.addCell(cell);
cell=new Cell();
dataPara=new Paragraph(designation.getTitle());
dataPara.setFont(dataFont);
dataPara.setFontSize(14);
cell.add(dataPara);
dataTable.addCell(cell);

x++;
if(sno%pageSize==0 ||x==this.designations.size())
{
document.add(dataTable);
document.add(softwareByPara);
if(x<this.designations.size())
{
//add new page to document;
document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
newPage=true;
}
}
}
document.close();
}catch(Exception e)
{
BLException blexception=new BLException();
blexception.setGenericException(e.getMessage());
}
}
}
