# Changelog

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Added
- Fluffy blocks are back
- Advancements are back
### Fixed
- Soul urns should be fully functional (but cannot be crafted in survival due to lack of soul gem drops)

## [3.0.3] - 2019-02-24
### Added
- Null check for BlockColors and ItemColors. For some reason, these turn up null at random. This should prevent the crash when it happens but certain blocks or items may be missing their colors.

## [3.0.2] - 2019-02-23
### Added
- Glowroses can now be planted in vanilla flower pots. This amplifies their light level to 15.
- Chaos and Ender ore generation
### Fixed
- Nether and End gems not spawning. Currently, both will spawn multi-gem ore. The overworld still has biome-dependent spawns.
- Glowrose hit box and collision fixed
- Silent Gear part files corrected. These should function correctly whenever Gear is ready to release.

## [3.0.1] - 2019-02-21
### Fixed
- Crash caused by a build error [#354]

## [3.0.0] - 2019-02-21
Ported to 1.13.2. This version will bring some major changes:
- The Flattening has changed almost every block and item ID. Old worlds are not compatible.
- Tools and armor have been removed.
    - For a similar system, check out Silent Gear.
    - I am considering a mod which will add simple, non-upgradeable gem tools.
- The chaos system is gone, for now. I am working on an alternative. Chaos was never intended to be an energy system and I hope to make it into something more interesting.
