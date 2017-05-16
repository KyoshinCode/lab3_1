package pl.com.bottega.ecommerce.sales.domain.reservation;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;

import java.util.Date;

/**
 * Created by Konrad Gos on 02.04.2017.
 */
public class ReservationBuilder {
    private Id id = Id.generate();
    private Reservation.ReservationStatus status = Reservation.ReservationStatus.OPENED;
    private ClientData clientData = new ClientData(Id.generate(),"Adam");
    private Date createDate = new Date();

    public ReservationBuilder() {
    }

    public ReservationBuilder Reservation() {
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
        return new Reservation(id,status, clientData, createDate);
    }
}
