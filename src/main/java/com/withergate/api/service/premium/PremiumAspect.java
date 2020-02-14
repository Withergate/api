package com.withergate.api.service.premium;

import com.withergate.api.profile.model.PremiumType;
import com.withergate.api.profile.model.Profile;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.profile.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Premium aspect. Handles @premium annotaiton.
 *
 * @author Martin Myslik
 */
@Aspect
@Slf4j
@RequiredArgsConstructor
@Component
public class PremiumAspect {

    private final ProfileService profileService;

    /**
     * Checks whetehr logged user has a premium account of required level.
     */
    @Around("@annotation(Premium)")
    public Object checkPremium(ProceedingJoinPoint joinPoint) throws Throwable {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.debug("Checking premium account for {}.", auth.getName());

        int profileId = Integer.parseInt(auth.getName()); // get logged in username as integer

        Profile profile = profileService.getProfile(profileId);

        // get premium type
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Premium annotation = signature.getMethod().getAnnotation(Premium.class);
        PremiumType type = annotation.type();

        if (profile.getPremiumType() == null
                || (type.equals(PremiumType.GOLD) && profile.getPremiumType().equals(PremiumType.SILVER))) {
            throw new InvalidActionException("You must have a " + type + " premium account to use this feature!");
        }

        return joinPoint.proceed();
    }

}
