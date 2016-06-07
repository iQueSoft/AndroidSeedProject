package net.iquesoft.project.seed.domain;

import android.text.TextUtils;
import android.util.Patterns;

import net.iquesoft.project.seed.utils.Constants;

import java.util.regex.Pattern;

public class RegularExpressionValidation {

    public int isNameValid(String name) {
        String regEx = "[a-zA-Z0-9-_]+";

        if (TextUtils.isEmpty(name)) {
            return Constants.ERROR_EMPTY_NAME;
        } else if (!Pattern.compile(regEx).matcher(name).matches()) {
            return Constants.ERROR_INVALID_NAME;
        } else {
            return Constants.CODE_OK;
        }
    }

    public int isEmailValid(String email) {
        String regEx = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        if (TextUtils.isEmpty(email)) {
            return Constants.ERROR_EMPTY_EMAIL;
        } else if (!Pattern.compile(regEx).matcher(email).matches()) {
            return Constants.ERROR_INVALID_EMAIL;
        } else {
            return Constants.CODE_OK;
        }
    }

    public int isPasswordValid(String password) {
        String regEx = "[a-zA-Z0-9]{4,}";
        if (TextUtils.isEmpty(password)) {
            return Constants.ERROR_EMPTY_PASSWORD;
        } else if (!Pattern.compile(regEx).matcher(password).matches()) {
            return Constants.ERROR_INVALID_PASSWORD;
        } else {
            return Constants.CODE_OK;
        }
    }

    public int isIpAddressValid(String ipAddress) {
        if (TextUtils.isEmpty(ipAddress)) {
            return Constants.ERROR_EMPTY_FIELD;
        } else if (!Patterns.IP_ADDRESS.matcher(ipAddress).matches()) {
            return Constants.ERROR_INVALID_IP_ADDRESS;
        } else {
            return Constants.CODE_OK;
        }

    }

    public int isWebUrlValid(String webUrl) {
        if (TextUtils.isEmpty(webUrl)) {
            return Constants.ERROR_EMPTY_FIELD;
        } else if (!Patterns.WEB_URL.matcher(webUrl).matches()) {
            return Constants.ERROR_INVALID_WEB_URL;
        } else {
            return Constants.CODE_OK;
        }

    }

}
