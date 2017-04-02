package pl.com.bottega.ecommerce.sales.application.api.handler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.domain.client.Client;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import pl.com.bottega.ecommerce.sharedkernel.exceptions.DomainOperationException.DomainOperationException;
import pl.com.bottega.ecommerce.system.application.SystemContext;

import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;

/**
 * Created by Piotrek on 02.04.2017.
 */
public class AddProductCommandHandlerTest {

    private ReservationRepository reservationRepository;
    private ProductRepository productRepository;
    private SuggestionService suggestionService;
    private Reservation reservation;
    private Id id;
    private AddProductCommand addProductCommand;
    private AddProductCommandHandler addProductCommandHandler;
    private Product product;

    @Before
    public void setUp() throws Exception{
        reservationRepository = Mockito.mock(ReservationRepository.class);
        productRepository = Mockito.mock(ProductRepository.class);
        suggestionService = Mockito.mock(SuggestionService.class);
        ClientRepository clientRepository = Mockito.mock(ClientRepository.class);
        id = new Id("1");
        addProductCommand = new AddProductCommand(id, id, 1);
        addProductCommandHandler = new AddProductCommandHandler();
        ClientData clientData = new ClientData(Id.generate(), "TEST");
        product = new Product(id, new Money(200), "test", ProductType.STANDARD);
        reservation = Mockito.spy(new Reservation(id, Reservation.ReservationStatus.OPENED, clientData, new Date()));
        Client client = new Client();
        SystemContext systemContext = new SystemContext();

        Mockito.when(clientRepository.load(id)).thenReturn(client);
        Mockito.when(reservationRepository.load(addProductCommand.getOrderId())).thenReturn(reservation);
        Mockito.when(productRepository.load(addProductCommand.getProductId())).thenReturn(product);
        Mockito.when(suggestionService.suggestEquivalent(product, client)).thenReturn(product);

        //static field set
        Whitebox.setInternalState(addProductCommandHandler, "reservationRepository", reservationRepository);
        Whitebox.setInternalState(addProductCommandHandler, "productRepository", productRepository);
        Whitebox.setInternalState(addProductCommandHandler, "suggestionService", suggestionService);
        Whitebox.setInternalState(addProductCommandHandler, "clientRepository", clientRepository);
        Whitebox.setInternalState(addProductCommandHandler, "systemContext", systemContext);
    }

    @Test
    public void reservationSaveMethodCancelTest(){
        addProductCommandHandler.handle(addProductCommand);
        Mockito.verify(reservationRepository).save(reservation);
    }

    @Test(expected = DomainOperationException.class)
    public void reservationPreviouslyClosedTest(){
        reservation.close();
        addProductCommandHandler.handle(addProductCommand);
    }
    
    @Test
    public void reservationAddMethodCancelTest(){
        addProductCommandHandler.handle(addProductCommand);
        Mockito.verify(reservation).add(product, 1);
    }
}