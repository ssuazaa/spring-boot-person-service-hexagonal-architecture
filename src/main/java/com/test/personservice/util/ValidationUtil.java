package com.test.personservice.util;

import com.test.personservice.infrastructure.config.exceptions.ConstraintViolationException;
import java.util.Objects;

public class ValidationUtil {

  private ValidationUtil() {

  }

  public static void validateParamOrThrow(Object param, String key, String message) {
    if (Objects.isNull(param)) {
      throw new ConstraintViolationException(key, message);
    }
  }

}
