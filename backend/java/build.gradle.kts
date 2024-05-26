plugins {
	java
	id("org.springframework.boot") version "3.2.5"
	id("io.spring.dependency-management") version "1.1.4"
}

group = "com.numetrify"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {

	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")

	// Swagger dependencies
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")

	// Math parser dependencies
	implementation("org.mariuszgromada.math:MathParser.org-mXparser:6.0.0")
	implementation("org.jfree:jfreechart:1.5.3")
	implementation("org.apache.commons:commons-math3:3.6.1")
	implementation("org.matheclipse:matheclipse-core:3.0.0")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
