package com.fullcycle.admin.catalogo.domain.category;

import com.fullcycle.admin.catalogo.domain.AggregateRoot;
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler;

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
            final boolean isActive){

       final var id = CategoryID.unique();
       final var now =  Instant.now();
       final var deletedAt = isActive ? null : now;

       return new Category(id, aName, aDescription, isActive, now, now, deletedAt);

    }

    //getters

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public boolean isActive() {
        return active;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public CategoryID getId() {
        return id;
    }

    //validação

    @Override
    public void validate(final ValidationHandler handler) {
        new CategoryValidator(this, handler).validate();
    }


    public Category deactivate(){
        if(getDeletedAt() == null){
            this.deletedAt = Instant.now();

        }

        this.active = false;
        this.updatedAt = Instant.now();
        return this;
    }

    public Category activate(){
        this.active = true;
        this.updatedAt = Instant.now();
        this.deletedAt = null;

        return this;
    }

}

