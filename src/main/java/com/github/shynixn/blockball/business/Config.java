package com.github.shynixn.blockball.business;

import com.github.shynixn.blockball.api.entities.*;
import com.github.shynixn.blockball.business.bukkit.BlockBallPlugin;
import com.github.shynixn.blockball.lib.SConsoleUtils;
import com.github.shynixn.blockball.lib.SLocation;
import com.github.shynixn.blockball.lib.SPluginLoader.PluginLoader;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class Config {
    @PluginLoader
    private static JavaPlugin plugin;
    private static Config instance;
    private FileConfiguration c;

    private boolean asyncChat = true;
    private boolean highpriority = true;

    private IPosition joiningSpawnpoint;
    private boolean joiningSpawnpointEnabled;

    private boolean enableGoalsScoreboard;
    private String scoreboardTitle = ChatColor.GOLD + "Top Goals";
    private String firstplaceprefix;
    private String secondplaceprefix;
    private String thirdplaceprefix;
    private String otherprefix;

    private CommandContainer globalJoinCommand;
    private CommandContainer chatNavigateCommand;
    private CommandContainer globalLeaveCommand;
    private CommandContainer forcefieldHelperCommand;
    private CommandContainer eventContainerCommand;

    private SignContainer minigameSign;
    private SignContainer teamSign;
    private SignContainer leaveSign;

    private Vector entityProtectionVelocity;
    private Vector playerLaunchUpProtectionVelocity;

    private boolean particleVisibleForAll = true;
    private String particlePermission;

    public static Config getInstance() {
        if (instance == null)
            instance = new Config(plugin);
        return instance;
    }

    public void reload() {
        try {
            plugin.reloadConfig();
            this.c = plugin.getConfig();

            this.asyncChat = this.c.getBoolean("async-chat");
            this.highpriority = this.c.getBoolean("highest-priority");

            this.joiningSpawnpointEnabled = this.c.getBoolean("join-spawnpoint.enabled");
            this.joiningSpawnpoint = new SLocation()
                    .setWorldName(this.c.getString("join-spawnpoint.world"))
                    .setCoordinates(this.c.getDouble("join-spawnpoint.coordinates.x"), this.c.getDouble("join-spawnpoint.coordinates.y"), this.c.getDouble("join-spawnpoint.coordinates.z"))
                    .setRotation(this.c.getDouble("join-spawnpoint.coordinates.yaw"), this.c.getDouble("join-spawnpoint.coordinates.pitch"));

            this.enableGoalsScoreboard = this.c.getBoolean("goals-scoreboard.enabled");
            this.scoreboardTitle = ChatColor.translateAlternateColorCodes('&', this.c.getString("goals-scoreboard.scoreboard-title"));
            this.firstplaceprefix = ChatColor.translateAlternateColorCodes('&', this.c.getString("goals-scoreboard.firstplace-prefix"));
            this.secondplaceprefix = ChatColor.translateAlternateColorCodes('&', this.c.getString("goals-scoreboard.secondplace-prefix"));
            this.thirdplaceprefix = ChatColor.translateAlternateColorCodes('&', this.c.getString("goals-scoreboard.thirdplace-prefix"));
            this.otherprefix = ChatColor.translateAlternateColorCodes('&', this.c.getString("goals-scoreboard.other-prefix"));

            this.globalLeaveCommand = new CommandContainer("global-leave");
            this.globalJoinCommand = new CommandContainer("global-join");
            this.forcefieldHelperCommand = new CommandContainer("forcefield-join");
            this.chatNavigateCommand = new CommandContainer("navigate-chatmenu");
            this.eventContainerCommand = new CommandContainer("referee-game");

            this.minigameSign = new SignContainer("minigame-sign");
            this.teamSign = new SignContainer("lobbygame-sign");
            this.leaveSign = new SignContainer("leave-sign");

            this.entityProtectionVelocity = new Vector(this.c.getDouble("entity-velocity-protection.x"), this.c.getDouble("entity-velocity-protection.y"), this.c.getDouble("entity-velocity-protection.z"));
            this.playerLaunchUpProtectionVelocity = new Vector(this.c.getDouble("player-launch-up-protection.x"), this.c.getDouble("player-launch-up-protection.y"), this.c.getDouble("player-launch-up-protection.z"));

            this.particleVisibleForAll = this.c.getBoolean("particles.visible-for-all");
            this.particlePermission = this.c.getString("particles.visible-permission");
        } catch (Exception ex) {
            ex.printStackTrace();
            SConsoleUtils.sendColoredMessage("Please delete your config file to fix this problem.", ChatColor.GREEN, BlockBallPlugin.PREFIX_CONSOLE);
        }
    }

    public CommandContainer getEventContainerCommand() {
        return this.eventContainerCommand;
    }

    public boolean isParticleVisibleForAll() {
        return this.particleVisibleForAll;
    }

    public String getParticlePermission() {
        return this.particlePermission;
    }

    public SignContainer getLeaveSign() {
        return this.leaveSign;
    }

    public SignContainer getMinigameSign() {
        return this.minigameSign;
    }

    public SignContainer getTeamSign() {
        return this.teamSign;
    }

    public boolean isJoiningSpawnpointEnabled() {
        return this.joiningSpawnpointEnabled;
    }

    public Vector getEntityProtectionVelocity() {
        return this.entityProtectionVelocity.clone();
    }

    public Vector getPlayerLaunchUpProtectionVelocity() {
        return this.playerLaunchUpProtectionVelocity.clone();
    }

    public Location getJoinSpawnpoint() {
        if (this.joiningSpawnpoint == null)
            return null;
        return this.joiningSpawnpoint.toLocation();
    }

    public CommandContainer getGlobalLeaveCommand() {
        return this.globalLeaveCommand;
    }

    public boolean isEnableGoalsScoreboard() {
        return this.enableGoalsScoreboard;
    }

    public String getOtherprefix() {
        return this.otherprefix;
    }

    public String getScoreboardTitle() {
        return this.scoreboardTitle;
    }

    public String getFirstplaceprefix() {
        return this.firstplaceprefix;
    }

    public String getSecondplaceprefix() {
        return this.secondplaceprefix;
    }

    public String getThirdplaceprefix() {
        return this.thirdplaceprefix;
    }

    public boolean isHighpriority() {
        return this.highpriority;
    }

    public boolean isAsyncChat() {
        return this.asyncChat;
    }

    private Config(JavaPlugin plugin) {
        this.reload();
    }

    public CommandContainer getGlobalJoinCommand() {
        return this.globalJoinCommand;
    }

    public CommandContainer getChatNavigateCommand() {
        return this.chatNavigateCommand;
    }

    public CommandContainer getForcefieldHelperCommand() {
        return this.forcefieldHelperCommand;
    }

    public class CommandContainer {
        private final String command;
        private final boolean enabled;
        private final String useage;
        private final String description;
        private final String permission;
        private final String permissionMessage;

        public CommandContainer(String upper) {
            this.command = Config.this.c.getString(upper + ".command");
            this.enabled = Config.this.c.getBoolean(upper + ".enabled");
            this.useage = Config.this.c.getString(upper + ".useage");
            this.description = Config.this.c.getString(upper + ".description");
            this.permission = Config.this.c.getString(upper + ".permission");
            this.permissionMessage = Config.this.c.getString(upper + ".permission-message");
        }

        public String getCommand() {
            return this.command;
        }

        public boolean isEnabled() {
            return this.enabled;
        }

        public String getUseage() {
            return this.useage;
        }

        public String getDescription() {
            return this.description;
        }

        public String getPermission() {
            return this.permission;
        }

        public String getPermissionMessage() {
            return this.permissionMessage;
        }
    }

    public class SignContainer {
        private final String line1;
        private final String line2;
        private final String line3;
        private final String line4;

        public SignContainer(String upper) {
            this.line1 = Config.this.c.getString(upper + ".line-1");
            this.line2 = Config.this.c.getString(upper + ".line-2");
            this.line3 = Config.this.c.getString(upper + ".line-3");
            this.line4 = Config.this.c.getString(upper + ".line-4");
        }

        public void updateTeamSignConsideringMaxPlayers(Sign sign, Game game, Team team, List<Player> players) {
            int i = 0;
            for (String s : new String[]{this.line1, this.line2, this.line3, this.line4}) {
                if (Team.BLUE == team)
                    sign.setLine(i, this.replace(s, game.getArena(), team, true).replace("<players>", String.valueOf(players.size()))
                            .replace("<maxplayers>", String.valueOf(game.getArena().getTeamMeta().getTeamMaxSize())));
                if (Team.RED == team)
                    sign.setLine(i, this.replace(s, game.getArena(), team, true).replace("<players>", String.valueOf(players.size()))
                            .replace("<maxplayers>", String.valueOf(game.getArena().getTeamMeta().getTeamMaxSize())));
                i++;
            }
            sign.update();
        }

        public List<Player> getAmountFromTeam(Team team, Map<Player, Team> preselection) {
            List<Player> players = new ArrayList<>();
            for (Player player : preselection.keySet()) {
                if (preselection.get(player) == team)
                    players.add(player);
            }
            return players;
        }

        public void updateTeamSignConsideringMinigame(Sign sign, Game game, Team team, Map<Player, Team> preselection) {
            int i = 0;
            List<Player> players = null;
            for (String s : new String[]{this.line1, this.line2, this.line3, this.line4}) {
                if (team == Team.RED) {
                    if (game.getRedTeamPlayers().length > this.getAmountFromTeam(Team.RED, preselection).size()) {
                        players = Arrays.asList(game.getRedTeamPlayers());
                    } else {
                        players = this.getAmountFromTeam(Team.RED, preselection);
                    }
                }
                if (team == Team.BLUE) {
                    if (game.getBlueTeamPlayers().length > this.getAmountFromTeam(Team.BLUE, preselection).size()) {
                        players = Arrays.asList(game.getBlueTeamPlayers());
                    } else {
                        players = this.getAmountFromTeam(Team.BLUE, preselection);
                    }
                }
                sign.setLine(i, this.replace(s, game.getArena(), team, true).replace("<players>", String.valueOf(players.size()))
                        .replace("<maxplayers>", String.valueOf(game.getArena().getTeamMeta().getTeamMaxSize())));
                i++;
            }
            sign.update();
        }

        public void updateJoinSignConsideringMaxPlayers(Sign sign, Game game, List<Player> players) {
            int i = 0;
            MiniGame g = (MiniGame) game;
            String stage = "unknown";
            if (g.getStage() == GameStage.DISABLED)
                stage = Language.SIGN_DISABLED;
            else if (g.getStage() == GameStage.ENABLED)
                stage = Language.SIGN_ENABLED;
            else if (g.getStage() == GameStage.RUNNING)
                stage = Language.SIGN_RUNNING;
            for (String s : new String[]{this.line1, this.line2, this.line3, this.line4}) {
                if (game.getPlayers().size() < players.size()) {
                    sign.setLine(i, this.replace(s, game.getArena(), null, true).replace("<players>", String.valueOf(players.size()))
                            .replace("<maxplayers>", String.valueOf(game.getArena().getLobbyMeta().getMaxPlayers()))
                            .replace("<state>", stage));
                } else {
                    sign.setLine(i, this.replace(s, game.getArena(), null, true).replace("<players>", String.valueOf(game.getPlayers().size()))
                            .replace("<maxplayers>", String.valueOf(game.getArena().getLobbyMeta().getMaxPlayers()))
                            .replace("<state>", stage));
                }

                i++;
            }
            sign.update();
        }

        public int getGameLine() {
            int i = 0;
            for (String s : new String[]{this.line1, this.line2, this.line3, this.line4}) {
                if (s.equals("<game>")) {
                    return i;
                }
                i++;
            }
            return -1;
        }

        private String replace(String data, Arena arena, Team team, boolean ignoreDynmic) {
            if (arena.getAlias() == null)
                data = data.replace("<game>", String.valueOf(arena.getId()));
            else
                data = data.replace("<game>", arena.getAlias());
            if (team != null) {
                if (team == Team.RED)
                    data = data.replace("<team>", arena.getTeamMeta().getRedTeamName());
                else
                    data = data.replace("<team>", arena.getTeamMeta().getBlueTeamName());
            }
            if (!ignoreDynmic) {
                data = data.replace("<players>", "0");
                data = data.replace("<maxplayers>", "0");
                data = data.replace("<state>", Language.SIGN_DISABLED);
            }
            return ChatColor.translateAlternateColorCodes('&', data);
        }

        public String getLine1(Arena arena, Team team) {
            return this.replace(this.line1, arena, team, false);
        }

        public String getLine2(Arena arena, Team team) {
            return this.replace(this.line2, arena, team, false);
        }

        public String getLine3(Arena arena, Team team) {
            return this.replace(this.line3, arena, team, false);
        }

        public String getLine4(Arena arena, Team team) {
            return this.replace(this.line4, arena, team, false);
        }
    }
}
