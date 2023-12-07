package ea.slartibartfast.statemachine.config;

import ea.slartibartfast.statemachine.repository.BaseNaturalIdRepositoryImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "ea.slartibartfast.statemachine.repository" , repositoryBaseClass = BaseNaturalIdRepositoryImpl.class)
@Configuration
public class JpaConfig {
}
