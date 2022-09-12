package DataAccessLayer.SupplierDAL;

import BusinessLayer.SupplierBusiness.Contact;

import java.sql.*;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ContactMapper extends DalController {
    private static class ContactMapperHolder{
        private static ContactMapper instance = new ContactMapper();
    }

    public static ContactMapper getInstance() {
        return ContactMapperHolder.instance;
    }

    private final String ContactIDColumnName="ContactID";
    private final String SupplierBNColumnName="SupplierBN";
    private final String NameColumnName="Name";
    private final String PhoneNumberColumnName="PhoneNumber";
    private final String EmailColumnName="Email";

    private Map<Integer, Map<Integer, Contact>> contacts;

    private ContactMapper() { super("Contacts"); contacts = new HashMap<>(); }
    // get all contacts from database
    public List<Contact>selectSupplierContacts(int supplierBN){
        return (List<Contact>)(List<?>)selectList(supplierBN, SupplierBNColumnName);
    }
    // insert new contact to database
    public boolean insert(int contactId, int supplierBN, String contactName, String phoneNumber, String email) {
        checkSupplierExistsInDataBase(contactId, supplierBN);
        Contact contact = new Contact(contactId, supplierBN, contactName, phoneNumber, email);
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}, {4}, {5}) VALUES(?,?,?,?,?)",
                getTableName(), ContactIDColumnName, SupplierBNColumnName, NameColumnName, PhoneNumberColumnName, EmailColumnName);
        try (Connection conn = super.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, contact.getContactId());
            pstmt.setInt(2, contact.getSupplierBN());
            pstmt.setString(3, contact.getName());
            pstmt.setString(4, contact.getPhoneNumber());
            pstmt.setString(5, contact.getEmail());

            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        contacts.putIfAbsent(contact.getSupplierBN(), new HashMap<>());
        contacts.get(contact.getSupplierBN()).put(contact.getContactId(), contact);
        return true;
    }

    public void delete(int supplierBN, int contactId) {
        checkContactExists(supplierBN, contactId);
        delete(supplierBN, contactId, SupplierBNColumnName, ContactIDColumnName);
        contacts.get(supplierBN).remove(contactId);
    }

    public void checkSupplierExistsInDataBase(int supplierBN, int contactId) {
        if(contacts.containsKey(supplierBN) && contacts.get(supplierBN).containsKey(contactId))
            throw new IllegalArgumentException("contact already exists!");
        Contact contact = (Contact) select(supplierBN, contactId, SupplierBNColumnName, ContactIDColumnName);
        if(contact != null)
            throw new IllegalArgumentException("contact already exists!");
    }

    private void checkContactExists(int supplierBN, int contactId) {
        if (!contacts.containsKey(supplierBN) || !contacts.get(supplierBN).containsKey(contactId)) {
            Contact contact = (Contact) select(supplierBN,contactId, SupplierBNColumnName, ContactIDColumnName);
            if(contact ==null)
                throw new IllegalArgumentException("Contact is not exists!");
            contacts.putIfAbsent(supplierBN, new HashMap<>());
            contacts.get(supplierBN).put(contactId, contact);
        }
    }

    private void checkSupplierExists(int supplierBN){
        List<Contact> contactsList = selectSupplierContacts(supplierBN);
        if(!contacts.containsKey(supplierBN))
            if(contactsList.size()==0)
                throw new IllegalArgumentException("Supplier doesn't have contacts!");
        contacts.putIfAbsent(supplierBN, new HashMap<>());
        for(Contact contact : contactsList)
            contacts.get(supplierBN).putIfAbsent(contact.getContactId(), contact);
    }

    public void updateContactName(int supplierBN, int contactId, String newName) {
        stringValidation(newName);
        checkContactExists(supplierBN, contactId);
        update(supplierBN, contactId, NameColumnName, newName, SupplierBNColumnName, ContactIDColumnName);
        contacts.get(supplierBN).get(contactId).setName(newName);
    }

    public void updateContactPhoneNumber(int supplierBN, int contactId, String phoneNumber) {
        stringValidation(phoneNumber);
        checkContactExists(supplierBN, contactId);
        update(supplierBN, contactId, PhoneNumberColumnName, phoneNumber, SupplierBNColumnName, ContactIDColumnName);
        contacts.get(supplierBN).get(contactId).setPhoneNumber(phoneNumber);
    }

    public void updateContactEmail(int supplierBN, int contactId, String email) {
        stringValidation(email);
        checkContactExists(supplierBN, contactId);
        update(supplierBN, contactId, EmailColumnName, email, SupplierBNColumnName, ContactIDColumnName);
        contacts.get(supplierBN).get(contactId).setEmail(email);
    }
    private void stringValidation(String str){
        if(str == null)
            throw new IllegalArgumentException("Illegal input!");
    }

    @Override
    protected Object ConvertReaderToObject(ResultSet reader) {
        Contact result = null;
        try {
            result = new Contact(reader.getInt(1), reader.getInt(2), reader.getString(3), reader.getString(4), reader.getString(5));
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    @Override
    public void cleanCache() {
        Iterator<Map.Entry<Integer, Map<Integer, Contact>>> iter = contacts.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer, Map<Integer, Contact>> entry = iter.next();
            Iterator<Map.Entry<Integer, Contact>> iterContacts = entry.getValue().entrySet().iterator();
            while (iterContacts.hasNext()) {
                Map.Entry<Integer,Contact> entryContact = iterContacts.next();
                if(entryContact.getValue().shouldCleanCache()){
                    System.out.println("Cleaning contact "+entryContact.getValue().getContactId() +" from cache!");
                    iterContacts.remove();
                }
            }
        }
    }

    @Override
    protected void cleanCacheForTests() {
        contacts = new HashMap<>();
    }

    public List<Contact> getSupplierContacts(int supplierBN) {
        checkSupplierExists(supplierBN);
        return contacts.get(supplierBN).values().stream().collect(Collectors.toList());
    }

    public int getLastContactId(){
        Contact contact = (Contact) selectLastId(ContactIDColumnName);
        if(contact == null)
            return -1;
        return contact.getContactId();
    }
}
