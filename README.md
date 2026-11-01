# ECHO 🔁

**Your past is your enemy.**

Made by **Tech4U**.

A one-thumb arcade shooter for mobile and desktop. Every 20 seconds time resets, and your *echo* from the previous round comes back. It replays every move and every shot you made. Echo bullets kill enemies… and they kill you too.

## Play

Open `index.html` in any browser. There's no build step and nothing to install.

## Features

- **Echo mechanic.** Each round you play is recorded at a fixed 60 Hz and replayed in later rounds, with up to 6 echoes on screen at once.
- **Daily challenge.** One seeded level per day, the same for every player on Earth.
- **Endless mode.** A random seed each run.
- **Share card.** At the end of a run you get a Wordle-style emoji result you can paste anywhere.
- **Adaptive music.** A synthwave loop that gains a layer (hats, hook, pads, double kick) with each echo.
- **Procedural audio.** Layered explosions with sub-kick, distortion, crack, reverb tail and debris. Everything is made with the Web Audio API, so the game uses no audio files.
- **Combo multiplier.** Chain kills for up to x5 points.
- Three enemy types: chasers, dashers that telegraph before they charge, and tanks that split when destroyed.

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

## Tech

- A single self-contained `index.html` written in vanilla JavaScript, Canvas 2D and the Web Audio API
- Uses no frameworks and no assets. It works offline once loaded.
- Runs a fixed-timestep simulation, so echo replays are frame-exact
- Uses a seeded PRNG (mulberry32) for the daily challenge
- Keeps high scores in `localStorage`

## Credits

Designed and developed by **Tech4U**.

## License

MIT © 2026 Tech4U
