package com.aleksey.vasiliev.theshunt

import com.badlogic.gdx.Game
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g3d.Model

class MyGame: Game() {
    private val assets = AssetManager()
    private val sounds = arrayOf("crash", "train", "wheels", "shuntSound")
    private val sceneObjects = arrayOf("Police", "Car", "Car1", "Car2", "Car3", "Ambulance", "TrolleyBus", "TrolleyBus1", "Train", "Shunt", "Stones", "Stick", "Bus-stop", "Road", "Rails", "Grass", "Home", "Lantern", "Fir-tree")
    override fun create() {
        for (element in sceneObjects) {
            assets.load("$element.obj", Model::class.java)
        }
        for (element in sounds) {
            assets.load("$element.mp3", Sound::class.java)
        }
        assets.load("forest.mp3", Music::class.java)
        setScreen(MyShuntGame(this, assets))
    }
}