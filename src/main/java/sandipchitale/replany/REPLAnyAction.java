package sandipchitale.replany;

import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.WindowManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.terminal.ShellTerminalWidget;
import org.jetbrains.plugins.terminal.TerminalToolWindowManager;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public class REPLAnyAction extends AnAction {
    public static final String REPL_JAR =
            Path.of(
                    Objects.requireNonNull(PluginManagerCore.getPlugin(PluginId.getId("sandipchitale.replany"))).getPluginPath().toFile().getAbsolutePath(),
                    "lib",
                    "repl.jar"
            ).toString();

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        // Prompt for command name to REPL
        String commandToREPL = Messages.showInputDialog(
                Objects.requireNonNull(WindowManager.getInstance().getFrame(project)),
                "Enter command to REPL", "REPL Command",
                Messages.getQuestionIcon(),
                "kubectl",
                null);
        if (commandToREPL != null) {
            @NotNull ShellTerminalWidget shellTerminalWidget =
                    TerminalToolWindowManager.getInstance(Objects.requireNonNull(project)).createLocalShellWidget(project.getBasePath(), "repl");
            try {
                shellTerminalWidget.executeCommand(String.format("java -jar %s %s", REPL_JAR, commandToREPL));
            } catch (IOException ignore) {
            }
        }
    }
}
