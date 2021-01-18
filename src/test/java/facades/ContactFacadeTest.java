package facades;

import dto.ContactDTO;
import utils.EMF_Creator;
import entities.Contact;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class ContactFacadeTest {

    private static EntityManagerFactory emf;
    private static ContactFacade facade;

    public ContactFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
       emf = EMF_Creator.createEntityManagerFactoryForTest();
       facade = ContactFacade.getContactFacade(emf);
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
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Contact.deleteAllRows").executeUpdate();
            //TODO: Persist Testdata

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
    public void testCreateContact1() {
        String expectedName = "Karl Smart";
        String expectedEmail = "karl123@gmail.com";
        String expectedCompany = "Means Productions";
        String expectedJobtitle = "Journalist";
        String expectedPhone = "99118822";
        ContactDTO toCreate = new ContactDTO(expectedName, expectedEmail, expectedCompany, expectedJobtitle, expectedPhone);
        
        ContactDTO createdDTO = facade.createContact(toCreate);
        
        assertEquals(expectedName, createdDTO.getName());
        assertEquals(expectedEmail, createdDTO.getEmail());
        assertEquals(expectedCompany, createdDTO.getCompany());
        assertEquals(expectedJobtitle, createdDTO.getJobtitle());
        assertEquals(expectedPhone, createdDTO.getPhone());
        assertTrue(createdDTO.getId() > 0);
    }
}
