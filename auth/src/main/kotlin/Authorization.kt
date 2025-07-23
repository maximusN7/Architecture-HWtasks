package org.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories(basePackages = ["org.example.repository"])
@EntityScan(basePackages = ["org.example.model"])
class Authorization

fun main(args: Array<String>) {
    runApplication<Authorization>(*args)
}
