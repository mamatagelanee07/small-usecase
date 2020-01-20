package com.andigeeky.mvpapp.utils.resourcepools

import com.andigeeky.mvpapp.lines.vo.Line

object TestUtil {

    fun createLines(size : Int) :List<Line> {
        val lines = mutableListOf<Line>()
        repeat(size){
            lines.add(createLine(it))
        }
        return lines
    }

    fun createLine(id: Int) = Line(
        created = "created $id",
        id = "$id",
        name = "Line $id",
        modeName = if (id %2 == 0) "tube" else "train",
        modified = "modified $id"
    )
}
