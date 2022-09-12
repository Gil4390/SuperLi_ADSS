package BusinessLayer.SupplierBusiness;

import java.time.Duration;
import java.time.Instant;

public class OrderProduct {
    private final int orderId;
    private final int productId;
    private final String name;
    private double priceAfterDiscount;
    private int amount;
    private final double discount;
    private final double singleItemPrice;
    private int unSuppliedAmount;
    private Instant timeStamp;

    public OrderProduct(int orderId, int productId, String name, double priceAfterDiscount, int amount, double discount, double singleItemPrice, int unSuppliedAmount) {
        this.orderId = orderId;
        this.productId = productId;
        this.name = name;
        this.priceAfterDiscount = priceAfterDiscount;
        this.amount = amount;
        this.discount = discount;
        this.singleItemPrice = singleItemPrice;
        this.unSuppliedAmount = unSuppliedAmount;
        this.timeStamp = Instant.now();
    }

    public int getOrderId() {
        updateTimeStamp();
        return orderId;
    }

    public String getName() {
        updateTimeStamp();
        return name;
    }

    public double getPriceAfterDiscount() {
        updateTimeStamp();
        return priceAfterDiscount;
    }

    public int getAmount() {
        updateTimeStamp();
        return amount;
    }

    public double getDiscount() {
        updateTimeStamp();
        return discount;
    }

    public double getSingleItemPrice() {
        updateTimeStamp();
        return singleItemPrice;
    }

    public int getProductId() {
        updateTimeStamp();
        return productId;
    }

    public void setPriceAfterDiscount(double priceAfterDiscount) {
        this.priceAfterDiscount = priceAfterDiscount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getUnSuppliedAmount() {
        return unSuppliedAmount;
    }

    public void setUnSuppliedAmount(int unSuppliedAmount) {
        this.unSuppliedAmount = unSuppliedAmount;
    }

    private void updateTimeStamp() { timeStamp = Instant.now();}

    public boolean shouldCleanCache() {
        Duration duration = Duration.between( timeStamp , Instant.now());
        Duration limit = Duration.ofMinutes(5);
        return (duration.compareTo( limit ) > 0);
    }
}
