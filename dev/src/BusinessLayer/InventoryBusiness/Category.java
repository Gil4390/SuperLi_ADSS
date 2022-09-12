package BusinessLayer.InventoryBusiness;

import DataAccessLayer.InventoryDAL.DALFacade;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private String name;
    private String catID;
    private final List<String> subCatList;
    private Category fatherCategory;
    private final List<Integer> itemIDList;
    private boolean isFatherCatLoadedFlag;

    private final String branchAddress;

    public Category(String branchAddress, String ID, String name){
        this.branchAddress=branchAddress;
        this.name=name;
        this.catID=ID;
        this.fatherCategory= null;
        isFatherCatLoadedFlag = false;
        subCatList = DALFacade.getInstance().getIDSubCat(branchAddress, ID);
        itemIDList = DALFacade.getInstance().getItemIDList(branchAddress, ID);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(validateName(name,getFatherCategory().getCatID())) {
            String newName = getFatherCategory().getName() + "-" + name;
            DALFacade.getInstance().setCategoryName(branchAddress, catID,newName);
            this.name = newName;
            DALFacade.getInstance().removeFromMap(branchAddress, catID);
        }
        else
            throw new IllegalArgumentException("this name:"+ name +",illegal");
        for (Category cat : DALFacade.getInstance().getSubCatList(branchAddress, catID)){
            cat.setName(cat.getName().split("-")[cat.getName().split("-").length-1]);
        }
    }

    public String getCatID() {
        return catID;
    }

    public void setCatID() {
        List<Integer> list = DALFacade.getInstance().getItemIDList(branchAddress, getCatID());
        for(Integer itemID : list){
            DALFacade.getInstance().removeItemFromCategory(branchAddress, getCatID(),itemID);
        }
        DALFacade.getInstance().removeCategory(branchAddress, getCatID());
        this.catID = getFatherCategory().getCatID()+"#"+(DALFacade.getInstance().getCategoryCounter(branchAddress, getFatherCategory().getCatID())+1);
        DALFacade.getInstance().addCategory(branchAddress, getCatID(),getName(),getFatherCategory().getCatID());
        for(Integer itemID : list){
            DALFacade.getInstance().addItemToCategory(branchAddress, getCatID(),itemID);
        }
    }

    public Category getFatherCategory() {
        if(isFatherCatLoadedFlag)
            return fatherCategory;
        else{
            fatherCategory = DALFacade.getInstance().getFatherCategory(branchAddress, getCatID());
            isFatherCatLoadedFlag = true;
            return fatherCategory;
        }
    }

    public void setFatherCategory(Category fatherCategory) {
        this.fatherCategory = fatherCategory;
        DALFacade.getInstance().setCategoryFather(branchAddress, catID,fatherCategory.getCatID());
        setCatID();
    }

    public List<Category> getSubCatList() {
        List<Category> result = new ArrayList<>();
        for(String subCat: subCatList)
            result.add(DALFacade.getInstance().getCategory(branchAddress, subCat));
        return result;
    }

    public List<Integer> getItemIDList() {
        return itemIDList;
    }

    public void deleteSubCat(String idCat){
        if(!subCatList.contains(idCat))
            throw new IllegalArgumentException("category:"+idCat+" not exist");
        subCatList.remove(idCat);
        DALFacade.getInstance().removeSubCategory(branchAddress, idCat);
        DALFacade.getInstance().setCategoryCounter(branchAddress, idCat, DALFacade.getInstance().getCategoryCounter(branchAddress, idCat)-1);
    }

    public void addNewSubCat(String catName){
        String catNewID = catID+"#"+(DALFacade.getInstance().getCategoryCounter(branchAddress, catID)+1);
        DALFacade.getInstance().addCategory(branchAddress, catNewID, name+"-"+catName, getCatID());
        DALFacade.getInstance().setCategoryCounter(branchAddress, catID, DALFacade.getInstance().getCategoryCounter(branchAddress, catID)+1);
        if(validateName(catName,catNewID))
            subCatList.add(catNewID);
        else
            throw new IllegalArgumentException("name:"+ catName+"not legal");
    }

    public void addSubCat(Category cat){
        try{
            if (validateName(cat.getName().split("-")[cat.getName().split("-").length-1],catID)){
                cat.setFatherCategory(this);
                cat.setName(cat.getName().split("-")[cat.getName().split("-").length-1]);
                subCatList.add(cat.getCatID());
            }
            else
                throw new IllegalArgumentException("name:"+ cat.getName()+" ,illegal");

        }
        catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }    }

    public Category getCategory(String categoryID){
        if(categoryID.contains("#")) {
            String [] arr = categoryID.split("#");
            String tempID=this.catID+"#"+arr[0]; //חזק
            if(!subCatList.contains(tempID))
                throw new IllegalArgumentException("the category: "+tempID+", not in the system");
            return DALFacade.getInstance().getCategory(branchAddress, tempID).getCategory(categoryID.substring(categoryID.indexOf('#')+1));
        }
        else{
            categoryID = this.catID + "#" + categoryID;
            if (!subCatList.contains(categoryID))
                throw new IllegalArgumentException("the category: " + categoryID + ", not in the system");
            return DALFacade.getInstance().getCategory(branchAddress, categoryID);
        }
    }

    private boolean validateName(String catName, String catID){
        List<Category> brother = DALFacade.getInstance().getSubCatList(branchAddress, catID);
        if(catName.contains("-"))
            throw new IllegalArgumentException("Illegal char in the name: - ");
        String testName = name+"-"+catName;
        for(Category tempCat : brother){
            if(tempCat.name.equals(testName) && !catID.equals(tempCat.getCatID()))
                return false;
        }
        return true;
    }

    public void removeItemID(int itemID) {
        if(!itemIDList.contains(itemID))
            throw new IllegalArgumentException("item id: "+itemID+", not in the category item list");
        itemIDList.remove(itemID);
        DALFacade.getInstance().removeItemFromCategory(branchAddress, catID,itemID);
    }

    public void addItemID(int itemID) {
        if(itemIDList.contains(itemID))
            throw new IllegalArgumentException("item id: "+itemID+", not in the category item list");
        itemIDList.add(itemID);
        DALFacade.getInstance().addItemToCategory(branchAddress, catID,itemID);
    }
}
