package de.plushnikov.intellij.plugin.activity;

import com.intellij.compiler.server.BuildManagerListener;
import com.intellij.notification.*;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.application.ReadAction;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationListener;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderEntry;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.openapi.util.Disposer;
import com.intellij.util.concurrency.AppExecutorUtil;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.util.messages.MessageBusConnection;
import de.plushnikov.intellij.plugin.LombokBundle;
import de.plushnikov.intellij.plugin.Version;
import de.plushnikov.intellij.plugin.settings.ProjectSettings;
import de.plushnikov.intellij.plugin.util.LombokLibraryUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Shows notifications about project setup issues, that make the plugin not working.
 *
 * @author Alexej Kubarev
 */
public class LombokProjectValidatorActivity implements StartupActivity {

  @Override
  public void runActivity(@NotNull Project project) {
    // enable annotationProcessing check
    final MessageBusConnection connection = project.getMessageBus().connect();
    connection.subscribe(BuildManagerListener.TOPIC, new LombokBuildManagerListener());

    final boolean hasLombokLibrary = LombokLibraryUtil.hasLombokLibrary(project);
    // If dependency is present and out of date notification setting is enabled (defaults to disabled)
    if (hasLombokLibrary && ProjectSettings.isEnabled(project, ProjectSettings.IS_LOMBOK_VERSION_CHECK_ENABLED, false)) {
      final ModuleManager moduleManager = ModuleManager.getInstance(project);
      for (Module module : moduleManager.getModules()) {
        String lombokVersion = Version.parseLombokVersion(findLombokEntry(ModuleRootManager.getInstance(module)));

        if (null != lombokVersion && Version.compareVersionString(lombokVersion, Version.LAST_LOMBOK_VERSION) < 0) {
          getNotificationGroup().createNotification(LombokBundle.message("config.warn.dependency.outdated.title"),
            LombokBundle
              .message("config.warn.dependency.outdated.message", project.getName(),
                module.getName(), lombokVersion, Version.LAST_LOMBOK_VERSION),
            NotificationType.WARNING, NotificationListener.URL_OPENING_LISTENER);
        }
      }
    }
  }

  @NotNull
  private static NotificationGroup getNotificationGroup() {
    NotificationGroup group = NotificationGroup.findRegisteredGroup(Version.PLUGIN_NAME);
    if (group == null) {
      group = new NotificationGroup(Version.PLUGIN_NAME, NotificationDisplayType.BALLOON, true);
    }
    return group;
  }

  @Nullable
  private static OrderEntry findLombokEntry(@NotNull ModuleRootManager moduleRootManager) {
    final OrderEntry[] orderEntries = moduleRootManager.getOrderEntries();
    for (OrderEntry orderEntry : orderEntries) {
      if (orderEntry.getPresentableName().contains("lombok")) {
        return orderEntry;
      }
    }
    return null;
  }
}
