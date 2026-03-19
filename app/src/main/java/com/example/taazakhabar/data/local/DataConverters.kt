package com.example.taazakhabar.data.local

fun List<String>.fromStringList(): String {
    return this.joinToString(",")
}

fun String.toStringList(): List<String> {
    return this.split(",").map { it.trim() }
}