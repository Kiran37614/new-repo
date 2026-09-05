package com.restaurant.reservation.config;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ConfigurationClassUtils;

/**
 * Bean definitions for {@link WebConfig}.
 */
@Generated
public class WebConfig__BeanDefinitions {
  /**
   * Get the bean definition for 'webConfig'.
   */
  public static BeanDefinition getWebConfigBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(WebConfig.class);
    beanDefinition.setTargetType(WebConfig.class);
    ConfigurationClassUtils.initializeConfigurationClass(WebConfig.class);
    InstanceSupplier<WebConfig> instanceSupplier = InstanceSupplier.using(WebConfig$$SpringCGLIB$$0::new);
    instanceSupplier = instanceSupplier.andThen(WebConfig__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
