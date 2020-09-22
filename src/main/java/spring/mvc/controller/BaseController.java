package spring.mvc.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.restfb.types.User;

import spring.mvc.utils.RestFB;

@Controller
public class BaseController {

	@Autowired
	private RestFB restFB;

	@RequestMapping(value = { "/", "login" })
	public String login(@RequestParam(name = "message", required = false) String message, final Model model) {
		if (message != null && !message.isEmpty()) {
			if (message.equals("logout")) {
				model.addAttribute("message", "Logout!");
			}
			if (message.equals("error")) {
				model.addAttribute("message", "Login Failed!");
			}
			if (message.equals("facebook_denied")) {
				model.addAttribute("message", "Login by Facebook Failed!");
			}
		}
		return "login";
	}

	@RequestMapping(value = "/login-facebook")
	public String loginFacebook(HttpServletRequest request) {
		String code = request.getParameter("code");
		String accessToken = "";
		try {
			accessToken = restFB.getToken(code);
		} catch (Exception e) {
			return "login?facebook=error";
		}

		User user = restFB.getUserInfo(accessToken);
		UserDetails userDetails = restFB.buildUserDetails(user);
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
				null, userDetails.getAuthorities());
		authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		return "redirect:/user";
	}

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public String user() {
		return "user";
	}

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String admin() {
		return "admin";
	}

	@RequestMapping(value = "/403")
	public String accessDenied(final Model model, Principal principal) {
		model.addAttribute("message", "Hi " + principal.getName() + " You do not have permission to access this page!");
		return "403";
	}
}