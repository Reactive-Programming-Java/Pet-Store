package fr.elsior.wfx.test.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @Author: Elimane
 */
public class PetNotFoundException extends PetException{
    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public String getMessage() {
        return "Pet not found.";
    }
}
