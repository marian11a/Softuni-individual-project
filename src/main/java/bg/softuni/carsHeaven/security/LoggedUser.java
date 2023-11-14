package bg.softuni.carsHeaven.security;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class LoggedUser {

    private String username;

    private boolean isLogged;

    public void login(String username) {
        setUsername(username);
        setLogged(true);
    }

    public void logout() {
        setUsername(null);
        setLogged(false);
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
    }
}
