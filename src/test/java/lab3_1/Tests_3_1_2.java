package lab3_1;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

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
import pl.com.bottega.ecommerce.sharedkernel.exceptions.DomainOperationException.DomainOperationException;
import pl.com.bottega.ecommerce.system.application.SystemContext;
import static org.hamcrest.CoreMatchers.*;

public class Tests_3_1_2 {
	
	AddProductCommandHandler testHandler = null;
	AddProductCommand testCommand = null;
	ProductRepository prRep = null;
	ReservationRepository resRep = null;
	ClientRepository clRep = null;
	SuggestionService suSe = null;
	SystemContext sysCon = null;
	
	@Before
	public void setupTests() {
		resRep = Mockito.mock(ReservationRepository.class);
		prRep = Mockito.mock(ProductRepository.class);
		clRep = Mockito.mock(ClientRepository.class);
		suSe = Mockito.mock(SuggestionService.class);
		sysCon = new SystemContext();
		testHandler = new AddProductCommandHandler();
		testCommand = new AddProductCommand(Id.generate(), Id.generate(), 123);
		Whitebox.setInternalState(testHandler, "reservationRepository", resRep);
		Whitebox.setInternalState(testHandler, "clientRepository", clRep);
		Whitebox.setInternalState(testHandler, "productRepository", prRep);
		Whitebox.setInternalState(testHandler, "suggestionService", suSe);
		Whitebox.setInternalState(testHandler, "systemContext", sysCon);
	}
	
	@Test
	public void testAvailableProductCorrectlyAddedToReservation() {
		Product tProd = new ProductBuilder()
				.available()
				.withName("test product")
				.withPrice(new Money(123))
				.withType(ProductType.STANDARD)
				.withId(Id.generate())
				.build();
		Reservation tRes = new ReservationBuilder()
				.open()
				.withClientData(new ClientData(Id.generate(), "wujeksado"))
				.withId(Id.generate())
				.build();
		Mockito.when(this.prRep.load(testCommand.getProductId())).thenReturn(tProd);
		Mockito.when(this.resRep.load(testCommand.getOrderId())).thenReturn(tRes);
		testHandler.handle(testCommand);
		assertThat(tRes.contains(tProd), is(equalTo(true)));
	}
	
	@Test (expected = DomainOperationException.class)
	public void testAvailableProductNotAddedToClosedReservation() {
		Product tProd = new ProductBuilder()
				.available()
				.withName("test product")
				.withPrice(new Money(123))
				.withType(ProductType.STANDARD)
				.withId(Id.generate())
				.build();
		Reservation tRes = new ReservationBuilder()
				.close()
				.withClientData(new ClientData(Id.generate(), "wujeksado"))
				.withId(Id.generate())
				.build();
		Mockito.when(this.prRep.load(testCommand.getProductId())).thenReturn(tProd);
		Mockito.when(this.resRep.load(testCommand.getOrderId())).thenReturn(tRes);
		testHandler.handle(testCommand);
	}
	
	@Test
	public void testSuggestionServiceNotCalledWhenProductAvailable() {
		Product tProd = new ProductBuilder()
				.available()
				.withName("test product")
				.withPrice(new Money(123))
				.withType(ProductType.STANDARD)
				.withId(Id.generate())
				.build();
		Product sProd = new ProductBuilder()
				.available()
				.withName("suggested")
				.withPrice(new Money(123))
				.withType(ProductType.DRUG)
				.withId(Id.generate())
				.build();
		Reservation tRes = new ReservationBuilder()
				.open()
				.withClientData(new ClientData(Id.generate(), "wujeksado"))
				.withId(Id.generate())
				.build();
		Mockito.when(this.prRep.load(testCommand.getProductId())).thenReturn(tProd);
		Mockito.when(this.resRep.load(testCommand.getOrderId())).thenReturn(tRes);
		Client testC = new Client();
		Mockito.when(clRep.load(Id.generate())).thenReturn(testC);
		Mockito.when(suSe.suggestEquivalent(tProd, testC)).thenReturn(sProd);
		testHandler.handle(testCommand);
		Mockito.verify(suSe, Mockito.times(0)).suggestEquivalent(tProd, testC);
	}
}
