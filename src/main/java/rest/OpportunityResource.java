package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.ContactDTO;
import dto.OpportunityDTO;
import errorhandling.InvalidInput;
import errorhandling.NotFound;
import utils.EMF_Creator;
import facades.ContactFacade;
import facades.OpportunityFacade;
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
@Path("opportunity")
public class OpportunityResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
       
    private static final OpportunityFacade FACADE =  OpportunityFacade.getOpportunityFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().setDateFormat("dd/MM/YYY").create();
            
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello World\"}";
    }
    
    @POST
    @Path("/{id}")
    @RolesAllowed({"user", "admin"})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addOpportunityToContact(@PathParam("id")int id, String input) throws NotFound {
        OpportunityDTO dto = GSON.fromJson(input, OpportunityDTO.class);
        return Response.ok().entity(GSON.toJson(FACADE.addOpportunityToContact(dto, id))).build(); 
    }
}
