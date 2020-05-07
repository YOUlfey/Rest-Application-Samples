package me.youlfey.rest.example.artists.portal.base;

import me.youlfey.rest.example.artists.portal.exception.HttpNotFoundException;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RequiredArgsConstructor
public class BaseService<T extends Base, S> {
    private final BaseRepository<T, S> repository;

    List<T> getAll() {
        return repository.findAll();
    }

    List<T> getAllByName(@NotNull String name) {
        return repository.findAllByName(name);
    }

    public T insert(@Valid T obj) {
        return repository.save(obj);
    }

    T update(@NotNull S id, T obj) {
        return repository.findById(id)
                .map(entity -> {
                    entity.update(obj);
                    return repository.save(entity);
                }).orElseThrow(HttpNotFoundException::new);
    }

    public void remove(@NotNull S id) {
        T exsObj = repository.findById(id).orElseThrow(HttpNotFoundException::new);
        repository.delete(exsObj);
    }

    protected T getById(S id) {
        return repository.findById(id).orElseThrow(HttpNotFoundException::new);
    }
}
