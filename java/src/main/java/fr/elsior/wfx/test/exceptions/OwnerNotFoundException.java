package fr.elsior.wfx.test.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @Author: Elimane
 */
public class OwnerNotFoundException extends PetException{
    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public String getMessage() {
        return "Owner not found.";
    }
}
