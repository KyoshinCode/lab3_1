package pl.com.bottega.ecommerce.sales.application.api.handler;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.system.application.SystemContext;

public class AddProductCommandHandlerTest {

	private ReservationRepository reservationRepository;
	private ProductRepository productRepository;
	private SuggestionService suggestionService;
	private ClientRepository clientRepository;
	private SystemContext systemContext;
	private AddProductCommand addProductCommand;
	private Reservation reservation;
	private Product product;
	private AddProductCommandHandler addProductCommandHandler;

	
	@Before
	public void setUp() {
		 reservationRepository = mock(ReservationRepository.class); 
		 productRepository = mock(ProductRepository.class);
		 suggestionService = mock(SuggestionService.class);
		 clientRepository = mock(ClientRepository.class);
		 systemContext = mock(SystemContext.class);
		 		
		addProductCommandHandler = new AddProductCommandHandler();
        addProductCommand = new AddProductCommand(Id.generate(), Id.generate(), 1);
        
        Whitebox.setInternalState(addProductCommandHandler, "reservationRepository", reservationRepository);
		Whitebox.setInternalState(addProductCommandHandler, "clientRepository", clientRepository);
		Whitebox.setInternalState(addProductCommandHandler, "productRepository", productRepository);
		Whitebox.setInternalState(addProductCommandHandler, "suggestionService", suggestionService);
		Whitebox.setInternalState(addProductCommandHandler, "systemContext", systemContext);

	}

}
