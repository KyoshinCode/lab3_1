package pl.com.bottega.ecommerce.sales.domain.reservation;


import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReservationBuilder {
    private Id id;
    private Reservation.ReservationStatus status = Reservation.ReservationStatus.OPENED;
    private List<ReservationItem> items = new ArrayList<ReservationItem>();
    private ClientData clientData;
    private Date createDate;

    public ReservationBuilder id(Id id) {
        this.id = id;
        return this;
    }
    public ReservationBuilder status(Reservation.ReservationStatus status){
        this.status = status;
        return this;
    }
    public ReservationBuilder clientData(ClientData clientData){
        this.status = status;
        return this;
    }
    public ReservationBuilder createDate(Date date){
        this.createDate = date;
        return this;
    }
    public Reservation build(){
        return new Reservation(id,status,clientData,createDate);
    }
}
