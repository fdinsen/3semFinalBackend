package facades;

import dto.ContactDTO;
import dto.ContactsDTO;
import entities.Contact;
import errorhandling.InvalidInput;
import errorhandling.NotFound;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

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
    public ContactDTO createContact(ContactDTO dto) throws InvalidInput {
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
    
    public ContactsDTO getAllContacts() {
        EntityManager em = getEntityManager();
        ContactsDTO all = new ContactsDTO();
        try {
            TypedQuery<Contact> q = em.createQuery("SELECT c From Contact c", Contact.class);
            q.getResultStream().forEach(contact -> {
                all.addContact(new ContactDTO(contact));
            });
        }finally{
            em.close();
        }
        return all;
    }
    
    public ContactDTO getContact(int id) throws NotFound {
        EntityManager em = getEntityManager();
        Contact contact = null;
        try {
            TypedQuery<Contact> q = em.createQuery("SELECT c FROM Contact c WHERE c.id=:value", Contact.class);
            q.setParameter("value", id);
            contact = q.getSingleResult();
        }catch(NoResultException ex) {
            throw new NotFound("No contact found by id " + id); 
        }finally{
            em.close();
        }
        return new ContactDTO(contact);
    } 
}
