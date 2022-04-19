package com.crypto.cryptodash.services;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.crypto.cryptodash.entities.User;
import com.crypto.cryptodash.errors.ApiError;
import com.crypto.cryptodash.repositories.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	public UserService() {
	};

	public ResponseEntity<?> createUser(String username, String password) {

		User newUser = new User();

		String salt = BCrypt.gensalt();

		String hashedPassword = BCrypt.hashpw(password, salt);

		newUser.setUsername(username);
		newUser.setHashedPassword(hashedPassword);
		newUser.setSalt(salt);

		try {
			userRepository.save(newUser);
		} catch (DataIntegrityViolationException e) {
			ApiError error = new ApiError(HttpStatus.BAD_REQUEST, "Duplicate Entry Exception",
					"Duplicate entry for username: " + username);

			return new ResponseEntity<ApiError>(error, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<User>(newUser, HttpStatus.OK);
	}

}
