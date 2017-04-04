package pl.com.bottega.ecommerce.sales.application.api.handler;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;

import java.util.Date;

/**
 * Created by Sasho on 2017-04-02.
 */
public class ReservationBuilder {
    private ClientData clientData;
    private Reservation.ReservationStatus reservationStatus;
    private Id id = Id.generate();

    public ReservationBuilder opened() {
        reservationStatus = Reservation.ReservationStatus.OPENED;
        return this;
    }

    public ReservationBuilder closed() {
        reservationStatus = Reservation.ReservationStatus.CLOSED;
        return this;
    }

    public ReservationBuilder setClient(ClientData clientData) {
        this.clientData = clientData;
        return this;
    }

    public ReservationBuilder setId(Id id) {
        this.id = id;
        return this;
    }

    public Reservation build() {
        return new Reservation(id, reservationStatus, clientData, new Date());
    }
}
