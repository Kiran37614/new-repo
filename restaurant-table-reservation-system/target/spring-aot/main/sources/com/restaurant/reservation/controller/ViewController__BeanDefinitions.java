package com.restaurant.reservation.controller;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ViewController}.
 */
@Generated
public class ViewController__BeanDefinitions {
  /**
   * Get the bean definition for 'viewController'.
   */
  public static BeanDefinition getViewControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ViewController.class);
    beanDefinition.setInstanceSupplier(ViewController::new);
    return beanDefinition;
  }
}
