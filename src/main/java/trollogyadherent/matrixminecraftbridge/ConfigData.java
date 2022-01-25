package trollogyadherent.matrixminecraftbridge;

public class ConfigData {
    private String host, username, password, token, roomId, startupMessage, stopMessage, joinMessage, leaveMessage, deathMessage, achievementMessage;

    public ConfigData(String host, String username, String password, String token, String roomId, String startupMessage, String stopMessage, String joinMessage, String leaveMessage, String deathMessage, String achievementMessage) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.token = token;
        this.roomId = roomId;
        this.startupMessage = startupMessage;
        this.stopMessage = stopMessage;
        this.joinMessage = joinMessage;
        this.leaveMessage = leaveMessage;
        this.deathMessage = deathMessage;
        this.achievementMessage = achievementMessage;
    }

    public String getHost() {
        return host;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getStartupMessage() {
        return startupMessage;
    }

    public String getStopMessage() {
        return stopMessage;
    }

    public String getJoinMessage() {
        return joinMessage;
    }

    public String getLeaveMessage() {
        return leaveMessage;
    }

    public String getDeathMessage() {
        return deathMessage;
    }

    public String getAchievementMessage() {
        return achievementMessage;
    }
}
