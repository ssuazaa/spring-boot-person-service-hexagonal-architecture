package com.test.personservice.infrastructure.config.bean;

import com.test.personservice.application.usecase.CreatePersonUseCaseImpl;
import com.test.personservice.application.usecase.DeletePersonUseCaseImpl;
import com.test.personservice.application.usecase.FindPersonUseCaseImpl;
import com.test.personservice.application.usecase.UpdatePersonUseCaseImpl;
import com.test.personservice.domain.port.in.CreatePersonUseCase;
import com.test.personservice.domain.port.in.DeletePersonUseCase;
import com.test.personservice.domain.port.in.FindPersonUseCase;
import com.test.personservice.domain.port.in.UpdatePersonUseCase;
import com.test.personservice.domain.port.out.PersonCacheOut;
import com.test.personservice.domain.port.out.PersonRepositoryOut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

  private final PersonCacheOut personCacheOut;
  private final PersonRepositoryOut personRepositoryOut;

  public BeanConfig(PersonCacheOut personCacheOut, PersonRepositoryOut personRepositoryOut) {
    this.personCacheOut = personCacheOut;
    this.personRepositoryOut = personRepositoryOut;
  }

  @Bean
  public FindPersonUseCase findPersonUseCase() {
    return new FindPersonUseCaseImpl(this.personCacheOut, this.personRepositoryOut);
  }

  @Bean
  public CreatePersonUseCase createPersonUseCase() {
    return new CreatePersonUseCaseImpl(this.personRepositoryOut);
  }

  @Bean
  public UpdatePersonUseCase updatePersonUseCase() {
    return new UpdatePersonUseCaseImpl(findPersonUseCase(), this.personCacheOut,
        this.personRepositoryOut);
  }

  @Bean
  public DeletePersonUseCase deletePersonUseCase() {
    return new DeletePersonUseCaseImpl(findPersonUseCase(), this.personCacheOut,
        this.personRepositoryOut);
  }

}
