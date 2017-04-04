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
	
	public ReservationBuilder withAggregateId(Id aggregateId) {
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
	
	public ReservationBuilder withDate(Date createDate) {
		this.createDate = createDate;
		return this;
	}
	
	public Reservation build() {
		return new Reservation(this.aggregateId, this.status, this.clientData, this.createDate);
	}
}
