package com.beside.archivist.repository.usergroup;

import com.beside.archivist.dto.group.GroupInfoDto;
import com.beside.archivist.dto.group.QGroupInfoDto;
import com.beside.archivist.entity.group.QGroup;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.beside.archivist.entity.usergroup.QUserGroup.userGroup;

public class UserGroupRepositoryCustomImpl implements UserGroupRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public UserGroupRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<GroupInfoDto> getGroupsByUserId(Long userId, boolean isOwner) {
        return queryFactory.select(new QGroupInfoDto(QGroup.group))
                .from(userGroup)
                .leftJoin(userGroup.groups, QGroup.group)
                .where(userGroup.users.id.eq(userId),
                        userGroup.isOwner.eq(isOwner))
                .fetch();
    }
}
