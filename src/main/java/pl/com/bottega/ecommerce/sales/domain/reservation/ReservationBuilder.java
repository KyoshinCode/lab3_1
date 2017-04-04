package pl.com.bottega.ecommerce.sales.domain.reservation;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;

import java.util.Date;

/**
 * Created by Patryk Wierzyński
 */

public class ReservationBuilder {

	private Id id = Id.generate();
	private Reservation.ReservationStatus status = Reservation.ReservationStatus.CLOSED;
	private ClientData clientData = new ClientData(Id.generate(), "defaultClient");
	private Date date = new Date();

	public ReservationBuilder withId(Id id) {
		this.id = id;
		return this;
	}

	public ReservationBuilder opened() {
		this.status = Reservation.ReservationStatus.OPENED;
		return this;
	}

	public ReservationBuilder closed() {
		this.status = Reservation.ReservationStatus.CLOSED;
		return this;
	}

	public ReservationBuilder withClient(ClientData clientData) {
		this.clientData = clientData;
		return this;
	}

	public ReservationBuilder withDate(Date date) {
		this.date = date;
		return this;
	}

	public Reservation build() {
		return new Reservation(id, status, clientData, date);
	}

}
