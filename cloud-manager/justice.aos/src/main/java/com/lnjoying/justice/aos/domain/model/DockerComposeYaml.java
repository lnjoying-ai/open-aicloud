package com.lnjoying.justice.aos.domain.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.util.CollectionUtils;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.lang.reflect.Field;
import java.util.*;

@Data
public class DockerComposeYaml
{
    @Order(1)
    String version;

    @Order(2)
    Map<String, Object> services;
//	Map<String, DockerComposeService> services;

    @Order(3)
    Map<String, Object> configs;

    @Order(4)
    Map<String, Object> volumes;

    @Order(5)
    Map<String, Object> networks;

    @SuppressWarnings("unchecked")
    public void setLabels(Map<String, Object> labels)
    {
        if (CollectionUtils.isEmpty(labels))
        {
            return;
        }

        Map<String, Object> services = this.getServices();
        if (!CollectionUtils.isEmpty(services))
        {
            services.values().stream().forEach(service ->
            {
                LinkedHashMap<String, Object> serviceMap = (LinkedHashMap<String, Object>) service;
                LinkedHashMap<String, Object> serviceLabels = (LinkedHashMap<String, Object>) serviceMap.get("labels");
                if (CollectionUtils.isEmpty(serviceLabels))
                {
                    serviceLabels = new LinkedHashMap<String, Object>();
                    serviceMap.put("labels", serviceLabels);
                }
                serviceLabels.putAll(labels);
            });
        }
    }

    @SuppressWarnings("unchecked")
    public List<String> getImages(){
        List<String> images = new ArrayList<>();
        Map<String, Object> services = this.getServices();
        if (!CollectionUtils.isEmpty(services))
        {
            services.values().stream().forEach(service -> {
                LinkedHashMap<String, Object> serviceMap = (LinkedHashMap<String, Object>) service;
                String image = (String) serviceMap.get("image");
                if (StringUtils.isNotBlank(image))
                {
                    images.add(image);
                }
            });
        }

        return images;
    }

    public static class DockerComposeRepresenter extends Representer
    {

        private static final Map<String, Integer> cache = new HashMap<>();
        private static final Comparator<Property> comparator = Comparator.comparingInt(p -> cache.get(p.getName()));

        static
        {
            Class<?> clazz = DockerComposeYaml.class;
            Field[] declaredFields = clazz.getDeclaredFields();
            Arrays.stream(declaredFields).sorted(Comparator.comparingInt(f -> f.getAnnotation(Order.class).value()))
                    .forEach(field -> cache.put(field.getName(), field.getAnnotation(Order.class).value()));
        }

        @Override
        protected Set<Property> getProperties(Class<?> type)
        {
            Set<Property> set = super.getProperties(type);
            Set<Property> treeSet = new TreeSet<>(comparator);
            if (type.equals(DockerComposeYaml.class))
            {
                treeSet.addAll(set);
                return treeSet;
            }

            return set;
        }

        @Override
        protected NodeTuple representJavaBeanProperty(Object javaBean, Property property, Object propertyValue, Tag customTag)
        {
            // if value of property is null, ignore it.
            if (propertyValue == null)
            {
                return null;
            }
            else
            {
                return super.representJavaBeanProperty(javaBean, property, propertyValue, customTag);
            }
        }
    }
}
