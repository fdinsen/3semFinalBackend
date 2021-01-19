package facades;

import dto.ContactDTO;
import dto.ContactsDTO;
import dto.OpportunityDTO;
import utils.EMF_Creator;
import entities.Contact;
import entities.Opportunity;
import entities.OpportunityStatus;
import entities.Role;
import entities.User;
import errorhandling.InvalidInput;
import errorhandling.NotFound;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

//Uncomment the line below, to temporarily disable this test
@Disabled
public class OpportunityFacadeTest {

    private static EntityManagerFactory emf;
    private static OpportunityFacade facade;
    private static Contact c1;
    private static Opportunity o1, o2, o3;
    private static OpportunityStatus os1, os2;
    

    public OpportunityFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = OpportunityFacade.getOpportunityFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        c1 = new Contact("Terry Jeffords", "tjeffords@nypd.gov", "New York Police Department", "Lieutenant", "11223344");
        o1 = new Opportunity("Find rare yoghurt", 30, new Date());
        o2 = new Opportunity("Expensive yoghurt fridge", 500, new Date());
        o3 = new Opportunity("Replacement MooMoo", 50, new Date());
        os1 = new OpportunityStatus("Failed");
        os2 = new OpportunityStatus("Started");

        o1.setOpportunityStatus(os2);
        o2.setOpportunityStatus(os1);
        o3.setOpportunityStatus(os2);

        c1.addOpportunity(o1);
        c1.addOpportunity(o2);
        c1.addOpportunity(o3);
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Opportunity.deleteAllRows").executeUpdate();
            em.createNamedQuery("Contact.deleteAllRows").executeUpdate();
            em.persist(c1);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    @Test
    public void testAddOpportunity1() throws NotFound {
        String expectedName = "Get Swift tickets";
        int expectedAmount = 200;
        Date expectedDate = new Date();
        String expectedOS = "Started";
        
        OpportunityDTO dto = new OpportunityDTO(expectedName, expectedAmount, expectedDate, expectedOS);
        OpportunityDTO actual;
        
        
        actual = facade.addOpportunityToContact(dto, c1.getId());
        
        
        assertEquals(expectedName, actual.getName());
        assertEquals(expectedAmount, actual.getAmount());
        assertEquals(expectedDate, actual.getCloseDate());
        assertEquals(expectedOS, actual.getOpportunityStatus());
        assertTrue(actual.getId() > 0);
    }
    
}
