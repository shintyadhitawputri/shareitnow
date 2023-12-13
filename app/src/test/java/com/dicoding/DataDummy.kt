package com.dicoding

import com.dicoding.mysubmissionstory.data.response.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                i.toString(),
                "times + $i",
                "username $i",
                "desc $i",
            )
            items.add(story)
        }
        return items
    }
}