package com.beside.archivist.repository.users;

import com.beside.archivist.entity.users.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.beside.archivist.entity.users.QUser.user;

public class UserRepositoryCustomImpl implements UserRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public UserRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
    @Override
    public List<String> getNicknames() {
        return queryFactory.select(user.nickname)
                .from(user)
                .fetch();
    }
}
