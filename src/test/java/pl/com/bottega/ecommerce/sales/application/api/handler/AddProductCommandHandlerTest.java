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
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientDataBuilder;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation.ReservationStatus;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationBuilder;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import pl.com.bottega.ecommerce.system.application.SystemContext;

@RunWith(MockitoJUnitRunner.class)
public class AddProductCommandHandlerTest {

	AddProductCommand command;
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

		command = new AddProductCommandBuilder().build();
		reservation = new ReservationBuilder().withClientData(new ClientDataBuilder().withName("Arleta").build())
				.build();
		product = new ProductBuilder().build();

		MockitoAnnotations.initMocks(addProductCommandHandler);
		Mockito.when(reservationRepository.load(command.getOrderId())).thenReturn(reservation);
		Mockito.when(productRepository.load(command.getProductId())).thenReturn(product);

	}

	@Test
	public void stateTest_addOneProduct_shouldContainsThatProduct() {
		addProductCommandHandler.handle(command);
		assertThat(reservation.contains(product), is(true));

	}

	@Test
	public void behaviorTest_reservationMethod_shouldBeRunOneTimes() {
		addProductCommandHandler.handle(command);
		Mockito.verify(reservationRepository, Mockito.times(1)).save(reservation);
	}
}
