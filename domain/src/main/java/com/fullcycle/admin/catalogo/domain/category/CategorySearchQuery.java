package com.fullcycle.admin.catalogo.domain.category;

public record CategorySearchQuery(
        int page,
        int perPage,
        String terms, //trecho a ser filtrado
        String sort,
        String direction
) {
}
