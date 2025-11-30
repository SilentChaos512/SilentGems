# Changelog

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Added
- Ukrainian translation (Lorp_OOO)

## [1.21.1-5.1.2.1] - 2025-09-07
### Fixed
- Missing recipes for new building blocks
- Missing tags for chaos essence blocks and silver blocks [#590]

## [1.21.1-5.1.2] - 2025-07-13
Requires Silent Gear 4.0.25 or higher
### Added
- Five new gems: garnet, aquamarine, tanzanite, opal, and pearl
- New traits
- Reinforced gold/silver rods
### Fixed
- Redstone teleporters will no longer move entities in the `c:teleporting_not_supported` tag and will not move entities across dimensions if the entity normally does not allow it.

## [1.21.1-5.1.1] - 2025-06-22
### Added
- Sparkling bone meal, which can be used to grow random glowroses (related to #588)
### Fixed
- Bone meal creating only ruby glowroses when used [#588]

## [1.21.1-5.1.0] - 2025-06-16
### Added
- Teleporters, redstone teleporters, and teleporter anchors are back!
  - They currently cost nothing to use, but some sort of "cost" will be added later
  - All three types can be linked to each other
  - Regular teleporters activate when used (right-click)
  - Redstone teleporters can be activated like regular ones, or with redstone, which moves all nearby entities
  - Anchors can be linked to, but cannot teleport anything; they are only an "exit point". This can be useful for building something like an elevator shaft.

## [1.21.1-5.0.3] - 2024-10-25
### Added
- Harvest tier level hints for Silent Gear materials (requires Silent Gear 4.0.8 or higher)

## [1.21.1-5.0.2] - 2024-10-15
### Added
- A configuration screen
### Fixed
- A crash caused by incorrect usage of ModConfigEvent

## [1.21.1-5.0.1] - 2024-10-03
### Added
- Silent Gear compatibility is back

## [1.21.1-5.0.0] - 2024-09-21
- Updated to 1.21.1 (NeoForge, 1.21 compatible)
- No Silent Gear compatibility at this time, since Silent Gear is not yet updated
- I have some rough plans to start adding new features, and re-introducing some old ones...
### Added
- Chaos ore, chaos essence, blocks of chaos essence (no other uses at this time)
- Cups of coffee are back
- Add a new entity type tag, `silentgems:coffee_producers` so the mobs that produce coffee can be customized

## [1.20.4-4.8.0] - 2024-06-22
- Updated to 1.20.4 (NeoForge)

## [1.20.1-4.7.0] - 2024-03-18
- Updated for Silent Gear 3.6.0 and higher (required!) Materials have been updated to use the new harvest tiers.
### Added
- Russian translation (Вэсэло)

## [1.20.1-4.6.0] - 2023-06-23
- Updated to 1.20.1
### Fixed
- World generation not working since 1.19.4
