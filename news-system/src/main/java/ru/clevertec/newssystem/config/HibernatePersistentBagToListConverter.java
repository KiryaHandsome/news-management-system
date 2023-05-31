package ru.clevertec.newssystem.config;


import org.hibernate.collection.spi.PersistentBag;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HibernatePersistentBagToListConverter extends AbstractConverter<PersistentBag<?>, List<?>> {

    @Override
    protected List<?> convert(PersistentBag<?> source) {
        return source.stream()
                .toList();
    }
}
