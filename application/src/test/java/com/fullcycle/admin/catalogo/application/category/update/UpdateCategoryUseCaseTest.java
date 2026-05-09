package com.fullcycle.admin.catalogo.application.category.update;


import com.fullcycle.admin.catalogo.application.category.create.CreateCategoryCommand;
import com.fullcycle.admin.catalogo.application.category.create.DefaultCreateCategoryUseCase;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)   //usada em testes JUnit 5 para integrar o Mockito ao ciclo de vida do teste. Ativa o suporte do Mockito: permite usar anotações como @Mock, @InjectMocks e @Spy sem precisar inicializar manualmente os mocks.Gerencia a criação e injeção de dependências simuladas: o MockitoExtension cuida de instanciar os mocks e injetá-los nas classes de teste.Simplifica o código de teste: elimina a necessidade de chamar MockitoAnnotations.openMocks(this) no método @BeforeEach.
public class UpdateCategoryUseCaseTest {

    @InjectMocks //  Cria uma instância real da classe que você quer testar. O Mockito injeta automaticamente os objetos anotados com @Mock (ou @Spy) nos campos dessa classe.Isso permite testar a lógica da classe em si, mas com dependências simuladas.
    private DefaultUpdateCategoryUseCase useCase;

    @Mock //Cria um mock isolado de uma classe ou interface.Esse objeto simulado não tem lógica real, apenas responde conforme você configurar com when(...) ou doReturn(...).É útil para substituir dependências externas em testes.
    CategoryGateway categoryGateway;


    //Teste caminhp feliz
    @Test
    public void givenAvalidCommand_whenCallsUpdateACategory_shouldReturnCategoryId(){

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var aCategory = Category.newCategory("Film", null,true);

        final var expectedID = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(expectedID.getValue() , expectedName, expectedDescription,expectedIsActive);

        when(categoryGateway.findById(eq(expectedID))).thenReturn(Optional.of(aCategory.clone()));

        when(categoryGateway.update(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();


        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        verify(categoryGateway, times(1)).findById(eq(expectedID));

        verify(categoryGateway, times(1))
                .update(argThat(aUpdatedCategory -> {

                            return Objects.equals(expectedName, aUpdatedCategory.getName())
                                    && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                                    && Objects.equals(expectedIsActive, aUpdatedCategory.isActive())
                                    && Objects.nonNull(aUpdatedCategory.getId())
                                    && Objects.equals(expectedID, aUpdatedCategory.getId())
                                    && Objects.nonNull(aUpdatedCategory.getCreatedAt())
                                    && Objects.equals(aCategory.getCreatedAt(), aUpdatedCategory.getCreatedAt())
                                    && aCategory.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                                    && Objects.nonNull(aUpdatedCategory.getUpdatedAt())
                                    && Objects.isNull(aUpdatedCategory.getDeletedAt());
                        }

                ));

    }

    // Teste passando um apropriedade inválida (name)
    @Test
    public void givenAInvalidName_whenCallsUpdateACategoty_shouldReturnDomainException(){
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedMessage = "'name' should not be null";
        final var expectesErrorCount = 1;
        final var aCategory = Category.newCategory("Film", null,true);

        final var expectedID = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(expectedID.getValue(),null, expectedDescription,expectedIsActive);

        when(categoryGateway.findById(eq(expectedID))).thenReturn(Optional.of(aCategory.clone()));

        final var notification = useCase.execute(aCommand).getLeft(); // chamada que deve falhar


        Assertions.assertEquals(expectedMessage, notification.firstError().message());
        Assertions.assertEquals(expectesErrorCount, notification.getErrors().size());
        //valida que o gateway nao tenha sido chamado nenhuma vez
        verify(categoryGateway, times(0)).update(any());

        verify(categoryGateway, times(1)).findById(eq(expectedID));

    }


    // Teste atualizando uma categoria para inativa
    @Test
    public void givenAValidInactivateCommand_whenUpdateACategory_shoudRetornInativatedCategory(){
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var aCategory = Category.newCategory("Fil",null,true);


        Assertions.assertNotNull(aCategory.activate());
        Assertions.assertNull(aCategory.getDeletedAt());

        final var expectedID = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(expectedID.getValue(), expectedName, expectedDescription,expectedIsActive);

        when(categoryGateway.findById(eq(expectedID))).thenReturn(Optional.of(aCategory.clone()));

        when(categoryGateway.update(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        verify(categoryGateway, times(1))
                .update(argThat(aUpdatedCategory -> {

                            return Objects.equals(expectedName, aUpdatedCategory.getName())
                                    && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                                    && Objects.equals(expectedIsActive, aUpdatedCategory.isActive())
                                    && Objects.nonNull(aUpdatedCategory.getId())
                                    && Objects.nonNull(aUpdatedCategory.getCreatedAt())
                                    && Objects.nonNull(aUpdatedCategory.getUpdatedAt())
                                    && Objects.nonNull(aUpdatedCategory.getDeletedAt());
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

        final var aCategory = Category.newCategory("Fil",null,true);

        final var expectedID = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(expectedID.getValue(), expectedName, expectedDescription,expectedIsActive);

        when(categoryGateway.findById((expectedID))).thenReturn(Optional.of(aCategory.clone()));

        when(categoryGateway.update(any())).thenThrow(new IllegalStateException("Gateway Error"));

        final var notification = useCase.execute(aCommand).getLeft(); // chamada que deve falhar


        Assertions.assertEquals(expectesErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedMessage,notification.firstError().message());

        //valida que o gateway tenha sido chamado 1 vez

        verify(categoryGateway, times(1))
                .update(argThat(aUpdatedCategory -> {
                        return Objects.equals(expectedName, aUpdatedCategory.getName())
                                && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                                && Objects.equals(expectedIsActive, aUpdatedCategory.isActive())
                                && Objects.nonNull(aUpdatedCategory.getId())
                                && Objects.nonNull(aUpdatedCategory.getCreatedAt())
                                && Objects.nonNull(aUpdatedCategory.getUpdatedAt())
                                && Objects.isNull(aUpdatedCategory.getDeletedAt());
                        }

                ));

    }

    // Teste atualizar categoria passando ID inválido
    @Test
    public void givenACommandWithaInvalidCategoryId_whenCallsUpdateCategory_shouldReturnANotFoundException(){
        final var expectedDescription = "A categoria mais assistida";
        final var expectedName = "Filmes";
        final var expectedIsActive = true;
        final var expectedMessage = "Category with ID 123 was not found";
        final var expectesErrorCount = 1;


        final var expectedID = "123";

        final var aCommand = UpdateCategoryCommand.with(expectedID, expectedName, expectedDescription,expectedIsActive);

        when(categoryGateway.findById((CategoryID.from(expectedID)))).thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(DomainException.class, () -> useCase.execute(aCommand));


        Assertions.assertEquals(expectedMessage,actualException.getErrors().get(0).message());

        //valida que o gateway tenha sido chamado 0 vezes
        verify(categoryGateway, times(0))
                .update(any());

        Assertions.assertEquals(expectesErrorCount, actualException.getErrors().size());


    }
}
