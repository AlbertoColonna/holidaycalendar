package com.sap.rest;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.domain.PublicHoliday;
import com.sap.domain.PublicHolidayDAO;

@Path("/helloworld")
public class HolidayRESTService extends HttpServlet {
	
	private static final Logger LOG = LoggerFactory.getLogger(HolidayRESTService.class);	
    private PublicHolidayDAO holidayDAO;	
    

    @PostConstruct
    public void init() {
    	 	 
			try {
				
	            InitialContext ctx = new InitialContext();				
				DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/DefaultDB");
				
                try {
					holidayDAO = new PublicHolidayDAO(ds);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            
                LOG.info("Database lookup successfull!");         
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}       
   			
    }
	

/*	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public void sayPlainTextHello() {
		//return "Hello World RESTful Jersey!";	
	}

	@GET
	@Produces(MediaType.TEXT_XML)
	public void sayXMLHello() {
		//return "<?xml version=\"1.0\"?>" + "<hello> Hello World RESTful Jersey"
		//		+ "</hello>";
	} */

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHolidays() {
		
		List<PublicHoliday> list = new ArrayList<PublicHoliday>();
		
		try {
			LOG.error("BEFORE");
			list = (ArrayList<PublicHoliday>) holidayDAO.selectAllHolidays();
			LOG.error("List:"+list.size());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return Response.ok(list).build();

	}
	
	@POST	
	@Produces(MediaType.TEXT_PLAIN)
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})	
    public Response postHolidays( PublicHoliday[] holidays) {
		
		String output = "";
		
		try {
			holidayDAO.deleteAllHolidays();
			
			for(int i=0; i<holidays.length; i++)
			{
				addHoliday(holidays[i]);
			}
		
			
		    return Response.status(200).entity(output).build(); 			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    return Response.status(500).entity(output).build(); 			
		}		
		
	    
    }
	
	
	private void addHoliday(PublicHoliday holiday)
	{
		try {
			holidayDAO.addPublicHoliday(holiday);
			LOG.error("ID of holiday:" + holiday.getId());			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	
    @GET
	@Path("{isholiday}")    
    @Produces("text/html")
    public Response getUser(@PathParam("isholiday") String date) {
        
    	String recordFound = "false";
    	
		try {
			recordFound = holidayDAO.getPublicHoliday(date);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

		return Response.ok(recordFound).build();
    	
    }
	
}	

