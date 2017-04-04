package pl.com.bottega.ecommerce.sales.application.api.handler;

import org.junit.Assert;
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
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationFactory;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import pl.com.bottega.ecommerce.system.application.SystemContext;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Wojciech Szczepaniak on 28.03.2017.
 */
public class AddProductCommandHandlerTest {

    private AddProductCommandHandler addProductCommandHandler;
    private AddProductCommand addProductCommand;
    private ReservationRepository reservationRepository;
    private ProductRepository productRepository;
    private SuggestionService suggestionService;
    private ClientRepository clientRepository;
    private SystemContext systemContext;

    @Before
    public void init() {
        addProductCommandHandler = new AddProductCommandHandler();

        addProductCommand = mock(AddProductCommand.class);
        reservationRepository = mock(ReservationRepository.class);
        productRepository = mock(ProductRepository.class);
        suggestionService = mock(SuggestionService.class);
        clientRepository = mock(ClientRepository.class);
        systemContext = mock(SystemContext.class);

        Whitebox.setInternalState(addProductCommandHandler, "reservationRepository", reservationRepository);
        Whitebox.setInternalState(addProductCommandHandler, "productRepository", productRepository);
        Whitebox.setInternalState(addProductCommandHandler, "suggestionService", suggestionService);
        Whitebox.setInternalState(addProductCommandHandler, "clientRepository", clientRepository);
        Whitebox.setInternalState(addProductCommandHandler, "systemContext", systemContext);
    }

    @Test
    public void clientWithNoProductAfterAddHaveOneProduct() {
        // given
        Reservation reservation = new ReservationFactory().createExampleReservation();
        Assert.assertThat(reservation.getReservedProducts().size(), is(0));

        Product product = mock(Product.class);
        when(product.isAvailable()).thenReturn(true);
        when(product.getPrice()).thenReturn(Money.ZERO);

        when(reservationRepository.load(any(Id.class))).thenReturn(reservation);
        when(productRepository.load(any(Id.class))).thenReturn(product);

        // when
        addProductCommandHandler.handle(addProductCommand);

        // then
        Assert.assertThat(reservation.getReservedProducts().size(), is(1));
    }
}
