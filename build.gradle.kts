plugins {
    java
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.github.node-gradle.node") version "7.0.2"
    jacoco
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation ("javax.servlet:javax.servlet-api:4.0.1")
    implementation("org.apache.commons:commons-csv:1.9.0")
    implementation ("com.google.zxing:core:3.5.2")
    implementation ("com.google.zxing:javase:3.5.2")
    implementation("org.postgresql:postgresql:42.7.4")
    implementation("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation ("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.webjars.npm:bootstrap-icons:1.11.1")
    implementation("org.webjars:bootstrap:5.3.2")
    implementation("org.webjars:webjars-locator-core:0.52")
    implementation ("org.springframework.boot:spring-boot-starter-security")
    implementation ("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
    implementation ("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation ("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation ("org.modelmapper:modelmapper:3.2.0")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-logging")
    implementation ("com.microsoft.sqlserver:mssql-jdbc:12.6.1.jre11")
    // Spring Boot’s test bundle includes JUnit 5 & Mockito
    testImplementation("org.springframework.security:spring-security-test")
    implementation("io.github.classgraph:classgraph:4.8.154")

}

tasks.named<Test>("test") {
    useJUnitPlatform()
}


tasks.bootJar {
    archiveFileName.set("evalbikes-0.0.1-SNAPSHOT.jar")
}

