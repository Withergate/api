package com.withergate.api.config;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Flyway config.
 *
 * @author Martin Myslik
 */
@RequiredArgsConstructor
@Configuration
@AutoConfigureAfter({ DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
public class FlywayCustomConfig {

    @Resource(name = "dataSource")
    private final DataSource gameDatasource;

    @Resource(name = "profileDataSource")
    private final DataSource profileDataSource;

    /**
     * Primary Flyway bean. Used for the game database. This database is being wiped clean after every game.
     *
     * @return Flyway bean
     */
    @Primary
    @Bean(name = "flyway")
    @ConfigurationProperties(prefix = "flyway.game")
    public Flyway flywayGame() {
        Flyway flyway = Flyway.configure()
                .locations("classpath:db/migration/game")
                .dataSource(gameDatasource)
                .load();
        flyway.migrate();
        return flyway;
    }

    /**
     * Profile Flyway bean. Used for the profile database. This database is persistent and should not change.
     *
     * @return Flyway bean
     */
    @Bean(name = "flywayProfile")
    @ConfigurationProperties(prefix = "flyway.profile")
    public Flyway flywayProfile() {
        Flyway flyway = Flyway.configure()
                .locations("classpath:db/migration/profile")
                .dataSource(profileDataSource)
                .load();
        flyway.migrate();
        return flyway;
    }

    @Bean
    @Primary
    public FlywayMigrationInitializer flywayInitializerGame(@Qualifier("flyway") Flyway flywayGame) {
        return new FlywayMigrationInitializer(flywayGame, null);
    }

    @Bean
    public FlywayMigrationInitializer flywayInitializerProfile(@Qualifier("flywayProfile") Flyway flywayProfile) {
        return new FlywayMigrationInitializer(flywayProfile, null);
    }

}
