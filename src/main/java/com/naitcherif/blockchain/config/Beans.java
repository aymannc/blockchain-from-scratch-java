package com.naitcherif.blockchain.config;

import com.naitcherif.blockchain.entities.Blockchain;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class Beans {
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public Blockchain blockchainInstance() {
        return new Blockchain();
    }
}
