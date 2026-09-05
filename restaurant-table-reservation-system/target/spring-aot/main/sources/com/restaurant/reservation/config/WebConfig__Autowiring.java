package com.restaurant.reservation.config;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link WebConfig}.
 */
@Generated
public class WebConfig__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static WebConfig apply(RegisteredBean registeredBean, WebConfig instance) {
    AutowiredFieldValueResolver.forRequiredField("uploadDir").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
