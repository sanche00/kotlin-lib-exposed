package com.ssg.inc.sp.db.sample.repository

import com.ssg.inc.sp.db.exposed.DBFactory
import com.ssg.inc.sp.db.sample.model.TbaCsmtsyncclsM
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll

class SampleRepository(val dbFactory: DBFactory) {

    suspend fun getAll() : List<SampleDto> {
        return dbFactory.readExec{ TbaCsmtsyncclsM.selectAll().map { toDTO(it) } }
    }

    private fun toDTO(it: ResultRow) = SampleDto(it[TbaCsmtsyncclsM.cmnyCd],it[TbaCsmtsyncclsM.csmtSmallCd],it[TbaCsmtsyncclsM.csmtSmallId] )
}

data class SampleDto(val cmnyCd: String, val csmtSmallCd: String, val csmtSmallId: String)