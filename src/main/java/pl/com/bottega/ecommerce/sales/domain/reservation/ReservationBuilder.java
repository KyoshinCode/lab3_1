package pl.com.bottega.ecommerce.sales.domain.reservation;

import java.util.Date;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation.ReservationStatus;

public class ReservationBuilder {
	private Id id = Id.generate();
	private ReservationStatus status = ReservationStatus.CLOSED;
	private ClientData clientData = new ClientData(Id.generate(), "tKlient");
	private Date createDate = new Date();

	public ReservationBuilder withId(Id id) {
		this.id = id;
		return this;
	}
	
	public ReservationBuilder withStatus(ReservationStatus status) {
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
		return new Reservation(id, status, clientData, createDate);
	}
}
