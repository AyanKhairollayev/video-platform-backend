package kz.khairollayev.videoplatformbackend.config;

import com.samskivert.mustache.Mustache;
import org.springframework.boot.autoconfigure.mustache.MustacheResourceTemplateLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

@Configuration
public class Config {
    @Bean
    public Mustache.Compiler mustacheCompiler(Mustache.TemplateLoader mustacheTemplateLoader) {
        return Mustache.compiler()
                .defaultValue("")
                .withLoader(mustacheTemplateLoader);
    }

    @Bean
    public Mustache.TemplateLoader mustacheTemplateLoader(ResourceLoader resourceLoader) {
        return new MustacheResourceTemplateLoader("classpath:/templates/", ".mustache");
    }
}
