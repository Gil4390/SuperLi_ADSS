package Presentation.View.SupplierView;

import Presentation.Model.SupplierModel.ContactModel;
import Presentation.Model.SupplierModel.SupplierModel;
import Presentation.View.ApplicationView;
import Presentation.View.MainMenuView;
import Presentation.View.View;
import Presentation.ViewModel.SupplierViewModel.ContactViewModel;

public class ContactView implements View {
    private final ContactViewModel contactViewModel;
    private final SupplierModel supplierModel;
    private final ContactModel contactModel;

    public ContactView(SupplierModel supplierModel, ContactModel contactModel) {
        this.contactViewModel = new ContactViewModel(supplierModel, contactModel);
        this.supplierModel = supplierModel;
        this.contactModel = contactModel;
    }

    @Override
    public void printMenu() {
        System.out.println(contactModel.toString());
        System.out.println("-----------------"+contactModel.getName()+" contact-----------------");
        System.out.println("1- Change this contact name");
        System.out.println("2- Change the contact phone number");
        System.out.println("3- Change the contact email");
        System.out.println("4- Back");
        System.out.println("5- Logout");
    }

    @Override
    public View nextInput(String input) {
        switch(input) {
            case "back":
            case "4":
                return new SupplierView(supplierModel);
            case "1":
                return contactViewModel.changeName();
            case "2":
                return contactViewModel.changePhoneNumber();
            case "3":
                return contactViewModel.changeEmail();
            case "5":
                return new MainMenuView(true);
            case "close":
                ApplicationView.shouldTerminate = true;
                break;
            default:
                System.out.println("Invalid input");
        }
        return this;
    }
}
