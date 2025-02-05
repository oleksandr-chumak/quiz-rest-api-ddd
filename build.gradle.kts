plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.4.2"
	id("io.spring.dependency-management") version "1.1.7"
	kotlin("plugin.jpa") version "1.9.25"
}

group = "com.github"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// Core dependencies for Spring Boot and JPA
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	runtimeOnly("org.postgresql:postgresql")

	// Test dependencies
	testImplementation("org.springframework.boot:spring-boot-starter-test") // Spring Boot Test
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5") // Kotlin JUnit5 test support
	testRuntimeOnly("org.junit.platform:junit-platform-launcher") // JUnit Platform launcher
	testImplementation("com.h2database:h2") // H2 Database for in-memory testing (required for integration tests)
	testImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0") // Mockito for mocking dependencies in unit tests
	testImplementation("org.mockito:mockito-inline:5.1.0") // Mockito inline for better mock handling
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
