package org.cdi.further.metrics;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedParameter;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.util.Nonbinding;

/**
 * @author Antoine Sabot-Durand
 */
public class NonBindingAnnotatedMethod<T> implements AnnotatedMethod<T> {

    public NonBindingAnnotatedMethod(AnnotatedMethod<T> m) {
        delegate = m;
        annotations = new HashSet<>(m.getAnnotations());
        annotations.add(new NonBindingLiteral());
    }

    @Override
    public Method getJavaMember() {
        return delegate.getJavaMember();
    }

    @Override
    public List<AnnotatedParameter<T>> getParameters() {
        return delegate.getParameters();
    }

    @Override
    public boolean isStatic() {
        return delegate.isStatic();
    }

    @Override
    public AnnotatedType<T> getDeclaringType() {
        return delegate.getDeclaringType();
    }

    @Override
    public Type getBaseType() {
        return delegate.getBaseType();
    }

    @Override
    public Set<Type> getTypeClosure() {
        return delegate.getTypeClosure();
    }

    @Override
    public <X extends Annotation> X getAnnotation(Class<X> annotationType) {
        if (Nonbinding.class.equals(annotationType)) {
            return (X) new NonBindingLiteral();
        } else
            return delegate.getAnnotation(annotationType);
    }

    @Override
    public Set<Annotation> getAnnotations() {
        return annotations;
    }

    @Override
    public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
        if (Nonbinding.class.equals(annotationType)) {
            return true;
        } else
            return delegate.isAnnotationPresent(annotationType);
    }

    private final AnnotatedMethod<T> delegate;

    private final Set<Annotation> annotations;
}
