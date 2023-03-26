package com.example.foody.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foody.util.Constants.Companion.FAVOURITES_RECIPES_TABLE
import  com.example.foody.models.Result

@Entity(tableName = FAVOURITES_RECIPES_TABLE)
class FavouritesEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var result: Result
)