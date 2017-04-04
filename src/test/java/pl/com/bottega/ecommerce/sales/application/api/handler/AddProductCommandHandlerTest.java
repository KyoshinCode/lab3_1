package pl.com.bottega.ecommerce.sales.application.api.handler;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductBuilder;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import pl.com.bottega.ecommerce.system.application.SystemContext;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation.ReservationStatus;

public class AddProductCommandHandlerTest {
	
	private AddProductCommandHandler addProductCommandHandler;
	private ReservationRepository mockedReservationRepository;
	private ProductRepository mockedProductRepository;
	private SuggestionService mockedSuggestionService;
	private ClientRepository mockedClientRepository;
	private SystemContext systemContext;
	
	@Before
	public void setUp() throws Exception {
		
		addProductCommandHandler = new AddProductCommandHandler();
		mockedReservationRepository = mock(ReservationRepository.class);
		mockedProductRepository = mock(ProductRepository.class);
		mockedSuggestionService = mock(SuggestionService.class);
		mockedClientRepository = mock(ClientRepository.class);
		systemContext = new SystemContext();
		
		Whitebox.setInternalState(addProductCommandHandler, "reservationRepository", mockedReservationRepository);
		Whitebox.setInternalState(addProductCommandHandler, "productRepository", mockedProductRepository);
		Whitebox.setInternalState(addProductCommandHandler, "suggestionService", mockedSuggestionService);
		Whitebox.setInternalState(addProductCommandHandler, "clientRepository", mockedClientRepository);
		Whitebox.setInternalState(addProductCommandHandler, "systemContext", systemContext);
	}

	@Test
	public void testHandleShouldInvokeSaveOnce() {
		
		// given
		ClientData dummyClientData = new ClientData(new Id("194972"), "Aleksander Kaczmarczyk");
		Reservation reservation = new Reservation(new Id("333"), ReservationStatus.OPENED, dummyClientData, new Date());
		AddProductCommand dummyAddProductCommand = new AddProductCommand(new Id("123"), new Id("456"), 1);
		
		ProductBuilder productBuilder = new ProductBuilder();
		Product product = productBuilder
			.withProductId(new Id("444"))
			.withPrice(new Money(10))
			.withName("Chicken")
			.withType(ProductType.FOOD)
			.build();
		
		when(mockedReservationRepository.load(dummyAddProductCommand.getOrderId()))
			.thenReturn(reservation);
		when(mockedProductRepository.load(dummyAddProductCommand.getProductId()))
			.thenReturn(product);
		
		// when
		addProductCommandHandler.handle(dummyAddProductCommand);
		
		// then
		verify(mockedReservationRepository, times(1)).load(dummyAddProductCommand.getOrderId());
	}
	
	@Test
	public void testHandleShouldAddCorrectProductToReservation() {
		
		// given
		ClientData dummyClientData = new ClientData(new Id("194972"), "Aleksander Kaczmarczyk");
		Reservation reservation = new Reservation(new Id("333"), ReservationStatus.OPENED, dummyClientData, new Date());
		AddProductCommand dummyAddProductCommand = new AddProductCommand(new Id("123"), new Id("456"), 1);

		ProductBuilder productBuilder = new ProductBuilder();
		Product product = productBuilder
			.withProductId(new Id("444"))
			.withPrice(new Money(10))
			.withName("Chicken")
			.withType(ProductType.FOOD)
			.build();
		
		when(mockedReservationRepository.load(dummyAddProductCommand.getOrderId()))
			.thenReturn(reservation);
		when(mockedProductRepository.load(dummyAddProductCommand.getProductId()))
			.thenReturn(product);
		
		// when
		addProductCommandHandler.handle(dummyAddProductCommand);
				
		// then
		Assert.assertThat(reservation.contains(product), is(equalTo(true)));
		Assert.assertThat(reservation.getReservedProducts().size(), is(equalTo(1)));
	}

}
