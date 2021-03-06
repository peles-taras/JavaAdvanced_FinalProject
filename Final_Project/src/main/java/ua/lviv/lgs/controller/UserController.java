package ua.lviv.lgs.controller;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import ua.lviv.lgs.domain.Faculty;
import ua.lviv.lgs.domain.Subject;
import ua.lviv.lgs.domain.User;
import ua.lviv.lgs.service.FacultyService;
import ua.lviv.lgs.service.SubjectService;
import ua.lviv.lgs.service.UserDTO;
import ua.lviv.lgs.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private FacultyService facultyService;

	@Autowired
	private SubjectService subjectService;

	@RequestMapping(value = "/registration", method = RequestMethod.GET)
	public String registration(Model model) {
		model.addAttribute("userForm", new User());
		return "registration";
	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model) throws IOException {

		if (bindingResult.hasErrors()) {
			return "registration";
		}
		userService.save(userForm);
		
		return "redirect:/home";
	}

	@RequestMapping(value = { "/", "/login" }, method = RequestMethod.GET)
	public String login(Model model, String error, String logout) {
		if (error != null)
			model.addAttribute("error", "Your username and password is invalid.");

		if (logout != null)
			model.addAttribute("message", "You have been logged out successfully.");

		return "login";
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ModelAndView welcome(HttpServletRequest req) {
		ModelAndView map = new ModelAndView("home");
		map.addObject("faculties", facultyService.getAllFaculties());
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = auth.getName();
		Optional<User> user = userService.findByEmail(userEmail);
		req.setAttribute("user", user.get());
		return map;
	}

	@RequestMapping(value = "/createFaculty", method = RequestMethod.GET)
	public ModelAndView createFaculty() {
		return new ModelAndView("createFaculty", "faculty", new Faculty());
	}

	@RequestMapping(value = "/profile/{email}", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView getProfileInfo(@PathVariable String email, HttpServletRequest req) {
		Optional<User> user = userService.findByEmail(email);
		req.setAttribute("user", user.get());

		ModelAndView map = new ModelAndView("profile");
		map.addObject("applications", user.get().getApplications());
		map.addObject("subjects", user.get().getSubjects());
		return map;
	}

	@RequestMapping(value = "/profile/{email}/addSubject", method = RequestMethod.POST)
	public ModelAndView addSubject(@PathVariable String email, @RequestParam String name, @RequestParam Double value) {

		Optional<User> user = userService.findByEmail(email);

		Subject subject = new Subject();
		subject.setName(name);
		subject.setValue(value);
		subject.setUser(user.get());
		subjectService.save(subject);

		Double averageScore = UserDTO.calculateScore(user.get());
		user.get().setAverageScore(averageScore);
		userService.update(user.get());

		return new ModelAndView("redirect:/profile/" + email);
	}

	@RequestMapping(value = "/profile/{email}/addCertificateScore", method = RequestMethod.POST)
	public ModelAndView addCertificateScore(@PathVariable String email, @RequestParam Double score) {
		Optional<User> user = userService.findByEmail(email);
		user.get().setAvgSchoolScore(score);

		Double averageScore = UserDTO.calculateScore(user.get());
		user.get().setAverageScore(averageScore);

		userService.update(user.get());
		return new ModelAndView("redirect:/profile/" + email);
	}
	
	@RequestMapping(value = "/profile/{email}/addPhoto", method = RequestMethod.POST)
	public ModelAndView addPhoto(@PathVariable String email, @RequestParam MultipartFile encodedImage) throws IOException {
		
		Optional<User> user = userService.findByEmail(email);
		UserDTO.encodeImage(encodedImage, user.get());
		userService.update(user.get());
		
		return new ModelAndView("redirect:/profile/" + email);
	}

}
