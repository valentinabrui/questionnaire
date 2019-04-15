package com.questionnaire.guice;

import com.google.inject.AbstractModule;
import com.questionnaire.dao.MySqlDataSourceProvider;
import com.questionnaire.dao.MySqlQuestionnaireDao;
import com.questionnaire.dao.QuestionnaireDao;
import com.questionnaire.resource.QuestionnaireResource;
import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.provider.ConfigurationProviderBuilder;
import org.cfg4j.source.ConfigurationSource;
import org.cfg4j.source.classpath.ClasspathConfigurationSource;

import javax.sql.DataSource;
import java.nio.file.Paths;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by Valentina on 2019-04-13.
 */
public class QuestionnaireModule extends AbstractModule {

  @Override
  protected void configure() {
    ConfigurationSource source = new ClasspathConfigurationSource(()
        -> newArrayList(Paths.get("questionnaire.properties")));
    ConfigurationProvider cfg = new ConfigurationProviderBuilder()
        .withConfigurationSource(source)
        .build();
    bind(ConfigurationProvider.class).toInstance(cfg);
    bind(DataSource.class).toProvider(MySqlDataSourceProvider.class);
    bind(QuestionnaireDao.class).to(MySqlQuestionnaireDao.class);
    bind(QuestionnaireResource.class);
  }
}
