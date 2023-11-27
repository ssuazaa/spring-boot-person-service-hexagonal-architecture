package com.test.personservice.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.personservice.domain.model.Person;
import com.test.personservice.domain.port.in.FindPersonUseCase;
import com.test.personservice.domain.port.in.UpdatePersonUseCase;
import com.test.personservice.domain.port.out.PersonRepositoryOut;
import com.test.personservice.infrastructure.config.exceptions.BaseException;
import com.test.personservice.infrastructure.config.exceptions.ObjectNotFoundException;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class UpdatePersonUseCaseImplTest {

  @MockBean
  PersonRepositoryOut personRepositoryOut;

  @MockBean
  FindPersonUseCase findPersonUseCase;

  UpdatePersonUseCase updatePersonUseCase;

  @BeforeEach
  void setUp() {
    this.personRepositoryOut = mock(PersonRepositoryOut.class);
    this.findPersonUseCase = mock(FindPersonUseCase.class);
    this.updatePersonUseCase = new UpdatePersonUseCaseImpl(this.findPersonUseCase,
        this.personRepositoryOut);
  }

  @Test
  @DisplayName("testUpdate() -> param id null case")
  void testUpdateIdNullCase() {
    // Arrange
    UUID personId = null;
    var personRequest = Person.builder().build();

    // Act & Assert
    assertThatThrownBy(() -> this.updatePersonUseCase.update(personId, personRequest))
        .isInstanceOf(BaseException.class)
        .hasFieldOrPropertyWithValue("key", "ID_IS_MANDATORY")
        .hasFieldOrPropertyWithValue("statusCode", 400)
        .hasMessage("The id attribute is mandatory");

    verify(this.findPersonUseCase, times(0)).findById(any(UUID.class));
    verify(this.personRepositoryOut, times(0)).save(any(Person.class));
  }

  @Test
  @DisplayName("testUpdate() -> param person null case")
  void testUpdatePersonNullCase() {
    // Arrange
    var personId = UUID.randomUUID();
    Person personRequest = null;

    // Act & Assert
    assertThatThrownBy(() -> this.updatePersonUseCase.update(personId, personRequest))
        .isInstanceOf(BaseException.class)
        .hasFieldOrPropertyWithValue("key", "PERSON_IS_MANDATORY")
        .hasFieldOrPropertyWithValue("statusCode", 400)
        .hasMessage("The person attribute cannot be null");

    verify(this.findPersonUseCase, times(0)).findById(any(UUID.class));
    verify(this.personRepositoryOut, times(0)).save(any(Person.class));
  }

  @Test
  @DisplayName("testUpdate() -> Good case")
  void testUpdate() {
    // Arrange
    var personId = UUID.randomUUID();
    var personRequest = Person.builder()
        .firstName("John 2")
        .lastName("Doe 2")
        .build();
    var personDb = Person.builder()
        .id(personId)
        .firstName("John")
        .lastName("Doe")
        .build();
    var personUpdated = personRequest.toBuilder()
        .id(personId)
        .build();

    when(this.findPersonUseCase.findById(any(UUID.class)))
        .thenReturn(Mono.just(personDb));
    when(this.personRepositoryOut.save(any(Person.class)))
        .thenReturn(Mono.just(personUpdated));

    // Act
    var result = this.updatePersonUseCase.update(personId, personRequest);

    // Assert
    StepVerifier.create(result)
        .expectComplete()
        .verify();

    assertNotNull(result);
    assertThat(personUpdated.id()).isEqualTo(personId);
    assertThat(personUpdated.firstName()).isEqualTo(personRequest.firstName());
    assertThat(personUpdated.lastName()).isEqualTo(personRequest.lastName());

    verify(this.findPersonUseCase, times(1)).findById(any(UUID.class));
    verify(this.personRepositoryOut, times(1)).save(any(Person.class));
  }

  @Test
  @DisplayName("testUpdate() -> person not found")
  void testUpdatePersonNotFound() {
    // Arrange
    var personId = UUID.randomUUID();
    var personRequest = Person.builder().build();

    when(this.findPersonUseCase.findById(any(UUID.class)))
        .thenReturn(Mono.error(() -> new ObjectNotFoundException("PERSON_NOT_FOUND",
            String.format("Person with id '%s' was not found", personId))));

    // Act
    var result = this.updatePersonUseCase.update(personId, personRequest);

    // Assert
    StepVerifier.create(result)
        .consumeErrorWith((Throwable error) -> {
          assertInstanceOf(ObjectNotFoundException.class, error);
        })
        .verify();

    verify(this.findPersonUseCase, times(1)).findById(any(UUID.class));
    verify(this.personRepositoryOut, times(0)).save(any(Person.class));
  }

}
