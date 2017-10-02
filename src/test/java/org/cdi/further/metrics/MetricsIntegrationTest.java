package org.cdi.further.metrics;

import java.io.FileNotFoundException;

import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.annotation.Metric;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.testng.Assert;
import org.testng.annotations.Test;


/**
 * @author Antoine Sabot-Durand
 */

public class MetricsIntegrationTest extends Arquillian{

    @Deployment
    public static Archive<?> createTestArchive() throws FileNotFoundException {

        WebArchive ret = ShrinkWrap
                .create(WebArchive.class, "test.war")
                .addPackage("org.cdi.further.metrics")
                .addAsLibraries(Maven.resolver()
                                        .loadPomFromFile("pom.xml")
                                        .resolve("io.dropwizard.metrics:metrics-core",
                                                 "io.dropwizard.metrics:metrics-annotation")
                                        .withTransitivity().as(JavaArchive.class))
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsServiceProvider(Extension.class, MetricsExtension.class);

        return ret;
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
