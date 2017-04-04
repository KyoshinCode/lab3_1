package pl.com.bottega.ecommerce.sales.application.api.handler;


import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;

import java.util.Date;

public class ReservationBuilder {
    private Id id = Id.generate();
    private Reservation.ReservationStatus status;
    private ClientData clientData;
    private Date createDate;

    public ReservationBuilder id(Id id) {
        this.id = id;
        return this;
    }
    public ReservationBuilder reservationStatus(Reservation.ReservationStatus status) {
        this.status = status;
        return this;
    }
    public ReservationBuilder clientData(ClientData clientData) {
        this.clientData = clientData;
        return this;
    }
    public ReservationBuilder date(Date date) {
        this.createDate = date;
        return this;
    }
    public Reservation build() {
        Reservation reservation = new Reservation(id, status, clientData, createDate);
        return reservation;
    }

}