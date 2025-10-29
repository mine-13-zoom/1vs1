# 1vs1 Minecraft Plugin

A highly modified Minecraft plugin for 1v1 dueling with lobby and game server components. This plugin provides a comprehensive dueling system with custom kits, arena management, and GUI-based matchmaking.

## Features

### Core Features
- **GUI-based Duel System**: Players can open duel GUIs using `/openduelgui` command instead of signs
- **Custom Kit System**: Players can save and load custom inventory layouts
- **Arena Management**: Automatic arena creation with worldname-number naming convention
- **MySQL Database Integration**: Persistent stats and player data storage
- **Multi-server Architecture**: Separate lobby and game servers with BungeeCord support

### Game Server Features
- **Dynamic Arena Creation**: Automatic world cloning using Multiverse-Core
- **Kit Management**: Save custom kits with `/kitsave` and reset with `/resetkit`
- **Scoreboard Support**: Optional in-game scoreboards
- **TNT Support**: Configurable TNT gameplay
- **Statistics Tracking**: Win/loss ratios and game history

### Lobby Features
- **Duel GUI**: Interactive interface for joining duels
- **NPC Integration**: Command-based GUI opening through NPCs
- **Server Navigation**: Seamless connection to game servers

## Requirements

- **Java 17** or higher
- **Minecraft Server**: Paper/Spigot 1.17+ (tested on 1.21.10)
- **Multiverse-Core**: Required for world management
- **MySQL Database**: For player statistics and data persistence
- **BungeeCord/Velocity**: For multi-server setup (optional)

## Installation

### 1. Build the Plugin
```bash
# Build all modules
mvn clean package

# Build specific modules
mvn clean package -pl GameServer
mvn clean package -pl Lobby
```

### 2. Setup Database
1. Create a MySQL database
2. Import the provided SQL schema from `src/main/resources/dbsetup.sql`
3. Configure database connection in `config.yml`

### 3. Configure Multiverse-Core
1. Install Multiverse-Core on your game server
2. Create template worlds for your arenas
3. Configure world names in the plugin config

### 4. Plugin Configuration
- **Game Server**: Place `GameServer-1.2.2.jar` in `plugins/` folder
- **Lobby Server**: Place `OneVsOneLobby.jar` in `plugins/` folder
- Configure `config.yml` files for each server type

## Configuration

### Game Server Config (`config.yml`)
```yaml
# Basic settings
ServerName: "GameServer1"
setupmode: false
debug: false

# Features
scoreboard: true
tnt: false
leaveCommand: true

# Database settings
database:
  host: "localhost"
  port: 3306
  database: "onevsone"
  username: "user"
  password: "password"

# Maps configuration
Maps:
  - "world1"
  - "world2"
  - "world3"
```

### Lobby Server Config
Configure server connections and GUI settings in the lobby configuration file.

## Commands

### Game Server Commands
- `/OneVsOne` - Manage OneVsOne plugin (admin)
- `/OneVsOneSetup` - Setup arenas and configuration (admin)
- `/leave` or `/l` - Leave current arena
- `/kitsave` - Save your current inventory as a kit
- `/resetkit` - Reset your kit to default

### Lobby Commands
- `/onevsonelobby` - Manage lobby settings (admin)
- `/openduelgui` - Open the duel selection GUI

## Permissions

### Game Server Permissions
- `OneVsOne.admin` - Administrative access
- `OneVsOne.stats.me` - View own statistics
- `OneVsOne.stats.other` - View other players' statistics

### Lobby Permissions
- `onevsonelobby.admin` - Lobby administration

## Architecture

### Multi-Module Structure
```
1vs1/
├── GameServer/          # Game server plugin
│   ├── src/main/java/    # Core game logic
│   └── src/main/resources/ # Configurations
└── Lobby/               # Lobby server plugin
    ├── src/main/java/    # Lobby and GUI logic
    └── src/main/resources/ # Configurations
```

### Key Components
- **Arena Management**: Dynamic world creation and deletion
- **Kit System**: Custom inventory saving/loading
- **Database Layer**: MySQL integration for persistence
- **GUI System**: Interactive duel selection interface

## Development

### Build Commands
```bash
# Build all modules
mvn clean package

# Build GameServer only
mvn clean package -pl GameServer

# Build Lobby only
mvn clean package -pl Lobby

# Compile only
mvn compile

# Run tests
mvn test -Dtest=TestClassName
```

### Code Style
- Java 17+ with 4-space indentation
- PascalCase for classes, camelCase for methods/variables
- Use `@Nullable` and `@NotNull` annotations
- Follow existing code patterns and conventions

## Dependencies

### Game Server Dependencies
- Spigot API 1.21.10-R0.1-SNAPSHOT
- Multiverse-Core 5.3.3
- MySQL Connector 8.4.0
- fastBoard 2.1.5
- bstats 3.0.2

### Lobby Dependencies
- Spigot API 1.21.10-R0.1-SNAPSHOT
- bstats 3.0.2

## Troubleshooting

### Common Issues
1. **Multiverse-Core not found**: Ensure Multiverse-Core is installed and enabled
2. **Database connection failed**: Check MySQL credentials and network connectivity
3. **World cloning fails**: Verify template worlds exist and are properly configured
4. **GUI not opening**: Check permissions and ensure lobby server is properly configured

### Debug Mode
Enable debug mode in `config.yml`:
```yaml
debug: true
```

This will provide detailed logging for troubleshooting.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes following the code style guidelines
4. Test your changes thoroughly
5. Submit a pull request

## License

This project is a modified version of an original plugin. Please check the original license terms.

## Support

For issues and support:
1. Check the troubleshooting section
2. Enable debug mode and check logs
3. Report issues with detailed information about your setup

## Version History

- **1.2.2**: Latest stable version with Multiverse-Core 5.3.3 integration
- **1.2.1**: Enhanced world cloning and error handling
- **1.2.0**: Added custom kit system and GUI improvements
- Earlier versions: Basic 1v1 functionality with sign-based joining# Testing workflow trigge 
 
