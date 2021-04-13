package com.stdout.springMem;

import java.lang.instrument.UnmodifiableClassException;

public class SpringMemModels {

    // models
    public static String exit() throws UnmodifiableClassException {
        return SpringMemShell.back();
    }



}
