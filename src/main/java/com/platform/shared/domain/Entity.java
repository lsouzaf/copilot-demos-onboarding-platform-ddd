package com.platform.shared.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 * Base class for all Entities in the domain.
 * Entities have identity and their equality is based on their ID.
 */
public abstract class Entity<ID extends Serializable> implements Serializable {
    
    protected ID id;
    
    protected Entity() {
    }
    
    protected Entity(ID id) {
        this.id = id;
    }
    
    public ID getId() {
        return id;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity<?> entity = (Entity<?>) o;
        return Objects.equals(id, entity.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
