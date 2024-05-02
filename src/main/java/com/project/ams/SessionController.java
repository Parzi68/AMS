package com.project.ams;

import java.util.Date; 
import java.util.concurrent.TimeUnit;

import com.project.ams.views.LoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;

public class SessionController {

	VaadinSession session = VaadinSession.getCurrent();

	public static final String ID = "USER_ID";
	public static final String NAME = "USER_NAME";
	public static final String ROLE = "ROLE";

	public Boolean isLoggedIn(Long id) {

		if (session.getAttribute(ID) != null) {
			if (session.getAttribute(ID).equals(id))
				return true;
			else
				return false;
		} else
			return false;
	}

	public Boolean setSessionAttributes(Long id, String username, String role) {

		try {
			if (id != null && username != null) {
				session.setAttribute(ID, id);
				session.setAttribute(NAME, username);
				session.setAttribute(ROLE, role);

				return true;
			} else
				return false;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}

	}

	public String getRole() {

		try {
			if (session.getAttribute(ROLE) != null) {

				return session.getAttribute(ROLE).toString();
			} else
				return null;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}

	public String getUsername() {

		try {
			if (session.getAttribute(NAME) != null) {

				return session.getAttribute(NAME).toString();
			} else
				return null;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}

	public String getId() {

		try {
			if (session.getAttribute(ID) != null) {
				return session.getAttribute(ID).toString();
			} else
				return null;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}

	public void checkSession(Long id) {

		Date dt = new Date(session.getSession().getLastAccessedTime());

		Date dtNow = new Date();

		long diss = dtNow.getTime() - dt.getTime();

		TimeUnit time = TimeUnit.MINUTES;
		diss = time.convert(diss, TimeUnit.MILLISECONDS);

		try {
			if (diss >= 30) {
				session.getService().closeSession(session);
//			System.out.println("Logout");
//			Notification.show("Logged Out");
				UI.getCurrent().navigate(LoginView.class);
			}

			else if (!isLoggedIn(id)) {
//			System.err.println("Loged Out");
//			Notification.show("Loged Out");
				UI.getCurrent().navigate(LoginView.class);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void clearSession() {

		session.getService().closeSession(session);
		UI.getCurrent().navigate(LoginView.class);
	}

}
