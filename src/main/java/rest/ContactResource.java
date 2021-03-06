package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.ContactDTO;
import errorhandling.InvalidInput;
import errorhandling.NotFound;
import utils.EMF_Creator;
import facades.ContactFacade;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//Todo Remove or change relevant parts before ACTUAL use
@Path("contact")
public class ContactResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
       
    private static final ContactFacade FACADE =  ContactFacade.getContactFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
            
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello World\"}";
    }
    
    @POST
    @RolesAllowed({"user", "admin"})
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createContact(String input) throws InvalidInput {
        ContactDTO inputDTO = GSON.fromJson(input, ContactDTO.class);
        ContactDTO outputDTO = FACADE.createContact(inputDTO);
        return Response.ok().entity(GSON.toJson(outputDTO)).build();
    }
    
    @GET
    @RolesAllowed({"user", "admin"})
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        return Response.ok().entity(GSON.toJson(FACADE.getAllContacts())).build();
    }
    
    @GET
    @RolesAllowed({"user", "admin"})
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getContact(@PathParam("id") int id) throws NotFound { 
        return Response.ok().entity(GSON.toJson(FACADE.getContact(id))).build();
    }

}
