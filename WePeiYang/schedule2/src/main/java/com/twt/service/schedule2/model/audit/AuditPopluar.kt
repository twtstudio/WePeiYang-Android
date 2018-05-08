package com.twt.service.schedule2.model.audit

import com.google.gson.annotations.SerializedName

data class AuditPopluar(@SerializedName("course_id") val courseId: String = "",
                        @SerializedName("updated_at") val updatedAt: String = "",
                        val count: String = "",
                        val rank: Int = 0,
                        val course: AuditPopluarCourse)


data class AuditPopluarCourse(@SerializedName("college_id") val collegeId: String = "",
                              val year: String = "",
                              val name: String = "",
                              val semester: String = "",
                              val id: Int = 0)


