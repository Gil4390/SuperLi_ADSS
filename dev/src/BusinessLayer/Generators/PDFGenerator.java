package BusinessLayer.Generators;

import BusinessLayer.DeliveryModule.Objects.Delivery;
import BusinessLayer.SupplierBusiness.Order;
import BusinessLayer.SupplierBusiness.OrderProduct;
import BusinessLayer.SupplierBusiness.Supplier;
import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.datatable.DataTable;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PDFGenerator {
    private static class PDFGeneratorHolder{
        private final static PDFGenerator instance = new PDFGenerator();
    }

    public static PDFGenerator getInstance() {
        return PDFGeneratorHolder.instance;
    }

    private final Logger logger;
    private PDDocument mainDocument;
    private PDPage myPage;
    private final float margin = 50;
    private final float bottomMargin = 70;
    private float yStartNewPage;
    private float tableWidth;
    private float yStart;

    private PDFGenerator() {
        logger = LoggerFactory.getLogger(PDFGenerator.class);
        try {
            Files.createDirectories(Paths.get((new File("").getAbsolutePath()).concat("\\docs\\orders")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createOrderPDF(Supplier supplier, Order order) throws IOException {
        preparePDF("Order details");
        createSupplierTable(supplier, order);
        createOrderTable(order);
        savePDF((new File("").getAbsolutePath()).concat("\\docs\\orders\\"+order.getOrderId()+".pdf"));
    }

    private void savePDF(String filePath) throws IOException {
        mainDocument.addPage(myPage);
        mainDocument.save(filePath);
        mainDocument.close();
    }

    private void preparePDF(String tableName) throws IOException {
        mainDocument = new PDDocument();
        myPage = new PDPage(PDRectangle.A4);
        yStartNewPage = myPage.getMediaBox().getHeight() - (2 * margin);
        tableWidth = myPage.getMediaBox().getWidth() - (2 * margin);
        yStart = yStartNewPage;
        PDPageContentStream contentStream = new PDPageContentStream(mainDocument, myPage);
        String pathToBackground = (new File("").getAbsolutePath()).concat("\\docs\\superlilogo.png");
        File fi = new File(pathToBackground);
        byte[] imageBytes = Files.readAllBytes(fi.toPath());
        PDImageXObject image = PDImageXObject.createFromByteArray(mainDocument, imageBytes, "background");
        contentStream.drawImage(image, 0, 400);
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 22);
        contentStream.newLineAtOffset(225, 750);
        contentStream.showText(tableName);
        contentStream.endText();
        contentStream.close();
    }

    public void createTruckingPDF(Supplier supplier, Order order, Delivery delivery) throws IOException {
        preparePDF("Trucking details");
        createTruckingTable(supplier, order, delivery);
        createTruckingOrderTable(order);
        savePDF((new File("").getAbsolutePath()).concat("\\docs\\trucks\\"+order.getOrderId()+".pdf"));
    }

    private void createTruckingOrderTable(Order order) throws IOException {
        List<List> data = new ArrayList();
        data.add(new ArrayList<>(
                Arrays.asList("Item name", "Amount")));
        for (OrderProduct orderProduct : order.getOrderProducts().values())
            data.add(new ArrayList<>(
                    Arrays.asList(orderProduct.getName(), orderProduct.getAmount())));
        BaseTable dataTable = new BaseTable(yStart-100, yStartNewPage, bottomMargin, tableWidth, margin, mainDocument, myPage, true, true);
        DataTable t = new DataTable(dataTable, myPage);
        t.addListToTable(data, DataTable.HASHEADER);
        dataTable.draw();
    }

    private void createTruckingTable(Supplier supplier, Order order, Delivery delivery) { }

    private void createSupplierTable(Supplier supplier, Order order) throws IOException {
        List<List> data = new ArrayList<>();
        data.add(new ArrayList<>(
                Arrays.asList("Supplier name", "Supplier number", "Supplier Address", "Order date", "Order number", "Contact phone number")));
        data.add(new ArrayList<>(
                Arrays.asList(supplier.getName(), supplier.getBusinessNumber(), supplier.getAddress(),
                        order.getOrderDate(), order.getOrderId(), supplier.getAllContacts().get(0).getPhoneNumber())));
        BaseTable dataTable = new BaseTable(yStart-10, yStartNewPage, bottomMargin, tableWidth, margin, mainDocument, myPage, true, true);
        DataTable t = new DataTable(dataTable, myPage);
        t.addListToTable(data, DataTable.HASHEADER);
        dataTable.draw();
    }
    private void createOrderTable(Order order) throws IOException {
        List<List> data = new ArrayList<>();
        data.add(new ArrayList<>(
                Arrays.asList("Item id", "Item name", "Amount", "Item price", "Discount", "Total price")));
        for (OrderProduct orderProduct : order.getOrderProducts().values())
            data.add(new ArrayList<>(
                    Arrays.asList(orderProduct.getProductId(), orderProduct.getName(), orderProduct.getAmount(),
                            orderProduct.getSingleItemPrice(), orderProduct.getDiscount(), orderProduct.getPriceAfterDiscount())));
        BaseTable dataTable = new BaseTable(yStart-48, yStartNewPage, bottomMargin, tableWidth, margin, mainDocument, myPage, true, true);
        DataTable t = new DataTable(dataTable, myPage);
        t.addListToTable(data, DataTable.HASHEADER);
        dataTable.draw();
    }
}
