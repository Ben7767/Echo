# ECHO 🔁

**Your past is your enemy.**

Made by **Tech4U**.

A one-thumb arcade shooter for mobile and desktop. Every 20 seconds time resets, and your *echo* from the previous round comes back. It replays every move and every shot you made. Echo bullets kill enemies… and they kill you too.

## Play

Open `index.html` in any browser. There's no build step and nothing to install.

## Features

- **Echo mechanic.** Each round you play is recorded at a fixed 60 Hz and replayed in later rounds, with up to 6 echoes on screen at once.
- **Daily challenge.** One seeded level per day, the same for every player on Earth.
- **World ranking.** A global top 50 for each Daily Challenge, with your own rank ("#37 of 1,204"). It uses anonymous Firebase Auth and Firestore REST, with no SDK.
- **Endless mode.** A random seed each run.
- **Challenge a friend.** Share a link that drops your friend into the exact same level with your score as the target, shows a live "beat them" banner, and lets them challenge you back.
- **Haptics.** Vibration on hits, bombs, bosses and unlocks, via the native Android vibrator, with an on/off toggle.
- **Share card.** At the end of a run you get a Wordle-style emoji result you can paste anywhere.
- **Adaptive music.** A synthwave loop that gains a layer (hats, hook, pads, double kick) with each echo.
- **Procedural audio.** Layered explosions with sub-kick, distortion, crack, reverb tail and debris. Everything is made with the Web Audio API, so the game uses no audio files.
- **Combo multiplier.** Chain kills for up to x5 points.
- **Upgrades between rounds.** Pick 1 of 3 perks after every round, such as Twin Shot, Overclock, Piercing, Seeker, Nova, Orbiter drones, Echo Shield, Phase and Extra Heart.
- **Power-ups.** Enemies drop Bomb, Slow-mo, Rapid Fire, Shield and Heart.
- **Boss every 5 rounds.** "The Core" has ring, spiral, summon and aimed-fan attack patterns, an HP bar, a rage phase and a huge death explosion.
- **Hangar.** Unlock 9 ships through achievements (round 5, boss kills, 60-kill combos, 7 daily challenges and more). Each ship has its own shape, color and trail, and your echoes keep the ship they were recorded with.
- **More enemies.** Shooters that telegraph their aim, plus swarms that attack in packs.
- Six enemy types: chasers, dashers, tanks that split, shooters, swarms and the boss.

## Android app (APK + AAB)

The `android/` folder is a tiny native Android project. It's a full-screen WebView that runs the same `index.html`, with a native share sheet, back-button handling, immersive mode and offline play.

**You don't need Android Studio.** Every push to `main` triggers `.github/workflows/android.yml`, which builds and publishes a new **GitHub Release** with:

- `ECHO-v1.0.N.apk`: install it directly on any Android phone.
- `ECHO-v1.0.N.aab`: upload this one to **Google Play Console**.

To get them, open the repo's **Releases** page, or **Actions → latest run → Artifacts**.

### Signing key

A development keystore (`android/app/echo-release.jks`, password `tech4u-echo`) is included so that every build is signed with the same key and installs update cleanly.

⚠️ **Before publishing on Google Play**, create your own private key and keep it secret:

```bash
keytool -genkeypair -keystore my-key.jks -storetype PKCS12 -alias echo \
  -keyalg RSA -keysize 2048 -validity 10000
base64 -w0 my-key.jks   # copy the output
```

Then in **GitHub → Settings → Secrets and variables → Actions** add:
`KEYSTORE_BASE64`, `KEYSTORE_PASSWORD`, `KEY_ALIAS` (`echo`), `KEY_PASSWORD`.
The workflow automatically uses them instead of the development key.

### Build locally (optional)

```bash
cd android
gradle assembleRelease bundleRelease   # needs JDK 17 + Android SDK
```

## Install as a web app (PWA)

When the game is served from GitHub Pages, open it in Chrome on Android and choose **Add to Home screen**. It runs full-screen and works offline.

## Controls

| Input | Action |
|---|---|
| Drag anywhere (touch / mouse) | Move (relative drag, so your finger never covers the ship) |
| WASD / arrow keys | Move |
| Esc / P | Pause |

Shooting is automatic. It targets the nearest enemy.

## Deploy to GitHub Pages

This repo includes a workflow at `.github/workflows/pages.yml` that deploys to GitHub Pages on every push to `main`.

1. Push the repo to GitHub.
2. Go to **Settings → Pages → Build and deployment → Source: GitHub Actions**.
3. The game will be live at `https://<your-username>.github.io/<repo-name>/`.

## Privacy

ECHO collects no data. See the [privacy policy](privacy.html).

## Tech

- A single self-contained `index.html` written in vanilla JavaScript, Canvas 2D and the Web Audio API
- Uses no frameworks and no audio or image assets. It works offline once loaded (PWA + service worker).
- The Android wrapper is plain Java with zero library dependencies.
- Runs a fixed-timestep simulation, so echo replays are frame-exact
- Uses a seeded PRNG (mulberry32) for the daily challenge
- Keeps high scores in `localStorage`

## Credits

Designed and developed by **Tech4U**.

## License

MIT © 2026 Tech4U
