package com.ajay.sqlite.model;

public class StudentData {
    private String rowId;
    private String fName;
    private String lName;
    private String image;
    private String cNumber;
    private String eMail;
    private String address;

    public String getRowId() {
        return rowId;
    }
    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public String getfName() {
        return fName;
    }
    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }
    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getcNumber() {
        return cNumber;
    }
    public void setcNumber(String cNumber) {
        this.cNumber = cNumber;
    }

    public String geteMail() {
        return eMail;
    }
    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }


}
