package Presentation.Model.SupplierModel;

import BusinessLayer.SupplierBusiness.FixedOrder;

import java.util.Date;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FixedOrderModel {
    private final int orderId;
    private final int supplierBN;
    private final double price;
    private final Date orderDate;
    private final Set<DayOfWeek> daysOfSupply;
    private final boolean isActive;
    private final Date canceledDate;
    Map<Integer, OrderProductModel> orderProducts;

    public FixedOrderModel(FixedOrder fixedOrder) {
        this.orderId = fixedOrder.getOrderId();
        this.supplierBN = fixedOrder.getSupplierBN();
        this.price = fixedOrder.getPrice();
        this.orderDate = fixedOrder.getOrderDate();
        this.isActive = fixedOrder.isActive();
        this.canceledDate = fixedOrder.getCanceledDate();
        daysOfSupply = fixedOrder.getDaysOfSupply();
        orderProducts = new HashMap<>();
        makeOrderProductModel(fixedOrder);
    }

    public String toString() {
        String output =  "-Order id: "+orderId+", supplierBN: "+supplierBN+", price: "+price+", order date: "+orderDate+", days of supply: "+daysOfSupply+", is active: "+isActive+"" +(canceledDate==null ? "" : ", canceled date: "+canceledDate)+"\nOrder products:\n";
        for(OrderProductModel orderProductModel : orderProducts.values())
            output += "\t"+orderProductModel.toString()+"\n";
        return output;
    }

    private void makeOrderProductModel(FixedOrder fixedOrder) {
        for(int orderProductId : fixedOrder.getOrderProducts().keySet()) {
            orderProducts.put(orderProductId, new OrderProductModel(fixedOrder.getOrderProducts().get(orderProductId)));
        }
    }

    public int getOrderId() {
        return orderId;
    }

    public int getSupplierBN() {
        return supplierBN;
    }

    public double getPrice() {
        return price;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public Set<DayOfWeek> getDaysOfSupply() {
        return daysOfSupply;
    }

    public boolean isActive() {
        return isActive;
    }

    public Date getCanceledDate() {
        return canceledDate;
    }

    public Map<Integer, OrderProductModel> getOrderProducts() {
        return orderProducts;
    }
}
