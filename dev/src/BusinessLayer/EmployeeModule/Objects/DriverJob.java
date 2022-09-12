package BusinessLayer.EmployeeModule.Objects;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class DriverJob extends Job {
    private final int id;
    private final List<String> isAvailable;

    public DriverJob(int id, Vector<String> driverLicenses){
        super("driver", driverLicenses);
//        for (driverLicense dl: driverLicenses)
//            super.addCertificate(dl.name());
        this.id = id;
        isAvailable = new LinkedList<>();
    }
    public void reloadsAvailable(List<String> dates)
    {
        updateTimeStamp();
        isAvailable.addAll(dates);
    }

    public int getId() {updateTimeStamp(); return id; }
    public boolean isAvailable(String date) {
        updateTimeStamp();
        return !isAvailable.contains(date);
    }

    public void setUnavailable(String date) {
        updateTimeStamp();
        isAvailable.add(date);
    }

//    public boolean isDriverLicenseFitForTruck(int truckWeight) {
//        List<driverLicense> license = getDriverLicenses();
//        if (license.contains(driverLicense.B) && truckWeight<3500)
//            return true;
//        if(license.contains(driverLicense.C1) && truckWeight>3500 & truckWeight<12000)
//            return true;
//        if(license.contains(driverLicense.C) && truckWeight>12000)
//            return true;
//        return license.contains(driverLicense.C_E) && truckWeight > 3500;
//    }

    public boolean checkMaxTruckWeight(int truckWeight) {
        updateTimeStamp();
        if (getCertifications().contains("B") && truckWeight<3500)
            return true;
        if(getCertifications().contains("C1") && truckWeight>3500 & truckWeight<12000)
            return true;
        if(getCertifications().contains("C") && truckWeight>12000)
            return true;
        return getCertifications().contains("C_E") && truckWeight > 3500;
    }

    public List<String> getDriverLicencesForDriver() {
        updateTimeStamp();
        List<String> licenses= new LinkedList<>();
        for (String s: getCertifications()){
            if (isLicense(s))
                licenses.add(s);
        }
        return licenses;
    }

    public boolean isLicense(String certification){
        updateTimeStamp();
        List<String> licenses = Arrays.asList("A", "A1","A2", "B","C","C1","C_E","D","D1","D3");
        if(licenses.contains(certification)) return true;
        return false;
    }
}