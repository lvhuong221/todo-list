package com.lvhuong.todolist.repositories;


import com.lvhuong.todolist.TestDataUtil;
import com.lvhuong.todolist.domains.entities.RoleEntity;
import com.lvhuong.todolist.domains.entities.TodoList;
import com.lvhuong.todolist.domains.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TodoListRepositoryIntergrationTest {

    private final TodoListRepository testListRepo;
    private final UserRepository userRepo;
    private final RoleRepository roleTest;

    @Autowired
    public TodoListRepositoryIntergrationTest(TodoListRepository listTest, UserRepository userRepo, RoleRepository roleTest) {
        this.testListRepo = listTest;
        this.userRepo = userRepo;
        this.roleTest = roleTest;
    }

    @Test
    public void testThatTodoListCanBeCreated(){
        UserEntity userA = TestDataUtil.createUserA();
        userA = userRepo.save(userA);
        TodoList todoListA = TestDataUtil.createTodoListA(userA);

        TodoList savedList = testListRepo.save(todoListA);
        Optional<TodoList>result = testListRepo.findByIdAndUserId(userA.getId(), savedList.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(todoListA);
    }

    @Test
    public void testThatMultipleTodoListCanBeCreated(){
        UserEntity userA = TestDataUtil.createUserA();
        Optional<RoleEntity> roles = roleTest.findByName("ROLE_USER");
        roles.ifPresent(roleEntity -> userA.setRoles(Collections.singleton(roleEntity)));

        userRepo.save(userA);

        TodoList todoListA = TestDataUtil.createTodoListA(userA);
        TodoList savedListA = testListRepo.save(todoListA);

        TodoList todoListB = TestDataUtil.createTodoListB(userA);
        TodoList savedListB = testListRepo.save(todoListB);

        TodoList todoListC = TestDataUtil.createTodoListC(userA);
        TodoList savedListC = testListRepo.save(todoListC);


        Page<TodoList> resultList = testListRepo.findPageByUserId(PageRequest.of(0, 5), userA.getId());


        assertThat(resultList.getContent())
                .hasSize(3)
                .containsExactly(todoListA, todoListB, todoListC);
    }

    @Test
    public void testThatTodoListCanBeUpdated(){
        UserEntity userA = TestDataUtil.createUserA();
        userA = userRepo.save(userA);
        TodoList todoListA = TestDataUtil.createTodoListA(userA);

        TodoList savedList = testListRepo.save(todoListA);

        todoListA.setTitle("New title");
        todoListA.setId(savedList.getId());
        testListRepo.save(todoListA);

        Optional<TodoList> updatedList = testListRepo.findByIdAndUserId(userA.getId(), todoListA.getId());

        assertThat(updatedList).isPresent();
        assertThat(updatedList.get().getTitle()).isEqualTo(todoListA.getTitle());


    }

    @Test
    public void testSoftDeletedTodoListCannotBeRetrieved(){
        UserEntity userA = TestDataUtil.createUserA();
        userA = userRepo.save(userA);
        TodoList todoListA = TestDataUtil.createTodoListA(userA);

        // this soft deleted list shouldn't be retrieved
        todoListA.setDeleted(true);
        testListRepo.save(todoListA);

        Page<TodoList> resultList = testListRepo.findPageByUserId(PageRequest.of(0, 5), userA.getId());

        assertThat(resultList.getContent())
                .isEmpty();
    }
}
