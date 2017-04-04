package pl.com.bottega.ecommerce.sales.domain.reservation;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;

import java.util.Date;

import static org.mockito.Mockito.mock;

/**
 * Created by Wojciech Szczepaniak on 04.04.2017.
 */
public class ReservationFactory {

    public Reservation createExampleReservation() {
        return new Reservation(Id.generate(), Reservation.ReservationStatus.OPENED, mock(ClientData.class), mock(Date.class));
    }
}
