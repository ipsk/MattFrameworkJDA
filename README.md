# Matt's Framework for JDA
This Framework is very similar to Matt's Framework for Spigot, though it is for creating Discord Bot commands rather than Plugin commands.

**Be aware that this is still a W.I.P, pull requests and suggestions are very welcome**
## Dependency
The dependency is on Maven Central, [![Maven Central](https://img.shields.io/maven-central/v/me.mattstudios.utils/matt-framework-jda.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22me.mattstudios.utils%22%20AND%20a:%22matt-framework-jda%22)

Be sure to replace **VERSION** with the current version found on the maven repository

**Maven:**
```xml
<dependency>
    <groupId>me.mattstudios.utils</groupId>
    <artifactId>matt-framework-jda</artifactId>
    <version>VERSION</version>
</dependency>
```

**Gradle:**
```groovy
dependencies {
    implementation "me.mattstudios.utils:matt-framework-jda:VERSION"
}
```

## Usage

### Creating commands
Creating a command can be done by making a class that extends `CommandBase` and annotating it with `@Command`

**Example !ping command:**

```java
@Prefix("!")
@Command("ping")
public class PingCommand extends CommandBase {
    
    @Default
    public void defaultCommand() {
        TextChannel channel = message.textChannel;
        channel.sendMessage("Pong!").queue();
    }
}
```

### Registering the command
Registering commands can be done by first creating a JDA instance, then passing that instance to an instance of `CommandManager`, then calling the `register()` method to register a command.

**Example command registry:**
```java
public class Main {

    public static void main(String[] args) {
        JDA jda = JDABuilder.create("my_bot_token", Arrays.asList(GatewayIntent.EXAMPLE))
                            .build();
        CommandManager commandManager = new CommandManager(jda);
        commandManager.register(PingCommand());
    }
}
```
You can also optionally provide a global prefix to the `CommandManager` by using the `CommandManager(JDA jda, String globalPrefix)` constructor

**Example global prefix:**
```java
CommandManager commandManager = new CommandManager(jda, "!");
```