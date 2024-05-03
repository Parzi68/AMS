//package com.project.ams;
//
//import java.util.Date;
//import java.util.concurrent.TimeUnit;
//
//import com.project.ams.vaadin.AssetInfo;
//import com.project.ams.vaadin.Index;
//import com.project.ams.views.LoginView;
//import com.vaadin.flow.component.UI;
//import com.vaadin.flow.router.RouteConfiguration;
//import com.vaadin.flow.server.VaadinSession;
//
//public class SessionController {
//
//	VaadinSession session = VaadinSession.getCurrent();
//
//	public static final String ID = "USER_ID";
//	public static final String NAME = "USER_NAME";
//	public static final String ROLE = "ROLE";
//
//	public Boolean isLoggedIn(Long id) {
//        // Check if the user is logged in
//        if (session.getAttribute(ID) != null) {
//            // Check if the user's role allows access to the requested URL
//            String role = getRole();
//            if (role != null && role.equals("ADMIN")) {
//                return true; // Allow access for ADMIN role
//            } else if (role != null && role.equals("OPERATOR")) {
//                // Check if the requested URL is allowed for USER role
//                String requestedUrl = RouteConfiguration.forSessionScope().getUrl(Index.class);
//                String requestedUrl2 = RouteConfiguration.forSessionScope().getUrl(AssetInfo.class);
//                if (requestedUrl != null && requestedUrl.equals("/admin") && requestedUrl2.equals("/asset")) {
//                    return false; // Deny access for USER role to /restricted URL
//                }
//                return true; // Allow access for USER role to other URLs
//            } else {
//                return false; // Deny access if role is unknown or null
//            }
//        } else {
//            return false; // Deny access if user is not logged in
//        }
//    }
//
//	public Boolean setSessionAttributes(Long id, String username, String role) {
//
//		try {
//			if (id != null && username != null) {
//				session.setAttribute(ID, id);
//				session.setAttribute(NAME, username);
//				session.setAttribute(ROLE, role);
//
//				return true;
//			} else
//				return false;
//		} catch (Exception e) {
//			// TODO: handle exception
//			return false;
//		}
//
//	}
//
//	public String getRole() {
//
//		try {
//			if (session.getAttribute(ROLE) != null) {
//
//				return session.getAttribute(ROLE).toString();
//			} else
//				return null;
//		} catch (Exception e) {
//			// TODO: handle exception
//			return null;
//		}
//
//	}
//
//	public String getUsername() {
//
//		try {
//			if (session.getAttribute(NAME) != null) {
//
//				return session.getAttribute(NAME).toString();
//			} else
//				return null;
//		} catch (Exception e) {
//			// TODO: handle exception
//			return null;
//		}
//
//	}
//
//	public String getId() {
//
//		try {
//			if (session.getAttribute(ID) != null) {
//				return session.getAttribute(ID).toString();
//			} else
//				return null;
//		} catch (Exception e) {
//			// TODO: handle exception
//			return null;
//		}
//
//	}
//
//	public void checkSession(Long id) {
//
//		Date dt = new Date(session.getSession().getLastAccessedTime());
//
//		Date dtNow = new Date();
//
//		long diss = dtNow.getTime() - dt.getTime();
//
//		TimeUnit time = TimeUnit.MINUTES;
//		diss = time.convert(diss, TimeUnit.MILLISECONDS);
//
//		try {
//			if (diss >= 30) {
//				session.getService().closeSession(session);
////			System.out.println("Logout");
////			Notification.show("Logged Out");
//				UI.getCurrent().navigate(LoginView.class);
//			}
//
//			else if (!isLoggedIn(id)) {
////			System.err.println("Loged Out");
////			Notification.show("Loged Out");
//				UI.getCurrent().navigate(LoginView.class);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//	}
//
//	public void clearSession() {
//
//		session.getService().closeSession(session);
//		UI.getCurrent().navigate(LoginView.class);
//	}
//
//}
