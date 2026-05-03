package com.fullcycle.admin.catalogo.application.category.update;

import com.fullcycle.admin.catalogo.domain.category.CategoryID;

public record UpdateCategoryCommand(
        String id,
        String name,
        String Description,
        boolean isActive
) {

    public static UpdateCategoryCommand with(
            final String id,
            final String aName,
            final String aDescription,
            final boolean isActive){

        return new UpdateCategoryCommand(id,aName,aDescription, isActive );

        }
}
