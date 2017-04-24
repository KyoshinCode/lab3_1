package pl.com.bottega.ecommerce.sales.application.api.handler;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.domain.client.Client;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.system.application.SystemContext;
import pl.com.bottega.ecommerce.system.application.SystemUser;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Rule;
import org.mockito.Mock;
import org.mockito.junit.*;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import static org.mockito.Mockito.*;


public class AddProductCommandHandlerTest {
	private AddProductCommandHandler addProductCommandHandler;
	
	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@Mock
	private ReservationRepository mockReservationRepositry;
	
	@Mock
	private ProductRepository mockProductRepositry;
	
	@Mock
	private ClientRepository mockClientRepository;
	
	@Mock
	private SuggestionService mockSuggestionService;
	
	@Mock
	private Reservation mockReservation;
	
	@Mock
	private Product dummyProduct;
	
	@Mock
	private ClientRepository mockClientRepositry;
	
	@Test
	public void test_DoNotCallClientLoadIfProductIsAvailable() {
		addProductCommandHandler = new AddProductCommandHandler();
		
		when(mockReservationRepositry.load(any(Id.class))).thenReturn(mockReservation);
		when(mockProductRepositry.load(any(Id.class))).thenReturn(dummyProduct);
		when(mockProduct.isAvailable()).thenReturn(true);
		verify(mockClientRepository, times(0)).load(any(Id.class));
	}

}
