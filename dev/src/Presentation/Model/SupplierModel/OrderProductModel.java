package Presentation.Model.SupplierModel;

import BusinessLayer.SupplierBusiness.OrderProduct;

public class OrderProductModel {
    private final int orderId;
    private final int productId;
    private final String name;
    private final double priceAfterDiscount;
    private final int amount;
    private final double discount;
    private final double singleItemPrice;

    public OrderProductModel(OrderProduct orderProduct) {
        this.orderId = orderProduct.getOrderId();
        this.productId = orderProduct.getProductId();
        this.name = orderProduct.getName();
        this.priceAfterDiscount = orderProduct.getPriceAfterDiscount();
        this.amount = orderProduct.getAmount();
        this.discount = orderProduct.getDiscount();
        this.singleItemPrice = orderProduct.getSingleItemPrice();
    }

    public String toString() {
        return "-Order id: "+orderId +", product id: "+productId+", name: "+name+", price after discount: "+priceAfterDiscount+", amount: "+amount+", discount: "+discount+", single item price: "+singleItemPrice;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getName() {
        return name;
    }

    public double getPriceAfterDiscount() {
        return priceAfterDiscount;
    }

    public double getDiscount() {
        return discount;
    }

    public double getSingleItemPrice() {
        return singleItemPrice;
    }

    public int getAmount() {
        return amount;
    }
}
