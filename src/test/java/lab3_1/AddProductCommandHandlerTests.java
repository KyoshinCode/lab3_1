package lab3_1;

import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.application.api.handler.AddProductCommandHandler;
import pl.com.bottega.ecommerce.sales.domain.client.Client;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.invoicing.BookKeeper;
import pl.com.bottega.ecommerce.sales.domain.invoicing.InvoiceFactory;
import pl.com.bottega.ecommerce.sales.domain.invoicing.InvoiceRequest;
import pl.com.bottega.ecommerce.sales.domain.invoicing.RequestItem;
import pl.com.bottega.ecommerce.sales.domain.invoicing.Tax;
import pl.com.bottega.ecommerce.sales.domain.invoicing.TaxPolicy;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import pl.com.bottega.ecommerce.sharedkernel.exceptions.DomainOperationException.DomainOperationException;
import pl.com.bottega.ecommerce.system.application.SystemContext;

public class AddProductCommandHandlerTests {
	AddProductCommandHandler addProductCommandHandler;
	AddProductCommand addProductCommand;
	private ReservationRepository reservationRepository;
	private ProductRepository productRepository;
	private SuggestionService suggestionService;
	private ClientRepository clientRepository;
	private SystemContext systemContext;
	private Product avaiableProduct, unavaiableProduct;
	private Reservation reservation;
	private ClientData clientData;
	
	@Before
	public void setUp() {
		addProductCommandHandler = new AddProductCommandHandler();
		addProductCommand = new AddProductCommand(Id.generate(), Id.generate(), 123);
		systemContext = new SystemContext();
		avaiableProduct = new Product(Id.generate(), new Money(10),"Test name",ProductType.FOOD);
		unavaiableProduct = new Product(Id.generate(), new Money(10),"Test name",ProductType.FOOD);
		unavaiableProduct.markAsRemoved();
		clientData = new ClientData(Id.generate(), "sochacki");
		reservation = new Reservation(Id.generate(), Reservation.ReservationStatus.OPENED,clientData,new Date());
		// Mocking
		reservationRepository = Mockito.mock(ReservationRepository.class);
		productRepository = Mockito.mock(ProductRepository.class);
		suggestionService = Mockito.mock(SuggestionService.class);
		clientRepository = Mockito.mock(ClientRepository.class);
		Mockito.when(productRepository.load(Mockito.any(Id.class))).thenReturn(avaiableProduct);
		Mockito.when(reservationRepository.load(Mockito.any(Id.class))).thenReturn(reservation);
		//Mockito.when(reservation.add(Mockito.any(Product.class), Mockito.any(AddProductCommand.class))).thenReturn(product);
		// Generating data
		Whitebox.setInternalState(addProductCommandHandler, "reservationRepository", reservationRepository);
		Whitebox.setInternalState(addProductCommandHandler, "productRepository", productRepository);
		Whitebox.setInternalState(addProductCommandHandler, "suggestionService", suggestionService);
		Whitebox.setInternalState(addProductCommandHandler, "clientRepository", clientRepository);
		Whitebox.setInternalState(addProductCommandHandler, "systemContext", systemContext);
	}
	
	@Test
	public void testRepositorySave() {
		addProductCommandHandler.handle(addProductCommand);		
		Mockito.verify(reservationRepository, Mockito.times(1)).save(Mockito.any(Reservation.class));
	}
	
	@Test
	public void testProductSuggestionWhenProductAvaiable() {
		addProductCommandHandler.handle(addProductCommand);
		assertThat(avaiableProduct.isAvailable(), is(equalTo(true)));
		Mockito.verify(suggestionService, Mockito.times(0)).suggestEquivalent(Mockito.any(Product.class), Mockito.any(Client.class));
	}
	
	@Test
	public void testProductSuggestionWhenProductUnavaiable() {
		Client client = new Client();
		Mockito.when(clientRepository.load(Mockito.any(Id.class))).thenReturn(client);
		Mockito.when(suggestionService.suggestEquivalent(Mockito.any(Product.class), Mockito.any(Client.class))).thenReturn(avaiableProduct);
		Mockito.when(productRepository.load(Mockito.any(Id.class))).thenReturn(unavaiableProduct);
		addProductCommandHandler.handle(addProductCommand);		
		assertThat(unavaiableProduct.isAvailable(), is(equalTo(false)));
		Mockito.verify(suggestionService, Mockito.times(1)).suggestEquivalent(Mockito.any(Product.class), Mockito.any(Client.class));
	}
	
	@Test (expected = DomainOperationException.class)
	public void testClosedReservation() {
		reservation.close();
		addProductCommandHandler.handle(addProductCommand);
	}

}
