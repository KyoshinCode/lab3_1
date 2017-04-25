package lab3_1;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.application.api.handler.AddProductCommandHandler;
import pl.com.bottega.ecommerce.sales.domain.client.Client;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductBuilder;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationBuilder;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import pl.com.bottega.ecommerce.system.application.SystemContext;

public class AddProductCommandHandlerTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SuggestionService suggestionService;

    @Mock
    private ClientRepository clientRepository;

    private SystemContext systemContext;
	private AddProductCommandHandler addProductCommandHandler;
	private AddProductCommand addProductCommand;
	private Product product;
	private Reservation reservation;
	private ClientData clientData;

	@Before
	public void setUp(){
		addProductCommandHandler = new AddProductCommandHandler();
		addProductCommand = new AddProductCommand(Id.generate(), Id.generate(), 1);

		clientData = new ClientData(Id.generate(), "ClientName");
		reservation = new ReservationBuilder()
				.setReservationStatus(Reservation.ReservationStatus.OPENED)
				.setClientData(clientData)
				.build();
		product = new ProductBuilder().build();

		Mockito.when(reservationRepository.load(Mockito.any(Id.class))).thenReturn(reservation);
		Mockito.when(productRepository.load(Mockito.any(Id.class))).thenReturn(product);

		systemContext = new SystemContext();

        Whitebox.setInternalState(addProductCommandHandler, "reservationRepository", reservationRepository);
        Whitebox.setInternalState(addProductCommandHandler, "productRepository", productRepository);
        Whitebox.setInternalState(addProductCommandHandler, "suggestionService", suggestionService);
        Whitebox.setInternalState(addProductCommandHandler, "clientRepository", clientRepository);
        Whitebox.setInternalState(addProductCommandHandler, "systemContext", systemContext);
	}

	@Test
	public void reservationRepositorySaveTest() {
		addProductCommandHandler.handle(addProductCommand);
		Mockito.verify(reservationRepository, Mockito.times(1)).save(Mockito.any(Reservation.class));
	}

	@Test
	public void reservationRepositoryLoadTest() {
		addProductCommandHandler.handle(addProductCommand);
		Mockito.verify(reservationRepository, Mockito.times(1)).load(Mockito.any(Id.class));
	}

	@Test
	public void suggestEquivalentTest() {
		Client client = new Client();
		product.markAsRemoved();

		Product goodProduct = new Product(Id.generate(), new Money(10),"ProductName",ProductType.STANDARD);

		Mockito.when(clientRepository.load(Mockito.any(Id.class))).thenReturn(client);
		Mockito.when(suggestionService.suggestEquivalent(product, client)).thenReturn(goodProduct);

		addProductCommandHandler.handle(addProductCommand);

		Mockito.verify(suggestionService, Mockito.times(1)).suggestEquivalent(product, client);
	}

    @Test
    public void reservationWithOneProductTest() {
		addProductCommandHandler.handle(addProductCommand);

		Assert.assertThat(reservation.getReservedProducts().size(), is(equalTo(1)));
        Assert.assertThat(reservation.getClientData(), is(equalTo(clientData)));
        Assert.assertThat(reservation.contains(product), is(true));
    }
}
