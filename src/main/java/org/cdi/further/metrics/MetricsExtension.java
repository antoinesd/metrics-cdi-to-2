package org.cdi.further.metrics;


import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessProducer;
import javax.enterprise.util.AnnotationLiteral;

import com.codahale.metrics.annotation.Metric;
import com.codahale.metrics.annotation.Timed;

/**
 * @author Antoine Sabot-Durand
 */
public class MetricsExtension implements Extension {

    void addMetricAsQualifier(@Observes BeforeBeanDiscovery bdd) {
        bdd.addQualifier(Metric.class);
    }

    void addTimedBinding(@Observes BeforeBeanDiscovery bdd, BeanManager bm) throws Exception {

        bdd.addInterceptorBinding(new AnnotatedTypeWithAllMethodNonBinding<Timed>(bm.createAnnotatedType(Timed.class)));
    }

    <T extends com.codahale.metrics.Metric> void decorateMetricProducer(@Observes ProcessProducer<?, T> pp, BeanManager bm) {
        if (pp.getAnnotatedMember().isAnnotationPresent(Metric.class)) {
            String name = pp.getAnnotatedMember().getAnnotation(Metric.class).name();
            pp.setProducer(new MetricProducer(pp.getProducer(), name, bm));
        }
    }

    void registerProducedMetrics(@Observes AfterDeploymentValidation adv, BeanManager bm) {
        bm.getBeans(com.codahale.metrics.Metric.class, new AnnotationLiteral<Any>() {
        })
                .forEach(bean -> bm.getReference(bean, com.codahale.metrics.Metric.class, bm.createCreationalContext(bean)));

    }

}
