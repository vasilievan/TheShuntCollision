package com.aleksey.vasiliev.theshunt

import com.badlogic.gdx.Game
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g3d.Model

class MyGame: Game() {
    private val assets = AssetManager()
    var sceneObjects = arrayOf("Police", "Car", "Car1", "Car2", "Car3", "Ambulance", "TrolleyBus", "TrolleyBus1", "Train", "Shunt", "Stones", "Stick", "Bus-stop", "Road", "Rails", "Grass", "Home", "Lantern", "Fir-tree")
    override fun create() {
        for (element in sceneObjects) {
            assets.load("$element.obj", Model::class.java)
        }
        setScreen(MyShuntGame(this, assets))
    }
}