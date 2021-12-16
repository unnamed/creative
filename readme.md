# Uracle [![Build Status](https://img.shields.io/github/workflow/status/unnamed/uracle/build/main)]() [![MIT License](https://img.shields.io/badge/license-MIT-blue)](license.txt)
A server-side library for Minecraft: Java Edition [Resource Packs](https://minecraft.fandom.com/wiki/Resource_Pack), squeeze every last
drop of Minecraft's power.

## Structure
- [**api**](api) - The main resource-pack library, only represents Vanilla resource-packs

Other resource-pack extensions may be created for some client
modifications like OptiFine

## Building
You can build Uracle by executing `./gradlew build`, or install to your Maven local repository
using `./gradlew publishToMavenLocal`