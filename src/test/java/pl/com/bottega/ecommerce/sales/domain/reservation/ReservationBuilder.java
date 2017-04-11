package pl.com.bottega.ecommerce.sales.domain.reservation;

import pl.com.bottega.ddd.support.domain.BaseAggregateRoot;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;

import java.util.ArrayList;
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
}
