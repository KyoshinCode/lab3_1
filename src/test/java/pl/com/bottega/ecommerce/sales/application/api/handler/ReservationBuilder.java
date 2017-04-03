package pl.com.bottega.ecommerce.sales.application.api.handler;


import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;

import java.util.Date;

public class ReservationBuilder {
    private Id id = Id.generate();
    private Reservation.ReservationStatus reservationStatus;
    private ClientData clientData;
    private Date date;

    public ReservationBuilder id(Id id) {
        this.id = id;
        return this;
    }
    public ReservationBuilder reservationStatus(Reservation.ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
        return this;
    }
    public ReservationBuilder clientData(ClientData clientData) {
        this.clientData = clientData;
        return this;
    }
    public ReservationBuilder date(Date date) {
        this.date = date;
        return this;
    }
    public Reservation build() {
        Reservation reservation = new Reservation(id, reservationStatus, clientData, date);
        return reservation;
    }

}
