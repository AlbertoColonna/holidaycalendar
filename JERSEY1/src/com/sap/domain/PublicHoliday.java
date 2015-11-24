package com.sap.domain;

public class PublicHoliday {
	
	//attributes in json post have to have same name as in getter/setter methods
	
    private String id;
    private String datex;
    
    public PublicHoliday()
    {

    }

    public PublicHoliday(String date, String id) { 
        this.datex = date; 
        this.id    = id; 
    } 
    
    

    public String getId() {
        return id;
    }

    public void setId(String newId) {
        this.id = newId;
    }

    public String getDate() {
        return this.datex;
    }

    public void setDate(String newDate) {
        this.datex = newDate;
    } 
    

    @Override 
    public String toString() { 
        return new StringBuffer(" Date : ").append(this.datex) 
                .append(this.id).toString(); 
    } 
    
    

}
