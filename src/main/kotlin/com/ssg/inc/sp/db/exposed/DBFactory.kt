package com.ssg.inc.sp.db.exposed

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

enum class Profile {
    LOCAL, DEV, STG, PROD
    ;

    companion object {
        fun find(str: String): Profile? =
            values().find { it.name.equals(str, ignoreCase = true) }
    }

}

class DBFactory(val profile: Profile) {

    private val log = LoggerFactory.getLogger(this::class.java)
    var readDatabase: Database? = null
        private set

    var writeDatabase: Database? = null
        private set

    init {
        loadReadConnectPool(profile)
        loadWriteConnectPool(profile)
    }

    private fun loadReadConnectPool(profile: Profile?) {
        var hikariConfig: HikariConfig?
        if (profile == Profile.LOCAL) {
            hikariConfig = HikariConfig().apply {
                driverClassName = "org.postgresql.Driver"
                jdbcUrl = ""
                maximumPoolSize = 3
                username = ""
                password = ""
                isAutoCommit = false
                addDataSourceProperty("cachePrepStmts", "true")
                addDataSourceProperty("prepStmtCacheSize", "250")
                addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
                validate()
            }
        } else {
            hikariConfig = HikariConfig().apply {
                driverClassName = "org.postgresql.Driver"
                jdbcUrl = System.getProperty("READ_JDBC_URL")
                maximumPoolSize = (System.getProperty("READ_DBCP_SIZE") ?: 10) as Int
                username = System.getProperty("READ_DB_USER")
                password = System.getProperty("READ_DB_PWD")
                isAutoCommit = false
                addDataSourceProperty("cachePrepStmts", "true")
                addDataSourceProperty("prepStmtCacheSize", "250")
                addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
                validate()
            }
        }

        readDatabase = Database.connect(HikariDataSource(hikariConfig));
    }

    private fun loadWriteConnectPool(profile: Profile) {
        if (profile != Profile.STG && profile != Profile.PROD) {
            return
        }

        val hikariConfig = HikariConfig().apply {
            driverClassName = "org.postgresql.Driver"
            jdbcUrl = System.getProperty("WRITE_JDBC_URL")
            maximumPoolSize = (System.getProperty("WRITE_DBCP_SIZE") ?: 10) as Int
            username = System.getProperty("WRITE_DB_USER")
            password = System.getProperty("WRITE_DB_PWD")
            isAutoCommit = false
            addDataSourceProperty("cachePrepStmts", "true")
            addDataSourceProperty("prepStmtCacheSize", "250")
            addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
            validate()
        }
        writeDatabase = Database.connect(HikariDataSource(hikariConfig));
    }


    suspend fun <T> writeExec(
        block: () -> T
    ): T = withContext(Dispatchers.IO) {
        transaction(writeDatabase ?: readDatabase) { block() }
    }

    suspend fun <T> readExec(
        block: () -> T
    ): T = withContext(Dispatchers.IO) {
        transaction(readDatabase) { block() }
    }
}
