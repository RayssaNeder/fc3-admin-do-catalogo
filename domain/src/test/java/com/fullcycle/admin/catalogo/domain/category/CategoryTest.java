package com.fullcycle.admin.catalogo.domain.category;

import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import com.fullcycle.admin.catalogo.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CategoryTest {

    @Test
    public void givenAValidParams_whenCallNewCategory_thenInstanciateACategory(){
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

       final var actualCategory =
               Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAnInvalidNullName_whenCallNewCategoryAndValidate_thenShoudReciveError(){
        final String expectedName = null;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

       final var actualException =
               Assertions.assertThrows(DomainException.class,  () -> actualCategory.validate(new ThrowsValidationHandler()));

       Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size() );



    }

    @Test
    public void givenAnInvalidEmptyName_whenCallNewCategoryAndValidate_thenShoudReciveError(){
        final String expectedName = "";
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException =
                Assertions.assertThrows(DomainException.class,  () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size() );
    }


    @Test
    public void givenAnInvalidNameLegthLessThan3_whenCallNewCategoryAndValidate_thenShoudReciveError(){
        final String expectedName = "oi ";
        final var expectedErrorMessage = "'name' should not have size less than 3 or more than 255 characters";
        final var expectedErrorCount = 1;

        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException =
                Assertions.assertThrows(DomainException.class,  () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size() );
    }

    @Test
    public void givenAnInvalidNameLegthMoreThan255_whenCallNewCategoryAndValidate_thenShoudReciveError(){
        final String expectedName = "O que temos que ter sempre em mente é que a valorização de fatores subjetivos desafia a capacidade de equalização das regras de conduta normativas.O que temos que ter sempre em mente é que a valorização de fatores subjetivos desafia a capacidade de equalização das regras de conduta normativas";
        final var expectedErrorMessage = "'name' should not have size less than 3 or more than 255 characters";
        final var expectedErrorCount = 1;

        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException =
                Assertions.assertThrows(DomainException.class,  () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size() );
    }

    @Test
    public void givenAValidEmptyDescription_whenCallNewCategoryAndValidate_thenShouldCreate(){
        final String expectedName = "Filmes";

        final var expectedDescription = "";
        final var expectedIsActive = true;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

                Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidFalseActive_whenCallNewCategoryAndValidate_thenShouldCreate(){
        final String expectedName = "Filmes";

        final var expectedDescription =  "A categoria mais assistida";;
        final var expectedIsActive = false;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }


    @Test
    public void givenAValidActiveCategory_whenCallDeactivate_thenShouldReturnCategoryInactivated(){
        final String expectedName = "Filmes";
        final var expectedDescription =  "A categoria mais assistida";;
        final var expectedIsActive = true;

        final var aCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        final var updatedAt = aCategory.getUpdatedAt();

        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertNotNull(aCategory.getCreatedAt());
        Assertions.assertEquals(updatedAt,aCategory.getUpdatedAt());
        Assertions.assertNull(aCategory.getDeletedAt());

       final var actualCategory = aCategory.deactivate();

        Assertions.assertEquals(actualCategory.getId(),aCategory.getId());
        Assertions.assertEquals(actualCategory.getName(),aCategory.getName());
        Assertions.assertEquals(actualCategory.getDescription(),aCategory.getDescription());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertEquals(actualCategory.getCreatedAt(),aCategory.getCreatedAt());
        Assertions.assertFalse(actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getDeletedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));




    }

    @Test
    public void givenAValidInactivateCategory_whenCallActivate_thenShouldReturnCategoryActivated(){
        final String expectedName = "Filmes";

        final var expectedDescription =  "A categoria mais assistida";;
        final var expectedIsActive = false;

        final var aCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        final var updatedAt = aCategory.getUpdatedAt();

        Assertions.assertFalse(aCategory.isActive());
        Assertions.assertNotNull(aCategory.getCreatedAt());
        Assertions.assertEquals(updatedAt,aCategory.getUpdatedAt());
        Assertions.assertNotNull(aCategory.getDeletedAt());

        final var actualCategory = aCategory.activate();

        Assertions.assertEquals(actualCategory.getId(),aCategory.getId());
        Assertions.assertEquals(actualCategory.getName(),aCategory.getName());
        Assertions.assertEquals(actualCategory.getDescription(),aCategory.getDescription());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertEquals(actualCategory.getCreatedAt(),aCategory.getCreatedAt());


        Assertions.assertTrue(actualCategory.isActive());
        Assertions.assertNull(actualCategory.getDeletedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
    }


    @Test
    public void givenAValidCategory_whenCallUpdate_thenShouldReturnCategoryUpdated(){
        final String expectedName = "Filmes";

        final var expectedDescription =  "A categoria mais assistida";;
        final var expectedIsActive = true;

        final var aCategory =
                Category.newCategory("Film", "A categoria", expectedIsActive);

        Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        final var updatedAt = aCategory.getUpdatedAt();

        final var actualCategory = aCategory.update(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(actualCategory.getId(),aCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertEquals(actualCategory.getCreatedAt(),aCategory.getCreatedAt());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNull(actualCategory.getDeletedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
    }


    @Test
    public void givenAValidCategory_whenCallUpdateToInactivate_thenShouldReturnCategoryDeactvated(){
        final String expectedName = "Filmes";

        final var expectedDescription =  "A categoria mais assistida";;
        final var expectedIsActive = false;

        final var aCategory =
                Category.newCategory(expectedName, expectedDescription, true);

        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertNull(aCategory.getDeletedAt());

        final var updatedAt = aCategory.getUpdatedAt();

        Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        final var actualCategory = aCategory.update(expectedName, expectedDescription, false);

        Assertions.assertEquals(actualCategory.getId(),aCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertEquals(actualCategory.getCreatedAt(),aCategory.getCreatedAt());
        Assertions.assertFalse(actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getDeletedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
    }

    @Test
    public void givenAnInvalidCategory_whenCallUpdateWithInvalidParam_thenShoudReciveError(){
        final String expectedName = null;

        final var expectedDescription =  "A categoria mais assistida";;
        final var expectedIsActive = true;

        final var aCategory =
                Category.newCategory("Filmes", "A Categoria", expectedIsActive);

        Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        final var actualCategory = aCategory.update(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(actualCategory.getId(),aCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertEquals(actualCategory.getCreatedAt(),aCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.isActive());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

}
