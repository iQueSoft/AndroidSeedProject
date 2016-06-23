package net.iquesoft.project.seed.domain;

import android.text.TextUtils;
import android.util.Patterns;

import net.iquesoft.project.seed.domain.exception.EmptyFieldException;
import net.iquesoft.project.seed.domain.exception.InvalidEmailException;
import net.iquesoft.project.seed.domain.exception.InvalidIpAddressException;
import net.iquesoft.project.seed.domain.exception.InvalidNameException;
import net.iquesoft.project.seed.domain.exception.InvalidPasswordException;
import net.iquesoft.project.seed.domain.exception.InvalidWebUrlException;

import java.util.regex.Pattern;

public class RegularExpressionValidation {

    public boolean isNameValid(String name) throws InvalidNameException, EmptyFieldException {
        String regEx = "[a-zA-Z0-9-_]+";

        if (TextUtils.isEmpty(name)) {
            throw new EmptyFieldException();
        } else if (!Pattern.compile(regEx).matcher(name).matches()) {
            throw new InvalidNameException();
        } else {
            return true;
        }
    }

    public boolean isEmailValid(String email) throws InvalidEmailException, EmptyFieldException {
        String regEx = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        if (TextUtils.isEmpty(email)) {
            throw new EmptyFieldException();
        } else if (!Pattern.compile(regEx).matcher(email).matches()) {
            throw new InvalidEmailException();
        } else {
            return true;
        }
    }

    public boolean isPasswordValid(String password) throws EmptyFieldException, InvalidPasswordException {
        String regEx = "[a-zA-Z0-9]{4,}";
        if (TextUtils.isEmpty(password)) {
            throw new EmptyFieldException();
        } else if (!Pattern.compile(regEx).matcher(password).matches()) {
            throw new InvalidPasswordException();
        } else {
            return true;
        }
    }

    public boolean isIpAddressValid(String ipAddress) throws EmptyFieldException, InvalidIpAddressException {
        if (TextUtils.isEmpty(ipAddress)) {
            throw new EmptyFieldException();
        } else if (!Patterns.IP_ADDRESS.matcher(ipAddress).matches()) {
            throw new InvalidIpAddressException();
        } else {
            return true;
        }

    }

    public boolean isWebUrlValid(String webUrl) throws EmptyFieldException, InvalidWebUrlException {
        if (TextUtils.isEmpty(webUrl)) {
            throw new EmptyFieldException();
        } else if (!Patterns.WEB_URL.matcher(webUrl).matches()) {
            throw new InvalidWebUrlException();
        } else {
            return true;
        }

    }

}
