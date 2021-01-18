package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.ContactDTO;
import entities.Contact;
import entities.Role;
import entities.User;
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

            em.createQuery("delete from User").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();

            Role userRole = new Role("user");
            User user = new User("user", "user");
            user.addRole(userRole);
            em.persist(userRole);
            em.persist(user);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    //This is how we hold on to the token after login, similar to that a client must store the token somewhere
    private static String securityToken;

    //Utility method to login and set the returned securityToken
    private static void login(String role, String password) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", role, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                //.when().post("/api/login")
                .when().post("/login")
                .then()
                .extract().path("token");
        //System.out.println("TOKEN ---> " + securityToken);
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
        login("user", "user");

        String expectedName = "Brian Sej";
        String expectedEmail = "xXbrianXx@gmail.com";
        String expectedCompany = "Brians Fælge";
        String expectedJobtitle = "Mekaniker";
        String expectedPhone = "33882277";
        ContactDTO toCreate = new ContactDTO(expectedName, expectedEmail, expectedCompany, expectedJobtitle, expectedPhone);

        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
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
        login("user", "user");

        String expectedName = "Brian Sej";
        String expectedEmail = "xXbrianXx@gmail.com";
        String expectedCompany = "Brians Fælge";
        String expectedJobtitle = "Mekaniker";
        String expectedPhone = "";
        ContactDTO toCreate = new ContactDTO(expectedName, expectedEmail, expectedCompany, expectedJobtitle, expectedPhone);

        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .body(GSON.toJson(toCreate))
                .when()
                .post("/contact/").then()
                .assertThat().statusCode(HttpStatus.BAD_REQUEST_400.getStatusCode())
                .body("message", equalTo("All fields must be set"));
    }

    @Test
    public void testGetAllContacts1() {
        login("user", "user");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .get("/contact/all/").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("all.size()", is(2));
    }

    @Test
    public void testGetContact1() {
        login("user", "user");

        String expectedName = "Jake Peralta";
        String expectedEmail = "cool-jake@nypd.gov";
        String expectedCompany = "New York Police Department";
        String expectedJobtitle = "Detective";
        String expectedPhone = "69420720";

        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .get("/contact/" + c1.getId()).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("name", equalTo(expectedName)).and()
                .body("email", equalTo(expectedEmail)).and()
                .body("company", equalTo(expectedCompany)).and()
                .body("jobtitle", equalTo(expectedJobtitle)).and()
                .body("phone", equalTo(expectedPhone));

    }

    @Test
    public void testGetContactMissing1() {
        login("user", "user");
        int idToGet = 214412;
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .get("/contact/" + idToGet).then()
                .assertThat().statusCode(HttpStatus.NOT_FOUND_404.getStatusCode())
                .body("message", equalTo("No contact found by id " + idToGet));
    }
}
