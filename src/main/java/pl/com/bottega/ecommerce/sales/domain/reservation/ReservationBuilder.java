package pl.com.bottega.ecommerce.sales.domain.reservation;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;

import java.util.Date;

/**
 * Created by Piotrek on 02.04.2017.
 */
public class ReservationBuilder {
    private Reservation.ReservationStatus status;
    private ClientData clientData;
    private Date createDate;
    private Id id = Id.generate();

    private ReservationBuilder() {
    }

    public ReservationBuilder reservationBuilder(){
        return new ReservationBuilder();
    }

    public ReservationBuilder withId(Id id) {
        this.id = id;
        return this;
    }

    public ReservationBuilder withStatus(Reservation.ReservationStatus status) {
        this.status = status;
        return this;
    }

    public ReservationBuilder withClientData(ClientData clientData) {
        this.clientData = clientData;
        return this;
    }

    public ReservationBuilder withDate(Date createDate) {
        this.createDate = createDate;
        return this;
    }

    public Reservation build(){
        Reservation reservation = new Reservation(id, status, clientData, createDate);
        return reservation;
    }
}
