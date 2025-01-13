package fr.elsior.wfx.test.ws;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: Elimane
 */
@RestController
@RequestMapping("/commands")
public class CommandsRestController {

    /**
     * Health check endpoint.
     * Verifies if the service is up and running.
     *
     * @return HTTP 200 (OK) with the response "pong".
     */
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return new ResponseEntity<>("pong", HttpStatus.OK);
    }

}
