package vttp2023.batch4.paf.assessment.bedandbreakfastapp.services;

public class BookingException extends Exception{
    public BookingException(){
        super();
    }

    public BookingException(String msg){
        super(msg);
    }
}
