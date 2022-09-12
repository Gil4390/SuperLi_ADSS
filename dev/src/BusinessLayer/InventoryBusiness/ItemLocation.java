package BusinessLayer.InventoryBusiness;

import DataAccessLayer.InventoryDAL.DALFacade;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ItemLocation {
    private List<Integer> shelvesInFrontRoom;
    private List<Integer> shelvesInBackRoom;

    private final String branchAddress;

    public ItemLocation(String branchAddress, List<Integer> newShelvesInFrontRoom, List<Integer> newShelvesInBackRoom){
        this.shelvesInFrontRoom = newShelvesInFrontRoom;
        this.shelvesInBackRoom = newShelvesInBackRoom;
        this.branchAddress = branchAddress;
    }

    public void changeItemAppointedShelvesByRoom(boolean fromBackRoom, int [] toWhichShelf, int itemID)
    {
        if(toWhichShelf == null)
            throw new IllegalArgumentException("toWhichShelf cant be null");

        if(toWhichShelf.length==0)
            throw new IllegalArgumentException("toWhichShelf received null or empty");
        if(fromBackRoom)
            shelvesInBackRoom = Arrays.stream(toWhichShelf).boxed().collect(Collectors.toList());
        else
            shelvesInFrontRoom = Arrays.stream(toWhichShelf).boxed().collect(Collectors.toList());
        DALFacade.getInstance().changeItemAppointedShelvesByRoom(branchAddress, fromBackRoom, Arrays.stream(toWhichShelf).boxed().collect(Collectors.toList()), itemID);
    }

    public List<Integer> getShelvesInFrontRoom() {
        return shelvesInFrontRoom;
    }

    public List<Integer> getShelvesInBackRoom() {
        return shelvesInBackRoom;
    }
}
