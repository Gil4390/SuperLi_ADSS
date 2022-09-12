package Presentation.Model.SupplierModel;

import BusinessLayer.SupplierBusiness.Contact;
import Presentation.Model.BackendController;

import java.util.Scanner;

public class ContactModel {
    private final int contactId;
    private final int supplierBN;
    private String name;
    private String phoneNumber;
    private String email;
    private final BackendController backendController;
    private final Scanner scanner;

    public ContactModel(Contact contact) {
        this.contactId = contact.getContactId();
        this.supplierBN = contact.getSupplierBN();
        this.name = contact.getName();
        this.phoneNumber = contact.getPhoneNumber();
        this.email = contact.getEmail();
        this.backendController = BackendController.getInstance();
        scanner = new Scanner(System.in);
    }

    public String toString(){
        return "Contact details: name: "+name+ " phone number: "+phoneNumber+ " email: "+email;
    }

    public int getContactId() {
        return contactId;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void changeEmail() {
        System.out.println("Please enter a new contact email:");
        String input = scanner.nextLine();
        backendController.updateContactEmail(supplierBN, contactId, input);
        this.email = input;
    }

    public void changePhoneNumber() {
        System.out.println("Please enter a new contact phone number:");
        String input = scanner.nextLine();
        backendController.updateContactPhone(supplierBN, contactId, input);
        this.phoneNumber = input;
    }

    public void changeName() {
        System.out.println("Please enter a new contact name:");
        String input = scanner.nextLine();
        backendController.updateContactName(supplierBN, contactId, input);
        this.name = input;
    }
}
