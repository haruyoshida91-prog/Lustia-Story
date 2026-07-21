package com.example.lustia.player

enum class Race { HUMAN, ELF, DWARF }

data class PlayerCharacter(
    var name: String = "Hero",
    var race: Race = Race.HUMAN,
    var heightCm: Int = 170,
    var hp: Int = 100,
    var maxHp: Int = 100,
    var lust: Int = 0,
    var maxLust: Int = 100,
    var money: Int = 0,
    var exp: Int = 0
)
