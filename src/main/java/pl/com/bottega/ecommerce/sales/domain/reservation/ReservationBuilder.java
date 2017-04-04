package pl.com.bottega.ecommerce.sales.domain.reservation;

import java.util.Date;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation.ReservationStatus;

public class ReservationBuilder {
	
	private Id aggregateId = Id.generate();
	private ReservationStatus status = ReservationStatus.OPENED;
	private ClientData clientData = new ClientData(Id.generate(), "Default Client");
	private Date createDate = new Date();
	
	public ReservationBuilder() {
	}
}
