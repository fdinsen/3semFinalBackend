package facades;

import dto.ContactDTO;
import entities.Contact;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class ContactFacade {

    private static ContactFacade instance;
    private static EntityManagerFactory emf;
    
    //Private Constructor to ensure Singleton
    private ContactFacade() {}
    
    
    /**
     * 
     * @param _emf
     * @return an instance of this facade class.
     */
    public static ContactFacade getContactFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new ContactFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    //Takes in a dto, containing every field except id
    //Persists the contact in the db, adds id to the object
    //Returns same data in a dto, now including the created id
    public ContactDTO createContact(ContactDTO dto) {
        EntityManager em = getEntityManager();
        Contact contact;
        try {
            em.getTransaction().begin();
            
            contact = new Contact(dto);
            em.persist(contact);
            
            em.getTransaction().commit();
        }finally {
            em.close();
        }
        
        return new ContactDTO(contact);
    }
}
