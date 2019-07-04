package com.withergate.api.filter;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.withergate.api.model.Clan;
import com.withergate.api.service.clan.ClanService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Lst activity filter. Tracks the last activity for the currently authenticated clan.
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@Component
public class ClanActivityFilter implements Filter {

    private final ClanService clanService;

    @Override
    public void destroy() {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterchain) throws IOException, ServletException {
        // get the authenticated user from the context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            try {
                Clan clan = clanService.getClan(Integer.parseInt(authentication.getName()));

                // if clan exists - set last activity
                if (clan != null) {
                    clan.setLastActivity(LocalDateTime.now());
                    clanService.saveClan(clan);
                }
            } catch (Exception e) {
                log.trace("Error parsing authentication name.", e);
            }
        }

        // proceed with the request
        filterchain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterconfig) {}
}
