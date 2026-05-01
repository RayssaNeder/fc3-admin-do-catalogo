package com.fullcycle.admin.catalogo.application.category.create;

public record CreateCategoryCommand(
        String name,
        String Description,
        boolean isActive
) {

    public static CreateCategoryCommand with(
            final String aName,
            final String aDescription,
            final boolean isActive){

        return new CreateCategoryCommand(aName,aDescription, isActive );

        }
}
