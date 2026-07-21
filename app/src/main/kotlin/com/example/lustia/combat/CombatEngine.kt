package com.example.lustia.combat

enum class DamageType { PHYSICAL, LUST }

data class Combatant(
    val id: String,
    var hp: Int,
    var maxHp: Int,
    var lust: Int = 0,
    var maxLust: Int = 100,
    var atk: Int = 10,
    var def: Int = 5,
    var moneyOnDefeat: Int = 0,
    var expOnDefeat: Int = 0,
    val dropItems: List<String> = emptyList()
) {
    fun isDefeated() = hp <= 0
    fun isOverwhelmedByLust() = lust >= maxLust
}

interface CombatListener {
    fun onTurnLog(text: String)
    fun onVictory(enemy: Combatant, sexualOptions: List<SexualOption>)
    fun onDefeat(badEndId: String)
    fun onUpdate(player: Combatant, enemy: Combatant)
}

data class SexualOption(val id: String, val text: String, val actions: List<String> = emptyList())

class CombatEngine(
    private val player: Combatant,
    private val enemy: Combatant,
    private val listener: CombatListener
) {
    private var playerTurn = true

    fun start() {
        listener.onTurnLog("Combat started: ${player.id} vs ${enemy.id}")
        listener.onUpdate(player, enemy)
    }

    // Basic attack action (player)
    fun playerAttack() {
        if (player.isDefeated() || player.isOverwhelmedByLust()) return
        val dmg = (player.atk - enemy.def).coerceAtLeast(1)
        applyDamage(target = enemy, amount = dmg, type = DamageType.PHYSICAL)
        listener.onTurnLog("You attack for $dmg physical damage.")
        checkPostAction()
    }

    // Lust attack applies lust points instead of HP damage
    fun playerUseLustAttack(amount: Int) {
        if (player.isDefeated() || player.isOverwhelmedByLust()) return
        applyDamage(target = enemy, amount = amount, type = DamageType.LUST)
        listener.onTurnLog("You apply $amount lust to the enemy.")
        checkPostAction()
    }

    // Enemy performs a simple action (could be extended with AI)
    fun enemyAction() {
        if (enemy.isDefeated() || enemy.isOverwhelmedByLust()) return
        // Example enemy: deals physical damage and occasionally applies lust
        val dmg = (enemy.atk - player.def).coerceAtLeast(1)
        applyDamage(target = player, amount = dmg, type = DamageType.PHYSICAL)
        listener.onTurnLog("${enemy.id} attacks for $dmg physical damage.")
        // small chance to apply lust
        val lust = (enemy.atk / 4)
        if (lust > 0) {
            applyDamage(target = player, amount = lust, type = DamageType.LUST)
            listener.onTurnLog("${enemy.id} teases you, applying $lust lust.")
        }
        checkPostAction()
    }

    private fun checkPostAction() {
        listener.onUpdate(player, enemy)

        if (player.isDefeated()) {
            listener.onDefeat("bad_end_dead")
            return
        }
        if (player.isOverwhelmedByLust()) {
            listener.onDefeat("bad_end_lust")
            return
        }
        if (enemy.isDefeated()) {
            // Build sexual options based on enemy state - placeholder
            val sexOptions = listOf(SexualOption("sex_1", "Take advantage of the fallen"))
            listener.onVictory(enemy, sexOptions)
            return
        }
        if (enemy.isOverwhelmedByLust()) {
            val sexOptions = listOf(SexualOption("sex_enemy_overcome", "Exploit enemy lust"))
            listener.onVictory(enemy, sexOptions)
            return
        }
    }

    private fun applyDamage(target: Combatant, amount: Int, type: DamageType) {
        when (type) {
            DamageType.PHYSICAL -> {
                target.hp -= amount
                if (target.hp < 0) target.hp = 0
            }
            DamageType.LUST -> {
                target.lust += amount
                if (target.lust > target.maxLust) target.lust = target.maxLust
            }
        }
    }
}
