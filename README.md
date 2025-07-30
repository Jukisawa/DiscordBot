# Discord Bot in Java

This project is a simple Discord bot implemented in Java. It comes with some League of Legends commands, music commands and random voice troll function.

## Prerequisites

- Java Development Kit (JDK) 21 or higher
- Maven 3.9 or higher
- A Discord account and a server where you can test the bot

## Installation

1. Clone the repository:
   ```
   git clone https://github.com/jukisawa/DiscordBot.git
   ```

2. Navigate to the project directory:
   ```
   cd DiscorBot/discord_bot
   ```

3. Update the `src/main/resources/config.properties` file with your Discord bot token and other configuration settings.

4. Build the project using Maven:
   ```
   mvn clean install
   ```

## Usage

1. Run the bot:
   ```
   java -jar discord_bot/target/discordBot-1.0-SNAPSHOT.jar
   ```

2. Invite the bot to your Discord server using the OAuth2 URL generated in the Discord Developer Portal.

## Contributing

Feel free to submit issues or pull requests to improve the bot or add new features.