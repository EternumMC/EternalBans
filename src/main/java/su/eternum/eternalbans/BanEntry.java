package su.eternum.eternalbans;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.UUID;

public class BanEntry {

    private final @NotNull String username;
    private final @NotNull UUID uuid;

    private final @Nullable String reason;

    private final @NotNull Date timestamp;
    private final @Nullable Date expires;

    private final @NotNull String serverName;

    public BanEntry(@NotNull String username, @NotNull UUID uuid, @Nullable String reason, @NotNull Date timestamp,
                    @Nullable Date expires, @NotNull String serverName) {
        this.username = username;
        this.uuid = uuid;
        this.reason = reason;
        this.timestamp = timestamp;
        this.expires = expires;
        this.serverName = serverName;
    }

    @NotNull
    public String getUsername() {
        return this.username;
    }

    @NotNull
    public UUID getUUID() {
        return this.uuid;
    }

    @Nullable
    public String getReason() {
        return this.reason;
    }

    @NotNull
    public Date getTimestamp() {
        return this.timestamp;
    }

    @Nullable
    public Date getExpires() {
        return this.expires;
    }

    @NotNull
    public String getServerName() {
        return this.serverName;
    }

}
