package BusinessLayer.SupplierBusiness;

import DataAccessLayer.SupplierDAL.OrderProductMapper;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Order {
    private final String branchAddress;
    private final int orderId;
    private final int supplierBN;
    private double price;
    private final Date orderDate;
    private Instant timeStamp;
    private final OrderProductMapper orderProductMapper;

    protected Order(String branchAddress, int orderId, int supplierBN, Date orderDate, HashMap<Integer, Integer> itemIdAndAmount, BillOfQuantity billOfQuantity) {
        this.branchAddress = branchAddress;
        this.orderId = orderId;
        this.supplierBN = supplierBN;
        this.orderDate = orderDate;
        this.orderProductMapper = OrderProductMapper.getInstance();
        makeOrderItems(itemIdAndAmount, billOfQuantity);
        this.timeStamp = Instant.now();
    }

    protected Order(String branchAddress, int orderId, int supplierBN, double price, Date orderDate) {
        this.branchAddress = branchAddress;
        this.orderId = orderId;
        this.supplierBN = supplierBN;
        this.price = price;
        this.orderDate = orderDate;
        this.orderProductMapper = OrderProductMapper.getInstance();
        this.timeStamp = Instant.now();
    }

    public void calculatePrice() {
        this.price = 0;
        for(OrderProduct orderProduct : orderProductMapper.getOrderProducts(orderId).values())
            this.price += orderProduct.getPriceAfterDiscount();
        updateTimeStamp();
    }

    protected void makeOrderItems(HashMap<Integer, Integer> itemIdAndAmount, BillOfQuantity billOfQuantity) {
        orderProductMapper.checkOrderProductsNotExists(orderId, itemIdAndAmount.keySet());
        for(int productId : itemIdAndAmount.keySet()) {
            String name = billOfQuantity.getItemName(branchAddress, productId);
            int amount = itemIdAndAmount.get(productId);
            double discount = billOfQuantity.getDiscountForOrder(branchAddress, productId, amount);
            double price = calculatePriceAfterDiscount(branchAddress, productId, amount, billOfQuantity);
            orderProductMapper.insert(orderId, productId, name, price, amount, discount, billOfQuantity.getItemPrice(branchAddress, productId));
        }
        calculatePrice();
        updateTimeStamp();
    }

    protected void updateProducts(HashMap<Integer, Integer> itemIdAndAmount, BillOfQuantity billOfQuantity) {
        orderProductMapper.checkOrderProductsExists(orderId, itemIdAndAmount.keySet());
        for(int productId : itemIdAndAmount.keySet()) {
            int amount = itemIdAndAmount.get(productId);
            double price = calculatePriceAfterDiscount(branchAddress, productId, amount, billOfQuantity);
            orderProductMapper.updateAmount(orderId, productId, amount);
            orderProductMapper.updatePrice(orderId, productId, price);
        }
        calculatePrice();
        updateTimeStamp();
    }

    protected void updateUnSuppliedAmount(HashMap<Integer, Integer> itemIdAndUnSuppliedAmount) {
        orderProductMapper.checkOrderProductsExists(orderId, itemIdAndUnSuppliedAmount.keySet());
        for(int productId : itemIdAndUnSuppliedAmount.keySet())
            orderProductMapper.updateUnSuppliedAmount(orderId, productId, itemIdAndUnSuppliedAmount.get(productId));
        //todo: check if need to update order price
    }

    public double calculatePriceAfterDiscount(String branchAddress, int productId, int amount, BillOfQuantity billOfQuantity) {
        double discount = billOfQuantity.getDiscountForOrder(branchAddress, productId, amount);
        double totalPrice = billOfQuantity.getItemPrice(branchAddress, productId)*amount;
        discount = totalPrice*discount/100.0;
        return totalPrice - discount;
    }


    public int getOrderId() {
        updateTimeStamp();
        return orderId;
    }

    public int getSupplierBN() {
        updateTimeStamp();
        return supplierBN;
    }

    public double getPrice() {
        updateTimeStamp();
        return price;
    }

    public Date getOrderDate() {
        updateTimeStamp();
        return orderDate;
    }

    public Map<Integer, OrderProduct> getOrderProducts() {
        updateTimeStamp();
        return orderProductMapper.getOrderProducts(orderId);
    }

    public boolean shouldCleanCache(){
        Duration duration = Duration.between( timeStamp , Instant.now());
        Duration limit = Duration.ofMinutes(5);
        return (duration.compareTo( limit ) > 0);
    }

    protected void updateTimeStamp() { timeStamp = Instant.now();}

    public void deleteOrderProducts() {
        orderProductMapper.delete(orderId);
    }

    public void deleteOrderProducts(List<Integer> orderProducts) {
        for(int productId : orderProducts)
            orderProductMapper.delete(orderId, productId);
        calculatePrice();
        updateTimeStamp();
    }

    public String getBranchAddress() {
        return branchAddress;
    }
}
