package com.restaurant.reservation.service;

import com.restaurant.reservation.model.User;
import com.restaurant.reservation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final Path uploadRoot;
	private final BCryptPasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository,
					   @Value("${app.upload.dir:uploads}") String uploadDir) {
		this.userRepository = userRepository;
		this.uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
		this.passwordEncoder = new BCryptPasswordEncoder();
	}

	public User register(User user) {
		if (userRepository.existsByEmail(user.getEmail())) {
			throw new IllegalArgumentException("Email is already registered");
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	public Optional<User> login(String email, String password) {
		return userRepository.findByEmail(email)
				.filter(user -> passwordEncoder.matches(password, user.getPassword()));
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public Optional<User> findById(Long id) {
		return userRepository.findById(id);
	}

	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public User create(User user) {
		if (userRepository.existsByEmail(user.getEmail())) {
			throw new IllegalArgumentException("Email is already registered");
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	public User update(Long id, User payload) {
		User existing = userRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("User not found"));

		if (payload.getFullName() != null) {
			existing.setFullName(payload.getFullName());
		}
		if (payload.getEmail() != null && !payload.getEmail().equalsIgnoreCase(existing.getEmail())) {
			if (userRepository.existsByEmail(payload.getEmail())) {
				throw new IllegalArgumentException("Email is already registered");
			}
			existing.setEmail(payload.getEmail());
		}
		if (payload.getPassword() != null && !payload.getPassword().isBlank()) {
			existing.setPassword(passwordEncoder.encode(payload.getPassword()));
		}
		if (payload.getPhoneNumber() != null) {
			existing.setPhoneNumber(payload.getPhoneNumber());
		}
		if (payload.getAddress() != null) {
			existing.setAddress(payload.getAddress());
		}
		if (payload.getCity() != null) {
			existing.setCity(payload.getCity());
		}
		if (payload.getStateProvince() != null) {
			existing.setStateProvince(payload.getStateProvince());
		}
		if (payload.getCountry() != null) {
			existing.setCountry(payload.getCountry());
		}
		if (payload.getProfileImageUrl() != null) {
			existing.setProfileImageUrl(payload.getProfileImageUrl());
		}

		return userRepository.save(existing);
	}

	public void delete(Long id) {
		if (!userRepository.existsById(id)) {
			throw new IllegalArgumentException("User not found");
		}
		userRepository.deleteById(id);
	}

	public String storeProfileImage(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			return null;
		}

		try {
			Files.createDirectories(uploadRoot);
			String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
			String safeExt = extension == null ? "jpg" : extension.toLowerCase();
			String fileName = "profile-" + UUID.randomUUID() + "." + safeExt;
			Path target = uploadRoot.resolve(fileName);
			Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
			return "/uploads/" + fileName;
		} catch (IOException e) {
			throw new IllegalStateException("Could not store uploaded file", e);
		}
	}
}
