package com.portfolioprojects.learnspringsecurity.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TodoResource {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public static final List<Todo> TODOS_LIST = List.of(
            new Todo("Robert", "Learn AWS"),
            new Todo("Robert", "Learn Spring Boot"));

    @GetMapping("/todos")
    public List<Todo> retrieveAllTodos() {
        return TODOS_LIST;
    }


    @GetMapping("/users/{username}/todos")
    public Todo retrieveAllTodosForSpecificUser(@PathVariable String username, @RequestParam(defaultValue = "retrieveAllTodosForSpecificUser", required = false) String name) {
        logger.info(name);
        return TODOS_LIST.get(0);
    }

    @PostMapping("/users/{username}/todos")
    public void createTodoForSpecificUser(@PathVariable String username, @RequestBody Todo todo) {
        logger.info("Create {} for {}", todo, username);
    }

}

record Todo(String username, String description) {

}
