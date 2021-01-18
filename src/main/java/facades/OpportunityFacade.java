package facades;

import dto.ContactDTO;
import dto.ContactsDTO;
import dto.OpportunityDTO;
import entities.Contact;
import entities.Opportunity;
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
public class OpportunityFacade {

    private static OpportunityFacade instance;
    private static EntityManagerFactory emf;
    
    //Private Constructor to ensure Singleton
    private OpportunityFacade() {}
    
    
    /**
     * 
     * @param _emf
     * @return an instance of this facade class.
     */
    public static OpportunityFacade getOpportunityFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new OpportunityFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public OpportunityDTO addOpportunityToContact(OpportunityDTO dto, int contactId) throws NotFound {
        EntityManager em = getEntityManager();
        OpportunityDTO toReturn = null;
        try {
            em.getTransaction().begin();
            Opportunity o = new Opportunity(dto);
            
            TypedQuery<Contact> q = em.createQuery("SELECT c FROM Contact c WHERE c.id=:value", Contact.class);
            q.setParameter("value", contactId);
            Contact c = q.getSingleResult();
            
            c.addOpportunity(o);
            em.persist(o);
            em.getTransaction().commit();
            
            toReturn = new OpportunityDTO(o);
        }catch(NoResultException ex) {
            throw new NotFound("No contact found by id " + contactId); 
        }finally {
            em.close();
        }
        
        return toReturn;
    }
}
