package pl.com.bottega.ecommerce.sales.application.api.handler;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import pl.com.bottega.ecommerce.system.application.SystemContext;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation.ReservationStatus;

public class AddProductCommandHandlerTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testHandleShouldInvokeSaveOnce() {
		
		// given
		AddProductCommandHandler addProductCommandHandler = new AddProductCommandHandler();
		ReservationRepository mockedReservationRepository = mock(ReservationRepository.class);
		ProductRepository mockedProductRepository = mock(ProductRepository.class);
		SuggestionService mockedSuggestionService = mock(SuggestionService.class);
		ClientRepository mockedClientRepository = mock(ClientRepository.class);
		SystemContext systemContext = new SystemContext();
		ClientData dummyClientData = new ClientData(new Id("194972"), "Aleksander Kaczmarczyk");
		
		Reservation reservation = new Reservation(new Id("333"), ReservationStatus.OPENED, dummyClientData, new Date());
		AddProductCommand dummyAddProductCommand = new AddProductCommand(new Id("123"), new Id("456"), 1);
		
		Whitebox.setInternalState(addProductCommandHandler, "reservationRepository", mockedReservationRepository);
		Whitebox.setInternalState(addProductCommandHandler, "productRepository", mockedProductRepository);
		Whitebox.setInternalState(addProductCommandHandler, "suggestionService", mockedSuggestionService);
		Whitebox.setInternalState(addProductCommandHandler, "clientRepository", mockedClientRepository);
		Whitebox.setInternalState(addProductCommandHandler, "systemContext", systemContext);
		
		when(mockedReservationRepository.load(dummyAddProductCommand.getOrderId()))
			.thenReturn(reservation);
		when(mockedProductRepository.load(dummyAddProductCommand.getProductId()))
			.thenReturn(new Product(new Id("444"), new Money(10), "Chicken", ProductType.FOOD));
		
		// when
		addProductCommandHandler.handle(dummyAddProductCommand);
		
		// then
		verify(mockedReservationRepository, times(1)).load(dummyAddProductCommand.getOrderId());
	}
	
	@Test
	public void testHandleShouldAddCorrectProductToReservation() {
		
	}

}
