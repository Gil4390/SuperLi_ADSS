package Presentation.ViewModel.SupplierViewModel;

import Presentation.Model.SupplierModel.ContactModel;
import Presentation.Model.SupplierModel.SupplierModel;
import Presentation.View.SupplierView.ContactView;
import Presentation.View.View;

public class ContactViewModel {
    private final SupplierModel supplierModel;
    private final ContactModel contactModel;

    public ContactViewModel(SupplierModel supplierModel, ContactModel contactModel) {
        this.supplierModel = supplierModel;
        this.contactModel = contactModel;
    }

    public View changeName() {
        try {
            contactModel.changeName();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ContactView(supplierModel, contactModel);
    }

    public View changePhoneNumber() {
        try {
            contactModel.changePhoneNumber();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ContactView(supplierModel, contactModel);
    }

    public View changeEmail() {
        try {
            contactModel.changeEmail();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ContactView(supplierModel, contactModel);
    }
}
