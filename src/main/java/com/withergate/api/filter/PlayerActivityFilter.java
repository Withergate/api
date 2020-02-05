package com.withergate.api.filter;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.withergate.api.profile.model.Profile;
import com.withergate.api.service.profile.ProfileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Lst activity filter. Tracks the last activity for the currently authenticated player.
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@Component
public class PlayerActivityFilter implements Filter {

    private final ProfileService profileService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterchain) throws IOException, ServletException {
        // get the authenticated user from the context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            try {
                Profile profile = profileService.getProfile(Integer.parseInt(authentication.getName()));

                // if profile exists - set last activity
                if (profile != null) {
                   profile.setLastActivity(LocalDateTime.now());
                    profileService.saveProfile(profile);
                }
            } catch (Exception e) {
                log.trace("Error parsing authentication name.", e);
            }
        }

        // proceed with the request
        filterchain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterconfig) {
        // no action needed
    }

    @Override
    public void destroy() {
        // no action needed
    }

}
