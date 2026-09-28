pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        val azureToken = providers.gradleProperty("AZURE_PERSONAL_ACCESS_TOKEN").orNull
            ?: System.getenv("AZURE_PERSONAL_ACCESS_TOKEN")

        println("TOKEN CARREGADO: ${azureToken != null}")

        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }

        maven {
            name = "SDK_UNICO"
            url = uri("https://pkgs.dev.azure.com/stndtef/SmartPOS/_packaging/SDK_UNICO@Release/maven/v1")
            credentials {
                username = "stndtef"
                password = azureToken
            }
        }

        maven {
            name = "SDK_UNICO_DEPENDENCIES"
            url = uri("https://pkgs.dev.azure.com/stndtef/SmartPOS/_packaging/SDK_UNICO@Dependencies/maven/v1")
            credentials {
                username = "stndtef"
                password = azureToken
            }
        }
    }
}

rootProject.name = "EricAC"
include(":app")