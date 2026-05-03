package com.fullcycle.admin.catalogo.application.category.update;


import com.fullcycle.admin.catalogo.application.category.create.CreateCategoryCommand;
import com.fullcycle.admin.catalogo.application.category.create.DefaultCreateCategoryUseCase;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
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

        when(categoryGateway.findById(eq(expectedID))).thenReturn(Optional.of(aCategory));

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
                                  //  && aCategory.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt())         TODO fix assertation
                                    && Objects.nonNull(aUpdatedCategory.getUpdatedAt())
                                    && Objects.isNull(aUpdatedCategory.getDeletedAt());
                        }

                ));

    }

    // Teste passando um apropriedade inválida (name)
    // Teste atualizando uma categoria para inativa
    // Teste simulando um erro genérico vindo do gateway
    // Teste atualizar categoria passando ID inválido
}
