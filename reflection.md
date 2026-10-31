# Prototype reflection: where AI stopped

## What AI did well
- Generated the Home screen, navigation between the three screens, and the Add Recipe form (title field, photo picker, dynamic ingredient rows with validation) in Compose quickly from a plain-language description.
- Produced working Material 3 UI (Scaffold, TopAppBar, field-level validation errors) without hand-writing it from scratch.

## What broke when you stressed it
- **Rotate the device:** an in-progress Add Recipe form resets to blank. Plain `remember` state doesn't survive a configuration change. A recipe already saved to "My Recipes" does survive rotation, because it lives in `RecipeViewModel`, which Android keeps alive across rotation.
- **Kill and reopen the app:** everything is gone. `adb shell am force-stop` followed by relaunch confirmed a saved recipe disappears. Nothing is written to disk anywhere.
- **Airplane mode:** no visible difference, because there's no network code in the app at all yet. Nothing to degrade.
- **500 items in a list:** no crash or ANR. `LazyColumn` only renders the visible rows. Measured with `adb shell dumpsys gfxinfo`: about 7% of frames were janky (90th percentile around 21 to 32ms against a 16.6ms budget for 60fps). Noticeably imperfect, though this was measured on an emulator, which adds its own GPU-translation overhead.
- **Two people editing the same data:** not testable as-is. There is no concept of a second user, shared storage, or conflict resolution anywhere in the code. `RecipeViewModel` is a single in-memory list local to one app instance.

## What is missing for production

| Gap in the prototype | Lab that fixes it |
|---|---|
| No separation of UI and logic. In-progress form state resets on rotation | Lab 4 (architecture) |
| No data from a real server. Nutrition is typed by hand | Lab 5 (networking) |
| Nothing is saved on the device. A kill/reopen loses everything | Lab 6 (persistence) |
| Nothing is online yet to begin with, so there's no offline behaviour to speak of. No background sync | Lab 7 (offline-first, WorkManager) |
| Dependencies wired by hand (plain `viewModel()`, no DI container) | Lab 8 (dependency injection) |
| No real tests. Only the Android Studio template's placeholder tests exist | Lab 9 (testing, CI) |
| No mobile-computing concept beyond a static UI yet (no on-device ML, no placement decision implemented) | Lab 10 (integrated concept) |

## One sentence
> AI built a prototype fast, but only what we asked for. But what happens when an edge case (rotation, restart, offline) appears is not something the LLM could infer, it's up to us (devs) and the user to make a judgment call on what happens / should happen.
