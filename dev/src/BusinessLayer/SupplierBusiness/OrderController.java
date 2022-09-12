package BusinessLayer.SupplierBusiness;

import BusinessLayer.Generators.EmailGenerator;
import BusinessLayer.Generators.PDFGenerator;
import DataAccessLayer.SupplierDAL.DemandOrderMapper;
import DataAccessLayer.SupplierDAL.FixedOrderMapper;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.sql.Date;
import java.time.DayOfWeek;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OrderController {
    private int nextOrderId;
    private final DemandOrderMapper demandOrderMapper;
    private final FixedOrderMapper fixedOrderMapper;

    protected OrderController() {
        this.demandOrderMapper = DemandOrderMapper.getInstance();
        this.fixedOrderMapper = FixedOrderMapper.getInstance();
        int demandLastOrderId = demandOrderMapper.getLastOrderId();
        int fixedLastOrderId = fixedOrderMapper.getLastOrderId();
        nextOrderId = demandLastOrderId>fixedLastOrderId ? demandLastOrderId + 1 : fixedLastOrderId + 1;
    }

    protected DemandOrder addDemandOrder(String branchAddress, int supplierBN, Date orderDate, Date supplyDate, HashMap<Integer, Integer> itemIdAndAmount, BillOfQuantity billOfQuantity) {
        if(supplyDate!=null && supplyDate.before(new Date(System.currentTimeMillis())))
            throw new IllegalArgumentException("Invalid date!");
        return demandOrderMapper.insert(branchAddress, nextOrderId++, supplierBN, orderDate, supplyDate, itemIdAndAmount, billOfQuantity);
    }

    protected void addFixedOrder(String branchAddress, int supplierBN, Date orderDate, Set<DayOfWeek> supplyDays, HashMap<Integer, Integer> itemIdAndAmount, BillOfQuantity billOfQuantity) {
        fixedOrderMapper.insert(branchAddress, nextOrderId++, supplierBN, orderDate, supplyDays, itemIdAndAmount, billOfQuantity);
    }

    protected void removeUnSuppliedDemandOrder(int supplierBN, int orderId) {
        demandOrderMapper.removeUnSuppliedDemandOrder(supplierBN, orderId);
    }

    protected List<DemandOrder> getAllSupplierDemandOrders(int supplierBN) {
        return demandOrderMapper.getAllSupplierDemandOrders(supplierBN);
    }

    protected List<FixedOrder> getAllSupplierFixedOrders(int supplierBN) {
        return fixedOrderMapper.getAllSupplierFixedOrders(supplierBN);
    }

    protected List<Order> getAllSupplierOrders(int supplierBN) {
        return Stream.concat(getAllSupplierDemandOrders(supplierBN).stream(), getAllSupplierFixedOrders(supplierBN).stream()).collect(Collectors.toList());
    }

    protected List<Pair<Integer, OrderProduct>> supplyDemandOrder(int supplierBN, int orderId) {
        DemandOrder demandOrder = demandOrderMapper.getDemandOrder(supplierBN, orderId);

        if(demandOrder.isSupplied())
            return new ArrayList<>();

        demandOrderMapper.setSupply(supplierBN, orderId, new Date(Calendar.getInstance().getTime().getTime()));
        List<Pair<Integer, OrderProduct>> orderProducts = new ArrayList<>();


        for(OrderProduct orderProduct : demandOrder.getOrderProducts().values())
            orderProducts.add(new Pair<>(supplierBN, orderProduct));
        return orderProducts;
    }

    protected void unActiveFixedOrder(int supplierBN, int orderId, Date date) {
        fixedOrderMapper.setUnActive(supplierBN, orderId, date);
    }

    protected List<FixedOrder> getAllFixedOrders() {
        return fixedOrderMapper.getAllFixedOrders();
    }

    protected List<DemandOrder> getAllDemandOrders() {
        return demandOrderMapper.getAllDemandOrders();
    }

    public void sendPDFDOrder(Supplier supplier, int orderId, String email) throws IOException {
        Order order = demandOrderMapper.getDemandOrder(supplier.getBusinessNumber(), orderId);
        sendPDFOrder(supplier, order, email);
    }

    private void sendPDFOrder(Supplier supplier, Order order, String email) throws IOException {
        PDFGenerator.getInstance().createOrderPDF(supplier, order);
        EmailGenerator.getInstance().sendOrderEmail(email, (new File("").getAbsolutePath()).concat("\\docs\\orders\\"+order.getOrderId()+".pdf"));
    }

    protected void addProductsToFixedOrder(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndAmount, BillOfQuantity billOfQuantity) {
        fixedOrderMapper.addProductsToOrder(supplierBN, orderId, itemIdAndAmount, billOfQuantity);
    }

    protected void removeProductsFromFixedOrder(int supplierBN, int orderId, List<Integer> productsId) {
        fixedOrderMapper.removeProducts(supplierBN, orderId, productsId);
    }

    protected void updateOrderProductsOfFixedOrder(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndAmount, BillOfQuantity billOfQuantity) {
        fixedOrderMapper.updateOrderProducts(supplierBN, orderId, itemIdAndAmount, billOfQuantity);
    }

    protected void addProductsToDemandOrder(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndAmount, BillOfQuantity billOfQuantity) {
        demandOrderMapper.addProductsToOrder(supplierBN, orderId, itemIdAndAmount, billOfQuantity);
    }

    protected void removeProductsFromDemandOrder(int supplierBN, int orderId, List<Integer> productsId) {
        demandOrderMapper.removeProducts(supplierBN, orderId, productsId);
    }

    protected void updateOrderProductsOfDemandOrder(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndAmount, BillOfQuantity billOfQuantity) {
        demandOrderMapper.updateOrderProducts(supplierBN, orderId, itemIdAndAmount, billOfQuantity);
    }

    protected void updateUnSuppliedAmount(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndUnSuppliedAmount) {
        demandOrderMapper.updateUnSuppliedAmount(supplierBN, orderId, itemIdAndUnSuppliedAmount);
    }

    public List<FixedOrder> dailyFixedOrders() {
        List<FixedOrder> fixedOrderList = fixedOrderMapper.getAllFixedOrders();
        List<FixedOrder> dailyFixedOrders = new ArrayList<>();
        for(FixedOrder fixedOrder : fixedOrderList)
            if(fixedOrder.isDaily())
                dailyFixedOrders.add(fixedOrder);
        return dailyFixedOrders;
    }

    public void addSupplyDayToFixedOrder(int supplierBN, int orderId, DayOfWeek day) {
        fixedOrderMapper.addSupplyDay(supplierBN, orderId, day);
    }

    public void removeSupplyDayToFixedOrder(int supplierBN, int orderId, DayOfWeek day) {
        fixedOrderMapper.removeSupplyDay(supplierBN, orderId, day);
    }

    public DemandOrder createNextWeekFixedOrder(String branchAddress, FixedOrder fixedOrder, BillOfQuantity billOfQuantity, boolean shouldDeliver) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        Map<Integer, OrderProduct> orderProducts = fixedOrder.getOrderProducts();
            HashMap<Integer, Integer> itemIdAndAmount = new HashMap<>();
            for(OrderProduct orderProduct : orderProducts.values())
                itemIdAndAmount.put(orderProduct.getProductId(), orderProduct.getAmount());
            if(shouldDeliver)
                return demandOrderMapper.insert(branchAddress, nextOrderId++, fixedOrder.getSupplierBN(), new Date(Calendar.getInstance().getTime().getTime()), null, itemIdAndAmount, billOfQuantity);
            else
                return demandOrderMapper.insert(branchAddress, nextOrderId++, fixedOrder.getSupplierBN(), new Date(Calendar.getInstance().getTime().getTime()), new Date(calendar.getTime().getTime()), itemIdAndAmount, billOfQuantity);
    }

    public List<OrderProduct> getOrderProductsOfUnSuppliedDemandOrders() {
        List<DemandOrder> demandOrdersList = demandOrderMapper.getAllDemandOrders();
        List<OrderProduct> orderProductList = new ArrayList<>();
        for(DemandOrder demandOrder : demandOrdersList)
            if(!demandOrder.isSupplied())
                orderProductList.addAll(demandOrder.getOrderProducts().values());
        return orderProductList;
    }

    public void setSupplyDateForDemandOrder(int supplierBN, int orderId, Date supplyDate) {
        demandOrderMapper.setSupplyDate(supplierBN, orderId, supplyDate);
    }

    public DemandOrder getDemandOrder(int supplierBN, int orderId) {
        return demandOrderMapper.getDemandOrder(supplierBN, orderId);
    }

}
