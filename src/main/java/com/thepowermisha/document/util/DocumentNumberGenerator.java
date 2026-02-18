package com.thepowermisha.document.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;

@UtilityClass
public class DocumentNumberGenerator {
    public String generate(){
        return "DocNum-" + System.nanoTime();
    }
}
