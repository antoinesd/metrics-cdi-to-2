package org.cdi.further.metrics;

import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.Producer;
import java.util.Set;

/**
 *
 */
public class MetricProducer<X extends Metric> implements Producer<X> {

    Producer<X> decorate;
    String metricName;
    BeanManager bm;


    public MetricProducer( Producer<X> decorate, String metricName, BeanManager bm) {
        this.decorate = decorate;
        this.metricName = metricName;
        this.bm = bm;
    }



    @Override
    public X produce(CreationalContext<X> ctx) {
        Bean<?> mrbean = bm.resolve(bm.getBeans(MetricRegistry.class));
        MetricRegistry reg = (MetricRegistry) bm.getReference(mrbean, MetricRegistry.class, bm.createCreationalContext(mrbean));
        if (!reg.getMetrics().containsKey(metricName)) {
            reg.register(metricName,decorate.produce(ctx));
        }

        return (X)reg.getMetrics().get(metricName);
    }

    @Override
    public void dispose(X instance) {
    }

    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        return decorate.getInjectionPoints();
    }
}
