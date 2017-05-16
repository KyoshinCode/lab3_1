package pl.com.bottega.ecommerce.sales.domain.reservation;

import java.util.Date;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientDataBuilder;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation.ReservationStatus;

public class ReservationBuilder {
	private Id aggregateId;
	private ReservationStatus status;
	private ClientData clientData;
	private Date createDate;

	public ReservationBuilder() {
		aggregateId = Id.generate();
		status = ReservationStatus.OPENED;
		clientData = new ClientDataBuilder().build();
		createDate = new Date();
	}
	
	public ReservationBuilder withId(Id aggregateId) {
		this.aggregateId = aggregateId;
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
		return new Reservation(aggregateId, status, clientData, createDate);
	}

}