package com.sbigeneral.LoginCapatchePoc.Repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sbigeneral.LoginCapatchePoc.Entity.User;



public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByEmployeeId(String employeeId);
}
