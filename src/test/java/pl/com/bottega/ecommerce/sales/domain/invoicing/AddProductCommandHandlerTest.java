package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Before;
import static org.mockito.Mockito.*;
import org.mockito.internal.util.reflection.Whitebox;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.application.api.handler.AddProductCommandHandler;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.system.application.SystemContext;

public class AddProductCommandHandlerTest {

	AddProductCommandHandler testHandler = null;
	AddProductCommand testCommand = null;
	ProductRepository prodRep = null;
	ReservationRepository reserRep = null;
	ClientRepository clientRep = null;
	SuggestionService suggServ = null;
	SystemContext sysContext = null;
	
	@Before
	public void setUp() {
		reserRep = mock(ReservationRepository.class);
		prodRep = mock(ProductRepository.class);
		clientRep = mock(ClientRepository.class);
		suggServ = mock(SuggestionService.class);
		sysContext = new SystemContext();
		testHandler = new AddProductCommandHandler();
		testCommand = new AddProductCommand(Id.generate(), Id.generate(), 123);
		Whitebox.setInternalState(testHandler, "reservationRepository", reserRep);
		Whitebox.setInternalState(testHandler, "clientRepository", clientRep);
		Whitebox.setInternalState(testHandler, "productRepository", prodRep);
		Whitebox.setInternalState(testHandler, "suggestionService", suggServ);
		Whitebox.setInternalState(testHandler, "systemContext", sysContext);
	}

}
