package com.example.lustia.story

import androidx.annotation.Keep

// Scene flags to allow filtering by user preference
enum class SceneFlag { NSFW, SEXUAL, VIOLENCE }

// SceneType can include flags and metadata
data class SceneType(val name: String, val flags: Set<SceneFlag> = emptySet())

// Actions that an option can trigger (serializable-ish, easy to interpret)
sealed class StoryAction {
    data class StartCombat(val enemyId: String) : StoryAction()
    data class ModifyStat(val stat: String, val amount: Int) : StoryAction()
    data class GiveItem(val itemId: String, val amount: Int = 1) : StoryAction()
    object None : StoryAction()
}

// Option model: each option has its own actions/setup and next node link
data class StoryOption(
    val id: String,
    val text: String,
    val nextNodeId: String? = null,          // null = terminal node or handled by action
    val actions: List<StoryAction> = emptyList(),
    val isSexualOption: Boolean = false
)

// Node model: content + options
data class StoryNode(
    val id: String,
    val title: String,
    val content: String,
    val sceneType: SceneType = SceneType("normal"),
    val options: List<StoryOption> = emptyList()
)

class StoryRepository {
    private val nodes = mutableMapOf<String, StoryNode>()

    init {
        buildExample()
    }

    fun getNode(nodeId: String): StoryNode? = nodes[nodeId]

    fun addNode(node: StoryNode) {
        nodes[node.id] = node
    }

    // Example build; replace with your story data or load via JSON
    private fun buildExample() {
        addNode(
            StoryNode(
                id = "start",
                title = "A Strange Crossroads",
                content = "You stand at a dimly lit crossroads. An enemy lurks nearby.",
                sceneType = SceneType("danger", setOf(SceneFlag.NSFW)),
                options = listOf(
                    StoryOption(
                        id = "fight_option",
                        text = "Confront the enemy",
                        nextNodeId = "combat_intro",
                        actions = listOf(StoryAction.StartCombat(enemyId = "goblin_1"))
                    ),
                    StoryOption(
                        id = "leave_option",
                        text = "Walk away",
                        nextNodeId = "peaceful_path"
                    )
                )
            )
        )
        addNode(
            StoryNode(
                id = "combat_intro",
                title = "Battle!",
                content = "You engage the foe. Prepare to fight.",
                options = emptyList()
            )
        )
        addNode(
            StoryNode(
                id = "peaceful_path",
                title = "Peaceful Path",
                content = "You walk into the woods and continue your journey.",
                options = emptyList()
            )
        )
    }
}
