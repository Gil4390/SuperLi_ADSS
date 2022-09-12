package Presentation.Model;

import Presentation.View.Printer;
import ServiceLayer.ApplicationService;
import ServiceLayer.Objects.InventoryObjects.FDamageReason;
import ServiceLayer.Responses.Response;

import java.io.File;
import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DataLoader {
    private final ApplicationService applicationService;
    public DataLoader(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    protected Connection connect() {
        String path = (new File("").getAbsolutePath()).concat("\\SuperLiDB.db");
        String connectionString = "jdbc:sqlite:".concat(path);
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(connectionString);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public boolean isDataLoaded()
    {
        String sql = "SELECT * FROM LoadDataTest";
        try(Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            if(rs.next())
                return true;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public void loadDataForTests() {
        if(!isDataLoaded()) {
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            try {
                System.out.print("Loading data");
                scheduler.scheduleAtFixedRate(notifier, 0, 1, TimeUnit.SECONDS);
                loadData();
                scheduler.shutdownNow();
                System.out.println();
                System.out.println("Success! uploaded all the fake data for tests");
                setDataLoadedForTests();
            } catch (Exception e) {
                scheduler.shutdownNow();
                Printer.getInstance().print(e.getMessage());
                Printer.getInstance().print("Problem : Returning to report main menu");
            }
        }
        else
            System.out.println("Data is already loaded!");
    }

    private void setDataLoadedForTests() {
        String sql = "INSERT INTO LoadDataTest (Load) VALUES(1)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadData() throws Exception {
        loadSupplierData();
        loadEmployeeData();
        loadInventoryData();
        loadDeliveriesData();
    }

    private void loadInventoryData() {
        loadCategoriesData();
        loadShelvesAndItemsData();
        loadItemsDiscountData();
        loadQuantityForItemsData();
        loadDamagedRecordData();
    }

    private void loadCategoriesData() {
        Response t = applicationService.addCategory("Yasmin 3, Dimona", "0", "Drinks"); // 0#1
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        applicationService.addCategory("Yasmin 3, Dimona", "0#1", "Sparkled Drinks");// 0#1#1
        applicationService.addCategory("Yasmin 3, Dimona", "0#1#1", "1L");// 0#1#1#1
        applicationService.addCategory("Yasmin 3, Dimona", "0#1#1", "1.5L");// 0#1#1#2
        applicationService.addCategory("Yasmin 3, Dimona", "0#1#1", "2L");// 0#1#1#3
        applicationService.addCategory("Yasmin 3, Dimona", "0#1", "Juices");// 0#1#2
        t=applicationService.addCategory("Yasmin 3, Dimona", "0#1#2", "1L");// 0#1#2#1
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        applicationService.addCategory("Yasmin 3, Dimona", "0#1#2", "1.5L");// 0#1#2#2
        applicationService.addCategory("Yasmin 3, Dimona", "0#1#2", "2L");// 0#1#2#3
        applicationService.addCategory("Yasmin 3, Dimona", "0#1", "Liquors");// 0#1#3
        applicationService.addCategory("Yasmin 3, Dimona", "0#1#3", "1L");// 0#1#3#1
        applicationService.addCategory("Yasmin 3, Dimona", "0#1#3", "1.5L");// 0#1#3#2
        applicationService.addCategory("Yasmin 3, Dimona", "0#1#3", "2L");// 0#1#3#3

        applicationService.addCategory("Yasmin 3, Dimona", "0", "Snacks");// 0#2
        applicationService.addCategory("Yasmin 3, Dimona", "0#2", "Osem");// 0#2#1
        applicationService.addCategory("Yasmin 3, Dimona", "0#2#1", "Big");// 0#2#1#1
        t=applicationService.addCategory("Yasmin 3, Dimona", "0#2#1", "Small");// 0#2#1#2
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        applicationService.addCategory("Yasmin 3, Dimona", "0#2", "Elit");// 0#2#2
        applicationService.addCategory("Yasmin 3, Dimona", "0#2#2", "Big");// 0#2#2#1
        applicationService.addCategory("Yasmin 3, Dimona", "0#2#2", "Small");// 0#2#2#2

        applicationService.addCategory("Yasmin 3, Dimona", "0", "Vegan");// 03
        applicationService.addCategory("Yasmin 3, Dimona", "0#3", "Meat Alternatives");// 0#3#1
        applicationService.addCategory("Yasmin 3, Dimona", "0#3#1", "Teva-Deli");// 0#3#1#1
        t=applicationService.addCategory("Yasmin 3, Dimona", "0#3#1", "Tnuva");// 0#3#1#2
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        applicationService.addCategory("Yasmin 3, Dimona", "0#3", "Milk Alternatives");// 0#3#2
        applicationService.addCategory("Yasmin 3, Dimona", "0#3#2", "Alpro");// 0#3#2#1
        applicationService.addCategory("Yasmin 3, Dimona", "0#3#2", "Tnuva");// 0#3#2#2

        applicationService.addCategory("Rehov 2, Haifa", "0", "Drinks"); // 0#1
        applicationService.addCategory("Rehov 2, Haifa", "0#1", "Sparkled Drinks");// 0#1#1
        applicationService.addCategory("Rehov 2, Haifa", "0#1#1", "1L");// 0#1#1#1
        applicationService.addCategory("Rehov 2, Haifa", "0#1#1", "1.5L");// 0#1#1#2
        t=applicationService.addCategory("Rehov 2, Haifa", "0#1#1", "2L");// 0#1#1#3
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());applicationService.addCategory("Rehov 2, Haifa", "0#1", "Juices");// 0#1#2
        applicationService.addCategory("Rehov 2, Haifa", "0#1#2", "1L");// 0#1#2#1
        applicationService.addCategory("Rehov 2, Haifa", "0#1#2", "1.5L");// 0#1#2#2
        applicationService.addCategory("Rehov 2, Haifa", "0#1#2", "2L");// 0#1#2#3
        applicationService.addCategory("Rehov 2, Haifa", "0#1", "Liquors");// 0#1#3
        applicationService.addCategory("Rehov 2, Haifa", "0#1#3", "1L");// 0#1#3#1
        applicationService.addCategory("Rehov 2, Haifa", "0#1#3", "1.5L");// 0#1#3#2
        applicationService.addCategory("Rehov 2, Haifa", "0#1#3", "2L");// 0#1#3#3

        applicationService.addCategory("Rehov 2, Haifa", "0", "Snacks");// 0#2
        applicationService.addCategory("Rehov 2, Haifa", "0#2", "Osem");// 0#2#1
        applicationService.addCategory("Rehov 2, Haifa", "0#2#1", "Big");// 0#2#1#1
        t=applicationService.addCategory("Rehov 2, Haifa", "0#2#1", "Small");// 0#2#1#2
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        applicationService.addCategory("Rehov 2, Haifa", "0#2", "Elit");// 0#2#2
        applicationService.addCategory("Rehov 2, Haifa", "0#2#2", "Big");// 0#2#2#1
        applicationService.addCategory("Rehov 2, Haifa", "0#2#2", "Small");// 0#2#2#2

        applicationService.addCategory("Rehov 2, Haifa", "0", "Vegan");// 03
        applicationService.addCategory("Rehov 2, Haifa", "0#3", "Meat Alternatives");// 0#3#1
        applicationService.addCategory("Rehov 2, Haifa", "0#3#1", "Teva-Deli");// 0#3#1#1
        t=applicationService.addCategory("Rehov 2, Haifa", "0#3#1", "Tnuva");// 0#3#1#2
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        applicationService.addCategory("Rehov 2, Haifa", "0#3", "Milk Alternatives");// 0#3#2
        applicationService.addCategory("Rehov 2, Haifa", "0#3#2", "Alpro");// 0#3#2#1
        applicationService.addCategory("Rehov 2, Haifa", "0#3#2", "Tnuva");// 0#3#2#2
    }

    private void loadShelvesAndItemsData() {
        for (int i = 0; i < 5; i++)
            applicationService.addShelf("Yasmin 3, Dimona", true);
        for (int i = 0; i < 5; i++)
            applicationService.addShelf("Yasmin 3, Dimona", false);

        for (int i = 0; i < 5; i++)
            applicationService.addShelf("Rehov 2, Haifa", true);
        for (int i = 0; i < 5; i++)
            applicationService.addShelf("Rehov 2, Haifa", false);

        List<Integer> snacksBackShelf = new LinkedList<>();
        snacksBackShelf.add(1);
        snacksBackShelf.add(2);
        List <Integer> snacksFrontShelf = new LinkedList<>();
        snacksFrontShelf.add(6);
        snacksFrontShelf.add(7);

        List <Integer> drinksBackShelf = new LinkedList<>();
        drinksBackShelf.add(3);
        drinksBackShelf.add(4);
        List <Integer> drinksFrontShelf = new LinkedList<>();
        drinksFrontShelf.add(8);
        drinksFrontShelf.add(9);

        List <Integer> veganBackShelf = new LinkedList<>();
        veganBackShelf.add(5);
        List <Integer> veganFrontShelf = new LinkedList<>();
        veganFrontShelf.add(10);
        Response t = applicationService.addItem("Yasmin 3, Dimona", "Bamba Big", "0#2#1#1", 10, 20, 50, "Osem",snacksBackShelf,snacksFrontShelf);
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        t= applicationService.addItem("Yasmin 3, Dimona", "Bamba Small", "0#2#1#2", 5, 20, 50, "Osem",snacksBackShelf,snacksFrontShelf);
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        t= applicationService.addItem("Yasmin 3, Dimona", "Bisli Big", "0#2#1#1", 10, 20, 50, "Osem",snacksBackShelf,snacksFrontShelf);
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        t=applicationService.addItem("Yasmin 3, Dimona", "Bisli Small", "0#2#1#2", 5, 20, 50, "Osem",snacksBackShelf,snacksFrontShelf);
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        //5
        applicationService.addItem("Yasmin 3, Dimona", "Coca Cola 1L", "0#1#1#1", 6, 20, 50, "Coca Cola",drinksBackShelf,drinksFrontShelf);
        applicationService.addItem("Yasmin 3, Dimona", "Coca Cola 1.5L", "0#1#1#2", 8, 20, 50, "Coca Cola",drinksBackShelf,drinksFrontShelf);
        applicationService.addItem("Yasmin 3, Dimona", "Coca Cola 2L", "0#1#1#3", 10, 20, 50, "Coca Cola",drinksBackShelf,drinksFrontShelf);
        applicationService.addItem("Yasmin 3, Dimona", "Sprite 1L", "0#1#1#1", 6, 20, 50, "Coca Cola",drinksBackShelf,drinksFrontShelf);
        applicationService.addItem("Yasmin 3, Dimona", "Sprite 1.5L", "0#1#1#2", 8, 20, 50, "Coca Cola",drinksBackShelf,drinksFrontShelf);
        applicationService.addItem("Yasmin 3, Dimona", "Sprite 2L", "0#1#1#3", 10, 20, 50, "Coca Cola",drinksBackShelf,drinksFrontShelf);
        //11
        applicationService.addItem("Yasmin 3, Dimona", "Iced tea 1L", "0#1#2#1", 6, 20, 50, "Nes-Tea",drinksBackShelf,drinksFrontShelf);
        applicationService.addItem("Yasmin 3, Dimona", "Iced tea 1.5L", "0#1#2#2", 8, 20, 50, "Nes-Tea",drinksBackShelf,drinksFrontShelf);
        applicationService.addItem("Yasmin 3, Dimona", "Iced tea 2L", "0#1#2#3", 10, 20, 50, "Nes-Tea",drinksBackShelf,drinksFrontShelf);
        //14
        applicationService.addItem("Yasmin 3, Dimona", "JagerMeister Liquor", "0#1#3#1", 70, 5, 20, "Jager",drinksBackShelf,drinksFrontShelf);
        applicationService.addItem("Yasmin 3, Dimona", "FireBall Whisky", "0#1#3#1", 70, 5, 20, "FireBall",drinksBackShelf,drinksFrontShelf);

        t=applicationService.addItem("Rehov 2, Haifa", "Bamba Big", "0#2#1#1", 10, 20, 50, "Osem",snacksBackShelf,snacksFrontShelf);
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        applicationService.addItem("Rehov 2, Haifa", "Bamba Small", "0#2#1#2", 5, 20, 50, "Osem",snacksBackShelf,snacksFrontShelf);
        applicationService.addItem("Rehov 2, Haifa", "Bisli Big", "0#2#1#1", 10, 20, 50, "Osem",snacksBackShelf,snacksFrontShelf);
        applicationService.addItem("Rehov 2, Haifa", "Bisli Small", "0#2#1#2", 5, 20, 50, "Osem",snacksBackShelf,snacksFrontShelf);
        //5
        applicationService.addItem("Rehov 2, Haifa", "Coca Cola 1L", "0#1#1#1", 6, 20, 50, "Coca Cola",drinksBackShelf,drinksFrontShelf);
        applicationService.addItem("Rehov 2, Haifa", "Coca Cola 1.5L", "0#1#1#2", 8, 20, 50, "Coca Cola",drinksBackShelf,drinksFrontShelf);
        t=applicationService.addItem("Rehov 2, Haifa", "Coca Cola 2L", "0#1#1#3", 10, 20, 50, "Coca Cola",drinksBackShelf,drinksFrontShelf);
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        applicationService.addItem("Rehov 2, Haifa", "Sprite 1L", "0#1#1#1", 6, 20, 50, "Coca Cola",drinksBackShelf,drinksFrontShelf);
        applicationService.addItem("Rehov 2, Haifa", "Sprite 1.5L", "0#1#1#2", 8, 20, 50, "Coca Cola",drinksBackShelf,drinksFrontShelf);
        applicationService.addItem("Rehov 2, Haifa", "Sprite 2L", "0#1#1#3", 10, 20, 50, "Coca Cola",drinksBackShelf,drinksFrontShelf);
        //11
        applicationService.addItem("Rehov 2, Haifa", "Iced tea 1L", "0#1#2#1", 6, 20, 50, "Nes-Tea",drinksBackShelf,drinksFrontShelf);
        applicationService.addItem("Rehov 2, Haifa", "Iced tea 1.5L", "0#1#2#2", 8, 20, 50, "Nes-Tea",drinksBackShelf,drinksFrontShelf);
        applicationService.addItem("Rehov 2, Haifa", "Iced tea 2L", "0#1#2#3", 10, 20, 50, "Nes-Tea",drinksBackShelf,drinksFrontShelf);
        //14
        applicationService.addItem("Rehov 2, Haifa", "JagerMeister Liquor", "0#1#3#1", 70, 5, 20, "Jager",drinksBackShelf,drinksFrontShelf);
        t= applicationService.addItem("Rehov 2, Haifa", "FireBall Whisky", "0#1#3#1", 70, 5, 20, "FireBall",drinksBackShelf,drinksFrontShelf);
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
    }

    private void loadItemsDiscountData() {
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue();
        int currentDay = now.getDayOfMonth();
        Response t= applicationService.addDiscount("Yasmin 3, Dimona", 14, "JagerMeister Discount Check (30 from today)", LocalDate.of(currentYear, currentMonth, currentDay), LocalDate.of(2023, 1, 1), 0.3);
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        applicationService.addDiscount("Yasmin 3, Dimona", 1, "Bamba Big Discount Check (50 from today)", LocalDate.of(currentYear, currentMonth, currentDay), LocalDate.of(2022, 7, 1), 0.75);
        applicationService.addDiscount("Yasmin 3, Dimona", 8, "Sprite summer Discount", LocalDate.of(currentYear, currentMonth, currentDay), LocalDate.of(2022, 7, 1), 0.5);
        applicationService.addDiscount("Yasmin 3, Dimona", 9, "Sprite summer Discount", LocalDate.of(currentYear, currentMonth, currentDay), LocalDate.of(2022, 7, 1), 0.5);
        t=applicationService.addDiscount("Yasmin 3, Dimona", 10, "Sprite summer Discount", LocalDate.of(currentYear, currentMonth, currentDay), LocalDate.of(2022, 7, 1), 0.5);
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        applicationService.addDiscount("Yasmin 3, Dimona", 3, "Bisli Big Future Discount", LocalDate.of(2022, 7, 1), LocalDate.of(2022, 7, 20), 0.75);

        t=applicationService.addDiscount("Rehov 2, Haifa", 14, "JagerMeister Discount Check (30 from today)", LocalDate.of(currentYear, currentMonth, currentDay), LocalDate.of(2023, 1, 1), 0.3);
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());applicationService.addDiscount("Rehov 2, Haifa", 1, "Bamba Big Discount Check (50 from today)", LocalDate.of(currentYear, currentMonth, currentDay), LocalDate.of(2022, 7, 1), 0.75);
        applicationService.addDiscount("Rehov 2, Haifa", 8, "Sprite summer Discount", LocalDate.of(currentYear, currentMonth, currentDay), LocalDate.of(2022, 7, 1), 0.5);
        applicationService.addDiscount("Rehov 2, Haifa", 9, "Sprite summer Discount", LocalDate.of(currentYear, currentMonth, currentDay), LocalDate.of(2022, 7, 1), 0.5);
        t=applicationService.addDiscount("Rehov 2, Haifa", 10, "Sprite summer Discount", LocalDate.of(currentYear, currentMonth, currentDay), LocalDate.of(2022, 7, 1), 0.5);
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        applicationService.addDiscount("Rehov 2, Haifa", 3, "Bisli Big Future Discount", LocalDate.of(2022, 7, 1), LocalDate.of(2022, 7, 20), 0.75);

    }

    private void loadQuantityForItemsData() {
        Response t = applicationService.addQuantityToItem("Yasmin 3, Dimona", 1, 30, true);
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        applicationService.addQuantityToItem("Yasmin 3, Dimona", 1, 20, false);
        applicationService.addQuantityToItem("Yasmin 3, Dimona", 2, 30, true);
        applicationService.addQuantityToItem("Yasmin 3, Dimona", 2, 20, false);
        applicationService.addQuantityToItem("Yasmin 3, Dimona", 3, 30, true);
        applicationService.addQuantityToItem("Yasmin 3, Dimona", 3, 20, false);
        applicationService.addQuantityToItem("Yasmin 3, Dimona", 4, 30, true);
        applicationService.addQuantityToItem("Yasmin 3, Dimona", 4, 20, false);
        applicationService.addQuantityToItem("Yasmin 3, Dimona", 5, 40, true);
        applicationService.addQuantityToItem("Yasmin 3, Dimona", 5, 40, false);
        applicationService.addQuantityToItem("Yasmin 3, Dimona", 6, 40, true);
        applicationService.addQuantityToItem("Yasmin 3, Dimona", 6, 40, false);
        t= applicationService.addQuantityToItem("Yasmin 3, Dimona", 7, 40, true);
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());applicationService.addQuantityToItem("Yasmin 3, Dimona", 7, 40, false);
        applicationService.addQuantityToItem("Yasmin 3, Dimona", 8, 30, true);
        applicationService.addQuantityToItem("Yasmin 3, Dimona", 8, 20, false);
        applicationService.addQuantityToItem("Yasmin 3, Dimona", 9, 30, true);
        applicationService.addQuantityToItem("Yasmin 3, Dimona", 9, 20, false);
        applicationService.addQuantityToItem("Yasmin 3, Dimona", 10, 30, true);
        applicationService.addQuantityToItem("Yasmin 3, Dimona", 10, 20, false);
        applicationService.addQuantityToItem("Yasmin 3, Dimona", 11, 30, true);
        applicationService.addQuantityToItem("Yasmin 3, Dimona", 11, 20, false);
        applicationService.addQuantityToItem("Yasmin 3, Dimona", 12, 30, true);
        applicationService.addQuantityToItem("Yasmin 3, Dimona", 12, 20, false);
        applicationService.addQuantityToItem("Yasmin 3, Dimona", 13, 30, true);
        applicationService.addQuantityToItem("Yasmin 3, Dimona", 13, 20, false);
        applicationService.addQuantityToItem("Yasmin 3, Dimona", 14, 15, true);
        applicationService.addQuantityToItem("Yasmin 3, Dimona", 14, 5, false);
        applicationService.addQuantityToItem("Yasmin 3, Dimona", 15, 15, true);
        applicationService.addQuantityToItem("Yasmin 3, Dimona", 15, 5, false);

        t=applicationService.addQuantityToItem("Rehov 2, Haifa", 1, 30, true);
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        applicationService.addQuantityToItem("Rehov 2, Haifa", 1, 20, false);
        applicationService.addQuantityToItem("Rehov 2, Haifa", 2, 30, true);
        applicationService.addQuantityToItem("Rehov 2, Haifa", 2, 20, false);
        applicationService.addQuantityToItem("Rehov 2, Haifa", 3, 30, true);
        applicationService.addQuantityToItem("Rehov 2, Haifa", 3, 20, false);
        applicationService.addQuantityToItem("Rehov 2, Haifa", 4, 30, true);
        applicationService.addQuantityToItem("Rehov 2, Haifa", 4, 20, false);
        applicationService.addQuantityToItem("Rehov 2, Haifa", 5, 40, true);
        applicationService.addQuantityToItem("Rehov 2, Haifa", 5, 40, false);
        applicationService.addQuantityToItem("Rehov 2, Haifa", 6, 40, true);
        applicationService.addQuantityToItem("Rehov 2, Haifa", 6, 40, false);
        t= applicationService.addQuantityToItem("Rehov 2, Haifa", 7, 40, true);
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        applicationService.addQuantityToItem("Rehov 2, Haifa", 7, 40, false);
        applicationService.addQuantityToItem("Rehov 2, Haifa", 8, 30, true);
        applicationService.addQuantityToItem("Rehov 2, Haifa", 8, 20, false);
        applicationService.addQuantityToItem("Rehov 2, Haifa", 9, 30, true);
        applicationService.addQuantityToItem("Rehov 2, Haifa", 9, 20, false);
        applicationService.addQuantityToItem("Rehov 2, Haifa", 10, 30, true);
        applicationService.addQuantityToItem("Rehov 2, Haifa", 10, 20, false);
        applicationService.addQuantityToItem("Rehov 2, Haifa", 11, 30, true);
        applicationService.addQuantityToItem("Rehov 2, Haifa", 11, 20, false);
        applicationService.addQuantityToItem("Rehov 2, Haifa", 12, 30, true);
        applicationService.addQuantityToItem("Rehov 2, Haifa", 12, 20, false);
        applicationService.addQuantityToItem("Rehov 2, Haifa", 13, 30, true);
        applicationService.addQuantityToItem("Rehov 2, Haifa", 13, 20, false);
        applicationService.addQuantityToItem("Rehov 2, Haifa", 14, 15, true);
        t=applicationService.addQuantityToItem("Rehov 2, Haifa", 14, 5, false);
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        applicationService.addQuantityToItem("Rehov 2, Haifa", 15, 15, true);
        applicationService.addQuantityToItem("Rehov 2, Haifa", 15, 5, false);
    }

    private void loadDamagedRecordData() {
        Response t=applicationService.addDamagedRecord("Yasmin 3, Dimona", 4, 10,  FDamageReason.Damaged, 1);
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        t=applicationService.addDamagedRecord("Yasmin 3, Dimona", 2, 10,  FDamageReason.Expired, 0);
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        t=applicationService.addDamagedRecord("Rehov 2, Haifa", 4, 10,  FDamageReason.Damaged, 1);
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        t=applicationService.addDamagedRecord("Rehov 2, Haifa", 2, 10,  FDamageReason.Expired, 0);
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
    }

    private void loadSupplierData() {
        loadSuppliersData();
        loadSupplierItemsData();
        loadSupplierItemsDiscountData();
    }

    private void loadSuppliersData() {
        Set<DayOfWeek> dayOfWeekSet = new HashSet<>();
        dayOfWeekSet.add(DayOfWeek.SUNDAY);
        dayOfWeekSet.add(DayOfWeek.WEDNESDAY);
        Set<DayOfWeek> dayOfWeekSet2 = new HashSet<>();
        dayOfWeekSet2.add(DayOfWeek.MONDAY);
        Response t= applicationService.addSupplier("Coca Cola", 1234, 1111, true, "CreditCard", dayOfWeekSet, "30 yaakov st.", "South");
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        applicationService.addSupplier("Osem", 4321, 2222, true, "Cash", dayOfWeekSet2, "23 david st.", "North");
        t=applicationService.addContact(1234, "Eli Kurin", "0542888449", "eli@gmail.com");
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        applicationService.addContact(4321, "Yoav Avital", "0542658742", "yoavital98@gmail.com");
    }

    private void loadSupplierItemsData() {
        //Osem
        Response t= applicationService.addItemToSupplier("Yasmin 3, Dimona",4321, 1, 1111, 8, "Bamba Big");
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        applicationService.addItemToSupplier("Yasmin 3, Dimona",4321, 2, 1112, 3, "Bamba Small");
        t=applicationService.addItemToSupplier("Yasmin 3, Dimona",4321, 3, 1113, 8, "Bisli Big");
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        applicationService.addItemToSupplier("Yasmin 3, Dimona",4321, 4, 1114, 3, "Bisli Small");
        //Coca Cola
        applicationService.addItemToSupplier("Yasmin 3, Dimona",1234, 5, 2221, 3, "Coca Cola 1L");
        applicationService.addItemToSupplier("Yasmin 3, Dimona",1234, 6, 2222, 4, "Coca Cola 1.5L");
        t=applicationService.addItemToSupplier("Yasmin 3, Dimona",1234, 7, 2223, 5, "Coca Cola 2L");
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        applicationService.addItemToSupplier("Yasmin 3, Dimona",1234, 8, 2224, 3, "Sprite 1L");
        applicationService.addItemToSupplier("Yasmin 3, Dimona",1234, 9, 2225, 4, "Sprite 1.5L");
        applicationService.addItemToSupplier("Yasmin 3, Dimona",1234, 10, 2226, 5, "Sprite 2L");
        t=applicationService.addItemToSupplier("Yasmin 3, Dimona",1234, 11, 2227, 3, "Iced Tea 1L");
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        applicationService.addItemToSupplier("Yasmin 3, Dimona",1234, 12, 2228, 4, "Iced Tea 1.5L");
        applicationService.addItemToSupplier("Yasmin 3, Dimona",1234, 13, 2229, 5, "Iced Tea 2L");
        applicationService.addItemToSupplier("Yasmin 3, Dimona",1234, 14, 2230, 40, "JagerMeister Liquor");
        applicationService.addItemToSupplier("Yasmin 3, Dimona",1234, 15, 2231, 40, "FireBall Whisky");

        //Osem
        applicationService.addItemToSupplier("Rehov 2, Haifa",4321, 1, 1111, 8, "Bamba Big");
        t=applicationService.addItemToSupplier("Rehov 2, Haifa",4321, 2, 1112, 3, "Bamba Small");
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        applicationService.addItemToSupplier("Rehov 2, Haifa",4321, 3, 1113, 8, "Bisli Big");
        applicationService.addItemToSupplier("Rehov 2, Haifa",4321, 4, 1114, 3, "Bisli Small");
        //Coca Cola
        applicationService.addItemToSupplier("Rehov 2, Haifa",1234, 5, 2221, 3, "Coca Cola 1L");
        applicationService.addItemToSupplier("Rehov 2, Haifa",1234, 6, 2222, 4, "Coca Cola 1.5L");
        t=applicationService.addItemToSupplier("Rehov 2, Haifa",1234, 7, 2223, 5, "Coca Cola 2L");
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        applicationService.addItemToSupplier("Rehov 2, Haifa",1234, 8, 2224, 3, "Sprite 1L");
        applicationService.addItemToSupplier("Rehov 2, Haifa",1234, 9, 2225, 4, "Sprite 1.5L");
        applicationService.addItemToSupplier("Rehov 2, Haifa",1234, 10, 2226, 5, "Sprite 2L");
        applicationService.addItemToSupplier("Rehov 2, Haifa",1234, 11, 2227, 3, "Iced Tea 1L");
        t=applicationService.addItemToSupplier("Rehov 2, Haifa",1234, 12, 2228, 4, "Iced Tea 1.5L");
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        applicationService.addItemToSupplier("Rehov 2, Haifa",1234, 13, 2229, 5, "Iced Tea 2L");
        applicationService.addItemToSupplier("Rehov 2, Haifa",1234, 14, 2230, 40, "JagerMeister Liquor");
        applicationService.addItemToSupplier("Rehov 2, Haifa",1234, 15, 2231, 40, "FireBall Whisky");
    }

    private void loadSupplierItemsDiscountData() {
        Response t=applicationService.addItemDiscountAccordingToAmount("Yasmin 3 , Dimona", 1234, 11, 15, 50); //promoting Iced Tea
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        applicationService.addItemDiscountAccordingToAmount("Yasmin 3 , Dimona", 1234, 12, 15, 50); //promoting Iced Tea
        t=applicationService.addItemDiscountAccordingToAmount("Yasmin 3 , Dimona", 1234, 13, 15, 50); //promoting Iced Tea
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        applicationService.addItemDiscountAccordingToAmount("Yasmin 3 , Dimona", 4321, 1, 20, 25); //discount on Bamba Big 25% if more than 20
        applicationService.addItemDiscountAccordingToAmount("Yasmin 3 , Dimona", 4321, 3, 20, 25); //discount on Bisli Big 25% if more than 20
    }

    private void loadEmployeeData() {
        loadEmployeesData();
        loadJobConstraintsData();
        loadBranchesData();
        loadShiftsData();
    }

    private void loadEmployeesData() {
        Response t=applicationService.addEmployee("Mankal", 1, "bank1", 1000000, "2010-01-01", "CEO", "employmentDetails1","0144363655");
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        applicationService.addEmployee("cash0", 100, "bank1", 5000, "2022-01-01", "cashier", "employmentDetails100","0244363655");
        applicationService.addEmployee("cash1", 101, "bank1", 5000, "2022-01-01", "cashier", "employmentDetails101","0344363655");
        applicationService.addEmployee("cash2", 102, "bank1", 5000, "2022-01-01", "cashier", "employmentDetails102","0444363655");
        t=applicationService.addEmployee("cash3", 103, "bank1", 5000, "2022-01-01", "cashier", "employmentDetails103","0544363655");
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        applicationService.addEmployee("usher0", 200, "bank1", 6000, "2022-01-01", "usher", "employmentDetails200","0644363655");
        applicationService.addEmployee("usher1", 201, "bank1", 6000, "2022-01-01", "usher", "employmentDetails201","0744363655");
        applicationService.addEmployee("usher2", 202, "bank1", 6000, "2022-01-01", "usher", "employmentDetails202","0844363655");
        applicationService.addEmployee("usher3", 203, "bank1", 6000, "2022-01-01", "usher", "employmentDetails203","0944363655");
        applicationService.addEmployee("ware0", 300, "bank1", 7000, "2022-01-01", "warehouse", "employmentDetails300","0044363655");
        applicationService.addEmployee("ware1", 301, "bank1", 7000, "2022-01-01", "warehouse", "employmentDetails301","0514363655");
        applicationService.addEmployee("ware2", 302, "bank1", 7000, "2022-01-01", "warehouse", "employmentDetails302","0524363655");
        applicationService.addEmployee("ware3", 303, "bank1", 7000, "2022-01-01", "warehouse", "employmentDetails303","0534363655");
        applicationService.addEmployee("shify0", 400, "bank1", 9000, "2022-01-01", "shift manager", "employmentDetails400","0554363655");
        applicationService.addEmployee("shify1", 401, "bank1", 9000, "2022-01-01", "shift manager", "employmentDetails401","0564363655");
        t=applicationService.addEmployee("shify2", 402, "bank1", 9000, "2022-01-01", "shift manager", "employmentDetails402","0574363655");
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        applicationService.addEmployee("shify3", 403, "bank1", 9000, "2022-01-01", "shift manager", "employmentDetails403","0584363655");
        applicationService.addEmployee("hairy0", 500, "bank1", 12000, "2022-01-01", "hr", "employmentDetails500","0594363655");
        applicationService.addEmployee("hairy1", 501, "bank1", 12000, "2022-01-01", "hr", "employmentDetails501","0544313655");
        applicationService.addEmployee("driver0", 600, "bank1", 8000, "2022-01-01", "driver", "employmentDetails600", new Vector<>(Arrays.asList("C_E")), "0524361655");
        applicationService.addEmployee("driver1", 601, "bank1", 8000, "2022-01-01", "driver", "employmentDetails601", new Vector<>(Arrays.asList("C_E")),"0544362255");
        applicationService.addEmployee("driver2", 602, "bank1", 8000, "2022-01-01", "driver", "employmentDetails602","0544363455");
        t=applicationService.addEmployee("driver3", 603, "bank1", 8000, "2022-01-01", "driver", "employmentDetails603","0544366655");
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        applicationService.addEmployee("branchManager0", 700, "bank1", 5000, "2022-01-01", "branch manager", "employmentDetails700","0544223655");
        applicationService.addEmployee("branchManager1", 701, "bank1", 5000, "2022-01-01", "branch manager", "employmentDetails701","0544233655");
        applicationService.addEmployee("branchManager2", 702, "bank1", 5000, "2022-01-01", "branch manager", "employmentDetails702","05443363655");
        applicationService.addEmployee("branchManager3", 703, "bank1", 5000, "2022-01-01", "branch manager", "employmentDetails703","0545663655");
        applicationService.addEmployee("deliveryManager0", 800, "bank1", 5000, "2022-01-01", "logistics", "employmentDetails800","0544367855");
        t=applicationService.addEmployee("deliveryManager1", 801, "bank1", 5000, "2022-01-01", "logistics", "employmentDetails801","0544368855");
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        applicationService.addEmployee("deliveryManager2", 802, "bank1", 5000, "2022-01-01", "logistics", "employmentDetails802","0544369655");
        applicationService.addEmployee("deliveryManager3", 803, "bank1", 5000, "2022-01-01", "logistics", "employmentDetails803","0544369999");
    }

    private void loadJobConstraintsData() {
        Response t=applicationService.addJobConstraint(101, "2023-01-07","evening");
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        t=applicationService.addJobConstraint(201, "2023-01-07","evening");
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        applicationService.addJobConstraint(301, "2023-01-07","evening");
        t=applicationService.addJobConstraint(401, "2023-01-07","evening");
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        applicationService.addJobConstraint(601, "2023-01-07","evening");
    }

    private void loadBranchesData() {
        Response t=applicationService.addbranch("Ringelblum 9, Beersheba","Center",1);
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        t=applicationService.addbranch("Yasmin 3, Dimona","South",700);
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        applicationService.addbranch("Rehov 2, Haifa","North",701);

    }

    private void loadShiftsData() {
        HashMap<Integer, String> jobMap1 = new HashMap<>();
        jobMap1.put(101, "cashier"); jobMap1.put(201, "usher"); jobMap1.put(600, "driver"); jobMap1.put(301, "warehouse");jobMap1.put(401, "shift manager");
        Response t=applicationService.addShift("Ringelblum 9, Beersheba","2023-01-07", "morning", jobMap1);
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());

        HashMap<Integer, String> jobMap2 = new HashMap<>();
        jobMap2.put(100, "cashier"); jobMap2.put(200, "usher"); jobMap2.put(300, "warehouse");jobMap2.put(400, "shift manager");
        t=applicationService.addShift("Yasmin 3, Dimona","2023-01-07", "morning", jobMap2);
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        t=applicationService.addShift("Ringelblum 9, Beersheba","2023-02-07", "morning", jobMap1);
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
        applicationService.addShift("Yasmin 3, Dimona","2023-02-07", "morning", jobMap2);
        t=applicationService.addShift("Rehov 2, Haifa","2023-02-07", "morning", jobMap2);
        if(t.errorOccurred())
            System.out.println(t.getErrorMessage());
    }

    private void loadDeliveriesData() throws Exception {
        applicationService.getApplicationFacade().getDeliveryFacade().getTruckController().addTruck("0372", "Kia", 5000, 10000);
        applicationService.getApplicationFacade().getDeliveryFacade().getTruckController().addTruck("7766", "Ford", 4500, 9000);
        applicationService.getApplicationFacade().getDeliveryFacade().getTruckController().addTruck("3434", "Mercedes", 6000, 12000);
        applicationService.getApplicationFacade().getDeliveryFacade().getTruckController().addTruck("1834", "Volvo", 4800, 10000);
        applicationService.getApplicationFacade().getDeliveryFacade().getTruckController().addTruck("7790", "Toyota", 7500, 15000);
        applicationService.getApplicationFacade().getDeliveryFacade().getTruckController().addTruck("0175", "Mazda", 7000, 14000);
        applicationService.getApplicationFacade().getDeliveryFacade().getTruckController().addTruck("0578", "Ford", 4500, 9000);
        applicationService.getApplicationFacade().getDeliveryFacade().getTruckController().addTruck("1043", "Mercedes", 6000, 12000);
        applicationService.getApplicationFacade().getDeliveryFacade().getTruckController().addTruck("9966", "Ford", 4500, 9000);
        applicationService.getApplicationFacade().getDeliveryFacade().getTruckController().addTruck("0420", "Honda", 2000, 3000);
    }

    Runnable notifier = () -> System.out.print(".");

    public void deleteDataForTests() {
        applicationService.deleteDataForTests();
    }
}
