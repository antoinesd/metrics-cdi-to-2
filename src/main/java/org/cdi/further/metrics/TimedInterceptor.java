package org.cdi.further.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.annotation.Timed;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 *
 */
@Timed
@Interceptor
@Priority(Interceptor.Priority.LIBRARY_BEFORE)
public class TimedInterceptor {

    @Inject
    MetricRegistry registry;
    
    @AroundInvoke
    public Object timedMethod(InvocationContext context) throws Exception {
        String timerName = context.getMethod().getAnnotation(Timed.class).name();
        Timer timer = registry.timer(timerName);
        Timer.Context time = timer.time();
        try {
            return context.proceed();
        } finally {
            time.stop();
        }
    }
}
