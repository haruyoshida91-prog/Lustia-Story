package com.lustia.story.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Story(
    val id: String,
    val title: String,
    val description: String,
    val scenes: Map<String, Scene>,
    val startSceneId: String
) : Parcelable

@Parcelize
data class Scene(
    val id: String,
    val text: String,
    val imageUrl: String? = null,
    val choices: List<Choice>,
    val statChanges: Map<String, Int> = emptyMap(),
    val sceneType: SceneType = SceneType.NORMAL,
    val antagonist: Antagonist? = null
) : Parcelable

@Parcelize
data class Choice(
    val id: String,
    val text: String,
    val nextSceneId: String,
    val requirementCheck: StatRequirement? = null,
    val consequence: ConsequenceType = ConsequenceType.NONE
) : Parcelable

@Parcelize
data class StatRequirement(
    val statName: String,
    val minValue: Int
) : Parcelable

@Parcelize
data class Antagonist(
    val id: String,
    val name: String,
    val description: String,
    val stats: Map<String, Int>,
    val abilities: List<String> = emptyList()
) : Parcelable

enum class SceneType {
    NORMAL, COMBAT, DIALOGUE, EVENT, GAME_OVER, VICTORY
}

enum class ConsequenceType {
    NONE, STAT_CHANGE, ITEM_GAIN, ITEM_LOSS, BATTLE_START
}
