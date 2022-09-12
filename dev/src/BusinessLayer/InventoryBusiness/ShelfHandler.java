package BusinessLayer.InventoryBusiness;

import DataAccessLayer.InventoryDAL.DALFacade;

import java.util.List;

public class ShelfHandler {

    public ShelfHandler(){}

    public List<Integer> getBackRoomShelves(String branchAddress) {
        return DALFacade.getInstance().getAllBackShelves(branchAddress);
    }

    public List<Integer> getFrontRoomShelves(String branchAddress) {
        return DALFacade.getInstance().getAllFrontShelves(branchAddress);
    }

    public int getShelfCounter(String branchAddress) {
        return DALFacade.getInstance().getShelvesCounter(branchAddress)+1;
    }

    public void addShelf(String branchAddress, boolean shelfInBackRoom){
        DALFacade.getInstance().addShelf(branchAddress, getShelfCounter(branchAddress), shelfInBackRoom);
    }
}
