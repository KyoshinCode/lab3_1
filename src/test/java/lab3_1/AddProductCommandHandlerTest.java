package lab3_1;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.application.api.handler.AddProductCommandHandler;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation.ReservationStatus;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import pl.com.bottega.ecommerce.sharedkernel.exceptions.DomainOperationException.DomainOperationException;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import java.rmi.server.LoaderHandler;
import java.util.Date;

public class AddProductCommandHandlerTest {

	
	ReservationRepository mockedReservationRepository;
	ProductRepository mockedProductRepository;
	
	AddProductCommand command;
	AddProductCommandHandler commandHandler;
	Reservation reservationOpen, reservationClosed;
	Product product;
	ClientData client;
	
	@Before
	public void setUp() {
		mockedReservationRepository = mock(ReservationRepository.class);
		mockedProductRepository = mock(ProductRepository.class);
		
		commandHandler = new AddProductCommandHandler();
		command = new AddProductCommand(Id.generate(), Id.generate(), 2);
		client = new ClientData(Id.generate(), "John Doe");
		reservationOpen = new Reservation(Id.generate(), ReservationStatus.OPENED, client , new Date());
		reservationClosed = new Reservation(Id.generate(), ReservationStatus.CLOSED, client, new Date());
		product = new Product(Id.generate(), new Money(40), "product" , ProductType.FOOD);
		Whitebox.setInternalState(commandHandler, "reservationRepository", mockedReservationRepository);
		Whitebox.setInternalState(commandHandler, "productRepository", mockedProductRepository);
		
		
		when(mockedReservationRepository.load(command.getOrderId())).thenReturn(reservationOpen);
		when(mockedProductRepository.load(command.getProductId())).thenReturn(product);
		
	}
	
	
	
	@Test
	public void testOneProductIsAvailable(){
		
		reservationOpen.add(product, 1);
		commandHandler.handle(command);
		Assert.assertThat(product.isAvailable(), is(equalTo(true)));
	}
	
	@Test
	public void testIsReservationRepositorySaveMethodUsedOnce() {
		
		reservationOpen.add(product, 1);
		commandHandler.handle(command);
		verify(mockedReservationRepository, times(1)).save(reservationOpen);
		
	}
	
	@Test (expected = DomainOperationException.class)
	public void testAddProductToClosedReservation() {
		
		reservationClosed.add(product, 1);
		
	}
	
	@Test
	public void test() {
		
		
		product.markAsRemoved();
		Assert.assertThat(product.isAvailable(), is(false));
	}
	
}
