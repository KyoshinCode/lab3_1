package pl.com.bottega.ecommerce.sales.domain.reservation;

import pl.com.bottega.ddd.support.domain.BaseAggregateRoot;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Wojciech Szczepaniak on 11.04.2017.
 */
public class ReservationBuilder {

    private Id id = Id.generate();
    private boolean removed = false;
    private Reservation.ReservationStatus status = Reservation.ReservationStatus.OPENED;
    private List<ReservationItem> items = new ArrayList<>();
    private ClientData clientData = new ClientData(Id.generate(), "Test");
    private Date createDate;

    public ReservationBuilder withId(Id id) {
        this.id = id;

        return this;
    }

    public ReservationBuilder removed(boolean value) {
        this.removed = value;

        return this;
    }

    public ReservationBuilder withReservationStatus(Reservation.ReservationStatus status) {
        this.status = status;

        return this;
    }

    public ReservationBuilder withItems(List<ReservationItem> items) {
        this.items = items;

        return this;
    }

    public ReservationBuilder withItem(ReservationItem... item) {
        this.items.addAll(Arrays.asList(item));

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
        Reservation reservation = new Reservation(id, status, clientData, createDate);

        for (ReservationItem item : items) {
            reservation.add(item.getProduct(), item.getQuantity());
        }
        if (removed) {
            reservation.markAsRemoved();
        }

        return reservation;
    }


}
