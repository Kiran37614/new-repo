package com.restaurant.reservation;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ReservationApplication}.
 */
@Generated
public class ReservationApplication__BeanDefinitions {
  /**
   * Get the bean definition for 'reservationApplication'.
   */
  public static BeanDefinition getReservationApplicationBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ReservationApplication.class);
    beanDefinition.setInstanceSupplier(ReservationApplication::new);
    return beanDefinition;
  }
}
