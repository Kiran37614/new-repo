package com.restaurant.reservation.controller;

import com.restaurant.reservation.model.User;
import com.restaurant.reservation.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {

	private static final String SESSION_USER_ID = "loggedInUserId";

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/register")
	public String register(@RequestParam String fullName,
						   @RequestParam String email,
						   @RequestParam String password,
						   @RequestParam String confirmPassword,
					   @RequestParam String phoneNumber,
						   RedirectAttributes redirectAttributes,
						   HttpSession session) {
		try {
			if (!password.equals(confirmPassword)) {
				throw new IllegalArgumentException("Passwords do not match");
			}
			String normalizedPhone = phoneNumber == null ? "" : phoneNumber.trim();
			if (!normalizedPhone.matches("\\d{10}")) {
				throw new IllegalArgumentException("Phone number must contain exactly 10 digits");
			}
			User user = new User();
			user.setFullName(fullName);
			user.setEmail(email);
			user.setPassword(password);
			user.setPhoneNumber(normalizedPhone);
			user.setCountry("Sri Lanka");
			user.setProfileImageUrl("/assets/img/avatars/1.png");

			User saved = userService.register(user);
			session.setAttribute(SESSION_USER_ID, saved.getId());
			redirectAttributes.addFlashAttribute("successMessage", "Registration successful. Welcome, " + saved.getFullName() + "!");
			return "redirect:/users/profile";
		} catch (IllegalArgumentException ex) {
			redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
			return "redirect:/register";
		}
	}

	@PostMapping("/login")
	public String login(@RequestParam String email,
						@RequestParam String password,
						RedirectAttributes redirectAttributes,
						HttpSession session) {
		return userService.login(email, password)
				.map(user -> {
					session.setAttribute(SESSION_USER_ID, user.getId());
					return "redirect:/users/profile";
				})
				.orElseGet(() -> {
					redirectAttributes.addFlashAttribute("errorMessage", "Invalid email or password");
					return "redirect:/login";
				});
	}

	@GetMapping("/profile")
	public String profile(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
		Long userId = (Long) session.getAttribute(SESSION_USER_ID);
		if (userId == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "Please login first");
			return "redirect:/login";
		}

		User user = userService.findById(userId).orElse(null);
		if (user == null) {
			session.removeAttribute(SESSION_USER_ID);
			redirectAttributes.addFlashAttribute("errorMessage", "User account not found");
			return "redirect:/login";
		}

		model.addAttribute("user", user);
		return "profile";
	}

	@PostMapping("/profile/update")
	public String updateProfile(@ModelAttribute User payload,
								HttpSession session,
								RedirectAttributes redirectAttributes) {
		Long userId = (Long) session.getAttribute(SESSION_USER_ID);
		if (userId == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "Please login first");
			return "redirect:/login";
		}

		try {
			userService.update(userId, payload);
			redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully");
		} catch (IllegalArgumentException ex) {
			redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
		}
		return "redirect:/users/profile";
	}

	@PostMapping("/profile/photo")
	public String updatePhoto(@RequestParam("profilePhoto") MultipartFile profilePhoto,
							  HttpSession session,
							  RedirectAttributes redirectAttributes) {
		Long userId = (Long) session.getAttribute(SESSION_USER_ID);
		if (userId == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "Please login first");
			return "redirect:/login";
		}

		if (profilePhoto == null || profilePhoto.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "Please choose an image to upload");
			return "redirect:/users/profile";
		}

		try {
			String imageUrl = userService.storeProfileImage(profilePhoto);
			User updatePayload = new User();
			updatePayload.setProfileImageUrl(imageUrl);
			userService.update(userId, updatePayload);
			redirectAttributes.addFlashAttribute("successMessage", "Profile photo updated successfully");
		} catch (RuntimeException ex) {
			redirectAttributes.addFlashAttribute("errorMessage", "Failed to upload image");
		}

		return "redirect:/users/profile";
	}

	@PostMapping("/delete/{id}")
	public String delete(@PathVariable Long id,
						 HttpSession session,
						 RedirectAttributes redirectAttributes) {
		Long sessionUserId = (Long) session.getAttribute(SESSION_USER_ID);
		if (sessionUserId == null) {
			return "redirect:/login";
		}

		try {
			userService.delete(id);
			if (sessionUserId.equals(id)) {
				session.removeAttribute(SESSION_USER_ID);
				redirectAttributes.addFlashAttribute("successMessage", "Account deleted");
				return "redirect:/";
			}
			redirectAttributes.addFlashAttribute("successMessage", "User deleted");
		} catch (IllegalArgumentException ex) {
			redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
		}
		return "redirect:/users/profile";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/login";
	}
}
