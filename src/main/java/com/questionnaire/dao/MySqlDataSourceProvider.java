package com.questionnaire.dao;

import com.zaxxer.hikari.HikariDataSource;
import org.cfg4j.provider.ConfigurationProvider;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.sql.DataSource;
import java.util.Objects;

/**
 * Created by Valentina on 2019-04-13.
 */
@Singleton
public class MySqlDataSourceProvider implements Provider<DataSource> {

  private final String url;
  private final String username;
  private final String password;

  @Inject
  public MySqlDataSourceProvider(ConfigurationProvider cfg) {
    this.url = Objects.requireNonNull(cfg.getProperty("db.url", String.class));
    this.username = Objects.requireNonNull(cfg.getProperty("db.username", String.class));
    this.password = Objects.requireNonNull(cfg.getProperty("db.password", String.class));
  }

  @Override
  public DataSource get() {
    final HikariDataSource ds = new HikariDataSource();
    ds.setMaximumPoolSize(30);
    ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
    ds.setJdbcUrl(url);
    ds.addDataSourceProperty("user", username);
    ds.addDataSourceProperty("password", password);
    ds.setAutoCommit(true);
    return ds;
  }
}
