package com.beside.archivist.repository.redis.users;

import com.beside.archivist.entity.redis.Users;
import org.springframework.data.repository.CrudRepository;

public interface  UserRedisRepository extends CrudRepository<Users, String> {
}
