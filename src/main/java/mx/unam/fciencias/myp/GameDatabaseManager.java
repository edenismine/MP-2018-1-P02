package mx.unam.fciencias.myp;

import java.sql.*;

class GameDatabaseManager {
    static final int LOGIN_SUCCESSFUL = 0;
    static final int USER_DOES_NOT_EXIST = 1;
    static final int WRONG_PASSWORD = 2;
    private static final GameProperties PROPERTIES = GameProperties.getInstance();
    private static final String DATABASE_URL = PROPERTIES.getProperty("database_url");
    private static GameDatabaseManager instance = null;
    private static Connection connection;
    private static boolean hasData = false;

    private GameDatabaseManager() {
    }

    static GameDatabaseManager getInstance() {
        if (instance == null) {
            synchronized (GameDatabaseManager.class) {
                if (instance == null) {
                    instance = new GameDatabaseManager();
                }
            }
        }
        return instance;
    }

    private void getConnection() throws SQLException {
        // Class.forName("org.sqlite.JDBC");
        if (connection == null) {
            connection = DriverManager.getConnection(DATABASE_URL);
            connection.setAutoCommit(true);
            initialize();
        }
    }

    private void initialize() throws SQLException {
        if (!hasData) {
            String getUsersTable = "SELECT name FROM sqlite_master WHERE type='table' AND name='users'";
            hasData = true;
            Statement getUsersTableStatement = connection.createStatement();
            ResultSet resultSet = getUsersTableStatement.executeQuery(getUsersTable);
            if (!resultSet.next()) {
                String createTable = "CREATE TABLE users ("
                        + "username TEXT NOT NULL PRIMARY KEY, "
                        + "password TEXT NOT NULL, "
                        + "games INTEGER NOT NULL DEFAULT 0, "
                        + "won INTEGER NOT NULL DEFAULT 0)";
                System.out.println("Building the Users table with prepopulated values.");
                Statement createTableStatement = connection.createStatement();
                createTableStatement.execute(createTable);
                addUser("admin", "pass");
            }
        }
    }

    boolean addUser(String username, String password) throws SQLException {
        getConnection();
        // Ensure username doesn't already exist.
        String getUser = "SELECT username FROM users WHERE username==?;";
        PreparedStatement getUserStatement = connection.prepareStatement(getUser);
        getUserStatement.setString(1, username);
        getUserStatement.execute();
        ResultSet resultSet = getUserStatement.getResultSet();
        if (resultSet.next()) {
            return false;
        }

        // Insert new user into db
        String insertUser = "INSERT INTO users values(?,?,0,0);";
        PreparedStatement insertUserStatement = connection.prepareStatement(insertUser);
        insertUserStatement.setString(1, username);
        insertUserStatement.setString(2, password);
        insertUserStatement.execute();
        return true;
    }

    int authenticate(String username, String password) throws SQLException {
        getConnection();
        String getUser = "SELECT username, password FROM users WHERE username==?;";
        PreparedStatement getUserStatement = connection.prepareStatement(getUser);
        getUserStatement.setString(1, username);
        getUserStatement.execute();
        ResultSet resultSet = getUserStatement.getResultSet();
        if (!resultSet.next()) {
            return USER_DOES_NOT_EXIST;
        } else if (!resultSet.getString("password").equals(password)) {
            return WRONG_PASSWORD;
        } else {
            return LOGIN_SUCCESSFUL;
        }
    }

    String getStats(String username) throws SQLException {
        getConnection();
        String getUser = "SELECT games, won FROM users WHERE username==?;";
        PreparedStatement getUserStatement = connection.prepareStatement(getUser);
        getUserStatement.setString(1, username);
        getUserStatement.execute();
        ResultSet resultSet = getUserStatement.getResultSet();
        if (!resultSet.next()) {
            return "Something went wrong, errorcode: " + USER_DOES_NOT_EXIST + " user does not exist.";
        } else {
            int games = resultSet.getInt("games");
            int won = resultSet.getInt("won");
            String stats = String.format("You've played %d times", games);
            if (games == 0) {
                stats = "You haven't played the game yet!\nStart a new game and come back to track your progress.";
            } else if (won == 0) {
                stats += ", but you haven't won yet. Keep playing! You'll get better with time.";
            } else if (games == won) {
                stats += ", and you've won every time! You're awesome!";
            } else {
                stats += ", you've won %d games and lost the remaining %d.";
                stats = String.format(stats, won, games - won);
            }
            return stats;
        }
    }

    void updateStats(String username, boolean playerWon) throws SQLException {
        getConnection();
        if (playerWon) {
            String incrementWon = "UPDATE users SET won = won + 1 WHERE username==?;";
            PreparedStatement incrementWonStatement = connection.prepareStatement(incrementWon);
            incrementWonStatement.setString(1, username);
            incrementWonStatement.execute();
        }
        String incrementGames = "UPDATE users SET games = games + 1 WHERE username==?;";
        PreparedStatement incrementGamesStatement = connection.prepareStatement(incrementGames);
        incrementGamesStatement.setString(1, username);
        incrementGamesStatement.execute();
    }

    void resetStats(String username) throws SQLException {
        getConnection();
        String resetStats = "UPDATE users SET games = 0, won = 0 WHERE username==?;";
        PreparedStatement resetStatsStatement = connection.prepareStatement(resetStats);
        resetStatsStatement.setString(1, username);
        resetStatsStatement.execute();
    }
}