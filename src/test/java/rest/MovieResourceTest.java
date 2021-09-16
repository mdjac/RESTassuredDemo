/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;
import entities.Movie;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
//Uncomment the line below, to temporarily disable this test
//@Disabled

public class MovieResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Movie m1, m2, m3, m4;
    

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

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
        m1 = new Movie(1990,"Det virkede 1",new String[] {"test1","test1"});
        m2 = new Movie(2000,"Det virkede 2",new String[] {"test2","test2"});
        m3 = new Movie(2010,"Det virkede 3",new String[] {"test3","test3"});
        m4 = new Movie(2020,"Det virkede 4",new String[] {"test4","test4"});
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Movie.deleteAllRows").executeUpdate();
            em.persist(m1);
            em.persist(m2);
            em.persist(m3);
            em.persist(m4);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
       
    }
    
    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given()
                .contentType("application/json")
                .get("/movies").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("key", equalTo("Server is up!"));
    }
    
    @Test
    public void testMovieCount() {
        given()
                .contentType("application/json")
                .get("/movies/count").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("count", equalTo(4));
    }
    
    
    @Test
    public void testGetAllMoviesCountAndTitles() {
        given()
                .contentType("application/json")
                .get("/movies/all").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("title", hasItems(m1.getTitle(),m2.getTitle(),m3.getTitle(),m4.getTitle()))
                .body("$", hasSize(4));
                
    }
    
    @Test
    public void testGetMovieByTitle() {
        given()
                .contentType("application/json")
                .get("/movies/title/"+m2.getTitle()).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("id", is(m2.getId().intValue()));
    }
    
    @Test
    public void testGetMovieById() {
        given()
                .contentType("application/json")
                .get("/movies/"+m2.getId().intValue()).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("title", is(m2.getTitle()));
    }

}
