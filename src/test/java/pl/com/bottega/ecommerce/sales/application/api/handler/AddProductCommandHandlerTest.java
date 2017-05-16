package pl.com.bottega.ecommerce.sales.application.api.handler;

import org.junit.Assert;
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
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductBuilder;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationBuilder;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import pl.com.bottega.ecommerce.sharedkernel.exceptions.DomainOperationException.DomainOperationException;
import pl.com.bottega.ecommerce.system.application.SystemContext;

import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.spy;

/**
 * Created by Piotrek on 02.04.2017.
 */
public class AddProductCommandHandlerTest {

    private ReservationRepository reservationRepository;
    private ProductRepository productRepository;
    private SuggestionService suggestionService;
    private Reservation spyReservation;
    private Id id;
    private AddProductCommand addProductCommand;
    private AddProductCommandHandler addProductCommandHandler;
    private Product product;
    private Product productCopy;
    private ClientRepository clientRepository;
    private ClientData clientData;
    private Client client;
    private SystemContext systemContext;

    @Before
    public void setUp() throws Exception{
        reservationRepository = Mockito.mock(ReservationRepository.class);
        productRepository = Mockito.mock(ProductRepository.class);
        suggestionService = Mockito.mock(SuggestionService.class);
        clientRepository = Mockito.mock(ClientRepository.class);

        id = new Id("1");
        addProductCommand = new AddProductCommand(id, id, 1);
        addProductCommandHandler = new AddProductCommandHandler();

        product = ProductBuilder.
                productBuilder()
                .withId(Id.generate())
                .withMoney(new Money(200))
                .withName("test")
                .withProductType(ProductType.STANDARD)
                .build();

        productCopy = ProductBuilder.
                productBuilder()
                .withId(Id.generate())
                .withMoney(new Money(200))
                .withName("test")
                .withProductType(ProductType.STANDARD)
                .build();

        Reservation reservation = ReservationBuilder.
                reservationBuilder()
                .withId(Id.generate())
                .withStatus(Reservation.ReservationStatus.OPENED)
                .withClientData(new ClientData(Id.generate(), "TEST"))
                .withDate(new Date())
                .build();

        client = new Client();
        systemContext = new SystemContext();
        spyReservation = spy(reservation);

        Mockito.when(clientRepository.load(id)).thenReturn(client);
        Mockito.when(reservationRepository.load(addProductCommand.getOrderId())).thenReturn(spyReservation);
        Mockito.when(productRepository.load(addProductCommand.getProductId())).thenReturn(product);
        Mockito.when(suggestionService.suggestEquivalent(product, client)).thenReturn(product);
        Mockito.when(suggestionService.suggestEquivalent(product, client)).thenReturn(productCopy);

        //private field set
        Whitebox.setInternalState(addProductCommandHandler, "reservationRepository", reservationRepository);
        Whitebox.setInternalState(addProductCommandHandler, "productRepository", productRepository);
        Whitebox.setInternalState(addProductCommandHandler, "suggestionService", suggestionService);
        Whitebox.setInternalState(addProductCommandHandler, "clientRepository", clientRepository);
        Whitebox.setInternalState(addProductCommandHandler, "systemContext", systemContext);
    }

    @Test
    public void reservationSaveMethodCancelTest(){
        addProductCommandHandler.handle(addProductCommand);
        Mockito.verify(reservationRepository).save(spyReservation);
    }

    @Test(expected = DomainOperationException.class)
    public void reservationPreviouslyClosedTest(){
        spyReservation.close();
        addProductCommandHandler.handle(addProductCommand);
    }
    
    @Test
    public void reservationAddMethodCancelTest(){
        addProductCommandHandler.handle(addProductCommand);
        Mockito.verify(spyReservation).add(product, 1);
    }

    @Test
    public void checkIfPossibleToAddOverPrimaryUnavailable(){
        product.markAsRemoved();
        Assert.assertThat(product.isAvailable(), is(equalTo(false)));

        addProductCommandHandler.handle(addProductCommand);
        Mockito.verify(spyReservation).add(productCopy, 1);
    }
}