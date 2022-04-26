package com.tw.vapsi.biblioteca.repository;

import com.tw.vapsi.biblioteca.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
