package com.crypto.cryptodash.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.crypto.cryptodash.entities.User;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<User, Integer> {
	List<User> findByUsername(String username);
}