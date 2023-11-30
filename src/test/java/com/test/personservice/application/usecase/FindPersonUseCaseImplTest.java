package com.test.personservice.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.personservice.domain.model.Person;
import com.test.personservice.domain.port.in.FindPersonUseCase;
import com.test.personservice.domain.port.out.PersonCacheOut;
import com.test.personservice.domain.port.out.PersonRepositoryOut;
import com.test.personservice.infrastructure.config.exceptions.BaseException;
import com.test.personservice.infrastructure.config.exceptions.ObjectNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class FindPersonUseCaseImplTest {

  @MockBean
  PersonCacheOut personCacheOut;

  @MockBean
  PersonRepositoryOut personRepositoryOut;

  FindPersonUseCase findPersonUseCase;

  @BeforeEach
  void setUp() {
    this.personCacheOut = mock(PersonCacheOut.class);
    this.personRepositoryOut = mock(PersonRepositoryOut.class);
    this.findPersonUseCase = new FindPersonUseCaseImpl(this.personCacheOut,
        this.personRepositoryOut);
  }

  @Test
  @DisplayName("testFindAll() -> Good case")
  void testFindAll() {
    // Arrange
    var personsSaved = List.of(Person.builder()
        .id(UUID.randomUUID())
        .firstName("John")
        .lastName("Doe")
        .build(), Person.builder()
        .id(UUID.randomUUID())
        .firstName("Mike")
        .lastName("Tyson")
        .build());

    when(this.personRepositoryOut.findAll()).thenReturn(Mono.just(personsSaved));

    // Act
    var result = this.findPersonUseCase.findAll();

    // Assert
    StepVerifier.create(result)
        .consumeNextWith((List<Person> persons) -> assertThat(persons).hasSize(personsSaved.size()))
        .expectComplete()
        .verify();

    verify(this.personRepositoryOut, times(1)).findAll();
  }

  @Test
  @DisplayName("testFindAll() -> Empty case")
  void testFindAllEmptyCase() {
    // Arrange
    List<Person> personsSaved = Collections.emptyList();

    when(this.personRepositoryOut.findAll()).thenReturn(Mono.just(personsSaved));

    // Act
    var result = this.findPersonUseCase.findAll();

    // Assert
    StepVerifier.create(result)
        .consumeNextWith((List<Person> persons) -> assertThat(persons).isEmpty())
        .expectComplete()
        .verify();

    verify(this.personRepositoryOut, times(1)).findAll();
  }

  @Test
  @DisplayName("testFindById() -> Good case without cache")
  void testFindByIdWithoutCache() {
    // Arrange
    var personId = UUID.randomUUID();
    var personSaved = Person.builder()
        .id(personId)
        .firstName("John")
        .lastName("Doe")
        .build();

    when(this.personCacheOut.findByKey(any(UUID.class))).thenReturn(Mono.empty());
    when(this.personRepositoryOut.findById(any(UUID.class))).thenReturn(Mono.just(personSaved));
    when(this.personCacheOut.save(any(UUID.class), any(Person.class))).thenReturn(Mono.empty());

    // Act
    var result = this.findPersonUseCase.findById(personId);

    // Assert
    StepVerifier.create(result)
        .expectNextMatches(Objects::nonNull)
        .verifyComplete();

    assertNotNull(result);
    assertThat(personSaved.id()).isEqualTo(personId);
    assertThat(personSaved.firstName()).isEqualTo("John");
    assertThat(personSaved.lastName()).isEqualTo("Doe");

    verify(this.personCacheOut, times(1)).findByKey(any(UUID.class));
    verify(this.personRepositoryOut, times(1)).findById(any(UUID.class));
    verify(this.personCacheOut, times(1)).save(any(UUID.class), any(Person.class));
  }

  @Test
  @DisplayName("testFindById() -> Good case with cache")
  void testFindByIdWithCache() {
    // Arrange
    var personId = UUID.randomUUID();
    var personSaved = Person.builder()
        .id(personId)
        .firstName("John")
        .lastName("Doe")
        .build();

    when(this.personCacheOut.findByKey(any(UUID.class))).thenReturn(Mono.just(personSaved));

    // Act
    var result = this.findPersonUseCase.findById(personId);

    // Assert
    StepVerifier.create(result)
        .expectNextMatches(Objects::nonNull)
        .verifyComplete();

    assertNotNull(result);
    assertThat(personSaved.id()).isEqualTo(personId);
    assertThat(personSaved.firstName()).isEqualTo("John");
    assertThat(personSaved.lastName()).isEqualTo("Doe");

    verify(this.personCacheOut, times(1)).findByKey(any(UUID.class));
    verify(this.personRepositoryOut, times(0)).findById(any(UUID.class));
    verify(this.personCacheOut, times(0)).save(any(UUID.class), any(Person.class));
  }

  @Test
  @DisplayName("testFindById() -> person not found")
  void testFindByIdPersonNotFound() {
    // Arrange
    var personId = UUID.randomUUID();

    when(this.personCacheOut.findByKey(any(UUID.class))).thenReturn(Mono.empty());
    when(this.personRepositoryOut.findById(any(UUID.class))).thenReturn(Mono.empty());

    // Act
    var result = this.findPersonUseCase.findById(personId);

    // Assert
    StepVerifier.create(result)
        .consumeErrorWith((Throwable error) -> {
          assertInstanceOf(ObjectNotFoundException.class, error);
          assertEquals("PERSON_NOT_FOUND", ((BaseException) error).getKey());
          assertEquals(404, ((BaseException) error).getStatusCode());
          assertEquals(String.format("Person with id '%s' was not found", personId),
              error.getMessage());
        })
        .verify();

    verify(this.personRepositoryOut, times(1)).findById(any(UUID.class));
    verify(this.personRepositoryOut, times(1)).findById(any(UUID.class));
    verify(this.personCacheOut, times(0)).save(any(UUID.class), any(Person.class));
  }

}
