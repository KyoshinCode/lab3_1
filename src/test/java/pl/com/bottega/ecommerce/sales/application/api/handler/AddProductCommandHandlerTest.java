package pl.com.bottega.ecommerce.sales.application.api.handler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

	@Before
	public void setUp() throws Exception {
		reservationRepository = mock(ReservationRepository.class);
		productRepository = mock(ProductRepository.class);
		suggestionService = mock(SuggestionService.class);
		clientRepository = mock(ClientRepository.class);
		systemContext = mock(SystemContext.class);

		handler = new AddProductCommandHandler();
		Whitebox.setInternalState(handler, "reservationRepository", reservationRepository);
		Whitebox.setInternalState(handler, "productRepository", productRepository);
		Whitebox.setInternalState(handler, "suggestionService", suggestionService);
		Whitebox.setInternalState(handler, "clientRepository", clientRepository);
		Whitebox.setInternalState(handler, "systemContext", systemContext);

		command = new AddProductCommand(Id.generate(), Id.generate(), 12);
	}

	@Test
	public void handle_ProductAddedToReservationIsCorrect() throws Exception {
		Reservation reservation = new ReservationBuilder().opened().build();
		Product product = new ProductBuilder().withId(command.getProductId()).build();

		when(reservationRepository.load(command.getOrderId())).thenReturn(reservation);
		when(productRepository.load(command.getProductId())).thenReturn(product);

		handler.handle(command);

		assertThat(reservation.contains(product), is(true));
	}

}