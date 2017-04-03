package pl.com.bottega.ecommerce.sales.application.api.handler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.system.application.SystemContext;

import static org.mockito.Mockito.mock;

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

	}

	@Test
	public void handle() throws Exception {

	}

}