package com.withergate.api.service.premium;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.withergate.api.profile.model.PremiumType;
import lombok.Getter;

/**
 * Premium account checker annotation.
 *
 * @author Martin Myslik
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Premium {

    PremiumType type() default PremiumType.GOLD;

}
