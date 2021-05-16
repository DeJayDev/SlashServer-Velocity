package sr.will.slashserver;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.connection.Player;
import com.velocitypowered.api.proxy.connection.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class CommandServer implements SimpleCommand {

    private final RegisteredServer server;

    public CommandServer(RegisteredServer server) {
        this.server = server;
    }

    @Override
    public void execute(SimpleCommand.Invocation invocation) {
        if (!(invocation.source() instanceof Player)) {
            invocation.source().sendMessage(Component.text("Player only command!", NamedTextColor.RED));
            return;
        }

        Player player = (Player) invocation.source();
        ServerConnection connection = player.connectedServer();
        if (connection.serverInfo().name() == server.serverInfo().name()) {
            player.sendMessage(Component.text("You are already connected to this server!", NamedTextColor.RED));
            return;
        }

        player.createConnectionRequest(server).fireAndForget();
        player.sendMessage(Component.text("You have been sent to " + server.serverInfo().name(), NamedTextColor.GREEN));
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("slashserver." + server.serverInfo().name().toLowerCase());
    }
}
