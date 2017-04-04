package pl.com.bottega.ecommerce.sales.application.api.handler;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.system.application.SystemContext;

public class AddProductCommandHandlerTest {

	private ReservationRepository reservationRepository;
	private ProductRepository productRepository;
	private SuggestionService suggestionService;
	private ClientRepository clientRepository;
	private SystemContext systemContext;

	
	@Test
	public void test() {
		 reservationRepository = mock(ReservationRepository.class); 
		 productRepository = mock(ProductRepository.class);
		 suggestionService = mock(SuggestionService.class);
		 clientRepository = mock(ClientRepository.class);
		 systemContext = mock(SystemContext.class);
		 		
    
	}

}
