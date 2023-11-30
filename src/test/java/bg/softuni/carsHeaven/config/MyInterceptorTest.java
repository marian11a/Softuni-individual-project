package bg.softuni.carsHeaven.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class MyInterceptorTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Object handler;

    @Mock
    private ModelAndView modelAndView;

    @InjectMocks
    private MyInterceptor myInterceptor;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup()
                .addInterceptors(myInterceptor)
                .build();
    }

    @Test
    void preHandle() throws Exception {
        // Arrange
        setAuthentication("testUser");

        // Act
        boolean result = myInterceptor.preHandle(request, response, handler);

        // Assert
        verify(request, times(1)).getRequestURL();
        verify(response, never()).getStatus();
        assertTrue(result);
        // Add more assertions based on your specific logic
    }

    @Test
    void afterCompletion() throws Exception {
        // Arrange
        setAuthentication("testUser");

        // Act
        myInterceptor.afterCompletion(request, response, handler, null);

        // Assert
        verify(request, times(1)).getRequestURL();
        verify(response, times(1)).getStatus();
        // Add more assertions based on your specific logic
    }

    private void setAuthentication(String username) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
