package com.thepowermisha.document.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DocumentNumberGenerator {
    public String generate(){
        return "DocNum-" + System.nanoTime();
    }
}
