package ru.clevertec.newssystem.service.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * Interface that serves to implement services for read, update and delete
 * operations with different request and return type.
 *
 * @param <REQ>  incoming entity type (request)
 * @param <RESP> return entity type (response)
 * @param <ID>   id type of entity
 */
public interface RUDService<REQ, RESP, ID> {

    RESP find(ID id);

    Page<RESP> findAll(Pageable pageable);

    RESP update(ID id, REQ entity);

    void delete(ID id);
}
