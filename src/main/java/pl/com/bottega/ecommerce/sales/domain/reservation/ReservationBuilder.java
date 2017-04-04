package pl.com.bottega.ecommerce.sales.domain.reservation;

import java.util.Date;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;

public class ReservationBuilder {
	private Reservation.ReservationStatus rStat = null;
	private ClientData data;
	private Id id = Id.generate();
	
	public ReservationBuilder withClientData(ClientData data) {
		this.data = data;
		return this;
	}
	
	public ReservationBuilder withId(Id id) {
		this.id = id;
		return this;
	}
	
	public ReservationBuilder close() {
		rStat = Reservation.ReservationStatus.CLOSED;
		return this;
	}
	
	public ReservationBuilder open() {
		rStat = Reservation.ReservationStatus.OPENED;
		return this;
	}
	
	public Reservation build() {
		return new Reservation(id, rStat, data, new Date());
	}
}
