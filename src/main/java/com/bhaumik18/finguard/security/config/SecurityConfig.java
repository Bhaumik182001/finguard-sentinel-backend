package com.bhaumik18.finguard.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // Added this import
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.bhaumik18.finguard.security.filter.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final JwtAuthenticationFilter jwtAuthFilter;
	private final AuthenticationProvider authenticationProvider;

	private static final String[] SWAGGER_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
		    .csrf(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(auth -> auth
					.requestMatchers(SWAGGER_WHITELIST).permitAll()
					.requestMatchers("/api/v1/auth/**").permitAll()
                    // NEW FIX: Explicitly allow all pre-flight OPTIONS requests
					.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() 
					.anyRequest().authenticated()
			)
			.authenticationProvider(authenticationProvider)
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	
	@Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Whitelist React (3000), Vue/Vite (5173), and typical Next.js (3000) ports
        configuration.setAllowedOrigins(List.of(
                "http://localhost:3000", 
                "http://localhost:5173",
                "https://finguard-api.duckdns.org",
                "https://finguard-ui.vercel.app"
        ));
        
        // Allow standard HTTP methods
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // NEW FIX: Allow all headers so Axios doesn't trigger a rejection
        configuration.setAllowedHeaders(List.of("*"));
        
        // Required if your frontend needs to pass cookies or specific auth credentials
        configuration.setAllowCredentials(true);

        // Apply these rules to every single endpoint in our API
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}