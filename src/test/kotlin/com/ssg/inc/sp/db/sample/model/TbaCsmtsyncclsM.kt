package com.ssg.inc.sp.db.sample.model

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

object TbaCsmtsyncclsM : Table("tba_csmtsynccls_m") {
    val cmnyCd = varchar("cmny_cd", 16)
    val csmtSmallCd = varchar("csmt_small_cd", 8)
    val syncClsCd = varchar("sync_cls_cd", 20).nullable()
    val salePrcAutoCalYn = varchar("sale_prc_auto_cal_yn", 1)
    val frRegId = varchar("fr_reg_id", 20)
    val frRegDtm = timestamp("fr_reg_dtm")
    val frRegIp = varchar("fr_reg_ip", 20)
    val fnlUpdId = varchar("fnl_upd_id", 20)
    val fnlUpdDtm = timestamp("fnl_upd_dtm")
    val fnlUpdIp = varchar("fnl_upd_ip", 20)
    val csmtSmallId = varchar("csmt_small_id", 32)

    // Foreign keys
//    val cmnyCdFk = foreignKey("tba_csmtsynccls_m_fk01", cmnyCd, TbaCmnyM.cmnyCd)
//    val csmtSmallCdFk = foreignKey("tba_csmtsynccls_m_fk02", csmtSmallCd, TbaSmallM.smallCd)

//    init {
//        // Set the primary key
//        primaryKey(cmnyCd, csmtSmallCd, csmtSmallId)
//    }
    override val primaryKey = PrimaryKey(cmnyCd, csmtSmallCd, csmtSmallId)
}