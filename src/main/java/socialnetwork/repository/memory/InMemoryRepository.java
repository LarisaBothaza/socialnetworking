package socialnetwork.repository.memory;

import socialnetwork.domain.Entity;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID,E> {

    private Validator<E> validator;
    protected Map<ID,E> entities;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities=new HashMap<ID,E>();
    }

    /**
     *
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return the entity with the given id
     * @throws IllegalArgumentException - if the id is null
     */
    @Override
    public E findOne(ID id){
        if (id==null)
            throw new IllegalArgumentException("id must be not null");
        return entities.get(id);
    }

    /**
     *
     * @return an iterable list of all entities
     */
    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    /**
     *saves the given entity
     * @param entity
     *         entity must be not null
     * @return entity - if the id is different from null
     *          null - else
     * @throws IllegalArgumentException - if the id is null
     */
    @Override
    public E save(E entity) {
        if (entity==null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);
        if(entities.get(entity.getId()) != null) {
            return entity;
        }
        else entities.put(entity.getId(),entity);
        return null;
    }

    /**
     *
     * @param id
     *      id must be not null
     * @return null in case there is no given key = id, otherwise return entity deleted
     */
    @Override
    public E delete(ID id) {
        return entities.remove(id); //return null in caz ca nu exista key = id dat, altfel return enity stearsa
    }

    /**
     * modify an entity
     * @param entity
     *          entity must not be null
     * @return the modified entity
     */
    @Override
    public E update(E entity) {

        if(entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(entity);

        entities.put(entity.getId(),entity);

        if(entities.get(entity.getId()) != null) {
            entities.put(entity.getId(),entity);
            return null;
        }
        return entity;

    }

}
