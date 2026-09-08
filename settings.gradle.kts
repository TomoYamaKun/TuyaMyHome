pluginManagement {

    repositories {

        google()

        mavenCentral()

        gradlePluginPortal()
    }
}


dependencyResolutionManagement {

    repositoriesMode.set(
        RepositoriesMode.PREFER_SETTINGS
    )

    repositories {

        google()

        mavenCentral()


        // ========================================================
        // Tuya Maven Repository
        // ========================================================

        maven {
            url = uri(
                "https://maven-other.tuya.com/repository/maven-releases/"
            )
        }


        // Tuya Commercial Repository
        maven {
            url = uri(
                "https://maven-other.tuya.com/repository/maven-commercial-releases/"
            )
        }


        // Tuya Snapshot Repository
        maven {
            url = uri(
                "https://maven-other.tuya.com/repository/maven-snapshots/"
            )
        }
    }
}


rootProject.name = "TuyaMyHome"

include(":app")