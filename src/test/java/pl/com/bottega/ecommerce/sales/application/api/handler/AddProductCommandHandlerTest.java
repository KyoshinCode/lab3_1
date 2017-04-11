package pl.com.bottega.ecommerce.sales.application.api.handler;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation.ReservationStatus;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import pl.com.bottega.ecommerce.system.application.SystemContext;

@RunWith(MockitoJUnitRunner.class)
public class AddProductCommandHandlerTest {

	Id id;
	AddProductCommand command;
	ClientData clientData;
	Reservation reservation;
	Product product;

	@InjectMocks
	AddProductCommandHandler addProductCommandHandler = new AddProductCommandHandler();

	@Mock
	ReservationRepository reservationRepository;
	@Mock
	ProductRepository productRepository;
	@Mock
	ClientRepository clientRepository;
	@Mock
	SuggestionService suggestionService;
	@Mock
	SystemContext systemContext;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {

		id = Id.generate();
		command = new AddProductCommand(id, id, 5);
		clientData = new ClientData(id, "Arleta");
		reservation = new Reservation(id, ReservationStatus.OPENED, clientData, new Date());
		product = new Product(id, new Money(5), "sandwich", ProductType.FOOD);

	}

	@Test
	public void stateTest_addOneProduct_shouldContainsThatProduct() {
		MockitoAnnotations.initMocks(addProductCommandHandler);
		Mockito.when(reservationRepository.load(command.getOrderId())).thenReturn(reservation);
		Mockito.when(productRepository.load(command.getProductId())).thenReturn(product);

		addProductCommandHandler.handle(command);
		assertThat(reservation.contains(product), is(true));

	}

}
