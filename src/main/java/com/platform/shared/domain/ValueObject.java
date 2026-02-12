package com.platform.shared.domain;

import java.io.Serializable;

/**
 * Base class for all Value Objects in the domain.
 * Value Objects are immutable and equality is based on their attributes.
 */
public abstract class ValueObject implements Serializable {
    
    @Override
    public abstract boolean equals(Object obj);
    
    @Override
    public abstract int hashCode();
}
