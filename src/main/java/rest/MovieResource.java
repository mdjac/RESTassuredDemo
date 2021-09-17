/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.MovieDTO;
import utils.EMF_Creator;
import facades.FacadeExample;
import facades.MovieFacade;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//Todo Remove or change relevant parts before ACTUAL use
@Path("movies")
public class MovieResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
       
    private static final MovieFacade FACADE =  MovieFacade.getMovieFacade(EMF);
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
           
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String serverIsUp() {
        return "{\"key\":\"Server is up!\"}";
    }
    
    
    @Path("all")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON}) 
    public String getAllMovies() {
        List<MovieDTO> dtos = FACADE.getAll();
        return gson.toJson(dtos);
    }
    
    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public String getMovieById(@PathParam("id") int id) {
        MovieDTO dto = FACADE.getById(id);
        return gson.toJson(dto);
    }
    
    @Path("count")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getMovieCount(){
        return "{\"count\":"+FACADE.countMovies()+"}";
    }
    
    @Path("title/{title}")
    @GET
    @Produces(MediaType.APPLICATION_JSON) 
    public Response getMovieByTitle(@PathParam("title") String title){
        MovieDTO dto;
        dto = FACADE.getMovieByTitle(title);
        if (dto == null){
            return Response.status(Response.Status.NOT_FOUND).entity("Movie not found").build();
        }
        return Response.ok(gson.toJson(dto), MediaType.APPLICATION_JSON).build();
    }
    
    @Path("add")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String addMovie (String movie){
        MovieDTO mvDTO = gson.fromJson(movie, MovieDTO.class);
        mvDTO = FACADE.create(mvDTO);
        return gson.toJson(mvDTO);
    }
}
