package pl.com.bottega.ecommerce.sales.domain.reservation;

import java.util.Date;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation.ReservationStatus;

public class StubReservationRepositry implements ReservationRepository{
	@Override
	public void save(Reservation reservation) {
		
	}
	
	@Override
	public Reservation load(Id reservationId) {
		Id aggregateId = Id.generate();
		ClientData clientData = new ClientData(Id.generate(), "John Doe");
		Date createDate = new Date();
		
		return new Reservation(aggregateId, ReservationStatus.OPENED, clientData, createDate);
	}

}
