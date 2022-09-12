package Presentation.View;

public class Printer {
    private static class PrinterHolder{
        private final static Printer instance = new Printer();
    }

    public static Printer getInstance() {
        return PrinterHolder.instance;
    }

    private Printer(){
    }

    public void print(String msg){
        System.out.println(msg);
    }

}
