<p align="center"><img src="docs/assets/logo.svg" alt="logo" height="150"></p>
<h1 align="center">
Uracle
<p>
<a><img src="https://img.shields.io/github/workflow/status/unnamed/uracle/build/main" alt="Build Status"></a>
<a href="license.txt"><img src="https://img.shields.io/badge/license-MIT-blue" alt="MIT License"></a>
</p>
</h1>
<p align="center">
Library and API for next generation servers using resource packs.
</p>

## Features
- Library for compatibility between external plugins using resource packs
- Support for a variety of resource-pack servers

## Usage
Usage is simple, simply listen to `ResourcePackGenerateEvent` and write
your assets, check [the documentation](https://unnamed.team/uracle)

## Building
You can build Uracle by executing `./gradlew build`, or install to your Maven local repository
using `./gradlew publishToMavenLocal`