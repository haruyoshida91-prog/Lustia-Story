# Lustia Story

A text-adventure game for Android in Kotlin featuring a dungeon boss defending their realm against intruders. Inspired by Trials in Tainted Space.

## Features (Planned)

- **Story-Driven Gameplay**: Branching narrative with multiple choices
- **Dynamic Encounters**: Combat and dialogue systems with various antagonists
- **Player Stats**: Track health, strength, defense, charisma, and more
- **Inventory System**: Collect items and manage resources
- **Save/Load**: Persist game progress
- **NSFW Content**: Mature themes and content handling

## Architecture

### Core Components
- **StoryEngine**: Manages scene navigation and story progression
- **CombatEngine**: Handles combat mechanics and encounters
- **GameState**: Tracks all game data and player progression
- **StoryRepository**: Loads and manages story data

### Data Models
- **Story**: Root story object with all scenes and metadata
- **Scene**: Individual story segments with text, choices, and events
- **Choice**: Player decision points with consequences
- **GameState**: Current game progress and player stats
- **Encounter**: Active combat or dialogue encounter

## Building

```bash
# Build the APK
./gradlew build

# Install on device/emulator
./gradlew installDebug
```

## Project Structure

```
app/
├── src/main/
│   ├── kotlin/com/lustia/story/
│   │   ├── MainActivity.kt
│   │   ├── data/            # Data models
│   │   ├── engine/          # Game engines
│   │   ├── ui/              # UI components
│   │   └── repository/      # Data repository
│   └── res/                 # Resources (layouts, strings, colors)
└── build.gradle
```

## Story Format

Stories are defined with scenes, choices, and consequences. Each choice can have requirements and lead to different outcomes.

## Adding Your Stories

1. Create story JSON files in `app/src/main/assets/stories/`
2. Extend `StoryRepository` to load stories from assets
3. Add your story scenes with branching choices
4. Include stat modifications and encounter triggers

## Future Enhancements

- [ ] Save/Load game state to persistent storage
- [ ] Character relationship tracking
- [ ] Multiple story campaigns
- [ ] Audio and visual effects
- [ ] Settings and preferences UI
- [ ] Statistics and achievements

## License

MIT License