package com.example.fire.data.dto.localprank

data class MyFolderVideo(
    var folder: MyFile = MyFile(),
    var isShowList: Boolean = false,
    var list: ArrayList<MyVideo> = arrayListOf()
){
    override fun equals(other: Any?): Boolean {
        return this.folder.title == (other as MyFolderVideo).folder.title
    }
}