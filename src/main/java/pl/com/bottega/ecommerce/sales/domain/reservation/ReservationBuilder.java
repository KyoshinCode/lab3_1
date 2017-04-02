package pl.com.bottega.ecommerce.sales.domain.reservation;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;

import java.util.Date;

/**
 * Created by grusz on 02.04.2017.
 */
public final class ReservationBuilder {
    private Reservation.ReservationStatus status = Reservation.ReservationStatus.CLOSED;
    private ClientData clientData = new ClientData(Id.generate(),"Andrew");
    private Date createDate = new Date();
    private Id id = Id.generate();

    private ReservationBuilder() {
    }

    public static ReservationBuilder aReservation() {
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

    public ReservationBuilder withCreateDate(Date createDate) {
        this.createDate = createDate;
        return this;
    }



    public Reservation build() {
        Reservation reservation = new Reservation(id,status, clientData, createDate);
        return reservation;
    }
}
