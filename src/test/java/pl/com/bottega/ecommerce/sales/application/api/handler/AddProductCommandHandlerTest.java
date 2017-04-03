package pl.com.bottega.ecommerce.sales.application.api.handler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.domain.client.Client;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductBuilder;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationBuilder;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.system.application.SystemContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

/**
 * Created by Patryk Wierzyński.
 */
public class AddProductCommandHandlerTest {

	private AddProductCommandHandler handler;
	private AddProductCommand command;

	private ReservationRepository reservationRepository;
	private ProductRepository productRepository;
	private SuggestionService suggestionService;
	private ClientRepository clientRepository;
	private SystemContext systemContext;

	private Reservation reservation;
	private Product product;

	@Before
	public void setUp() throws Exception {
		reservationRepository = mock(ReservationRepository.class);
		productRepository = mock(ProductRepository.class);
		suggestionService = mock(SuggestionService.class);
		clientRepository = mock(ClientRepository.class);
		systemContext = new SystemContext();

		handler = new AddProductCommandHandler();
		Whitebox.setInternalState(handler, "reservationRepository", reservationRepository);
		Whitebox.setInternalState(handler, "productRepository", productRepository);
		Whitebox.setInternalState(handler, "suggestionService", suggestionService);
		Whitebox.setInternalState(handler, "clientRepository", clientRepository);
		Whitebox.setInternalState(handler, "systemContext", systemContext);

		command = new AddProductCommand(Id.generate(), Id.generate(), 12);
		reservation = new ReservationBuilder().opened().build();
		product = new ProductBuilder().withId(command.getProductId()).build();


		when(reservationRepository.load(command.getOrderId())).thenReturn(reservation);
		when(productRepository.load(command.getProductId())).thenReturn(product);

	}

	@Test
	public void handle_ProductAddedToReservationIsCorrect() throws Exception {
		handler.handle(command);

		assertThat(reservation.contains(product), is(true));
	}

	@Test
	public void handle_WhenProductAvailableSuggestionServiceAndClientRepositoryNotCalled() throws Exception {
		Client client = new Client();
		Product suggested = new ProductBuilder().withName("suggestedDefault").build();

		when(clientRepository.load(systemContext.getSystemUser().getClientId())).thenReturn(client);
		when(suggestionService.suggestEquivalent(product, client)).thenReturn(suggested);

		handler.handle(command);

		verify(suggestionService, times(0)).suggestEquivalent(product, client);
		verify(clientRepository, times(0)).load(systemContext.getSystemUser().getClientId());
	}

	@Test
	public void handle_WhenProductUnavailableSuggestionServiceAndClientRepositoryCalledOnce() throws Exception {
		product.markAsRemoved();

		Client client = new Client();
		Product suggested = new ProductBuilder().withName("suggestedDefault").build();

		when(clientRepository.load(systemContext.getSystemUser().getClientId())).thenReturn(client);
		when(suggestionService.suggestEquivalent(product, client)).thenReturn(suggested);

		handler.handle(command);

		verify(suggestionService, times(1)).suggestEquivalent(product, client);
		verify(clientRepository, times(1)).load(systemContext.getSystemUser().getClientId());
	}

	@Test
	public void handle_WhenProductUnavailableSuggestedProductIsInReservation() throws Exception {
		product.markAsRemoved();

		Client client = new Client();
		Product suggested = new ProductBuilder().withName("suggestedDefault").build();

		when(clientRepository.load(systemContext.getSystemUser().getClientId())).thenReturn(client);
		when(suggestionService.suggestEquivalent(product, client)).thenReturn(suggested);

		handler.handle(command);

		assertThat(reservation.contains(product), is(false));
		assertThat(reservation.contains(suggested), is(true));
	}

}