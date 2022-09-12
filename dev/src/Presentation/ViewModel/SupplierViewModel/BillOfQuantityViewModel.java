package Presentation.ViewModel.SupplierViewModel;

import Presentation.Model.SupplierModel.BillOfQuantityModel;
import Presentation.Model.SupplierModel.SupplierModel;
import Presentation.View.SupplierView.BillOfQuantityView;
import Presentation.View.SupplierView.EditProductView;
import Presentation.View.View;

public class BillOfQuantityViewModel {
    private final SupplierModel supplierModel;
    private final BillOfQuantityModel billOfQuantityModel;

    public BillOfQuantityViewModel(SupplierModel supplierModel) {
        this.supplierModel = supplierModel;
        this.billOfQuantityModel = supplierModel.getBillOfQuantity();
    }

    public View productsList() {
        try {
            billOfQuantityModel.productsList();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
            return new BillOfQuantityView(supplierModel);
    }

    public View addProduct() {
        try {
            billOfQuantityModel.addProduct();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new BillOfQuantityView(supplierModel);
    }

    public View removeProduct() {
        try {
            billOfQuantityModel.removeProduct();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new BillOfQuantityView(supplierModel);
    }

    public View editProduct() {
        try {
            int catalogNumber = billOfQuantityModel.editProduct();
            return new EditProductView(catalogNumber, this, supplierModel);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new BillOfQuantityView(supplierModel);
        }
    }

    public View addProductDiscount() {
        try {
            billOfQuantityModel.addProductDiscount();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new BillOfQuantityView(supplierModel);
    }

    public View updatePrice(int catalogNumber) {
        try {
            billOfQuantityModel.updatePrice(catalogNumber);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new EditProductView(catalogNumber, this, supplierModel);
    }

    public View updateName(int catalogNumber) {
        try {
            billOfQuantityModel.updateName(catalogNumber);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new EditProductView(catalogNumber, this, supplierModel);
    }

    public View updateSupplierCatalog(int catalogNumber) {
        try {
            billOfQuantityModel.updateSupplierCatalog(catalogNumber);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new EditProductView(catalogNumber, this, supplierModel);
    }

    public View updateDiscount(int catalogNumber) {
        try {
            billOfQuantityModel.updateDiscount(catalogNumber);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new EditProductView(catalogNumber, this, supplierModel);
    }

    public View removeProductDiscount() {
        try {
            billOfQuantityModel.removeProductDiscount();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new BillOfQuantityView(supplierModel);
    }
}
