plugins {
	java
	id("org.springframework.boot") version "3.0.6"
	id("io.spring.dependency-management") version "1.1.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
	maven(url = "https://jaspersoft.artifactoryonline.com/jaspersoft/jaspersoft-repo/")
	maven(url = "https://maven.icm.edu.pl/artifactory/repo/")
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("net.sf.jasperreports:jasperreports:6.7.0")
	implementation("com.lowagie:itext:2.1.7")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
