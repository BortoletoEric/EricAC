plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

val flavors = setOf(
    "linxtef" to 23, //subi pra 23 manualmente pq meu projeto n roda com 22
    "linxtefadyen" to 28,
    "linxtefgpos760" to 22,
    "linxtefgpos720" to 22,
    "stone" to 22,
    "pagseguro" to 23,
    "vero" to 22,
    "getnet" to 22,
    "rede" to 28,
    "cielo" to 25,
    "sicoob" to 24,
    "sitef" to 22,
    "gsurf" to 26,
)

android {
    namespace = "com.example.ericac"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.ericac"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }

    flavorDimensions += "providers"
    productFlavors {
        flavors.forEach{
            create(it.first) {
                minSdk = it.second
            }
        }
    }
}

// Defina a versão desejada utilizando o '+' para buscar o último build da release
val sdkPayServicesVersion = "2.2.0.+"
val adquirente = "stone"

// (Recomendado) Reduz o tempo de cache para buscar atualizações rapidamente
configurations.all {
    resolutionStrategy.cacheDynamicVersionsFor(30, "minutes")
    // Evita o crash do Manifest Merger removendo a biblioteca duplicada da Gertec
    exclude(group = "com.gertec", module = "ppcomp-gpos780-release")
}

dependencies {
    implementation("SDKPayServices:core-$adquirente:$sdkPayServicesVersion")
    implementation("SDKPayServices:$adquirente:$sdkPayServicesVersion")
    implementation("SDKPayServices:config:$sdkPayServicesVersion")
    implementation("SDKPayServices:common:$sdkPayServicesVersion")
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}