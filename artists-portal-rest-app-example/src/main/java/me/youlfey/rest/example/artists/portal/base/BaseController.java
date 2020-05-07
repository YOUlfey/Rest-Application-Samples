package me.youlfey.rest.example.artists.portal.base;

import me.youlfey.rest.example.artists.portal.exception.HttpNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class BaseController<T extends Base> {
    private final BaseService<T, UUID> service;

    public ResponseEntity getAll() {
        List<T> entities = service.getAll();
        return CollectionUtils.isEmpty(entities)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(entities);
    }

    public ResponseEntity getAllByName(@RequestParam String name) {
        List<T> entities = service.getAllByName(name);
        return CollectionUtils.isEmpty(entities)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(entities);
    }

    public ResponseEntity add(@RequestBody T obj) {
        return ResponseEntity.ok(service.insert(obj));
    }

    public ResponseEntity update(@PathVariable UUID id, @RequestBody T obj) {
        try {
            return ResponseEntity.ok(service.update(id, obj));
        } catch (HttpNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity delete(@PathVariable UUID id) {
        try {
            service.remove(id);
            return ResponseEntity.ok().build();
        } catch (HttpNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity get(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(service.getById(id));
        } catch (HttpNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
