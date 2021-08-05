package com.thinking.machines.hr.pl.ui;
import com.thinking.machines.hr.pl.models.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.io.*;
public class DesignationUI extends JFrame implements DocumentListener,ListSelectionListener
{
private JLabel titleLabel,searchLabel,searchErrorLabel;
private JTextField searchTextField;
private JButton clearSearchFieldButton;
private JTable designationTable;
private JScrollPane scrollPane;
private DesignationModel designationModel;
private Container container;
private DesignationPanel designationPanel;
private enum MODE{VIEW,ADD,EDIT,DELETE,EXPORT_TO_PDF};
private MODE mode;
private ImageIcon logoIcon;
private ImageIcon addIcon;
private ImageIcon saveIcon;
private ImageIcon editIcon;
private ImageIcon deleteIcon;
private ImageIcon cancelIcon;
private ImageIcon pdfIcon;
public DesignationUI()
{
initComponents();
setAppearance();
addListeners();
setViewMode();
designationPanel.setViewMode();
}


private void initComponents()
{//"c:"+File.separator+"javaProjects"+File.separator+"hr"+File.separator+"pl"+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"icons"+File.separator+"deer.png"
logoIcon=new ImageIcon(this.getClass().getResource("/icons/deer.png"));
addIcon=new ImageIcon(this.getClass().getResource("/icons/add.png"));
saveIcon=new ImageIcon(this.getClass().getResource("/icons/save.png"));
editIcon=new ImageIcon(this.getClass().getResource("/icons/edit.png"));
deleteIcon=new ImageIcon(this.getClass().getResource("/icons/delete.png"));
cancelIcon=new ImageIcon(this.getClass().getResource("/icons/cancel.png"));
pdfIcon=new ImageIcon(this.getClass().getResource("/icons/pdf.png"));
setIconImage(logoIcon.getImage());
designationModel=new DesignationModel();
designationTable=new JTable(designationModel);
scrollPane=new JScrollPane(designationTable,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
titleLabel=new JLabel("Designation");
searchLabel=new JLabel("search");
searchErrorLabel=new JLabel();
clearSearchFieldButton=new JButton("A");
searchTextField=new JTextField();
container=getContentPane();
designationPanel=new DesignationPanel();
}

private void setAppearance()
{
Font titleFont=new Font("Comic Sans MS",Font.BOLD,36);
Font captionFont=new Font("Verdana",Font.BOLD,16);
Font dataFont=new Font("Verdana",Font.PLAIN,16);
Font columnHeaderFont=new Font("Verdana",Font.BOLD,16);
Font searchErrorFont=new Font("Verdana",Font.BOLD,12);
titleLabel.setFont(titleFont);
searchLabel.setFont(captionFont);
searchTextField.setFont(dataFont);
searchErrorLabel.setFont(searchErrorFont);
designationTable.setFont(dataFont);
designationTable.getColumnModel().getColumn(0).setPreferredWidth(20);
designationTable.getColumnModel().getColumn(1).setPreferredWidth(350);
JTableHeader header=designationTable.getTableHeader();
header.setFont(columnHeaderFont);
header.setReorderingAllowed(false);
header.setResizingAllowed(false);
designationTable.setRowSelectionAllowed(true);
designationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
container.setLayout(null);
int lm,tm;
lm=0;
tm=0;
titleLabel.setBounds(lm+10,tm+10,200+200,40+40);
searchLabel.setBounds(lm+10,tm+10+40+10+40,100,20);
searchTextField.setBounds(lm+10+60+10,tm+10+40+10+40,350,20);
clearSearchFieldButton.setBounds(lm+10+60+10+5+350,tm+10+40+10+40,20,20);
searchErrorLabel.setBounds(lm+20+60+10+350-80,tm+10+40+10-20+40,100,20);
searchErrorLabel.setForeground(Color.red);
scrollPane.setBounds(lm+10,tm+10+40+10+20+10+40,468,200);
designationPanel.setBounds(lm+10,tm+10+40+10+20+10+200+10+40,468,150);
container.add(titleLabel);
container.add(searchLabel);
container.add(searchTextField);
container.add(clearSearchFieldButton);
container.add(searchErrorLabel);
container.add(scrollPane);
container.add(designationPanel);
int w=500;
int h=537;
setSize(w,h);
Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
setLocation((d.width/2)-(w/2),(d.height/2)-(h/2));
}

private void addListeners()
{
searchTextField.getDocument().addDocumentListener(this);
clearSearchFieldButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
searchTextField.setText("");
searchTextField.requestFocus();
}
});
designationTable.getSelectionModel().addListSelectionListener(this);
}

private void searchDesignation()
{
searchErrorLabel.setText("");
String title=searchTextField.getText().trim();
if(title.length()==0) return;
int rowIndex;
try
{
rowIndex=designationModel.indexOfTitle(title,true);
}catch(BLException blException)
{
searchErrorLabel.setText("Not Found");
return;
}
designationTable.setRowSelectionInterval(rowIndex,rowIndex);
Rectangle rectangle=designationTable.getCellRect(rowIndex,0,true);
designationTable.scrollRectToVisible(rectangle);
}
public void removeUpdate(DocumentEvent de)
{
searchDesignation();
}
public void insertUpdate(DocumentEvent de)
{
searchDesignation();
}
public void changedUpdate(DocumentEvent de)
{
searchDesignation();
}
public void valueChanged(ListSelectionEvent lse)
{
int selectedRowIndex=designationTable.getSelectedRow();
DesignationInterface designation;
try
{
designation=designationModel.getDesignationAt(selectedRowIndex);
designationPanel.setDesignation(designation);
}catch(BLException blExcpetion)
{
designationPanel.clearDesignation();
}
}
public void setViewMode()
{
this.mode=MODE.VIEW;
if(designationTable.getRowCount()==0)
{
searchTextField.setEnabled(false);
clearSearchFieldButton.setEnabled(false);
designationTable.setEnabled(false);
}
else
{
searchTextField.setEnabled(true);
clearSearchFieldButton.setEnabled(true);
designationTable.setEnabled(true);
}
}
private void setAddMode()
{
this.mode=MODE.ADD;

searchTextField.setEnabled(false);
clearSearchFieldButton.setEnabled(false);
designationTable.setEnabled(false);
}

private void setEditMode()
{
this.mode=MODE.EDIT;

searchTextField.setEnabled(false);
clearSearchFieldButton.setEnabled(false);
designationTable.setEnabled(false);
}

private void setDeleteMode()
{
this.mode=MODE.DELETE;

searchTextField.setEnabled(false);
clearSearchFieldButton.setEnabled(false);
designationTable.setEnabled(false);
}
private void setExportToPDFMode()
{
this.mode=MODE.EXPORT_TO_PDF;
searchTextField.setEnabled(false);
clearSearchFieldButton.setEnabled(false);
designationTable.setEnabled(false);
}
//inner class starts
class DesignationPanel extends JPanel
{
private JLabel titleCaptionLabel,titleLabel;
private JTextField titleTextField;
private JButton clearTitleTextFieldButton;
private JButton addButton,editButton,cancelButton,deleteButton;
private JButton exportToPDFButton;
private JPanel buttonsPanel;
private DesignationInterface designation;
DesignationPanel()
{
setBorder(BorderFactory.createLineBorder(new Color(150,150,150)));
initComponents();
setAppearance();
addListeners();
}
public void setDesignation(DesignationInterface designation)
{
this.designation=designation;
titleLabel.setText(designation.getTitle());
}
public void clearDesignation()
{
this.designation=null;
titleLabel.setText("");
}
private void initComponents()
{
designation=null;
titleCaptionLabel=new JLabel("Designation");
titleLabel=new JLabel("");
titleTextField=new JTextField("");
clearTitleTextFieldButton=new JButton("X");
addButton=new JButton(addIcon);
editButton=new JButton(editIcon);
cancelButton=new JButton(cancelIcon);
deleteButton=new JButton(deleteIcon);
exportToPDFButton=new JButton(pdfIcon);
buttonsPanel=new JPanel();
}
private void setAppearance()
{
Font captionFont=new Font("Comic Sans MS",Font.BOLD,20);
Font dataFont=new Font("Verdana",Font.PLAIN,16);
titleCaptionLabel.setFont(captionFont);
titleLabel.setFont(dataFont);
buttonsPanel.setBorder(BorderFactory.createLineBorder(new Color(150,150,150)));
setLayout(null);
int lm=10;
int tm=10;
titleCaptionLabel.setBounds(lm,tm,120,30);
titleLabel.setBounds(lm+100+20,tm+5,250,20);
titleTextField.setBounds(lm+100+20,tm+10,280,20);
clearTitleTextFieldButton.setBounds(lm+150+20+200+10+30,tm+10,20,20);
buttonsPanel.setLayout(null);
buttonsPanel.setBounds(lm+15,tm+30+10+10,420,150-50-20);
addButton.setBounds(lm+10+25,tm+5,50,50);
editButton.setBounds(lm+10+40+30+25,tm+5,50,50);
cancelButton.setBounds(lm+10+40+20+40+40+25,tm+5,50,50);
deleteButton.setBounds(lm+10+40+20+40+20+40+50+25,tm+5,50,50);
exportToPDFButton.setBounds(lm+10+40+20+40+20+40+20+40+60+25,tm+5,50,50);
buttonsPanel.add(addButton);
buttonsPanel.add(editButton);
buttonsPanel.add(cancelButton);
buttonsPanel.add(deleteButton);
buttonsPanel.add(exportToPDFButton);
add(titleCaptionLabel);
add(titleTextField);
add(titleLabel);
add(clearTitleTextFieldButton);
add(buttonsPanel);
}
private boolean addDesignation()
{
String title=titleTextField.getText().trim();
if(title.length()==0)
{
JOptionPane.showMessageDialog(this,"enter a designation to add");
titleTextField.requestFocus();
return false;
}
DesignationInterface d=new Designation();
d.setTitle(title);
try
{
designationModel.add(d);
int rowIndex=0;
try
{
rowIndex=designationModel.indexOfDesignation(d);
}catch(BLException blException)
{
//do nothing
}
designationTable.setRowSelectionInterval(rowIndex,rowIndex);
Rectangle rectangle=designationTable.getCellRect(rowIndex,0,true);
designationTable.scrollRectToVisible(rectangle);
}catch(BLException blexception)
{
if(blexception.hasGenericException())
{
JOptionPane.showMessageDialog(this,blexception.getGenericException());
titleTextField.requestFocus();
return false;
}else
{
if(blexception.hasException("title"))
{
JOptionPane.showMessageDialog(this,blexception.getGenericException());
titleTextField.requestFocus();
return false;
}
}
}
return true;
}

private boolean updateDesignation()
{
String title=titleTextField.getText().trim();
if(title.length()==0)
{
JOptionPane.showMessageDialog(this,"enter a designation to update");
titleTextField.requestFocus();
return false;
}
DesignationInterface d=new Designation();
d.setTitle(title);
d.setCode(this.designation.getCode());
try
{
designationModel.update(d);
int rowIndex=0;
try
{
rowIndex=designationModel.indexOfDesignation(d);
}catch(BLException blException)
{
//do nothing
}
designationTable.setRowSelectionInterval(rowIndex,rowIndex);
Rectangle rectangle=designationTable.getCellRect(rowIndex,0,true);
designationTable.scrollRectToVisible(rectangle);
}catch(BLException blexception)
{
if(blexception.hasGenericException())
{
JOptionPane.showMessageDialog(this,blexception.getGenericException());
titleTextField.requestFocus();
return false;
}else
{
if(blexception.hasException("title"))
{
JOptionPane.showMessageDialog(this,blexception.getException("title"));
titleTextField.requestFocus();
return false;
}
}
}
return true;
}
private void removeDesignation()
{
try
{
String title=this.designation.getTitle();
int selectedOption=JOptionPane.showConfirmDialog(this,"Delete "+title+" ?","Confirmation",JOptionPane.YES_NO_OPTION);
if(selectedOption==JOptionPane.NO_OPTION) return;
designationModel.remove(this.designation.getCode());
JOptionPane.showMessageDialog(this,title+" deleted");
}catch(BLException blexception)
{
if(blexception.hasGenericException())
{
JOptionPane.showMessageDialog(this,blexception.getGenericException());
}else
{
if(blexception.hasException("code"))
{
JOptionPane.showMessageDialog(this,blexception.getException("code"));
}
}
}
}
private void switchToViewMode()
{
setViewMode();
}
private void addListeners()
{
exportToPDFButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
JFileChooser jfc=new JFileChooser();
jfc.setCurrentDirectory(new File("."));
int selectedOption=jfc.showSaveDialog(DesignationUI.this);
if(selectedOption==JFileChooser.APPROVE_OPTION)
{
try
{
File selectedFile=jfc.getSelectedFile();
String pdfFile=selectedFile.getAbsolutePath();
if(pdfFile.endsWith("."))pdfFile+="pdf";
else if(pdfFile.endsWith(".pdf")==false)pdfFile+=".pdf";
File file=new File(pdfFile);
File parent=new File(file.getParent());
if(parent.exists()==false || parent.isDirectory()==false)
{
JOptionPane.showMessageDialog(DesignationUI.this,"Incorrect path : "+file.getAbsolutePath());
return;
}
designationModel.exportToPDF(file);
JOptionPane.showMessageDialog(DesignationUI.this,"Data exported to "+file.getAbsolutePath());
}catch(BLException blException)
{
if(blException.hasGenericException())
{
JOptionPane.showMessageDialog(DesignationUI.this,blException.getGenericException());
}
}
catch(Exception e)
{

}
}
}
});
addButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
if(mode==MODE.VIEW)
{
setAddMode();
}else
{
if(addDesignation())
{
switchToViewMode();
}
}
}
});
editButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
if(mode==MODE.VIEW)
{
setEditMode();
}else
{
if(updateDesignation())
{
switchToViewMode();
}
}
}
});
cancelButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
setViewMode();
}
});

deleteButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
setDeleteMode();
}
});

}

void setViewMode()
{
DesignationUI.this.setViewMode();
addButton.setIcon(addIcon);
editButton.setIcon(editIcon);
addButton.setEnabled(true);
cancelButton.setEnabled(false);
titleTextField.setVisible(false);
titleLabel.setEnabled(true);
clearTitleTextFieldButton.setVisible(false);
if(designationTable.getRowCount()==0)
{
deleteButton.setEnabled(false);
editButton.setEnabled(false);
exportToPDFButton.setEnabled(false);
}else
{
deleteButton.setEnabled(true);
editButton.setEnabled(true);
exportToPDFButton.setEnabled(true);
}
}
void setAddMode()
{
DesignationUI.this.setAddMode();
titleTextField.setText("");
titleTextField.setVisible(true);
titleLabel.setVisible(false);
clearTitleTextFieldButton.setVisible(true);
editButton.setEnabled(false);
cancelButton.setEnabled(true);
deleteButton.setEnabled(false);
exportToPDFButton.setEnabled(false);
addButton.setIcon(saveIcon);
}
void setEditMode()
{
if(designation==null)
{
JOptionPane.showMessageDialog(this,"Select designation to edit");
return;
}
DesignationUI.this.setEditMode();
titleTextField.setText(designation.getTitle());
titleLabel.setVisible(false);
titleTextField.setVisible(true);
clearTitleTextFieldButton.setVisible(true);
addButton.setEnabled(false);
cancelButton.setEnabled(true);
deleteButton.setEnabled(false);
exportToPDFButton.setEnabled(false);
editButton.setIcon(saveIcon);
}
void setDeleteMode()
{
if(designation==null)
{
JOptionPane.showMessageDialog(this,"Select designation to delete");
return;
}
DesignationUI.this.setDeleteMode();
titleTextField.setText(designation.getTitle());
titleLabel.setVisible(false);
titleTextField.setVisible(true);
clearTitleTextFieldButton.setVisible(true);
deleteButton.setEnabled(false);
addButton.setEnabled(false);
cancelButton.setEnabled(false);
editButton.setEnabled(false);
exportToPDFButton.setEnabled(false);
removeDesignation();
setViewMode();
}
void exportToPDFMode()
{
DesignationUI.this.setExportToPDFMode();
deleteButton.setEnabled(false);
addButton.setEnabled(false);
cancelButton.setEnabled(false);
editButton.setEnabled(false);
exportToPDFButton.setEnabled(false);
}
}
}