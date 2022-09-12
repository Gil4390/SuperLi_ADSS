package Presentation.Model.InventoryModel;

import Presentation.Model.BackendController;
import ServiceLayer.Objects.InventoryObjects.FCategory;

import java.time.LocalDate;
import java.util.List;

public class CategoryModel {
    private String name;
    private String catID;
    private String catFatherID;

    public CategoryModel(String name, String catID, String catFatherID){
        this.catID=catID;
        this.name=name;
        this.catFatherID=catFatherID;
    }

    public CategoryModel(FCategory cat){
        this.catID=cat.getCatID();
        this.name=cat.getName();
        this.catFatherID=cat.getFatherID();
    }

    public String getCatFatherID() {
        return catFatherID;
    }

    public void addCategory() {
        BackendController.getInstance().addCategory(this);
    }

    public void moveCategory(CategoryModel fatherCat){
        BackendController.getInstance().moveCategory(fatherCat,this);
        catFatherID = fatherCat.getCatID();
    }
    public String getName() {
        return name;
    }

    public String getCatID() {
        return catID;
    }

    public CategoryModel getCategory() {
        return BackendController.getInstance().getCategory(this);
    }

    public CategoryModel[] getSubCat() {
        return BackendController.getInstance().getSubCat(this);
    }

    public List<ItemModel> getCategoryItems() {
        return BackendController.getInstance().getCategoryItems(this);
    }

    public void setName(String newName) {
        name = newName;
        BackendController.getInstance().setCategoryName(this);
    }

    public CategoryModel getCatFather() {
        return BackendController.getInstance().getCategoryFather(this);
    }

    public void addDiscountToAllItemsInCategory(double discountFare, String discountName, LocalDate fromDate, LocalDate toDate) {
        BackendController.getInstance().giveDiscountToCategory(this, discountFare,  discountName,  fromDate,  toDate);
    }

}
