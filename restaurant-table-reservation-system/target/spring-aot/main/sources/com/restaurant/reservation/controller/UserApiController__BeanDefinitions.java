package com.restaurant.reservation.controller;

import com.restaurant.reservation.service.UserService;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link UserApiController}.
 */
@Generated
public class UserApiController__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'userApiController'.
   */
  private static BeanInstanceSupplier<UserApiController> getUserApiControllerInstanceSupplier() {
    return BeanInstanceSupplier.<UserApiController>forConstructor(UserService.class)
            .withGenerator((registeredBean, args) -> new UserApiController(args.get(0)));
  }

  /**
   * Get the bean definition for 'userApiController'.
   */
  public static BeanDefinition getUserApiControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(UserApiController.class);
    beanDefinition.setInstanceSupplier(getUserApiControllerInstanceSupplier());
    return beanDefinition;
  }
}
