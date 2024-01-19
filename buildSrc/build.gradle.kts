plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

val kotlinVersion = "2.0.0-Beta3"
val dokkaPluginVersion = "1.9.10"
val gradleCommon = "0.2.0"
val nexusPublishPlugin = "1.3.0"
val buildConfig = "3.1.0"

dependencies {
    api(gradleApi())
    api(kotlin("gradle-plugin", kotlinVersion))
    api(kotlin("serialization", kotlinVersion))
    api("org.jetbrains.dokka:dokka-gradle-plugin:$dokkaPluginVersion")

    // see https://docs.gradle.org/current/userguide/publishing_gradle_plugins.html
    api("com.gradle.publish:plugin-publish-plugin:1.2.1")
    // see https://github.com/gmazzo/gradle-buildconfig-plugin
    api("com.github.gmazzo:gradle-buildconfig-plugin:$buildConfig")
    // see https://github.com/bnorm/kotlin-power-assert#compatibility
    api("gradle.plugin.com.bnorm.power:kotlin-power-assert-gradle:0.12.0")
    // see https://github.com/gradle-nexus/publish-plugin
    api("io.github.gradle-nexus:publish-plugin:$nexusPublishPlugin")

    api("com.github.jengelman.gradle.plugins:shadow:6.1.0")

    implementation("love.forte.gradle.common:gradle-common-core:$gradleCommon")
    implementation("love.forte.gradle.common:gradle-common-kotlin-multiplatform:$gradleCommon")
    implementation("love.forte.gradle.common:gradle-common-publication:$gradleCommon")
}

// tasks.test {
//     useJUnitPlatform()
// }
//
// tasks.withType<KotlinCompile> {
//     kotlinOptions.jvmTarget = "1.8"
// }
