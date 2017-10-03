package org.cdi.further.metrics;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.inject.Inject;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.annotation.Metric;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/**
 * @author Antoine Sabot-Durand
 */

public class MetricsIntegrationTest {

    public static SeContainer container;

    @BeforeClass
    public void setup() {
        container = SeContainerInitializer.newInstance()
                .addPackages(MetricsIntegrationTest.class, MetricsExtension.class)
                .addExtensions(MetricsExtension.class)
                .initialize();
        BeanManager bm = container.getBeanManager();

        InjectionTarget it = bm.createInjectionTarget(bm.createAnnotatedType(MetricsIntegrationTest.class));

        it.inject(this, bm.createCreationalContext(null));


    }

    @Test
    public void shouldTimedInterceptorBeCalled() {
        mtb.timedMethod();
        Assert.assertEquals(1, registry.timer("myTimer").getCount());
    }

    @Test
    public void shouldMetricsBeTheSame() {
        Timer t = registry.timer("myTimer");
        Assert.assertSame(t, timer);
    }

    @Test
    public void shouldProducedMetricsBeInRegistry() {
        Assert.assertEquals(2, registry.getMetrics().size());
    }

    @Inject
    MetricRegistry registry;

    @Inject
    @Metric(name = "myTimer")
    Timer timer;

    @Inject
    MetricsTestBean mtb;


}
