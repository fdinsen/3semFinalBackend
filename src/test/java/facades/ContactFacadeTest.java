package facades;

import dto.ContactDTO;
import dto.ContactsDTO;
import utils.EMF_Creator;
import entities.Contact;
import errorhandling.InvalidInput;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
            em.persist(new Contact("Jake Peralta", "cool-jake@nypd.gov", "New York Police Department", "Detective", "69420720"));
            em.persist(new Contact("Amy Santiago", "asantiago@nypd.gov", "New York Police Department", "Seargant", "98765432"));
            em.persist(new Contact("Raymond Holt", "rholt@nypd.gov", "New York Police Department", "Captain", "12345678"));

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
    public void testCreateContact1() throws InvalidInput {
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

    @Test
    public void testCreateContactMissing1() throws InvalidInput {
        InvalidInput assertThrows;
        String expectedName = "Karl Smart";
        String expectedEmail = "karl123@gmail.com";
        String expectedCompany = "Means Productions";
        String expectedJobtitle = "Journalist";
        ContactDTO toCreate = new ContactDTO(expectedName, expectedEmail, expectedCompany, expectedJobtitle, null);

        assertThrows = Assertions.assertThrows(InvalidInput.class, () -> {
            facade.createContact(toCreate);
        });
        Assertions.assertNotNull(assertThrows);
    }

    @Test
    public void testGetAllContacts1() {
        int expectedSize = 3;
        
        ContactsDTO all = facade.getAllContacts();
        
        assertEquals(expectedSize, all.getAll().size());
    }
}
