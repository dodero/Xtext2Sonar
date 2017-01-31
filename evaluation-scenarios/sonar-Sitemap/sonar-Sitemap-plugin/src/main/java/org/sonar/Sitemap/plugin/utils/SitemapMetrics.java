package org.sonar.Sitemap.plugin.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

/**
 * {@inheritDoc}
 */
public class SitemapMetrics implements Metrics {

  public static final Metric<Serializable> EXTERNAL = new Metric.Builder("Sitemap-EXTERNAL", "External Sitemap rules violations", Metric.ValueType.INT)
      .setDirection(Metric.DIRECTION_WORST)
      .setQualitative(true)
      .setDomain("Sitemap")
      .create();
  public static final Metric<Serializable> SQUID = new Metric.Builder("Sitemap-SQUID", "Sitemap checks", Metric.ValueType.INT)
      .setDirection(Metric.DIRECTION_WORST)
      .setQualitative(true)
      .setDomain("Sitemap")
      .create();
  public static final Metric<Serializable> DEPENDENCIES = new Metric.Builder("Sitemap-DEPENDENCIES", "Cyclic dependency violations", Metric.ValueType.INT)
  	 .setDirection(Metric.DIRECTION_WORST)
  	 .setQualitative(true)
  	 .setDomain("Sitemap")
  	 .create();

public List<Metric> getMetrics() {
    List<Metric> list = new ArrayList<Metric>();
    list.add(EXTERNAL);
    list.add(SQUID);
    return list;
  }
}
