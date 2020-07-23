package com.epam.esm.entity;

import java.io.Serializable;

public interface Identifable extends Serializable {
    static Object getQueryNames() {
        return null;
    }

    long getId();
}
