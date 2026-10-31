# AI-use log

You are encouraged to use AI throughout this project. In return you keep this log. It is part of
every milestone, not just Lab 3, so keep adding to it all semester. It is quick if you do it as you
work and painful if you reconstruct it later.

The point is not to prove you used AI, or that you did not. The point is to show that you **own** what
you submitted: that you understood what the model produced, judged it, and decided what to keep. In
the defense you must be able to explain and modify any of it.

## How to log

One row per meaningful AI interaction. You do not need to log trivial autocompletions; do log
anything where the model produced a chunk of code or a design decision you accepted or rejected.

| Date       | What you were trying to do                                                                 | What you prompted (short)                                                                                                                                                                                                                                                                                                                                                                                 | What the model produced                                            | What you accepted or rejected, and WHY |
| ---------- | ------------------------------------------------------------------------------------------ | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------ | -------------------------------------- |
| 27.10.2026 | Create the structure of the app.                                                           | Give me a list of instructions for a LLM, to create this: "Create a home page with two buttons: one that takes me to the “Add Recipe” page and another to the “My Recipes” page. Also add a short text about the application on the home page."                                                                                                                                                           | A plan for LLM of how it should look.                              | All                                    |
| 27.10.2026 | To create the pages with nevigation between them.                                          | The plan from the previous prompt.                                                                                                                                                                                                                                                                                                                                                                        | Three pages in the app with the right navigations and titles.      | All                                    |
| 27.10.2026 | Give whole context before explaining the next step because the next 2 are closely related. | I was thinking that a recipe should have a picture of the ingredients, a name, and the calories and protein per 100g written on its card. On the “Add Recipe” page, I should be able to select a picture and enter the title. Below that, there should be a section where I can manually write the ingredient name, its value per 100 grams, and how much I use in the recipe. Give me step 2 separately. | The plan for how whould the screen for adding recipes should look. |

                                                                                                                                                                                                                                               |                                                               |                                        |

27.10.2026 | Create the "Add Receipe" page. | Give me a list of instructions for a LLM, to create this: "Create a home page with two buttons: one that takes me to the “Add Recipe” page and another to the “My Recipes” page. Also add a short text about the application on the home page." | A plan for LLM of how it should look. | All |

| Date | What you were trying to do | What you prompted (short) | What the model produced | What you accepted or rejected, and WHY |
| ---------- | ------------------------------------------------------------------------------------------ | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------ | -------------------------------------- |
| 31.10.2026 | Set up CI despite only knowing GitLab-CI, not GitHub Actions. | Asked it to translate the GitLab-CI concept into a GitHub Actions equivalent that builds and tests on every push/PR to main. | `.github/workflows/ci.yml`; fixed it after a real CI run failed on a broken `android-actions/setup-android` step, by removing it in favor of the Android SDK already preinstalled on GitHub's runner. | Accepted — verified locally with the same two Gradle tasks before trusting them in CI, then confirmed a real run went green on GitHub. |
| 31.10.2026 | Run the Lab 3 Part 4 stress tests (rotate, kill/reopen, airplane mode, 500 items, two-user edit) against the real app instead of guessing. | Asked it to drive the emulator directly via adb (force-stop, rotation, a temporary 500-recipe seed + swipe gestures, `dumpsys gfxinfo` for real frame-timing numbers) and report what actually happened. | Automated each check and reported concrete results (e.g. ~7% janky frames with real percentile data, not a guess); also diagnosed that earlier emulator lag was a memory-starved Play Store system image, not an app problem. | Accepted — independently re-ran the kill/reopen test myself to confirm; the temporary 500-recipe seed code was reverted right after, never kept in the app. |
| 31.10.2026 | Check whether the app still matched the original implementation plan before M2 architecture work starts on top of it. | Asked it to compare the colleague's original AI-written implementation plan (`local_stuff/persona_work_Log/...md`) against the actual current code. | Found that `app-idea.md` described a different app (camera + on-device ML + nutrition API, "PlateLog") than what's actually built (manual recipe entry, no camera/ML/API); logged the divergence in `local_stuff/worklog.md`. | Accepted — rewrote `app-idea.md` to describe the real prototype plus the same target vision renamed to "Meal Log," instead of silently carrying the mismatch into M2. |

## Prompts worth remembering

Optionally, keep a few prompts that worked especially well or especially badly, with a note on why.
This is useful for you, and for your team if you are working in one.

-
-
