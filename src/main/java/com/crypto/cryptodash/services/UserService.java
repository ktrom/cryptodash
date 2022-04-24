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

	public ResponseEntity<?> login(String username, String password) {

		User u = userRepository.findByUsername(username).get(0);

		boolean passwordsMatch = BCrypt.checkpw(password, u.getHashedPassword());
//		String hashedPassword = BCrypt.hashpw(password, salt);
//
//		newUser.setUsername(username);
//		newUser.setHashedPassword(hashedPassword);
//		newUser.setSalt(salt);
//
//		try {
//			userRepository.save(newUser);
//		} catch (DataIntegrityViolationException e) {
//			ApiError error = new ApiError(HttpStatus.BAD_REQUEST, "Duplicate Entry Exception",
//					"Duplicate entry for username: " + username);
//
//			return new ResponseEntity<ApiError>(error, HttpStatus.BAD_REQUEST);
//		}

		if (passwordsMatch) {
			return new ResponseEntity<User>(u, HttpStatus.OK);
		} else {
			ApiError error = new ApiError(HttpStatus.BAD_REQUEST, "Incorrect Password Exception",
					"Incorrect password for username: " + username);
			return new ResponseEntity<ApiError>(error, HttpStatus.BAD_REQUEST);
		}
	}

}
