package de.plushnikov.intellij.plugin.resolver;

import com.intellij.codeInsight.daemon.quickFix.ExternalLibraryResolver;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ExternalLibraryDescriptor;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.PathUtil;
import com.intellij.util.ThreeState;
import de.plushnikov.intellij.plugin.Version;
import net.jcip.annotations.GuardedBy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static de.plushnikov.intellij.plugin.LombokClassNames.MAIN_LOMBOK_CLASSES;

public class LombokExternalLibraryResolver extends ExternalLibraryResolver {

  private final Set<String> allLombokPackages;
  private final Map<String, String> simpleNameToFQNameMap;

  private static final ExternalLibraryDescriptor LOMBOK =
    new ExternalLibraryDescriptor("org.projectlombok", "lombok", Version.LAST_LOMBOK_VERSION, Version.LAST_LOMBOK_VERSION) {
      @NotNull
      @Override
      public List<String> getLibraryClassesRoots() {
        return Collections.emptyList();
      }

      @Override
      public String getPresentableName() {
        return "lombok.jar";
      }
    };

  public LombokExternalLibraryResolver() {
    allLombokPackages = Collections.unmodifiableSet(MAIN_LOMBOK_CLASSES.stream().map(StringUtil::getPackageName).collect(Collectors.toSet()));
    simpleNameToFQNameMap = MAIN_LOMBOK_CLASSES.stream().collect(Collectors.toMap(StringUtil::getShortName, Function.identity()));
  }

  @Nullable
  @Override
  public ExternalClassResolveResult resolveClass(@NotNull String shortClassName,
                                                 @NotNull ThreeState isAnnotation,
                                                 @NotNull Module contextModule) {
    if (isAnnotation == ThreeState.YES && simpleNameToFQNameMap.containsKey(shortClassName)) {
      return new ExternalClassResolveResult(simpleNameToFQNameMap.get(shortClassName), LOMBOK);
    }
    return null;
  }

  @Nullable
  @Override
  public ExternalLibraryDescriptor resolvePackage(@NotNull String packageName) {
    if (allLombokPackages.contains(packageName)) {
      return LOMBOK;
    }
    return null;
  }
}
