package bg.softuni.carsHeaven.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private MyInterceptor myInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myInterceptor)
                .addPathPatterns(
                        "/brands/edit/{brandId}",
                        "/brands/remove/{brandId}",
                        "/brands/add",
                        "/models/add-car",
                        "/models/remove/{brandId}/{modelId}",
                        "/models/edit/{brandId}/{modelId}",
                        "/details/{modelId}/add-detail",
                        "/details/remove/{modelId}/{detailId}",
                        "/details/{modelId}/edit-detail/{detailId}",
                        "/users/all-users",
                        "/api/remove-model",
                        "/api/make-admin",
                        "/api/remove-admin");
    }
}
