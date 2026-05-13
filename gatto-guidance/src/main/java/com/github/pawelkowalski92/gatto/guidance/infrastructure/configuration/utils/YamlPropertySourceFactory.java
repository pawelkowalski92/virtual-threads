package com.github.pawelkowalski92.gatto.guidance.infrastructure.configuration.utils;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;
import java.util.Optional;

import static java.util.function.Predicate.not;

public class YamlPropertySourceFactory implements PropertySourceFactory {

    private final YamlPropertySourceLoader yamlPropertySourceLoader;

    public YamlPropertySourceFactory() {
        this.yamlPropertySourceLoader = new YamlPropertySourceLoader();
    }

    @Override
    public @NonNull PropertySource<?> createPropertySource(@Nullable String name,
                                                           EncodedResource encodedResource) throws IOException {
        var resource = encodedResource.getResource();
        if (name == null) {
            name = nameFromResource(resource);
        }
        var propertySources = yamlPropertySourceLoader.load(name, resource);
        if (propertySources.size() == 1) return propertySources.getFirst();

        var compositeSource = new CompositePropertySource(name);
        propertySources.forEach(compositeSource::addPropertySource);
        return compositeSource;
    }

    private String nameFromResource(Resource resource) {
        return Optional.of(resource.getDescription())
                .filter(not(String::isBlank))
                .orElseGet(resource::getFilename);
    }

}
