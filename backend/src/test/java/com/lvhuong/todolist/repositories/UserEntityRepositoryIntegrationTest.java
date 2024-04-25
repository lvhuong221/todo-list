package com.lvhuong.todolist.repositories;

import com.lvhuong.todolist.TestDataUtil;
import com.lvhuong.todolist.domains.entities.RoleEntity;
import com.lvhuong.todolist.domains.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.swing.text.html.Option;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserEntityRepositoryIntegrationTest {

    private final UserRepository userTest;
    private final RoleRepository roleTest;

    @Autowired
    public UserEntityRepositoryIntegrationTest(UserRepository underTest, RoleRepository roleTest) {
        this.userTest = underTest;
        this.roleTest = roleTest;
    }

    @Test
    public void testThatUserCanBeCreatedAndRecalled(){
        UserEntity userA = TestDataUtil.createUserA();
        Optional<RoleEntity> roles = roleTest.findByName("ROLE_USER");
        roles.ifPresent(roleEntity -> userA.setRoles(Collections.singleton(roleEntity)));

        userTest.save(userA);

        Optional<UserEntity> savedUser = userTest.findByUsername(userA.getUsername());

        assertThat(savedUser).isPresent();
        assertThat(savedUser.get()).isEqualTo(userA);
    }

    @Test
    public void testThatMultipleUsersCanBeCreatedAndRecalled() {
        UserEntity userEntityA = TestDataUtil.createUserA();
        UserEntity userEntityB = TestDataUtil.createUserB();
        UserEntity userEntityC = TestDataUtil.createUserC();

        userTest.save(userEntityA);
        userTest.save(userEntityB);
        userTest.save(userEntityC);

        Iterable<UserEntity> result = userTest.findAll();

        assertThat(result).hasSize(3)
                .containsExactly(userEntityA, userEntityB, userEntityC);
    }

    @Test
    public void testThatUserCanBeUpdated(){
        UserEntity userEntity = TestDataUtil.createUserA();

        userTest.save(userEntity);

        Optional<UserEntity> result = userTest.findByUsername(userEntity.getUsername());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(userEntity);
    }
}
