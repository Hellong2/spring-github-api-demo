package pl.domdom.gapi.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.domdom.gapi.dto.GHUserReposDto;
import pl.domdom.gapi.rest.ApiController;
import pl.domdom.gapi.web.service.GHWebServiceMock;
import pl.domdom.gapi.web.service.exceptions.UserNotFoundException;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApiControllerTest {
    private final ApiController apiController = new ApiController(new GHWebServiceMock());

    @Test
    void testPositiveCase() throws IOException {
        ResponseEntity<List<GHUserReposDto>> validUserdata = apiController.getRepos("validUser");

        assertEquals(HttpStatus.OK, validUserdata.getStatusCode());
        assertNotNull(validUserdata.getBody());
        assertEquals("First", validUserdata.getBody().getFirst().repoName());
        assertEquals("Second", validUserdata.getBody().getLast().repoName());
    }

    @Test
    void testUserNotFoundCase() {
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> apiController.getRepos("invalidUser"));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testServerErrorCase() {
        IOException exception = assertThrows(IOException.class, () -> apiController.getRepos("throwIOException"));

        assertEquals("Test IOException", exception.getMessage());
    }
}
