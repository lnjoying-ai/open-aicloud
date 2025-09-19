package com.lnjoying.justice.commonweb.swagger;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/20 21:13
 */

public class AnnotatedTypeScanner implements ResourceLoaderAware, EnvironmentAware
{
    private final Iterable<Class<? extends Annotation>> annotationTypess;
    private final boolean considerInterfaces;
    @Nullable
    private ResourceLoader resourceLoader;
    @Nullable
    private Environment environment;

    @SafeVarargs
    public AnnotatedTypeScanner(Class<? extends Annotation>... annotationTypes) {
        this(true, annotationTypes);
    }

    @SafeVarargs
    public AnnotatedTypeScanner(boolean considerInterfaces, Class<? extends Annotation>... annotationTypes) {
        this.annotationTypess = Arrays.asList(annotationTypes);
        this.considerInterfaces = considerInterfaces;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Set<Class<?>> findTypes(String... basePackages) {
        return this.findTypes((Iterable)Arrays.asList(basePackages));
    }

    public Set<Class<?>> findTypes(Iterable<String> basePackages) {
        ClassPathScanningCandidateComponentProvider provider = new InterfaceAwareScanner(this.considerInterfaces);
        if (this.resourceLoader != null) {
            provider.setResourceLoader(this.resourceLoader);
        }

        if (this.environment != null) {
            provider.setEnvironment(this.environment);
        }

        Iterator var3 = this.annotationTypess.iterator();

        while(var3.hasNext()) {
            Class<? extends Annotation> annotationType = (Class)var3.next();
            provider.addIncludeFilter(new AnnotationTypeFilter(annotationType, true, this.considerInterfaces));
        }

        Set<Class<?>> types = new HashSet();
        ResourceLoader loader = this.resourceLoader;
        ClassLoader classLoader = loader == null ? null : loader.getClassLoader();
        Iterator var6 = basePackages.iterator();

        while(var6.hasNext()) {
            String basePackage = (String)var6.next();
            Iterator var8 = provider.findCandidateComponents(basePackage).iterator();

            while(var8.hasNext()) {
                BeanDefinition definition = (BeanDefinition)var8.next();
                String beanClassName = definition.getBeanClassName();
                if (beanClassName == null) {
                    throw new IllegalStateException(String.format("Unable to obtain bean class name from bean definition %s!", definition));
                }

                try {
                    types.add(ClassUtils.forName(beanClassName, classLoader));
                } catch (ClassNotFoundException var12) {
                    throw new IllegalStateException(var12);
                }
            }
        }

        return types;
    }

    private static class InterfaceAwareScanner extends ClassPathScanningCandidateComponentProvider {
        private final boolean considerInterfaces;

        public InterfaceAwareScanner(boolean considerInterfaces) {
            super(false);
            this.considerInterfaces = considerInterfaces;
        }

        @Override
        protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
            return super.isCandidateComponent(beanDefinition) || this.considerInterfaces && beanDefinition.getMetadata().isInterface();
        }
    }
}
