package pl.com.bottega.ecommerce.sales.application.api.handler;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation.ReservationStatus;

public class AddProductCommandHandlerTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void stateTest() {
		AddProductCommand addProductCommand = new AddProductCommand(Id.generate(), Id.generate(), 5);
		Reservation reservation = new Reservation(Id.generate(), ReservationStatus.OPENED,
				new ClientData(Id.generate(), "Arleta"), new Date());
	}

}
