/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.infinispan.examples.carmart.session;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import org.infinispan.api.BasicCacheContainer;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.loaders.jdbc.stringbased.JdbcStringBasedCacheStore;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.transaction.LockingMode;
import org.infinispan.transaction.TransactionMode;
import org.infinispan.transaction.lookup.GenericTransactionManagerLookup;
import org.infinispan.util.concurrent.IsolationLevel;
import org.infinispan.examples.carmart.session.CacheContainerProvider;

/**
 * {@link CacheContainerProvider}'s implementation creating a DefaultCacheManager 
 * which is configured programmatically. Infinispan's libraries need to be bundled 
 * with the application - this is called "library" mode.
 * 
 * The only difference against TomcatCacheContainerProvider is the transaction 
 * manager lookup class.
 * 
 * @author Martin Gencur
 * 
 */
@ApplicationScoped
public class JBossASCacheContainerProvider implements CacheContainerProvider {

    private BasicCacheContainer manager;

    public BasicCacheContainer getCacheContainer() {
        if (manager == null) {
            GlobalConfiguration glob = new GlobalConfigurationBuilder()
                .nonClusteredDefault().globalJmxStatistics().enable()
                .jmxDomain("org.infinispan.carmart.tx-jdbc")
                .build();
            Configuration loc = new ConfigurationBuilder()
                .clustering().cacheMode(CacheMode.LOCAL)
                .transaction().transactionMode(TransactionMode.TRANSACTIONAL).autoCommit(false)
                .lockingMode(LockingMode.OPTIMISTIC).transactionManagerLookup(new GenericTransactionManagerLookup())
                .locking().isolationLevel(IsolationLevel.REPEATABLE_READ)
                .loaders().passivation(false).preload(false).shared(false)
                .addCacheLoader().cacheLoader(new JdbcStringBasedCacheStore()).fetchPersistentState(false).purgeOnStartup(true)
                .addProperty("stringsTableNamePrefix", "carmart_table")
                .addProperty("idColumnName", "ID_COLUMN")
                .addProperty("dataColumnName", "DATA_COLUMN")
                .addProperty("timestampColumnName", "TIMESTAMP_COLUMN")
                .addProperty("timestampColumnType", "BIGINT")
                .addProperty("connectionFactoryClass", "org.infinispan.loaders.jdbc.connectionfactory.PooledConnectionFactory")
                .addProperty("connectionUrl", "jdbc:mysql://localhost:3306/carmartdb")
                .addProperty("userName", "carmart")
                .addProperty("password", "carmart")
                .addProperty("driverClass", "com.mysql.jdbc.Driver")
                .addProperty("idColumnType", "VARCHAR(255)")
                .addProperty("dataColumnType", "VARBINARY(1000)")
                .addProperty("dropTableOnExit", "false")
                .addProperty("createTableOnStart", "true")
                .addProperty("databaseType", "MYSQL")
                //.addProperty("datasourceJndiLocation", "java:jboss/datasources/ExampleDS")
                .build();
            manager = new DefaultCacheManager(glob, loc, true);
        }
        return manager;
    }

    @PreDestroy
    public void cleanUp() {
        manager.stop();
        manager = null;
    }
}
