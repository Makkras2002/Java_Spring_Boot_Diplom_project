package com.makkras.shop.validator.impl;

import com.makkras.shop.entity.SupplierCompany;
import com.makkras.shop.validator.SupplierDataValidator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CustomSupplierDataValidator implements SupplierDataValidator {
    private static final int MIN_TEXT_LENGTH = 3;
    private static final int MAX_NAME_AND_EMAIL_AND_LOCATION_LENGTH = 100;
    private static final String PHONE_PATTERNS = """
            ^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$
            |^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$
            |^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$""";
    private static final String INVALID_NAME = "Неверное название поставщика";
    private static final String INVALID_EMAIL = "Неверный email поставщика";
    private static final String INVALID_LOCATION = "Неверное местоположение поставщика";
    private static final String INVALID_PHONE_NUMBER = "Неверный номер телефона поставщика";

    @Override
    public boolean validateSupplierData(SupplierCompany company, List<String> invalidParams) {
        Pattern pattern = Pattern.compile(CustomUserDataValidator.EMAIL_REGEX);
        Matcher matcher = pattern.matcher(company.getSupplierCompanyEmail());
        Pattern phonePattern = Pattern.compile(PHONE_PATTERNS);
        Matcher phonePatternMatcher = phonePattern.matcher(company.getTelNumber());
        if(company.getSupplierCompanyName().length() < MIN_TEXT_LENGTH ||
                company.getSupplierCompanyName().length() > MAX_NAME_AND_EMAIL_AND_LOCATION_LENGTH) {
            invalidParams.add(INVALID_NAME);
        }

        if(!matcher.matches() ||
                company.getSupplierCompanyEmail().length() < MIN_TEXT_LENGTH ||
                company.getSupplierCompanyEmail().length() > MAX_NAME_AND_EMAIL_AND_LOCATION_LENGTH) {
            invalidParams.add(INVALID_EMAIL);
        }

        if(company.getLocation().length() < MIN_TEXT_LENGTH ||
                company.getLocation().length()>MAX_NAME_AND_EMAIL_AND_LOCATION_LENGTH) {
            invalidParams.add(INVALID_LOCATION);
        }
        if(!phonePatternMatcher.matches()) {
            invalidParams.add(INVALID_PHONE_NUMBER);
        }
        return invalidParams.size()==0;
    }
}
