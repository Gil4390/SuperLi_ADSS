package BusinessLayer.InventoryBusiness;

import DataAccessLayer.InventoryDAL.DALFacade;

import java.time.LocalDate;
import java.util.*;
import java.lang.*;

public class ItemPrice {
    private double currentPrice;
    private LinkedList<Discount> futureDiscounts;
    private LinkedList<Discount> pastDiscounts;

    private String branchAddress;

    public ItemPrice(String branchAddress, int itemID, double currentPrice){
        this.branchAddress=branchAddress;
        if(currentPrice <= 0)
            throw new IllegalArgumentException("Price can't be non-positive");
        this.currentPrice=currentPrice;
        List<Discount> discounts = DALFacade.getInstance().getAllItemsDiscounts(branchAddress, itemID);
        futureDiscounts = new LinkedList<>();
        pastDiscounts = new LinkedList<>();
        for(Discount discount : discounts){
            if(discount.getFromDate().isBefore(LocalDate.now()))
                pastDiscounts.add(discount);
            else
                futureDiscounts.add(discount);
        }
        pastDiscounts.sort(Discount::compareStartDate); //sort by start date
        futureDiscounts.sort(Discount::compareStartDate); //sort by start date
    }

    public LinkedList<Discount> getFutureDiscounts() {
        return futureDiscounts;
    }
    public LinkedList<Discount> getPastDiscounts() {
        return pastDiscounts;
    }
    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice, int itemID) {
        if(currentPrice <= 0)
            throw new IllegalArgumentException("Price can't be non-positive");
        this.currentPrice = currentPrice;
        DALFacade.getInstance().setCurrentPrice(branchAddress, currentPrice,itemID);
    }

    public void addDiscount(int itemID, Discount discount){
        if(discount == null)
            throw new NullPointerException("Expected Discount, received Null");
        LocalDate now = LocalDate.now();
        if(discount.getToDate().compareTo(now)<0)
            throw new IllegalArgumentException("The discount you wanted to add is of the past");
        validDate(discount); //check if not overlapping with other discounts
        DALFacade.getInstance().addDiscount(branchAddress, itemID, discount.getName(), discount.getFromDate().toString(), discount.getToDate().toString(), discount.getDiscountFare());
        futureDiscounts.add(discount);
        futureDiscounts.sort(Discount::compareStartDate); //sort by start date
    }

    public double getCalculatedPrice(){
        Discount firstDiscount = null;
        if(!futureDiscounts.isEmpty())
            firstDiscount = futureDiscounts.getFirst();
        LocalDate now = LocalDate.now();
        if(firstDiscount != null && firstDiscount.getToDate().compareTo(now)<=0)
            maintainDiscountsList(now);
        if(!futureDiscounts.isEmpty())
            firstDiscount = futureDiscounts.getFirst();
        if (firstDiscount != null && firstDiscount.getFromDate().compareTo(now)<=0 && firstDiscount.getToDate().compareTo(now)>=0) {
            Double num = getCurrentPrice() * (1 - firstDiscount.getDiscountFare());
            return (double)(Math.round((num) * 100)) / 100;
        }
        return getCurrentPrice();
    }

    public void removeDiscount(LocalDate fromDate,int itemID){
        if(fromDate == null)
            throw new NullPointerException("Expected Date, received Null");
        boolean dateErased = false;
        for(Discount discount: futureDiscounts) {
            if (discount.getFromDate().equals(fromDate)){
                futureDiscounts.remove(discount);
                DALFacade.getInstance().removeDiscount(branchAddress, itemID,fromDate);
                dateErased = true;
                break;
            }
        }
        for(Discount discount: pastDiscounts) {
            if (discount.getFromDate().equals(fromDate)){
                pastDiscounts.remove(discount);
                dateErased = true;
                break;
            }
        }
        if(!dateErased)
            throw new IllegalArgumentException("There isn't such a Discount");
    }

    private void validDate(Discount discount){
        for(Discount discount2 : futureDiscounts){ //check if not overlapping with other discounts
            if(discount.getFromDate().compareTo(discount2.getFromDate())<0) {
                if (discount.getToDate().compareTo(discount2.getFromDate())>0)
                    throw new IllegalArgumentException("Your discount+ is overlapping another discount in this date range. Other discount name: " + discount2.getName());
            }
            else if(discount.getFromDate().compareTo(discount2.getFromDate())>0) {
                if (discount.getFromDate().compareTo(discount2.getToDate())<0)
                    throw new IllegalArgumentException("Your discount is overlapping another discount in this date range. Other discount name: " + discount2.getName());
            }
            else if(discount.getFromDate().equals(discount2.getFromDate()))
                throw new IllegalArgumentException("Your discount is overlapping another discount in this date range. Other discount name: " + discount2.getName());
        }
    }

    private void maintainDiscountsList (LocalDate now){
        for(Discount discount : futureDiscounts){
            if(discount.getToDate().compareTo(now)<0){
                pastDiscounts.add(discount);
                futureDiscounts.remove(discount);
            }
        }
    }
}
