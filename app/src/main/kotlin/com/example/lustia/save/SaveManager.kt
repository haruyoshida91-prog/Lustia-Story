package com.example.lustia.save

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONObject
import com.example.lustia.player.PlayerCharacter
import com.example.lustia.player.Race

object SaveManager {
    private const val PREFS = "lustia_save"
    private const val KEY_PLAYER = "player"

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun savePlayer(context: Context, player: PlayerCharacter) {
        val json = JSONObject().apply {
            put("name", player.name)
            put("race", player.race.name)
            put("heightCm", player.heightCm)
            put("hp", player.hp)
            put("maxHp", player.maxHp)
            put("lust", player.lust)
            put("maxLust", player.maxLust)
            put("money", player.money)
            put("exp", player.exp)
        }
        prefs(context).edit().putString(KEY_PLAYER, json.toString()).apply()
    }

    fun loadPlayer(context: Context): PlayerCharacter? {
        val s = prefs(context).getString(KEY_PLAYER, null) ?: return null
        return try {
            val obj = JSONObject(s)
            PlayerCharacter(
                name = obj.optString("name", "Hero"),
                race = try { Race.valueOf(obj.optString("race", Race.HUMAN.name)) } catch (e: Exception) { Race.HUMAN },
                heightCm = obj.optInt("heightCm", 170),
                hp = obj.optInt("hp", 100),
                maxHp = obj.optInt("maxHp", 100),
                lust = obj.optInt("lust", 0),
                maxLust = obj.optInt("maxLust", 100),
                money = obj.optInt("money", 0),
                exp = obj.optInt("exp", 0)
            )
        } catch (e: Exception) {
            null
        }
    }
}
