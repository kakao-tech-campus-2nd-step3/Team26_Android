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
        maven { setUrl("https://jitpack.io") }
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
        //카카오맵
        maven("https://devrepo.kakao.com/nexus/repository/kakaomap-releases/")
        //키해시용
        maven("https://devrepo.kakao.com/nexus/content/groups/public/")
    }
}

rootProject.name = "WouldYouIn"
include(":app")
