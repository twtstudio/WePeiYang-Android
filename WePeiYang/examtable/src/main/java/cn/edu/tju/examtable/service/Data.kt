package cn.edu.tju.examtable.service

data class ExamBean(
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
