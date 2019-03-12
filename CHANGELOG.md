# Changelog

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [3.0.7] - 2019-03-12
### Fixed
- Crash related to soul gems

## [3.0.6] - 2019-03-12
### Added
- Teleporters are back.
- Return home charms are back. Each gem variant is now a separate item.
- Soul gems are back, with changes. A soul gem is automatically added for all living entities. Plant soul gems are gone. Elements are randomized and they will probably have nonsense names (TBD). The elements are meaningless until gear souls are added.
- Soul gem ingredient for use in recipes. This allows vanilla shaped/shapeless recipes and token enchanter recipes to require a soul gem of a specific entity. See the wiki for details if you want to take advantage of this ~~(politely yell at me on Discord if I have forgotten to write the wiki page)~~.
- You can now create spawn eggs in the token enchanter. Requires eggs, soul gems, and enriched chaos crystals (you can view the recipes in REI)
- `sg_chaos` command can now target the world
### Fixed
- Ornate gold and silver rods crashing the game
- `sg_chaos get` command now works correctly

## [3.0.5] - 2019-03-09
### Added
- New chaos system. This is no longer an energy system, but more like a "pollution" system. Certain blocks and items (token enchanter, supercharger) will generate chaos. Chaos will dissipate naturally over time. Currently, there is no way to increase the dissipation rate. If chaos gets too high, you may see random lightning strikes or discover patches of corrupted stone or dirt underground. More effects are planned.
- Token enchanter. This is the new way of creating enchantment tokens. It supports loading recipes from data packs (place the files in `data/*/silentgems/token_recipes`, where `*` is your data pack's namespace). These recipes are not limited to enchantment tokens, you could use it to create anything.
- Partial REI support for token enchanter and supercharger. Speed crafting (shift-click to transfer ingredients) is _not_ supported at this time.
- Wild fluffy puff plants to provide a source of seeds. Can be found in most (not all) biomes that get rain.
- All foods have their respective effects again (try the donuts!)
### Fixed
- Gems missing rarity stat (Silent Gear) [#358]
- Foods will return items again (sticks, bowls)

## [3.0.4] - 2019-03-02
### Added
- Supercharger is back, along with full Silent Gear integration
- Fluffy blocks are back
- Advancements are back
- Color handlers are back. Wrapped in a try-catch to (hopefully) prevent crashes.
- Lots of missing recipes
### Changed
- Overhauled the localization file, minimizing duplication. Should make translating the mod easier.
### Fixed
- Soul urns should be fully functional (but cannot be crafted in survival due to lack of soul gem drops)
- Ender crystal block effect was missing
- Chaos coal blocks not working as fuel
- Broken translations

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
