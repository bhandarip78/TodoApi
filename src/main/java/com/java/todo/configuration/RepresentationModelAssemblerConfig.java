package com.java.todo.configuration;

import com.java.todo.controller.TodoController;
import com.java.todo.model.Todo;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Configuration
public class RepresentationModelAssemblerConfig implements RepresentationModelAssembler<Todo, EntityModel<Todo>> {
    @Override
    public EntityModel<Todo> toModel(Todo entity) {
        EntityModel<Todo> todoEntityModel = EntityModel.of(entity);

        todoEntityModel.add(linkTo(methodOn(TodoController.class).get(entity.getId())).withSelfRel());
        todoEntityModel.add(linkTo(methodOn(TodoController.class).getAll(entity.getUsername())).withRel("todos"));
        todoEntityModel.add(linkTo(methodOn(TodoController.class).getUncompleteTask()).withRel("uncompleteTasks"));
        todoEntityModel.add(linkTo(methodOn(TodoController.class).addTask(null)).withRel("addTask"));
        todoEntityModel.add(linkTo(methodOn(TodoController.class).updateTask(null)).withRel("updateTask"));
        todoEntityModel.add(linkTo(methodOn(TodoController.class).updateTaskStatus(null)).withRel("updateTaskStatus"));
        todoEntityModel.add(linkTo(methodOn(TodoController.class).deleteTask(entity.getId())).withRel("deleteTask"));

        return todoEntityModel;
    }
}
