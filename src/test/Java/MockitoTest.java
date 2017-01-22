import com.tickets.dao.TicketDAOHibernateImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;


/**
 * Created by lukasz.homik on 2016-12-20.
 */
public class MockitoTest {

    @Mock
    TicketDAOHibernateImpl ticketDAOHibernate;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test1(){
        assertNotNull(ticketDAOHibernate);
    }


}
