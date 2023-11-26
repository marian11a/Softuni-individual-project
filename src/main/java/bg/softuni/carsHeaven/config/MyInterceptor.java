package bg.softuni.carsHeaven.config;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;

@Component
public class MyInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(System.lineSeparator()).append(System.lineSeparator())
                .append("              ----My Interceptor:---- ")
                .append(System.lineSeparator())
                .append("Intercepting request!")
                .append(System.lineSeparator())
                .append("Request URL: ").append(request.getRequestURL())
                .append(System.lineSeparator())
                .append("by: (").append(SecurityContextHolder.getContext().getAuthentication().getName()).append(")")
                .append(System.lineSeparator())
                .append("at: ").append(LocalDateTime.now())
                .append(System.lineSeparator())
                .append("              ----My Interceptor:----")
                .append(System.lineSeparator()).append(System.lineSeparator());

        System.out.println(sb);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(System.lineSeparator()).append(System.lineSeparator())
                .append("              ----My Interceptor:---- ")
                .append(System.lineSeparator())
                .append("Requested URL: ").append(request.getRequestURL())
                .append(" by (").append(SecurityContextHolder.getContext().getAuthentication().getName()).append(") processed.")
                .append(System.lineSeparator())
                .append("Response status: ")
                .append(response.getStatus())
                .append(System.lineSeparator())
                .append("              ----My Interceptor:----")
                .append(System.lineSeparator()).append(System.lineSeparator());

        System.out.println(sb);
    }
}
