package com.fullcycle.admin.catalogo.domain.category;

import com.fullcycle.admin.catalogo.domain.AggregateRoot;

import java.time.Instant;
import java.util.UUID;

public class Category extends AggregateRoot<CategoryID> {

    private String name;
    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(
            final CategoryID anId,
            final String aName,
            final String aDescription,
            final boolean isActive,
            final Instant aUpdatedAt,
            final Instant aCreatedAt,
            final Instant aDeletedAt) {
        super(anId);
        this.deletedAt = aDeletedAt;
        this.updatedAt = aUpdatedAt;
        this.createdAt = aCreatedAt;
        this.active = isActive;
        this.description = aDescription;
        this.name = aName;

    }

    public static Category newCategory(
            final String aName,
            final String aDescription,
            final boolean active){

       final var id = CategoryID.unique();
       final var now =  Instant.now();
        return new Category(id, aName, aDescription,active, now, now, null);
    }

    //getters ans setters

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public CategoryID getId() {
        return id;
    }

}

