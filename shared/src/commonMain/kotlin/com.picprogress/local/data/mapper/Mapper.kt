package com.picprogress.local.data.mapper

interface Mapper<I, O> {
    fun map(input: I): O
}