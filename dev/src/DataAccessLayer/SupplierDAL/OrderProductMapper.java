package DataAccessLayer.SupplierDAL;

import BusinessLayer.SupplierBusiness.OrderProduct;

import java.sql.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class OrderProductMapper extends DalController {
    private static class DOrderProductControllerHolder{
        private static OrderProductMapper instance = new OrderProductMapper();
    }

    public static OrderProductMapper getInstance() {
        return DOrderProductControllerHolder.instance;
    }

    private final String OrderIdColumnName="OrderId";
    private final String ProductIdColumnName="ProductId";
    private final String NameColumnName="Name";
    private final String PriceAfterDiscountColumnName="PriceAfterDiscount";
    private final String AmountColumnName="Amount";
    private final String DiscountColumnName="Discount";
    private final String SingleItemPriceColumnName="SingleItemPrice";
    private final String UnSuppliedAmountColumnName="UnSuppliedAmount";

    private Map<Integer, Map<Integer, OrderProduct>> orderProducts;

    private OrderProductMapper() {
        super("OrderProducts");
        orderProducts = new HashMap<>();
    }
    public List<OrderProduct> selectAllOrderProducts(){
        return (List<OrderProduct>)(List<?>)select();
    }
    public List<OrderProduct> selectOrderProductsOfOrder(int orderId){
        return (List<OrderProduct>)(List<?>)selectList(orderId, OrderIdColumnName);
    }
    public OrderProduct selectOrderProduct(int orderId, int productId){
        return (OrderProduct)select(orderId, productId, OrderIdColumnName, ProductIdColumnName);
    }

    // insert new orderProduct to database
    public boolean insert(int orderId, int productId, String name, double priceAfterDiscount, int amount, double discount, double singleItemPrice) {
        checkOrderProductNotExists(orderId, productId);
        OrderProduct orderProduct = new OrderProduct(orderId, productId, name, priceAfterDiscount, amount, discount, singleItemPrice, 0);
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}, {4}, {5}, {6}, {7}, {8}) VALUES(?,?,?,?,?,?,?,?)",
                getTableName(), OrderIdColumnName, ProductIdColumnName,
                NameColumnName, PriceAfterDiscountColumnName, AmountColumnName,
                DiscountColumnName, SingleItemPriceColumnName, UnSuppliedAmountColumnName);
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderProduct.getOrderId());
            pstmt.setInt(2, orderProduct.getProductId());
            pstmt.setString(3, orderProduct.getName());
            pstmt.setDouble(4, orderProduct.getPriceAfterDiscount());
            pstmt.setInt(5, orderProduct.getAmount());
            pstmt.setDouble(6, orderProduct.getDiscount());
            pstmt.setDouble(7, orderProduct.getSingleItemPrice());
            pstmt.setInt(8, 0);
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        orderProducts.putIfAbsent(orderId, new ConcurrentHashMap<>());
        orderProducts.get(orderId).put(productId, orderProduct);
        return true;
    }

    public void delete(int orderId, int productId) {
        checkOrderProductExists(orderId, productId);
        delete(orderId, productId, OrderIdColumnName, ProductIdColumnName);
        orderProducts.get(orderId).remove(productId);
    }

    public void delete(int orderId) {
        delete(orderId, OrderIdColumnName);
        orderProducts.remove(orderId);
    }

    public Map<Integer, OrderProduct> getOrderProducts(int orderId) {
        checkOrderExists(orderId);
        return orderProducts.get(orderId);
    }

    public void updateAmount(int orderId, int productId, int amount) {
        update(orderId, productId, AmountColumnName, amount, OrderIdColumnName, ProductIdColumnName);
        orderProducts.get(orderId).get(productId).setAmount(amount);
    }

    public void updatePrice(int orderId, int productId, double price) {
        update(orderId, productId, PriceAfterDiscountColumnName, price, OrderIdColumnName, ProductIdColumnName);
        orderProducts.get(orderId).get(productId).setPriceAfterDiscount(price);
    }

    public void updateUnSuppliedAmount(int orderId, int productId, int unSuppliedAmount) {
        update(orderId, productId, UnSuppliedAmountColumnName, unSuppliedAmount, OrderIdColumnName, ProductIdColumnName);
        orderProducts.get(orderId).get(productId).setUnSuppliedAmount(unSuppliedAmount);
    }

    private boolean checkOrderProductExistsInCache(int orderId, int productId) {
        return orderProducts.containsKey(orderId) && orderProducts.get(orderId).containsKey(productId);
    }

    private void checkOrderProductExists(int orderId, int productId) {
        if(!checkOrderProductExistsInCache(orderId, productId)) {
            OrderProduct orderProduct = selectOrderProduct(orderId, productId);
            if(orderProduct == null)
                throw new IllegalArgumentException("Order product is not exists!");
            orderProducts.putIfAbsent(orderProduct.getOrderId(), new HashMap<>());
            orderProducts.get(orderProduct.getOrderId()).put(orderProduct.getProductId(), orderProduct);
        }
    }

    public void checkOrderProductNotExists(int orderId, int productId) {
        if(checkOrderProductExistsInCache(orderId, productId))
            throw new IllegalArgumentException("Order product already exists!");
        OrderProduct orderProduct = selectOrderProduct(orderId, productId);
        if(orderProduct!=null)
            throw new IllegalArgumentException("Order product already exists!");
    }

    private void checkOrderExists(int orderId) {
        List<OrderProduct> orderProductsList = selectOrderProductsOfOrder(orderId);
        if(orderProductsList.size()==0)
            throw new IllegalArgumentException("Order is not exists!");
        orderProducts.putIfAbsent(orderId, new HashMap<>());
        for(OrderProduct orderProduct : orderProductsList)
            orderProducts.get(orderId).putIfAbsent(orderProduct.getProductId(), orderProduct);
    }

    public void checkOrderProductsNotExists(int orderId, Set<Integer> productsID) {
        for(int productId : productsID)
            checkOrderProductNotExists(orderId, productId);
    }

    public void checkOrderProductsExists(int orderId, Set<Integer> productsId) {
        for(int productId: productsId)
            checkOrderProductExists(orderId, productId);
    }

    @Override
    protected Object ConvertReaderToObject(ResultSet reader) {
        OrderProduct result = null;
        try {
            result = new OrderProduct(reader.getInt(1), reader.getInt(2), reader.getString(3), reader.getDouble(4), reader.getInt(5), reader.getDouble(6), reader.getDouble(7), reader.getInt(8));
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    @Override
    public void cleanCache() {
        Iterator<Map.Entry<Integer, Map<Integer, OrderProduct>>> iter = orderProducts.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer, Map<Integer, OrderProduct>> entry = iter.next();
            Iterator<Map.Entry<Integer, OrderProduct>> iterOrderProducts = entry.getValue().entrySet().iterator();
            while (iterOrderProducts.hasNext()) {
                Map.Entry<Integer,OrderProduct> entryOrderProduct = iterOrderProducts.next();
                if(entryOrderProduct.getValue().shouldCleanCache()){
                    System.out.println("Cleaning order product "+entryOrderProduct.getValue().getProductId() +" from cache!");
                    iterOrderProducts.remove();
                }
            }
        }
    }
    @Override
    protected void cleanCacheForTests() {
        orderProducts = new HashMap<>();
    }
}
