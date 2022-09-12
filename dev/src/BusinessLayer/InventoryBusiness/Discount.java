package BusinessLayer.InventoryBusiness;

import DataAccessLayer.InventoryDAL.DALFacade;

import java.time.LocalDate;

public class Discount {
    private String name;
    private LocalDate fromDate;
    private LocalDate toDate;
    private double discountFare;

    public Discount (String name, LocalDate fromDate, LocalDate toDate, double discountFare){
        if(fromDate == null | toDate==null)
            throw new NullPointerException("Dates can't be null");
        if(fromDate.compareTo(LocalDate.now())<0)
            throw new IllegalArgumentException("Start date can't be before today");
        if(toDate.compareTo(fromDate)<0)
            throw new IllegalArgumentException("End date can't be before Start date");
        if(discountFare <0 | discountFare>1)
            throw new IllegalArgumentException("Discount fare is not between 0 and 1");
        this.name = name;
        this.fromDate=fromDate;
        this.toDate=toDate;
        this.discountFare=discountFare;
    }

    public Discount (String name, String fromDate, String endDate, double discountFare){
        this.name = name;
        this.fromDate = LocalDate.parse(fromDate);
        this.toDate = LocalDate.parse(endDate);
        this.discountFare = discountFare;
    }

    public String getName(){return name;}
    public void setName(String branchAddress, String newName, int itemID){
        if(newName == null)
            throw new NullPointerException("name cant be null");
        name = newName;
        DALFacade.getInstance().setDiscountName(branchAddress, itemID,newName, fromDate.toString());
    }
    public LocalDate getFromDate() {
        return fromDate;
    }
    public LocalDate getToDate() {
        return toDate;
    }
    public double getDiscountFare() {
        return discountFare;
    }
    public int compareStartDate(Discount d2) {
        if (getFromDate().compareTo(d2.getFromDate())<0)
            return -1;
        return 1;
    }
}
