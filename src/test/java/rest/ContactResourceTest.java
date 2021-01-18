package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.ContactDTO;
import entities.Contact;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import java.net.URI;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
//Uncomment the line below, to temporarily disable this test
//@Disabled

public class ContactResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Contact c1, c2;

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;
    
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();

        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        c1 = new Contact("Jake Peralta", "cool-jake@nypd.gov", "New York Police Department", "Detective", "69420720");
        c2 = new Contact("Amy Santiago", "asantiago@nypd.gov", "New York Police Department", "Seargant", "98765432");
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Contact.deleteAllRows").executeUpdate();
            em.persist(c1);
            em.persist(c2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testServerIsUp() {
        given().when().get("/contact").then().statusCode(200);
    }

    //This test assumes the database contains two rows
    @Test
    public void testDummyMsg() throws Exception {
        given()
                .contentType("application/json")
                .get("/contact/").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("msg", equalTo("Hello World"));
    }
    
    @Test
    public void testCreateContact1() {
        String expectedName = "Brian Sej";
        String expectedEmail = "xXbrianXx@gmail.com";
        String expectedCompany = "Brians Fælge";
        String expectedJobtitle = "Mekaniker";
        String expectedPhone = "33882277";
        ContactDTO toCreate = new ContactDTO(expectedName, expectedEmail, expectedCompany, expectedJobtitle, expectedPhone);
        
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .body(GSON.toJson(toCreate))
                .when()
                .post("/contact/").then()
                .assertThat().statusCode(HttpStatus.OK_200.getStatusCode())
                .body("name", equalTo(expectedName)).and()
                .body("email", equalTo(expectedEmail)).and()
                .body("company", equalTo(expectedCompany)).and()
                .body("jobtitle", equalTo(expectedJobtitle)).and()
                .body("phone", equalTo(expectedPhone));
    }
    
    @Test
    public void testCreateContactMissing1() {
        String expectedName = "Brian Sej";
        String expectedEmail = "xXbrianXx@gmail.com";
        String expectedCompany = "Brians Fælge";
        String expectedJobtitle = "Mekaniker";
        String expectedPhone = "";
        ContactDTO toCreate = new ContactDTO(expectedName, expectedEmail, expectedCompany, expectedJobtitle, expectedPhone);
        
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .body(GSON.toJson(toCreate))
                .when()
                .post("/contact/").then()
                .assertThat().statusCode(HttpStatus.BAD_REQUEST_400.getStatusCode())
                .body("message", equalTo("All fields must be set"));
    }
    
    @Test
    public void testGetAllContacts1() {
        given()
                .contentType("application/json")
                .get("/contact/all/").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("all.size()", is(2));
    }
}
