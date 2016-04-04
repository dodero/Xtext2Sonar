package org.sonar.NAME.visitors;

import java.util.Set;

import org.sonar.NAME.api.NAMEKeyword;
import org.sonar.NAME.api.NAMEMetric;
import org.sonar.squidbridge.SquidAstVisitor;
import org.sonar.squidbridge.api.SourceCode;
import org.sonar.squidbridge.measures.MetricDef;

import com.google.common.collect.Sets;
import com.sonar.sslr.api.AstAndTokenVisitor;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Grammar;
import com.sonar.sslr.api.Token;
import com.sonar.sslr.api.Trivia;

public class NAMECommentLinesVisitor <GRAMMAR extends Grammar> extends SquidAstVisitor<GRAMMAR> implements AstAndTokenVisitor {

  private final MetricDef metric;
  private Set<Integer> comments = Sets.newHashSet();
  private boolean seenFirstToken;
  
  public NAMECommentLinesVisitor(MetricDef metric) {
	    this.metric = metric;
  }

  @Override
  public void init() {
    subscribeTo(NAMEKeyword.ALGORITMO);
    subscribeTo(NAMEKeyword.MODULO);
    subscribeTo(NAMEKeyword.INICIO);
    subscribeTo(NAMEKeyword.IMPLEMENTACION);
    subscribeTo(NAMEKeyword.FUNCION);
    subscribeTo(NAMEKeyword.PROCEDIMIENTO);
  }

  @Override
  public void visitFile(AstNode astNode) {
    comments.clear();
    seenFirstToken = false;
  }

  public void visitToken(Token token) {
    for (Trivia trivia : token.getTrivia()) {
      if (trivia.isComment()) {
        if (seenFirstToken) {
          String[] commentLines = getContext().getCommentAnalyser().getContents(trivia.getToken().getOriginalValue())
              .split("(\r)?\n|\r", -1);
          int line = trivia.getToken().getLine();
          for (String commentLine : commentLines) {
            if (!commentLine.contains("NOSONAR") && !getContext().getCommentAnalyser().isBlank(commentLine)) {
              comments.add(line);
            }
            line++;
          }
        } else {
          seenFirstToken = true;
        }
      }
    }
    seenFirstToken = true;
  }

  @Override
  public void leaveNode(AstNode astNode) {
    SourceCode sourceCode = getContext().peekSourceCode();
    int commentlines = 0;
    for (int line = sourceCode.getStartAtLine(); line <= sourceCode.getEndAtLine(); line++) {
      if (comments.contains(line)) {
        commentlines++;
      }
    }
    sourceCode.setMeasure(NAMEMetric.COMMENT_LINES, commentlines);
  }

  public void leaveFile(AstNode ast) {
	getContext().peekSourceCode().add(metric, comments.size());
    getContext().peekSourceCode().setMeasure(NAMEMetric.COMMENT_LINES, comments.size());
    comments.clear();
  }

}
