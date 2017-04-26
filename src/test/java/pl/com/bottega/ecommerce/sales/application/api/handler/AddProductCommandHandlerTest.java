package pl.com.bottega.ecommerce.sales.application.api.handler;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.domain.client.Client;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationItem;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.sales.domain.reservation.StubReservationRepositry;
import pl.com.bottega.ecommerce.system.application.SystemContext;
import pl.com.bottega.ecommerce.system.application.SystemUser;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;
import org.junit.Rule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import static org.mockito.Mockito.*;

import java.util.List;


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
	private SystemContext mockSystemContext;
	
	@Mock
	private Reservation dummyReservation;
	
	@Mock
	private Product mockProduct;
	
	@Mock
	private Product mockSimilarProduct;
	
	@Mock
	private Client dummyClient;
	
	@Mock
	private SystemUser dummySystemUser;
	
	@Mock
	private Reservation mockReservation;
	
	@Mock
	private AddProductCommand mockCommand;
	
	@Mock
	private Id dummyId;
	
	@Test
	public void test_CallClientLoadOnceIfProductIsNotAvailable() {
		addProductCommandHandler = new AddProductCommandHandler(
				mockReservationRepositry,
				mockProductRepositry,
				mockSuggestionService,
				mockClientRepository,
				mockSystemContext);
		
		AddProductCommand addProductCommand = new AddProductCommand(Id.generate(), Id.generate(), 1);
		
		when(mockReservationRepositry.load(any(Id.class))).thenReturn(dummyReservation);
		when(mockProductRepositry.load(any(Id.class))).thenReturn(mockProduct);
		when(mockProduct.isAvailable()).thenReturn(false);
		when(mockClientRepository.load(any(Id.class))).thenReturn(dummyClient);
		when(mockSystemContext.getSystemUser()).thenReturn(dummySystemUser);
		addProductCommandHandler.handle(addProductCommand);
		verify(mockClientRepository, times(1)).load(any(Id.class));
	}
	
	@Test
	public void test_IfProductNotAvailableSuggestStandard() {
		final int QUANTITY = 5;
		
		StubReservationRepositry stubReservationRepositry = new StubReservationRepositry();
		AddProductCommand addProductCommand = new AddProductCommand(Id.generate(), Id.generate(), QUANTITY);
		
		addProductCommandHandler = new AddProductCommandHandler(
				stubReservationRepositry,
				mockProductRepositry,
				mockSuggestionService,
				mockClientRepository,
				mockSystemContext);
		
		when(mockProductRepositry.load(any(Id.class))).thenReturn(mockProduct);
		when(mockProduct.isAvailable()).thenReturn(false);
		when(mockClientRepository.load(any(Id.class))).thenReturn(dummyClient);
		when(mockSystemContext.getSystemUser()).thenReturn(dummySystemUser);
		
		when(mockSystemContext.getSystemUser()).thenReturn(dummySystemUser);
		when(mockSuggestionService.suggestEquivalent(mockProduct, dummyClient)).thenReturn(mockSimilarProduct);
		when(mockSimilarProduct.getProductType()).thenReturn(ProductType.STANDARD);
		when(mockSimilarProduct.isAvailable()).thenReturn(true);
		
		addProductCommandHandler.handle(addProductCommand);
		List<ReservationItem> items = stubReservationRepositry.getReservation().getItems();
		assertThat(items.get(0).getProduct().getProductType(), is(equalTo(ProductType.STANDARD)));
		assertThat(items.get(0).getQuantity(), is(equalTo(QUANTITY)));
	}
	
	@Test
	public void test_DoNotCallClientLoadIfProductIsAvailable() {
		addProductCommandHandler = new AddProductCommandHandler(
				mockReservationRepositry,
				mockProductRepositry,
				mockSuggestionService,
				mockClientRepository,
				mockSystemContext);
		
		AddProductCommand addProductCommand = new AddProductCommand(Id.generate(), Id.generate(), 1);
		
		when(mockReservationRepositry.load(any(Id.class))).thenReturn(dummyReservation);
		when(mockProductRepositry.load(any(Id.class))).thenReturn(mockProduct);
		when(mockProduct.isAvailable()).thenReturn(true);
		addProductCommandHandler.handle(addProductCommand);
		verify(mockClientRepository, times(0)).load(any(Id.class));
	}
	
	@Test
	public void test_CheckIfProductIsSavedInRepository() {
		final int QUANTITY = 5;
		
		StubReservationRepositry stubReservationRepositry = new StubReservationRepositry();
		AddProductCommand addProductCommand = new AddProductCommand(Id.generate(), Id.generate(), QUANTITY);
		
		addProductCommandHandler = new AddProductCommandHandler(
				stubReservationRepositry,
				mockProductRepositry,
				mockSuggestionService,
				mockClientRepository,
				mockSystemContext);
		
		when(mockProductRepositry.load(any(Id.class))).thenReturn(mockProduct);
		when(mockProduct.isAvailable()).thenReturn(true);
		when(mockProduct.getProductType()).thenReturn(ProductType.STANDARD);
		
		addProductCommandHandler.handle(addProductCommand);
		List<ReservationItem> items = stubReservationRepositry.getReservation().getItems();
		assertThat(items.get(0).getProduct().getProductType(), is(equalTo(ProductType.STANDARD)));
		assertThat(items.get(0).getQuantity(), is(equalTo(QUANTITY)));
	}	
}
