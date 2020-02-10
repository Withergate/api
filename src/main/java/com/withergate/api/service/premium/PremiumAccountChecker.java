package com.withergate.api.service.premium;

import javax.validation.constraints.NotNull;

import com.withergate.api.profile.model.PremiumType;
import com.withergate.api.profile.model.Profile;
import com.withergate.api.service.exception.InvalidActionException;

/**
 * Premium account checker.
 *
 * @author Martin Myslik
 */
public class PremiumAccountChecker {

    private PremiumAccountChecker() {
        // disabled
    }

    /**
     * Checks premium account. Throws an exception if not met.
     *
     * @param type premium type
     * @param profile profile
     */
    public static void checkPremiumAccount(@NotNull PremiumType type, @NotNull Profile profile) throws InvalidActionException {
        if (profile.getPremiumType() == null) {
            throw new InvalidActionException("You need a premium account to use this feature.");
        }
        if (profile.getPremiumType().equals(PremiumType.GOLD)) {
            return; // maximum premium type
        }
        if (type.equals(PremiumType.GOLD) && !profile.getPremiumType().equals(PremiumType.GOLD)) {
            throw new InvalidActionException("You need a gold premium account to use this feature.");
        }
    }
}
