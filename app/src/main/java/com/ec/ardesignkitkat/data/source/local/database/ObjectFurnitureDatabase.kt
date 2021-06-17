package com.ec.ardesignkitkat.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ec.ardesignkitkat.data.model.Furniture
import com.ec.ardesignkitkat.data.model.StandardFurniture
import com.ec.ardesignkitkat.data.model.User
import com.ec.ardesignkitkat.data.model.Wall

@Database(
    entities = [
        User::class,
        Wall::class,
        Furniture::class,
        StandardFurniture::class
    ],
    version = 1
)

abstract class ObjectFurnitureDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun wallDao(): WallDao
    abstract fun furnitureDao(): FurnitureDao
    abstract fun standFurnDao(): StandFurnitureDao
}