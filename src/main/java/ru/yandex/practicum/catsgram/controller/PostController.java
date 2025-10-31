package ru.yandex.practicum.catsgram.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.SortOrder;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public Collection<Post> findAll(
            @RequestParam(required = false) Integer from,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sort) {

        // Если параметры не указаны - вернуть все посты без пагинации
        if (from == null && size == null && sort == null) {
            return postService.findAll();
        }

        // Иначе использовать пагинацию с параметрами по умолчанию
        SortOrder sortOrder = SortOrder.from(sort != null ? sort : "desc");
        if (sortOrder == null) {
            throw new IllegalArgumentException("Неверный параметр сортировки. Используйте 'asc' или 'desc'");
        }

        int fromValue = from != null ? from : 0;
        int sizeValue = size != null ? size : 10;

        if (fromValue < 0) {
            throw new IllegalArgumentException("Параметр 'from' не может быть отрицательным");
        }
        if (sizeValue <= 0) {
            throw new IllegalArgumentException("Параметр 'size' должен быть положительным");
        }

        return postService.findAll(fromValue, sizeValue, sortOrder);
    }

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Post create(@RequestBody Post post) {
        return postService.create(post);
    }

    @PutMapping
    public Post update(@RequestBody Post newPost) {
        return postService.update(newPost);
    }
}