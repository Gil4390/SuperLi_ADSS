package ServiceLayer.Objects.InventoryObjects;

import java.time.LocalDate;

public class FDiscount {
    private final String name;
    private final LocalDate fromDate;
    private final LocalDate toDate;
    private final double discountFare;

//    public FDiscount(Discount dis){
//        name = dis.getName();
//        fromDate = dis.getFromDate();
//        toDate = dis.getToDate();
//        discountFare = dis.getDiscountFare();
//    }

    public FDiscount(String disName, LocalDate fromDate, LocalDate toDate, double discountFare){
        name = disName;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.discountFare = discountFare;
    }

    public String getName() {
        return name;
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
}
