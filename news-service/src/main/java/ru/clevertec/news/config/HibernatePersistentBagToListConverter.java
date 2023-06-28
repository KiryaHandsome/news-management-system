package ru.clevertec.news.config;


import org.hibernate.collection.spi.PersistentBag;
import org.modelmapper.AbstractConverter;

import java.util.List;

public class HibernatePersistentBagToListConverter extends AbstractConverter<PersistentBag<?>, List<?>> {

    @Override
    protected List<?> convert(PersistentBag<?> source) {
        return source.stream()
                .toList();
    }
}
