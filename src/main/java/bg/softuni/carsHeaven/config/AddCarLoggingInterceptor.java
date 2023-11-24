package bg.softuni.carsHeaven.config;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AddCarLoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        System.out.printf("\n\n              ----My Interceptor:---- \n" +
                "Intercepting request to add a car. Request URL: "
                + request.getRequestURL()
                + " by ("
                + SecurityContextHolder.getContext().getAuthentication().getName() +
                ")\n              ----My Interceptor:---- \n\n");
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        System.out.printf("\n\n" +
                "               ----My Interceptor:---- \n" +
                "Request to add a car by ("
                + SecurityContextHolder.getContext().getAuthentication().getName()
                + ") processed. Response status: " + response.getStatus() +
                "\n               ----My Interceptor:---- \n\n");
    }
}
