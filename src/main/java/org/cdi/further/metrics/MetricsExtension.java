package org.cdi.further.metrics;


import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessProducer;
import javax.enterprise.inject.spi.Producer;
import javax.enterprise.util.Nonbinding;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.annotation.Metric;
import com.codahale.metrics.annotation.Timed;

/**
 * @author Antoine Sabot-Durand
 */
public class MetricsExtension implements Extension {

    void addMetricAsQualifier(@Observes BeforeBeanDiscovery bdd) {
        bdd.addQualifier(Metric.class);
    }

    void addTimedBinding(@Observes BeforeBeanDiscovery bdd, BeanManager bm) {

        bdd.configureInterceptorBinding(Timed.class).methods().forEach(m -> m.add(Nonbinding.Literal.INSTANCE));
    }

    <T extends com.codahale.metrics.Metric> void decorateMetricProducer(@Observes ProcessProducer<?, T> pp, BeanManager bm) {
        if (pp.getAnnotatedMember().isAnnotationPresent(Metric.class)) {
            String name = pp.getAnnotatedMember().getAnnotation(Metric.class).name();
            Producer<T> producer = pp.getProducer();

            pp.configureProducer().produceWith(ctx -> {
                MetricRegistry reg = bm.createInstance().select(MetricRegistry.class).get();
                if (!reg.getMetrics().containsKey(name)) {
                    reg.register(name, producer.produce(ctx));
                }
                return (T) reg.getMetrics().get(name);
            });
        }
    }

    void registerProducedMetrics(@Observes AfterDeploymentValidation adv, BeanManager bm) {

        bm.createInstance().select(com.codahale.metrics.Metric.class, Any.Literal.INSTANCE).stream().forEach(metric -> {
        });
    }

}
