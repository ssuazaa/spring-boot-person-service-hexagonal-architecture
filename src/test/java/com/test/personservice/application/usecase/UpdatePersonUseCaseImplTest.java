package com.test.personservice.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
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
import com.test.personservice.domain.port.out.PersonCacheOut;
import com.test.personservice.domain.port.out.PersonRepositoryOut;
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
  FindPersonUseCase findPersonUseCase;

  @MockBean
  PersonCacheOut personCacheOut;

  @MockBean
  PersonRepositoryOut personRepositoryOut;

  UpdatePersonUseCase updatePersonUseCase;

  @BeforeEach
  void setUp() {
    this.findPersonUseCase = mock(FindPersonUseCase.class);
    this.personCacheOut = mock(PersonCacheOut.class);
    this.personRepositoryOut = mock(PersonRepositoryOut.class);
    this.updatePersonUseCase = new UpdatePersonUseCaseImpl(this.findPersonUseCase,
        this.personCacheOut, this.personRepositoryOut);
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

    when(this.findPersonUseCase.findById(any(UUID.class))).thenReturn(Mono.just(personDb));
    when(this.personRepositoryOut.save(any(Person.class))).thenReturn(Mono.just(personUpdated));
    when(this.personCacheOut.deleteByKey(any(UUID.class))).thenReturn(Mono.empty());

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
    verify(this.personCacheOut, times(1)).deleteByKey(any(UUID.class));
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
        .consumeErrorWith((Throwable error) ->
            assertInstanceOf(ObjectNotFoundException.class, error))
        .verify();

    verify(this.findPersonUseCase, times(1)).findById(any(UUID.class));
    verify(this.personRepositoryOut, times(0)).save(any(Person.class));
    verify(this.personCacheOut, times(0)).deleteByKey(any(UUID.class));
  }

}
