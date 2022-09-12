package Presentation.ViewModel.SupplierViewModel;

import Presentation.Model.SupplierModel.ContactModel;
import Presentation.Model.SupplierModel.SupplierModel;
import Presentation.View.SupplierView.ContactView;
import Presentation.View.SupplierView.SupplierView;
import Presentation.View.View;

public class SupplierViewModel {
    private final SupplierModel supplierModel;

    public SupplierViewModel(SupplierModel supplierModel) {
        this.supplierModel = supplierModel;
    }

    public View updateName() {
        try {
            supplierModel.updateName();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new SupplierView(supplierModel);
    }

    public View updateBankAccount() {
        try {
            supplierModel.updateBankAccount();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new SupplierView(supplierModel);
    }

    public View updateDeliveryMethod() {
        try {
            supplierModel.updateDeliveryMethod();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new SupplierView(supplierModel);
    }

    public View updatePaymentMethod() {
        try {
            supplierModel.updatePaymentMethod();
        }
        catch (Exception e)
        {
            System.out.println("Invalid input!");
        }
        return new SupplierView(supplierModel);
    }

    public View enterContact() {
        try{
            ContactModel contactModel = supplierModel.enterContact();
            return new ContactView(supplierModel, contactModel);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new SupplierView(supplierModel);
        }
    }

    public View addContact() {
        try {
            supplierModel.addContact();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new SupplierView(supplierModel);

    }

    public View removeContact() {
        try {
            supplierModel.removeContact();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new SupplierView(supplierModel);
    }

    public String getName() {
        return supplierModel.getName();
    }

    public SupplierModel getSupplierModel() {
        return supplierModel;
    }
}
