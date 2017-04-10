package lab3_1;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.application.api.handler.AddProductCommandHandler;
import pl.com.bottega.ecommerce.sales.domain.client.Client;
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
import pl.com.bottega.ecommerce.system.application.SystemContext;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import java.rmi.server.LoaderHandler;
import java.util.Date;

import javax.print.attribute.standard.PDLOverrideSupported;

public class AddProductCommandHandlerTest {

	
	ReservationRepository mockedReservationRepository;
	ProductRepository mockedProductRepository;
	SuggestionService mockedSuggestionService;
	ClientRepository mockedClientRepository;
	AddProductCommand command;
	AddProductCommandHandler commandHandler;
	Reservation reservationOpen, reservationClosed;
	Product product;
	ClientData clientData;
	Client client;
	
	
	@Before
	public void setUp() {
		SystemContext systemContext = new SystemContext();
		mockedReservationRepository = mock(ReservationRepository.class);
		mockedProductRepository = mock(ProductRepository.class);
		mockedSuggestionService = mock(SuggestionService.class);
		mockedClientRepository = mock(ClientRepository.class);
		commandHandler = new AddProductCommandHandler();
		command = new AddProductCommand(Id.generate(), Id.generate(), 2);
		client = new Client();
		clientData = new ClientData(Id.generate(), "John Doe");
		reservationOpen = new Reservation(Id.generate(), ReservationStatus.OPENED, clientData , new Date());
		reservationClosed = new Reservation(Id.generate(), ReservationStatus.CLOSED, clientData, new Date());
		product = new Product(Id.generate(), new Money(40), "product" , ProductType.FOOD);
		Whitebox.setInternalState(commandHandler, "reservationRepository", mockedReservationRepository);
		Whitebox.setInternalState(commandHandler, "productRepository", mockedProductRepository);
		Whitebox.setInternalState(commandHandler, "clientRepository", mockedClientRepository);
		
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
	public void testProductIsNotAvailable() {
		
		product.markAsRemoved();
		Assert.assertThat(product.isAvailable(), is(false));
	}
	
	@Test()
	public void testCommandWithoutProduct() {
		
		  commandHandler.handle(command);
		  verify(mockedProductRepository, times(0)).load(command.getOrderId());
		
	}
	
}
