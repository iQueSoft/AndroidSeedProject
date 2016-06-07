package net.iquesoft.project.seed.domain.exception;

import android.content.Context;

public class ErrorMessageFactory {

    private ErrorMessageFactory() {
    }


    /**
     * Creates a String representing an error message.
     *
     * @param context   Context needed to retrieve string resources.
     * @param exception An exception used as a condition to retrieve the correct error message.
     * @return {@link String} an error message.
     */
    public static String create(Context context, Class<Exception> exception) {

        /*
         * Here should be realised message creating logic.
         *  Define a type of the error and return correct message
         */
        return "Error message";
    }
}
