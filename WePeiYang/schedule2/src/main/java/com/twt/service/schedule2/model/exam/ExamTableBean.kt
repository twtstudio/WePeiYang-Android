package com.twt.service.schedule2.model.exam

data class ExamTableBean(
        val id: String,
        val name: String,
        val type: String,
        val date: String,
        val arrange: String,
        val location: String,
        val seat: String,
        val state: String,
        val ext: String
)
