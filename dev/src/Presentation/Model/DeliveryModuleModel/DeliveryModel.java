package Presentation.Model.DeliveryModuleModel;

import ServiceLayer.Objects.DeliveryObjects.FDelivery;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class DeliveryModel {
    private int id;
    private Date date;
    private Date time;
    private String truckLicense;
    private int driverId;
    private String sourceAddress;
    private List<String> destAddresses;
    private final Scanner scanner;

    public DeliveryModel(FDelivery fdelivery) throws ParseException {
        this.id = fdelivery.getId();
        this.date = new SimpleDateFormat("yyyy-MM-dd").parse(fdelivery.getDate());
        this.time = new SimpleDateFormat("HH:mm").parse(fdelivery.getTime());
        this.driverId = fdelivery.getDriverId();
        this.truckLicense = fdelivery.getTruckLicense();
        this.sourceAddress = fdelivery.getSourceAddress();
        this.destAddresses = fdelivery.getDestAddresses();
        this.scanner = new Scanner(System.in);
    }


    public int getId() { return id; }
    public Date getTime() {return time;}

    public String getTruckLicense() {
        return truckLicense;
    }
    public Date getDate(){return date;}
    public int getDriverId() {
        return driverId;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public List<String> getDestAddresses() {
        return destAddresses;
    }

    public String toString(){
        return "Id: "+id+"    Date: "+getDateString()+"    Time: "+getTimeString();
    }

    public String getDateString() {
        String year = String.valueOf(date.getYear()+1900);
        String month = String.valueOf(date.getMonth()+1);
        String day = String.valueOf(date.getDate());
        if (day.length()==1) day="0"+day;
        if (month.length()==1) month="0"+month;
        return  day+"/"+month+"/"+year;
    }
    public String getTimeString() {
        String hour = String.valueOf(time.getHours());
        if (hour.length()==1) hour="0"+hour;
        String minute = String.valueOf(time.getMinutes());
        if (minute.length()==1) minute="0"+minute;
        return hour + ":" + minute;
    }
}
