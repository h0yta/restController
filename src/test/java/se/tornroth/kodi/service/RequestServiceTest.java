package se.tornroth.kodi.service;

import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.tornroth.kodi.entity.Mediaplayer;
import se.tornroth.kodi.entity.Request;
import se.tornroth.kodi.entity.RequestType;

import static org.testng.Assert.*;

public class RequestServiceTest {

    @InjectMocks
    RequestService sut;

    @BeforeMethod
    public void beforeMethod() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateRequest() throws Exception {
        Request result = sut.createRequest(Mediaplayer.BASEMENT, "movie Sixth Sense");
        assertEquals(result.getType(), RequestType.MOVIE);
        assertEquals(result.getTitle(), "sixth sense");
    }

}