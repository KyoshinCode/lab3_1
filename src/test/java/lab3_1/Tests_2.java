package lab3_1;
 
 import static org.junit.Assert.*;
 
 import org.junit.Before;
 import org.junit.Test;
 import org.mockito.Mockito;
 import org.mockito.internal.util.reflection.Whitebox;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.application.api.handler.AddProductCommandHandler;
 import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
 import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
 import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
 import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
 import pl.com.bottega.ecommerce.system.application.SystemContext;
 
 public class Tests_2 {
 	
	 AddProductCommandHandler testHandler = null;
	 AddProductCommand testCommand = null;
	 ProductRepository prRep = null;
	 ReservationRepository resRep = null;
 	
 	@Before
 	public void setupTests() {
 		this.resRep = Mockito.mock(ReservationRepository.class);
 		this.prRep = Mockito.mock(ProductRepository.class);
 		ClientRepository clRep = Mockito.mock(ClientRepository.class);
 		SuggestionService suSe = Mockito.mock(SuggestionService.class);
 		SystemContext sysCon = Mockito.mock(SystemContext.class);
 		this.testCommand = new AddProductCommand(Id.generate(), Id.generate(), 123);
 		this.testHandler = new AddProductCommandHandler();
 		Whitebox.setInternalState(testHandler, "reservationRepository", resRep);
 		Whitebox.setInternalState(testHandler, "clientRepository", clRep);
 		Whitebox.setInternalState(testHandler, "productRepository", prRep);
 		Whitebox.setInternalState(testHandler, "suggestionService", suSe);
 		Whitebox.setInternalState(testHandler, "systemContext", sysCon);
 		
 	}
 	
 	@Test
 	public void test() {
 		fail("Not yet implemented");
 	}
 }