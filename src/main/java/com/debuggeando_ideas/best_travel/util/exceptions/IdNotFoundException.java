package com.debuggeando_ideas.best_travel.util.exceptions;

public class IdNotFoundException extends RuntimeException{
    //todas las exepciones que maneja Spring son de tipo RuntimeExeption
    private static final String ERROR_MESSAGE = "Record no exist in %s";

    public IdNotFoundException(String tableName) {
        super(String.format(ERROR_MESSAGE,tableName));
    }
}
