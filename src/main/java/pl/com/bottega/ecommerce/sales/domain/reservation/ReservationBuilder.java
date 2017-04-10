package pl.com.bottega.ecommerce.sales.domain.reservation;

import java.sql.Date;
import java.util.List;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;

public class ReservationBuilder {

	private Reservation.ReservationStatus status;
	private ClientData client;
	private Date date;
	
	
	private ReservationBuilder(){
		
	}
	
	public ReservationBuilder withStatus(Reservation.ReservationStatus status) {
		this.status = status;
		return this;
	}
	

	
	public ReservationBuilder withClientData(ClientData client){
		this.client = client;
		return this;
	}
	
	public ReservationBuilder withDate(Date date){
		this.date = date;
		return this;
	}
	
	public Reservation build(){
		return new Reservation(Id.generate(),status,client,date);
	}
	
}
