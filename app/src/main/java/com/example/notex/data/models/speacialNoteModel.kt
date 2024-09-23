package com.example.notex.data.models

data class SpecialNoteModel(
    var title: String ="",
    var categoryTitle:String ="",
    var specialField1: specialField?=null,
    var specialField2: specialField?=null,
    var specialField3: specialField?=null,
    var specialField4: specialField?=null,
    var specialField5: specialField?=null,
    var specialField6: specialField?=null,
    var specialField7: specialField?=null,
    var userId: String = ""
    )
