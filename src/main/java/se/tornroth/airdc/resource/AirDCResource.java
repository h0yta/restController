package se.tornroth.airdc.resource;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import se.tornroth.airdc.service.AirDCService;

@Path("airdc")
public class AirDCResource {

	@Inject
	private AirDCService airDCService;

	@POST
	@Path("download/{item}")
	public void pause(@PathParam("item") String request) {
		airDCService.download(request);
	}
}
