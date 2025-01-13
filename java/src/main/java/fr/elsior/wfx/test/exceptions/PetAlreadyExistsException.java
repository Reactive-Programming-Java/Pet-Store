package fr.elsior.wfx.test.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Author: Elimane
 */
public class PetAlreadyExistsException extends PetException {
    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }

    @Override
    public String getMessage() {
        return "Pet already exists in database";
    }
}
