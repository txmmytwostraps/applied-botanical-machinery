# Changelog

All notable changes to this project will be documented in this file.

## v0.1.1 — 2025-12-23

### Added
- Conditional recipe loading for MythicBotany-dependent machines
  - Four Mana Infuser recipes (`base`, `upgraded`, `advanced`, `ultimate`) now include a top-level Forge condition (`"conditions": [{ "type": "forge:mod_loaded", "modid": "mythicbotany" }]`) so they are only loaded when the `mythicbotany` mod is present.
- Checking if mana storage is full before inserting mana
  - Mana insertion logic now verifies if the target mana storage can accept more mana before performing the insertion, preventing overflows and voids.

### Changed
- JEI integration hardened for optional MythicBotany compatibility
  - Reflection lookup for MythicBotany JEI/recipe types is now cached and guarded by `ModList` checks to avoid ClassNotFound/Linkage errors.

### Fixed
- Prevent recipe/resource parsing errors when MythicBotany is not installed
  - Conditional recipe loading prevents JSON parser errors for missing MythicBotany items (e.g. `mythicbotany:mana_infuser`).
---
*Generated on 2025-12-23.*