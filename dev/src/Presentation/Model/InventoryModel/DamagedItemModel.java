package Presentation.Model.InventoryModel;

import Presentation.Model.BackendController;
import ServiceLayer.Objects.InventoryObjects.FDamagedItem;

import java.time.LocalDate;
import java.util.List;

public class DamagedItemModel extends BasicItemModel{
    private final LocalDate dateFound;
    private final int quantityFound;
    private final double priceInStore;
    private final PDamageReason ExpiredOrFault;
    private final boolean isFrontRoom;

    public DamagedItemModel(FDamagedItem item) {
        super(item.getItemID(), item.getName(), item.getCategoryName(), item.getManufacture());
        dateFound = item.getDateFound();
        quantityFound = item.getQuantityFound();
        priceInStore = item.getPriceInStore();
        if(item.getExpiredOrFault().toString().equals("Expired"))
            this.ExpiredOrFault = PDamageReason.Expired;
        else
            this.ExpiredOrFault = PDamageReason.Damaged;
        isFrontRoom = item.getIsFrontRoom();
    }

//    public DamagedItemModel(int itemID, String itemName, String categoryName, String manufacture, LocalDate dateFound,
//                            int quantityFound, double priceInStore, PDamageReason ExpiredOrFault, boolean isFrontRoom) {
//        super(itemID , itemName, categoryName, manufacture);
//        this.dateFound = dateFound;
//        this.quantityFound = quantityFound;
//        this.priceInStore = priceInStore;
//        this.ExpiredOrFault = ExpiredOrFault;
//        this.isFrontRoom = isFrontRoom;
//    }

    public static List<DamagedItemModel> getReport() {
        return BackendController.getInstance().getDamagedReport();
    }

    public static List<DamagedItemModel> getReportByDate(LocalDate sinceWhen, LocalDate untilWhen) {
        return BackendController.getInstance().getDamagedReportByDate(sinceWhen,untilWhen);
    }
    public static List<DamagedItemModel> getReportByItemID(int itemID) {
        return BackendController.getInstance().getDamagedReportByItemID(itemID);
    }

    public static List<DamagedItemModel> getExpiredReport() {
        return BackendController.getInstance().getOnlyExpiredReport();
    }

    public static List<DamagedItemModel> getDefectiveReport() {
        return BackendController.getInstance().getOnlyDamagedReport();
    }

    public static List<DamagedItemModel> getDamagedReportByDate(LocalDate sinceDate, LocalDate toDate) {
        return BackendController.getInstance().getOnlyDamagedReportByDate(sinceDate,toDate);
    }

    public static List<DamagedItemModel> getExpiredReportByDate(LocalDate sinceDate, LocalDate toDate) {
        return BackendController.getInstance().getOnlyExpiredReportByDate(sinceDate,toDate);
    }

    public static List<DamagedItemModel> getDamagedReportByItemID(int itemID) {
        return BackendController.getInstance().getOnlyDamagedReportByItemID(itemID);
    }

    public static List<DamagedItemModel> getExpiredReportByItemID(int itemID) {
        return BackendController.getInstance().getOnlyExpiredReportByItemID(itemID);
    }


    public LocalDate getDateFound() {
        return dateFound;
    }

    public int getQuantityFound() {
        return quantityFound;
    }

    public double getPriceInStore() {
        return priceInStore;
    }

    public PDamageReason getExpiredOrFault() {
        return ExpiredOrFault;
    }

    public boolean isFrontRoom() {
        return isFrontRoom;
    }


}
