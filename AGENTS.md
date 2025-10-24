# Agent Instructions for 1vs1 Minecraft Plugin

## Build Commands
- Build all modules: `mvn clean package`
- Build GameServer only: `mvn clean package -pl GameServer`
- Build Lobby only: `mvn clean package -pl Lobby`
- Compile: `mvn compile`
- Run single test: `mvn test -Dtest=TestClassName`

## Code Style Guidelines

### Naming Conventions
- Classes: PascalCase (e.g., `Arena`, `MySQLManager`)
- Methods/Variables: camelCase (e.g., `arenaName`, `getPlugin()`)
- Constants: UPPER_SNAKE_CASE (e.g., `GAMESLOST`)
- Packages: lowercase with dots (e.g., `com.github.gamedipoxx.oneVsOne`)

### Imports
- Standard Java imports first
- Third-party libraries second
- Project-specific imports last
- Use wildcard imports sparingly

### Error Handling
- Use try-catch blocks with `e.printStackTrace()` for debugging
- Return boolean/null to indicate success/failure where appropriate
- Log errors using `plugin.getLogger()`

### Code Structure
- Use static methods/variables for shared utilities
- Prefer composition over inheritance
- Use `@Nullable` and `@NotNull` annotations from JetBrains
- Keep methods focused and single-purpose

### Formatting
- 4-space indentation (standard Java)
- Opening braces on same line
- Use meaningful variable names
- Add comments for complex logic only when necessary