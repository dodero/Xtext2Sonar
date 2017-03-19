package org.sonar.VaryGrammar.plugin;

import java.util.List;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.VaryGrammar.checks.CheckList;
import org.sonar.squidbridge.annotations.AnnotationBasedRulesDefinition;

public class VaryGrammarRuleRepository implements RulesDefinition {
  @Override
  public void define(Context context) {
	  final String languageKey = VaryGrammarLanguage.KEY;
      final String repositoryKey = CheckList.REPOSITORY_KEY;

      @SuppressWarnings("rawtypes")
      final List<Class> list = CheckList.getChecks();

      final NewRepository repository
          = context.createRepository(repositoryKey, languageKey)
          .setName("SonarQube");

      AnnotationBasedRulesDefinition.load(repository, languageKey, list);
      
      for (NewRule rule : repository.rules()) {
          //FIXME: set internal key to key to ensure rule templates works properly : should be removed when SONAR-6162 is fixed.
          rule.setInternalKey(rule.key());
      }

      repository.done();
  }
  
  
}
