package com.palich.fragments


object DataHolder {

        private var list: MutableList<String> = mutableListOf()

        fun setList(list: MutableList<String>) {
            this.list = list
        }

        fun getList(): MutableList<String> {
            return list
        }

}