package pl.com.bottega.ecommerce.sales.domain.reservation;

import java.util.Date;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;

public class ReservationBuilder {

	private Id id = Id.generate();
    private Reservation.ReservationStatus reservationStatus;
    private ClientData clientData;
    private Date date = new Date();

    public Reservation build() {
        return new Reservation(id, reservationStatus, clientData, date);
    }

    public ReservationBuilder setId(Id id) {
        this.id = id;
        return this;
    }

    public ReservationBuilder setReservationStatus(Reservation.ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
        return this;
    }

    public ReservationBuilder setClientData(ClientData clientData) {
        this.clientData = clientData;
        return this;
    }

    public ReservationBuilder setDate(Date date) {
        this.date = date;
        return this;
    }
}
