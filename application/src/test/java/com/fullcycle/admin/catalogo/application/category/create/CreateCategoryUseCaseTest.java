package com.fullcycle.admin.catalogo.application.category.create;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import com.fullcycle.admin.catalogo.domain.validation.Error;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryUseCaseTest {

    @InjectMocks
    private DefaultCreateCategoryUseCase useCase;

    @Mock
    CategoryGateway categoryGateway;

    //1. Teste Caminho Feliz
    @Test
    public void givenAvalidCommand_whenCallsCreateACategory_shouldReturnCategoryId(){
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedCategory = Category.newCategory(expectedName,expectedDescription,expectedIsActive);

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription,expectedIsActive);

        when(categoryGateway.create(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        verify(categoryGateway, times(1))
                .create(argThat(aCategory -> {

                    return Objects.equals(expectedName, aCategory.getName())
                            && Objects.equals(expectedDescription, aCategory.getDescription())
                            && Objects.equals(expectedIsActive, aCategory.isActive())
                            && Objects.nonNull(aCategory.getId())
                            && Objects.nonNull(aCategory.getCreatedAt())
                            && Objects.nonNull(aCategory.getUpdatedAt())
                            && Objects.isNull(aCategory.getDeletedAt());
                }

                ));

    }

    //2. Teste passando uma propriedade inválida (name)
    @Test
    public void givenAInvalidName_whenCallsCreateACategoty_shouldReturnDomainException(){
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedMessage = "'name' should not be null";
        final var expectesErrorCount = 1;

        final var aCommand = CreateCategoryCommand.with(null, expectedDescription,expectedIsActive);

        final var notification = useCase.execute(aCommand).getLeft(); // chamada que deve falhar


        Assertions.assertEquals(expectedMessage, notification.firstError().message());
        Assertions.assertEquals(1, notification.getErrors().size());
        //valida que o gateway nao tenha sido chamado nenhuma vez
        verify(categoryGateway, times(0)).create(any());

    }


    //3. Teste criando uma categoria inativa
    @Test
    public void givenAValidCommandWithaInativeCategory_whenCreateCategory_shoudRetornCategoryId(){
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var expectedCategory = Category.newCategory(expectedName,expectedDescription,expectedIsActive);

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription,expectedIsActive);

        when(categoryGateway.create(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        verify(categoryGateway, times(1))
                .create(argThat(aCategory -> {

                            return Objects.equals(expectedName, aCategory.getName())
                                    && Objects.equals(expectedDescription, aCategory.getDescription())
                                    && Objects.equals(expectedIsActive, aCategory.isActive())
                                    && Objects.nonNull(aCategory.getId())
                                    && Objects.nonNull(aCategory.getCreatedAt())
                                    && Objects.nonNull(aCategory.getUpdatedAt())
                                    && Objects.nonNull(aCategory.getDeletedAt());
                        }

                ));

    }


    //4. Teste simulando um erro generico vindo do gateway
    @Test
    public void givenAInvalidCommand_whenGatewayThrowsRandomException_shouldReturnAException(){
        final var expectedDescription = "A categoria mais assistida";
        final var expectedName = "Filmes";
        final var expectedIsActive = true;
        final var expectedMessage = "Gateway Error";
        final var expectesErrorCount = 1;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription,expectedIsActive);

        when(categoryGateway.create(any())).thenThrow(new IllegalStateException("Gateway Error"));


               final var notification = useCase.execute(aCommand).getLeft(); // chamada que deve falhar


        Assertions.assertEquals(expectesErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedMessage,notification.firstError().message());

        //valida que o gateway tenha sido chamado 1 vez

        verify(categoryGateway, times(1))
                .create(argThat(aCategory -> {

                            return Objects.equals(expectedName, aCategory.getName())
                                    && Objects.equals(expectedDescription, aCategory.getDescription())
                                    && Objects.equals(expectedIsActive, aCategory.isActive())
                                    && Objects.nonNull(aCategory.getId())
                                    && Objects.nonNull(aCategory.getCreatedAt())
                                    && Objects.nonNull(aCategory.getUpdatedAt())
                                    && Objects.isNull(aCategory.getDeletedAt());
                        }

                ));

    }

}
