package org.cdi.further.metrics;

import com.codahale.metrics.MetricRegistry;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * @author Antoine Sabot-Durand
 */
public class RegistryBeanProducer {


        @Produces
        @ApplicationScoped
        MetricRegistry registry = new MetricRegistry();

}
