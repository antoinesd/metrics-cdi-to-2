package org.cdi.further.metrics;

import com.codahale.metrics.SlidingTimeWindowReservoir;
import com.codahale.metrics.Timer;
import com.codahale.metrics.annotation.Metric;
import com.codahale.metrics.annotation.Timed;

import javax.enterprise.inject.Produces;
import java.util.concurrent.TimeUnit;

/**
 * @author Antoine Sabot-Durand
 */
public class MetricsTestBean {
    
    @Produces
    @Metric(name="myTimer")
    Timer timer = new Timer(new SlidingTimeWindowReservoir(1L, TimeUnit.MINUTES));

    @Produces
    @Metric(name="mySecondTimer")
    Timer timer2 = new Timer(new SlidingTimeWindowReservoir(1L, TimeUnit.HOURS));


    @Timed(name="myTimer")
    public void timedMethod()  {
        System.out.println("doing a pause");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    
}
