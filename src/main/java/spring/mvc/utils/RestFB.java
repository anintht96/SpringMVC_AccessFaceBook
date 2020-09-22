package spring.mvc.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import com.restfb.types.User;

@Component
public class RestFB {

	public static final String FACEBOOK_APP_ID = "2815057678774570";
	public static final String FACEBOOK_APP_SECRET = "2403dd29c215c555054e21a5d8a15c4f";
	public static final String FACEBOOK_REDIRECT_URL = "https://localhost:8443/SpringMVC_AccessFaceBook/login-facebook";
	public static final String FACEBOOK_LINK_GET_TOKEN = "https://graph.facebook.com/oauth/access_token?client_id=%s&client_secret=%s&redirect_uri=%s&code=%s";

	public String getToken(final String code) throws ClientProtocolException, IOException {
		String link = String.format(FACEBOOK_LINK_GET_TOKEN, FACEBOOK_APP_ID, FACEBOOK_APP_SECRET,
				FACEBOOK_REDIRECT_URL, code);
		String response = Request.Get(link).execute().returnContent().asString();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(response).get("access_token");
		return jsonNode.textValue();
	}

	public User getUserInfo(final String accessToken) {
		FacebookClient facebookClient = new DefaultFacebookClient(accessToken, FACEBOOK_APP_SECRET, Version.LATEST);
		return facebookClient.fetchObject("me", User.class);
	}

	public UserDetails buildUserDetails(User user) {
		boolean enabled = true;
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getId(), "", enabled,
				accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		return userDetails;

	}
}
