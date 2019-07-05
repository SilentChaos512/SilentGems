# Changelog

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Fixed
- Soul gems not having their data on the client when playing on a server [#380]
- Hardened stone generating over the ocean [#379]

## [3.2.1] - 2019-07-03
### Fixed
- Token enchanter recipes preventing server connection [#378]
- JEI plugin failing to load on servers

## [3.2.0] - 2019-07-01
Update to Minecraft 1.14.3
### Added
- Gems added to some loot tables
### Fixed
- Teleporters and return home charms not working

## [3.1.3] - 2019-06-24
JEI plugin is up to date. Recommended Forge is 26.0.51 or later.
### Added
- Treasure Bags support (adds bags for gem sets, no way to obtain aside from commands yet)
- A new command, `sg_soul`. Can currently be used to give soul gems or get a list of all registered souls.
### Changed
- Token enchanter recipes now use the vanilla recipe system, so they have moved to the recipes folder. `type` is `silentgems:token_enchanting`, the format is the same otherwise.
- Updated gem shard textures
### Fixed
- Soul gem ingredient not working correctly

## [3.1.2] - 2019-06-21
(would have been 3.1.1, Git had other plans)  
Minimum Silent Gear version: 1.1.2
### Added
- Gem geodes are back. These are deposits of gems that are encased in hardened rock. There are classic, dark, and light variants, all found in the overworld.
- Gem ores in the Nether and The End now spawn in "regions", similar to old optional configs. Each region will have 2-4 types of gems.
- Dark and light glowroses now spawn in the Nether and The End, respectively
- Implemented traits (some traits are still missing effects)
    - Chaotic - generates chaos when used
    - Entropy - increases block breaking speed depending on chaos levels
    - Luna - attacks deal more damage at night (cancels with Sol)
    - Persistence - slow self-repair
    - Runic - adds damage based on magic damage stat (this will change later)
    - Sol - attacks deal more damage during the day (cancels with Luna)
### Changed
- Updated dark gem ores to match new netherrack texture
- Hardened stone, netherrack, and end stone textures
- Gear souls no longer gain the armor trait unless on an armor item
### Fixed
- Requirements on some advancements (pedestals, hardened rock) [#373]
- Undyed soul urn recipe not considering gem used

## [3.1.0] - 2019-06-17
### Added
- Configs for ender slime spawns are back
- Recipe advancements for most items (except gem stuff)

## [3.0.12] - 2019-04-24
### Added
- Silver
- Corrupting powder and purifying powder. These will create corrupted blocks, or restore them to their normal state. Recipes subject to change.
### Changed
- World chaos will now balance at a certain level, instead of zero. The level fluctuates over time.
- Wild fluffy puff plants will only drop seeds now 
### Fixed
- Possible fix for [#366]
- Crash when Silent Gear not installed [#365]
- Server crash

## [3.0.11] - 2019-04-14
### Added
- Granite, diorite, andesite, and obsidian pedestals. Obsidian pedestal is black-resistant.
- Chaos gems placed on pedestals will apply effects to nearby players, if enabled. The chaos produced is reduced slightly when applied to multiple players. Chaos gems can be enabled/disabled with redstone.
- JEI plugin is back
- Supercharger JEI support, also shows the blocks required for different pillar tiers
- Token enchanter JEI support
- Couple of advancements for pedestals
- (API) IPedestalItem, allows items to be ticked when placed on pedestals
### Removed
- Unneeded block items (blocks you would normally never see in your inventory)
### Changed
- Pedestals can now accept a redstone signal to control some items.
- Rebalanced chaos buff costs 
- Add leakage rate to chaos orb tooltip
### Fixed
- Pedestals not showing items on world load. Currently fixing this by just sending an update packet occasionally, so you may need to wait a few seconds.
- Various localization errors

## [3.0.10] - 2019-04-12
Add some traits to some gems (still working on this). Many of the new traits do nothing right now.
### Added
- Chaos orbs are back. They absorb chaos, but efficiency varies. All orbs have a certain amount of "leakage". They can absorb a fixed amount of chaos, then they break.
- Pedestals. Currently just a plain stone variety, will likely add more. Right-click to place/remove an item. Holds one item only.
- Chaos orbs placed on pedestals will absorb chaos produced by nearby blocks
- Config file (finally)
### Changed
- Glowroses spawn in sparser patches (you can tweak this in the config file). You can get the old behavior by setting `glowrose.world.placeTryCount` to 64 and maybe increase `glowrose.world.maxPerPatch`
- Soul gem average drop rate reduced to 2.5% (from 3.5%). You can change this in the config file.

## [3.0.9] - 2019-03-21
### Added
- Gear souls (formerly tool souls) are back! These work with Silent Gear items only. Soul skills are now learnable traits. Currently no way to customize the skill list, but it will likely happen. This means ANY trait could be made learnable, even user-defined traits.
- New potion effects, grounded and insulated. These protect from the shocking and freezing effects (caused by the lightning/ice aspect enchantments). No way to get these effects yet.
### Fixed
- A server crash (EntityPlayerSP in ChaosGem)
- Crash when Silent Gear is not loaded
- Fluffy puffs not growing [#360]
- Life Steal, Lightning Aspect, and Ice Aspect not working (Gravity still seems off)

## [3.0.8] - 2019-03-16
### Added
- Chaos gems and chaos runes are back. Both are crafted in the token enchanter.
- More advancements (supercharger pillar tiers, teleporters, and more)
- Teleporter linker recipe
- Ender slime spawns (plus spawn egg)
### Changed
- Enriched chaos crystals are now crafted in the token enchanter (same ingredients)
- Reorganized some advancements
### Fixed
- "Modify soul urn" recipe is working again. Craft a soul urn with a gem or dyes to change its appearance. Dyeing working just like leather armor.
- Chaos dust is now craftable (and therefore chaos iron)
- Ender slime loot table

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
