package fr.elsior.wfx.test.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Author: Elimane
 */
abstract class PetException extends Exception {
    public abstract HttpStatus getStatus();
    public abstract String getMessage();
}
