package com.test.personservice.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.personservice.domain.model.Person;
import com.test.personservice.domain.port.in.CreatePersonUseCase;
import com.test.personservice.domain.port.out.PersonRepositoryOut;
import com.test.personservice.infrastructure.config.exceptions.BaseException;
import java.util.Objects;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class CreatePersonUseCaseImplTest {

  @MockBean
  PersonRepositoryOut personRepositoryOut;

  CreatePersonUseCase createPersonUseCase;

  @BeforeEach
  void setUp() {
    this.personRepositoryOut = mock(PersonRepositoryOut.class);
    this.createPersonUseCase = new CreatePersonUseCaseImpl(this.personRepositoryOut);
  }

  @Test
  @DisplayName("testSave() -> param person null case")
  void testSavePersonNullCase() {
    // Arrange
    Person person = null;

    // Act & Assert
    assertThatThrownBy(() -> this.createPersonUseCase.create(person))
        .isInstanceOf(BaseException.class)
        .hasFieldOrPropertyWithValue("key", "PERSON_IS_MANDATORY")
        .hasFieldOrPropertyWithValue("statusCode", 400)
        .hasMessage("The person attribute cannot be null");

    verify(this.personRepositoryOut, times(0)).save(any(Person.class));
  }

  @Test
  @DisplayName("testSave() -> Good case")
  void testSave() {
    // Arrange
    var person = Person.builder()
        .firstName("John")
        .lastName("Doe")
        .build();
    var personSaved = person.toBuilder()
        .id(UUID.randomUUID())
        .build();

    when(this.personRepositoryOut.save(any(Person.class)))
        .thenReturn(Mono.just(personSaved));

    // Act
    var result = this.createPersonUseCase.create(person);

    // Assert
    StepVerifier.create(result)
        .expectNextMatches(Objects::nonNull)
        .verifyComplete();

    assertNotNull(result);
    assertThat(personSaved.id()).isNotNull();
    assertThat(personSaved.firstName()).isEqualTo("John");
    assertThat(personSaved.lastName()).isEqualTo("Doe");

    verify(this.personRepositoryOut, times(1)).save(any(Person.class));
  }

}
