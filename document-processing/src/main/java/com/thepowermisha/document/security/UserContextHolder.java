package com.thepowermisha.document.security;

public class UserContextHolder {
    private static final ThreadLocal<UserContext> currentUser = new ThreadLocal<>();

    public static void setCurrentUser(UserContext user) {
        currentUser.set(user);
    }

    public static UserContext getCurrentUser() {
        return currentUser.get();
    }
}
