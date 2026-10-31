# App idea

## The basics
- **App name:** Meal Log
- **One-line pitch:** A recipe log for home cooks that turns a photo and a few typed numbers into per-serving calories and protein, working fully offline with automatic nutrition lookups when online.
- **The problem:** Manual nutrition tracking is tedious — searching up each ingredient's values and typing them in by hand is the main reason people give up on logging what they eat.
- **Target users:** Home cooks logging what they make, often in a kitchen with weak or no Wi-Fi, who want their recipes' nutrition without re-typing the same ingredient data every time.

## The prototype scope (what exists today)
- **Core screens:** Home (links to Add Recipe / My Recipes), Add Recipe (title, a photo picked from the gallery, a dynamic list of ingredients — name, calories/100g, protein/100g, amount used), My Recipes (saved recipes with photo and computed calories/protein per 100g).
- **The main thing managed:** a Recipe made of manually entered Ingredients.
- **A few fields:** Recipe(title, photo, ingredients); Ingredient(name, caloriesPer100g, proteinPer100g, amountGrams).
- **Not yet built:** taking a photo with the camera (currently just picks an existing one), on-device ingredient recognition, a nutrition API, any persistence, any network code at all.

## The data
- **What records does the app create or collect?** Recipes, each with a photo and a list of ingredients; eventually, nutrition values the app looks up instead of the user typing them.
- **Where does data come from?** Today: entirely the user (typed values, a picked photo). Eventually: user input plus an on-device model suggesting ingredient names from a photo, plus a public nutrition API filling in calories/protein once online.
- **Where does data need to go?** Everything stays on the device. Only ingredient names would ever be sent out, to a nutrition API for lookup; photos never leave the phone.

## Mobile-computing framing
- **What must persist on the device?** Every saved recipe and its ingredients — today this already fails (see `reflection.md`); it's the most urgent gap.
- **What crosses the network?** Nothing yet. Eventually: outgoing ingredient names for a nutrition lookup; incoming per-100g values, converted to totals and merged into the stored ingredient.
- **What must still work with no signal?** Everything except an automatic nutrition lookup: adding a recipe, typing or correcting ingredient values by hand, browsing saved recipes. The manual entry the prototype already has becomes the offline fallback, not wasted work.
- **The placement question:** ingredient-name suggestion from a photo, if added, would run **on-device** (privacy — kitchen photos never leave the phone; latency — no round trip; works with no signal). Nutrition lookup stays **in the cloud** — the food database is too large and too frequently updated to ship inside the app. Trade-off: on-device recognition is less accurate than a cloud model, so the user would always confirm or correct it — which is exactly the manual-entry flow that already exists today.

## What comes next
Per Lab 4: move `AddRecipeScreen`'s local form state (currently plain `remember { mutableStateOf(...) }`) into a dedicated ViewModel backed by `SavedStateHandle`, so an in-progress recipe survives rotation. That's the most visible failure from this round of stress testing, and it's exactly what Lab 4 (architecture) covers next.
