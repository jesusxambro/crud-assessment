package com.crud.hillcountry.demo.dao;

import com.crud.hillcountry.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


}
