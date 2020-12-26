package de.plushnikov.intellij.plugin.inspection.modifiers;

import de.plushnikov.intellij.plugin.LombokBundle;
import de.plushnikov.intellij.plugin.LombokClassNames;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import static com.intellij.psi.PsiModifier.*;

/**
 * @author Rowicki Michał
 */
public class RedundantModifiersOnValueLombokAnnotationInspection extends LombokRedundantModifierInspection {

  public RedundantModifiersOnValueLombokAnnotationInspection() {
    super(
      LombokClassNames.VALUE,
      new RedundantModifiersInfo(RedundantModifiersInfoType.CLASS, null,
                                 LombokBundle.message("inspection.message.value.already.marks.class.final"), FINAL),
      new RedundantModifiersInfo(RedundantModifiersInfoType.FIELD, STATIC,
                                 LombokBundle.message("inspection.message.value.already.marks.non.static.fields.final"), FINAL),
      new RedundantModifiersInfo(RedundantModifiersInfoType.FIELD, STATIC,
                                 LombokBundle.message("inspection.message.value.already.marks.non.static.package.local.fields.private"), PRIVATE));
  }

  @Override
  public @Nls(capitalization = Nls.Capitalization.Sentence) @NotNull String getDisplayName() {
    return "@Value modifiers";
  }
}
