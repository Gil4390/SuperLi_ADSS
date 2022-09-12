package Presentation.Model.SupplierModel;

import BusinessLayer.SupplierBusiness.DemandOrder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DemandOrderModel {
    private final int orderId;
    private final int supplierBN;
    private final double price;
    private final Date orderDate;
    private final Date supplyDate;
    private final boolean isSupplied;
    Map<Integer, OrderProductModel> orderProducts;

    public DemandOrderModel(DemandOrder demandOrder) {
        this.orderId = demandOrder.getOrderId();
        this.supplierBN = demandOrder.getSupplierBN();
        this.price = demandOrder.getPrice();
        this.orderDate = demandOrder.getOrderDate();
        this.supplyDate = demandOrder.getSupplyDate();
        this.isSupplied = demandOrder.isSupplied();
        orderProducts = new HashMap<>();
        makeOrderProductModel(demandOrder);
    }

    public String toString() {
        String output =  "-Order id: "+orderId+", supplierBN: "+supplierBN+", price: "+price+", order date: "+orderDate+", supply date: "+(supplyDate==null ? " " : supplyDate.toString())+", is supplied: "+isSupplied+"\nOrder products:\n";
        for(OrderProductModel orderProductModel : orderProducts.values())
            output += "\t"+orderProductModel.toString()+"\n";
        return output;
    }

    private void makeOrderProductModel(DemandOrder demandOrder) {
        for(int orderProductId : demandOrder.getOrderProducts().keySet())
            orderProducts.put(orderProductId, new OrderProductModel(demandOrder.getOrderProducts().get(orderProductId)));
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

    public Date getSupplyDate() {
        return supplyDate;
    }

    public boolean isSupplied() {
        return isSupplied;
    }

    public Map<Integer, OrderProductModel> getOrderProducts() {
        return orderProducts;
    }
}
