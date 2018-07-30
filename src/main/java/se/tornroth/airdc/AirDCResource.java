package se.tornroth.airdc;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

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
