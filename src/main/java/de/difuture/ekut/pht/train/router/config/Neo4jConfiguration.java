package de.difuture.ekut.pht.train.router.config;

import javax.persistence.EntityManagerFactory;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableNeo4jRepositories(
        basePackages = {"de.difuture.ekut.pht.train.router.repository.traindestination"},
        transactionManagerRef = "neo4jTransactionManager")
@EnableJpaRepositories(
        basePackages = {
                "de.difuture.ekut.pht.train.router.repository.routeevent",
                "de.difuture.ekut.pht.train.router.repository.trainroutes"},
        transactionManagerRef = "jpaTransactionManager")
@EnableTransactionManagement
public class Neo4jConfiguration {

    @Bean
    public org.neo4j.ogm.config.Configuration configuration() {

        return new org.neo4j.ogm.config.Configuration.Builder()
                .uri("bolt://neo4j:7687")
                .build();
    }

    @Bean
    public SessionFactory sessionFactory(org.neo4j.ogm.config.Configuration configuration) {

        return new SessionFactory(
                configuration,
                "de.difuture.ekut.pht.train.router.repository.traindestination");
    }

    @Bean
    public Neo4jTransactionManager neo4jTransactionManager(SessionFactory sessionFactory) {

        return new Neo4jTransactionManager(sessionFactory);
    }

    @Bean
    public JpaTransactionManager jpaTransactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
