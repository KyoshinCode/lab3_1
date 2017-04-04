package pl.com.bottega.ecommerce.sales.application.api.handler;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;

import java.util.Date;

/**
 * Created by pebuls on 03.04.17.
 */
public class ReservationBuilder {

    private ClientData data;
    private Reservation.ReservationStatus status;
    private Id id = Id.generate();

    public ReservationBuilder closed() {
        status = Reservation.ReservationStatus.CLOSED;
        return this;
    }

    public ReservationBuilder opened() {
        status = Reservation.ReservationStatus.OPENED;
        return this;
    }

    public ReservationBuilder withClient(ClientData data) {
        this.data = data;
        return this;
    }

    public ReservationBuilder withId(Id id) {
        this.id = id;
        return this;
    }

    public Reservation build() {
        return new Reservation(id, status, data, new Date());
    }
}
